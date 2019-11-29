package com.sample.customizableloginsample.storage;

import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.sample.customizableloginsample.app.ShellApplication;
import com.sample.loginkit.init.RootLoginController;
import com.sample.loginkit.models.Login;
import com.sample.loginkit.models.Profile;
import com.sample.loginkit.network.NetworkConfiguration;
import com.sample.loginkit.network.apiUtils.APIRequestInterface;
import com.sample.loginkit.network.generic.DataWrapper;
import com.sample.loginkit.utils.LogUtils;
import com.sample.loginkit.utils.StringConstants;

import java.util.List;

public class DataStore {
    private String TAG = com.sample.loginkit.network.apiUtils.DataRepository.class.getName();

    private APIRequestInterface apiRequest;
    private List<Profile> _profileList;
    final MutableLiveData<Login> loginInfo = new MutableLiveData<>();
    final MutableLiveData<List<Profile>> profiles = new MutableLiveData<>();
    private static final DataStore instance = new DataStore();



    public DataStore() {


    }

    public static DataStore getInstance() {
        return instance;
    }

    public APIRequestInterface getApiRequest() {
        return apiRequest;
    }


    public void storeUserSessionDetails(Login userSessionObject) {

        Gson gson = new Gson();
        String userSessionObjectinString = gson.toJson((Login) userSessionObject);
        ShellApplication.getEncryptedPreferences()
                .edit()
                .putString(StringConstants.USER_OBJECT_KEY, userSessionObjectinString)
                .apply();

    }

    public Login fetchUserSessionDetails() {

        String decryptedUserSession = ShellApplication.getEncryptedPreferences().getString(StringConstants.USER_OBJECT_KEY, "");

        Login userSession = null;
        try {
            Gson gson = new Gson();
            userSession = gson.fromJson(decryptedUserSession, Login.class);
          //  LogUtils.debug(TAG, "Decrypted user Object" + userSession.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userSession;


    }

    public void clearUserSessionDetails() {


        ShellApplication.getEncryptedPreferences().edit()
                .putString(StringConstants.USER_OBJECT_KEY, null)
                .apply();

    }


    public void cacheProfileDetails(List<Profile> profileData) {


        _profileList = profileData;


    }



    public List<Profile> get_profileList() {
        return _profileList;
    }

    public void storeUserName(String username) {
        ShellApplication.getEncryptedPreferences().edit()
                .putString(StringConstants.USERNAME_KEY, username)
                .apply();
    }

    public void storeRememberMeStatus(boolean rememberMe) {
        ShellApplication.getEncryptedPreferences().edit()
                .putBoolean(StringConstants.IS_REMEMBER_ME, rememberMe)

                .apply();
    }

    public void storePassword(String password) {
        ShellApplication.getEncryptedPreferences().edit()
                .putString(StringConstants.PASSWORD_KEY, password)

                .apply();
    }

    public String fetchUserName() {
        String decryptedValue = ShellApplication.getEncryptedPreferences().getString(StringConstants.USERNAME_KEY, "");
        return decryptedValue;

    }

    public boolean fetchRememberMe() {
        boolean decryptedValue = ShellApplication.getEncryptedPreferences().getBoolean(StringConstants.IS_REMEMBER_ME, false);
        return decryptedValue;

    }

    public String fetchPassword() {
        String decryptedValue = ShellApplication.getEncryptedPreferences().getString(StringConstants.PASSWORD_KEY, "");
        return decryptedValue;

    }

    public RequestOptions getrequestoptions() {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(android.R.drawable.sym_def_app_icon)
                .error(android.R.drawable.sym_def_app_icon);

        return options;


    }

}
