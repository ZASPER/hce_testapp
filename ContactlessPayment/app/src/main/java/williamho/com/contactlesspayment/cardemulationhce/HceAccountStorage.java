package williamho.com.contactlesspayment.cardemulationhce;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by williamho on 13/08/16.
 */

/**
 * Utility class for persisting account numbers to disk.
 *
 * <p>The default SharedPreferences instance is used as the backing storage. Values are cached
 * in memory for performance.
 *
 * <p>This class is thread-safe.
 */
public class HceAccountStorage {
    private static final String PREF_ACCOUNT_NUMBER = "hce_account_number";
    private static final String DEFAULT_ACCOUNT_NUMBER = "12345678";
    private static final String TAG = "HceAccountStorage";
    private static String sAccount = null;
    private static final Object sAccountLock = new Object();

    public static void SetAccount(Context c, String s) {
        synchronized(sAccountLock) {
            Log.i(TAG, "Setting account number: " + s);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
            prefs.edit().putString(PREF_ACCOUNT_NUMBER, s).commit();
            sAccount = s;
        }
    }

    public static String GetAccount(Context c) {
        synchronized (sAccountLock) {
            if (sAccount == null) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
                String account = prefs.getString(PREF_ACCOUNT_NUMBER, DEFAULT_ACCOUNT_NUMBER);
                sAccount = account;
            }
            return sAccount;
        }
    }
}