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

    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> anonymousClass) {
        if (anonymousClass == String.class) {
            return (T) mSharedPreferences.getString(key, Constants.EMPTY);
        } else if (anonymousClass == Boolean.class) {
            return (T) Boolean.valueOf(mSharedPreferences.getBoolean(key, false));
        } else if (anonymousClass == Float.class) {
            return (T) Float.valueOf(mSharedPreferences.getFloat(key, Constants.DEFAULT_NUMBER));
        } else if (anonymousClass == Integer.class) {
            return (T) Integer.valueOf(mSharedPreferences.getInt(key, Constants.DEFAULT_NUMBER));
        } else if (anonymousClass == Long.class) {
            return (T) Long.valueOf(mSharedPreferences.getLong(key, Constants.DEFAULT_NUMBER));
        } else {
            return (T) new Gson()
                    .fromJson(mSharedPreferences.getString(key, Constants.EMPTY), anonymousClass);
        }
    }

    public <T> void put(String key, T data) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        if (data instanceof String) {
            editor.putString(key, (String) data);
        } else if (data instanceof Boolean) {
            editor.putBoolean(key, (Boolean) data);
        } else if (data instanceof Float) {
            editor.putFloat(key, (Float) data);
        } else if (data instanceof Integer) {
            editor.putInt(key, (Integer) data);
        } else if (data instanceof Long) {
            editor.putLong(key, (Long) data);
        } else {
            Gson gson = new Gson();
            String json = gson.toJson(data);
            editor.putString(key, json);
        }
        editor.apply();
    }

    public void saveAccountCustomer(Customer customer) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(customer);
        editor.putString(Constants.CUSTOMER_ACCOUNT_KEY, json);
        editor.apply();
    }
    public void removeAccountCustomer() {
        mSharedPreferences.edit().remove(Constants.CUSTOMER_ACCOUNT_KEY).apply();
    }

    public void remove(String key) {
        mSharedPreferences.edit().remove(key).apply();
    }

}
