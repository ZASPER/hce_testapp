package williamho.com.contactlesspayment.common.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import williamho.com.contactlesspayment.common.logger.Log;
import williamho.com.contactlesspayment.common.logger.LogWrapper;

/**
 * Created by williamho on 13/08/16.
 */

/**
 * Base launcher activity, to handle most of the common plumbing for samples.
 */
public class SampleActivityBase extends FragmentActivity {

    public static final String TAG = "SampleActivityBase";

    @Override
    protected void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    protected void onStart() {
        super.onStart();
        initialiseLogging();
    }

    /** Set up targets to receive log data */
    public void initialiseLogging() {
        // Using Log, front-end to the logging chain, emulates android.util.log method signatures.
        // Wraps Android's native log framework
        LogWrapper logWrapper = new LogWrapper();
        williamho.com.contactlesspayment.common.logger.Log.setLogNode(logWrapper);

        Log.i(TAG, "Ready");
    }
}
