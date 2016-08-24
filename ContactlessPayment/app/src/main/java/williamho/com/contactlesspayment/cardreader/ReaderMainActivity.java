package williamho.com.contactlesspayment.cardreader;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;
import android.widget.ViewAnimator;
import android.support.v7.widget.Toolbar;

import williamho.com.contactlesspayment.MainActivity;
import williamho.com.contactlesspayment.R;
import williamho.com.contactlesspayment.common.activities.SampleActivityBase;
import williamho.com.contactlesspayment.common.logger.Log;
import williamho.com.contactlesspayment.common.logger.LogFragment;
import williamho.com.contactlesspayment.common.logger.LogWrapper;
import williamho.com.contactlesspayment.common.logger.MessageOnlyLogFilter;

/**
 * Created by williamho on 13/08/16.
 */

/**
 * A launcher activity containing a summary description, log and a custom
 * {@link android.support.v4.app.Fragment} which can display a view.
 * <p>
 * For devices with displays with a width of 720dp or greater, the log is always visible,
 * on other devices it's visibility is conrtrolled by an item on the Action Bar.
 */

public class ReaderMainActivity extends SampleActivityBase {

    public static final String TAG = "ReaderMainActivity";

    // Whether the Log Fragment is currently shown
    private boolean mLogShown;

    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reader_activity_main);

        NfcManager manager = (NfcManager) context.getSystemService(Context.NFC_SERVICE);
        NfcAdapter adapter = manager.getDefaultAdapter();
        if (adapter != null && !adapter.isEnabled()) {
            //Yes NFC available
            setContentView(R.layout.reader_activity_main);
            startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
            Toast.makeText(context, "Please enable NFC and return to the application!", Toast.LENGTH_SHORT).show();

        }else{

            Intent intentmain = new Intent(this, MainActivity.class);
            startActivity(intentmain);
            Toast.makeText(context, "Sorry your device does not support NFC!", Toast.LENGTH_SHORT).show();

        }

        ActionBar bar = getActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3F51B5")));

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            CardReaderFragment fragment = new CardReaderFragment();
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem logToggle = menu.findItem(R.id.menu_toggle_log);
        logToggle.setVisible(findViewById(R.id.sample_output) instanceof ViewAnimator);
        logToggle.setTitle(mLogShown ? R.string.sample_hide_log : R.string.sample_show_log);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_toggle_log:
                mLogShown = !mLogShown;
                ViewAnimator output = (ViewAnimator) findViewById(R.id.sample_output);
                if (mLogShown) {
                    output.setDisplayedChild(1);
                } else {
                    output.setDisplayedChild(0);
                }
                supportInvalidateOptionsMenu();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /** Create a chain of targets that will receive log data */
    @Override
    public void initialiseLogging() {
        // Wraps Android's native log framework.
        LogWrapper logWrapper = new LogWrapper();
        // Using Log, front-end to the logging chain, emulates android.util.log method signatures.
        Log.setLogNode(logWrapper);

        // Filter strips out everything except the message text.
        MessageOnlyLogFilter msgFilter = new MessageOnlyLogFilter();
        logWrapper.setNext(msgFilter);

        // On screen logging via a fragment with a TextView.
        LogFragment logFragment = (LogFragment) getSupportFragmentManager()
                .findFragmentById(R.id.log_fragment);
        msgFilter.setNext(logFragment.getLogView());

        Log.i(TAG, "Ready");
    }
}