package williamho.com.contactlesspayment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import williamho.com.contactlesspayment.cardemulationhce.HceMainActivity;
import williamho.com.contactlesspayment.cardemulationnfc.NfcMainActivity;
import williamho.com.contactlesspayment.cardreader.ReaderMainActivity;
import williamho.com.contactlesspayment.models.ServerRequest;
import williamho.com.contactlesspayment.models.ServerResponse;
import williamho.com.contactlesspayment.models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by williamho on 12/08/16.
 */

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private TextView tv_name,tv_email,tv_card1,tv_card1_balance,tv_message;
    private SharedPreferences pref;
    private AppCompatButton btn_change_password,btn_logout,btn_hce,btn_nfc,btn_pos;
    private EditText et_old_password,et_new_password;
    private AlertDialog dialog;
    private ProgressBar progress;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile,container,false);
        initViews(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        pref = getActivity().getPreferences(0);
        tv_name.setText("Welcome: "+pref.getString(Constants.NAME,""));
        tv_email.setText(pref.getString(Constants.EMAIL,""));

        tv_card1.setText("Card No: "+pref.getString(Constants.CARD1,""));
        tv_card1_balance.setText("Balance: "+pref.getString(Constants.CARD1_BALANCE,""));

    }

    private void initViews(View view){

        tv_name = (TextView)view.findViewById(R.id.tv_name);
        tv_email = (TextView)view.findViewById(R.id.tv_email);
        tv_card1 = (TextView)view.findViewById(R.id.tv_card1);
        tv_card1_balance = (TextView)view.findViewById(R.id.tv_card1_balance);
        btn_change_password = (AppCompatButton)view.findViewById(R.id.btn_chg_password);
        btn_logout = (AppCompatButton)view.findViewById(R.id.btn_logout);
        btn_hce = (AppCompatButton)view.findViewById(R.id.btn_hce);
        btn_nfc = (AppCompatButton)view.findViewById(R.id.btn_nfc);
        btn_pos = (AppCompatButton)view.findViewById(R.id.btn_pos);
        btn_change_password.setOnClickListener(this);
        btn_logout.setOnClickListener(this);
        btn_hce.setOnClickListener(this);
        btn_nfc.setOnClickListener(this);
        btn_pos.setOnClickListener(this);

    }

    private void showDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_change_password, null);
        et_old_password = (EditText)view.findViewById(R.id.et_old_password);
        et_new_password = (EditText)view.findViewById(R.id.et_new_password);
        tv_message = (TextView)view.findViewById(R.id.tv_message);
        progress = (ProgressBar)view.findViewById(R.id.progress);
        builder.setView(view);
        builder.setTitle("Change Password");
        builder.setPositiveButton("Change Password", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String old_password = et_old_password.getText().toString();
                String new_password = et_new_password.getText().toString();
                if(!old_password.isEmpty() && !new_password.isEmpty()){

                    progress.setVisibility(View.VISIBLE);
                    changePasswordProcess(pref.getString(Constants.EMAIL,""),old_password,new_password);

                }else {

                    tv_message.setVisibility(View.VISIBLE);
                    tv_message.setText("Fields are empty");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_chg_password:
                showDialog();
                break;
            case R.id.btn_logout:
                logout();
                break;
            case R.id.btn_hce:
                String accountno = pref.getString(Constants.CARD1,"");
                Intent intenthce = new Intent(getActivity(), HceMainActivity.class);
                intenthce.putExtra("account_string", accountno);
                startActivity(intenthce);
                break;
            case R.id.btn_nfc:
                Intent intentnfc = new Intent(getActivity(), NfcMainActivity.class);
                startActivity(intentnfc);
                break;
            case R.id.btn_pos:
                Intent intentpos = new Intent(getActivity(), ReaderMainActivity.class);
                startActivity(intentpos);
                break;
        }
    }

    private void logout() {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(Constants.IS_LOGGED_IN,false);
        editor.putString(Constants.EMAIL,"");
        editor.putString(Constants.NAME,"");
        editor.putString(Constants.CARD1,"");
        editor.putString(Constants.CARD1_BALANCE,"");
        editor.putString(Constants.UNIQUE_ID,"");
        editor.apply();
        goToLogin();
    }

    private void goToLogin(){

        Fragment login = new williamho.com.contactlesspayment.LoginFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame,login);
        ft.commit();
    }

    private void changePasswordProcess(String email,String old_password,String new_password){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        williamho.com.contactlesspayment.RequestInterface requestInterface = retrofit.create(williamho.com.contactlesspayment.RequestInterface.class);

        User user = new User();
        user.setEmail(email);
        user.setOld_password(old_password);
        user.setNew_password(new_password);
        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.CHANGE_PASSWORD_OPERATION);
        request.setUser(user);
        Call<ServerResponse> response = requestInterface.operation(request);

        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {

                ServerResponse resp = response.body();
                if(resp.getResult().equals(Constants.SUCCESS)){
                    progress.setVisibility(View.GONE);
                    tv_message.setVisibility(View.GONE);
                    dialog.dismiss();
                    Snackbar.make(getView(), resp.getMessage(), Snackbar.LENGTH_LONG).show();

                }else {
                    progress.setVisibility(View.GONE);
                    tv_message.setVisibility(View.VISIBLE);
                    tv_message.setText(resp.getMessage());

                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                Log.d(Constants.TAG,"failed");
                progress.setVisibility(View.GONE);
                tv_message.setVisibility(View.VISIBLE);
                tv_message.setText(t.getLocalizedMessage());


            }
        });
    }
}