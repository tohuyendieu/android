package fptu.ninhtbm.thebookshop.library;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import fptu.ninhtbm.thebookshop.model.Customer;


public class SharePreferencesUtils {

    private static final String SHARED_PREFERENCES_FILE_NAME = "TheBookshopPreferences";

    private static SharePreferencesUtils mInstance;
    private final SharedPreferences mSharedPreferences;
    private final Context mContext;

    public SharePreferencesUtils(Context context) {
        mContext = context;
        mSharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
    }

    public void saveAccountCustomer(Customer customer) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(customer);
        editor.putString(Constants.CUSTOMER_ACCOUNT_KEY, json);
        editor.apply();
    }

    public Customer getAccountCustomer() {
        return new Gson().fromJson(mSharedPreferences.getString(Constants.CUSTOMER_ACCOUNT_KEY, Constants.EMPTY), Customer.class);
    }

    public void removeAccountCustomer() {
        mSharedPreferences.edit().remove(Constants.CUSTOMER_ACCOUNT_KEY).apply();
    }

    public void remove(String key) {
        mSharedPreferences.edit().remove(key).apply();
    }

}
