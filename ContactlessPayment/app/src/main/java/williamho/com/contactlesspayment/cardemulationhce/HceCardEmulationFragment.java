package williamho.com.contactlesspayment.cardemulationhce;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import williamho.com.contactlesspayment.R;

/**
 * Created by williamho on 13/08/16.
 */

/**
 * Generic UI for sample discovery.
 */
public class HceCardEmulationFragment extends Fragment {

    public static final String TAG = "HceCardEmulationFragment";

    /** Called when sample is created. Displays generic UI with welcome text. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String value = getActivity().getIntent().getStringExtra("account_string");
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.main_fragment, container, false);
        TextView account = (TextView) v.findViewById(R.id.card_account_field);
        //account.setText(HceAccountStorage.GetAccount(getActivity()));
        account.setText(value);
        account.addTextChangedListener(new AccountUpdater());
        return v;
    }

    private class AccountUpdater implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Not implemented.
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // Not implemented.
        }

        @Override
        public void afterTextChanged(Editable s) {
            String account = s.toString();
            HceAccountStorage.SetAccount(getActivity(), account);
        }
    }
}