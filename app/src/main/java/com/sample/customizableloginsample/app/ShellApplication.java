package com.sample.customizableloginsample.app;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.sample.loginkit.listeners.CallbackManager;
import com.sample.loginkit.utils.LogUtils;

import java.io.IOException;
import java.security.GeneralSecurityException;


import static com.sample.loginkit.utils.StringConstants.ENCRYPTED_PREFERENCES_NAME;

public class ShellApplication extends Application {

    private static String TAG = ShellApplication.class.getName();
    private static SharedPreferences encryptedPreferences = null;
    private static CallbackManager commonListener;

    @Override
    public void onCreate() {
        super.onCreate();

        initializeSharedPreferences();

    }


    /*

     Method to initialize Encrypted Preferences in the Application


     */

    private void initializeSharedPreferences() {
        commonListener = new CallbackManager();
        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);


            encryptedPreferences = EncryptedSharedPreferences.create(
                    ENCRYPTED_PREFERENCES_NAME,
                    masterKeyAlias,
                    getApplicationContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            LogUtils.debug(TAG, "Initializing Encrypted Preferences..");


        }
        catch (GeneralSecurityException e)
        {
            LogUtils.debug(TAG, "Security Exception : " + e.getLocalizedMessage());
            e.printStackTrace();
        }

        catch (IOException e) {

            LogUtils.debug(TAG, "Security Exception : " + e.getLocalizedMessage());
            e.printStackTrace();

        }
    }

    public static CallbackManager getCommonListener()
    {
        return commonListener;
    }

    public static SharedPreferences getEncryptedPreferences()
    {
        return encryptedPreferences;
    }
}
