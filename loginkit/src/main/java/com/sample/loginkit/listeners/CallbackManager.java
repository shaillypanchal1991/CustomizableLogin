package com.sample.loginkit.listeners;


/*
The class is to be used by the shell app to directly call APIs like login ,getProfiles etc.
User just need to send parameters for appropriate APIs

 */
public class CallbackManager {


    public CallbackManager() {


    }

    /* Function to be called by Shell APP to perform Login and get Response by implementing CallbackHelper.GenericCallbacks
eg :  ShellApplication.getCommonListener().loginwithCredentials(this, "Login",binding.editTextEmail.getText().toString(), binding.editTextPassword.getText().toString());

     */
    public void loginwithCredentials(CallbackHelper.GenericCallbacks genericCallbacks, String requestType, String username, String password) {

        new CallbackHelper().loginwithCredentials(requestType, genericCallbacks, username, password);

    }

    /*
     * For fetching profiles of the user
     * */
    public void loadProfiles(CallbackHelper.GenericCallbacks genericCallbacks, String requestType, String accessToken) {


        new CallbackHelper().loadProfiles(requestType, genericCallbacks, accessToken);

    }


    /*
     * When a profile is clicked
     * */
    public void loginWithProfile(CallbackHelper.GenericCallbacks genericCallbacks, String requestType, String granttype, String username, String password, String profileid, int pin) {


        new CallbackHelper().loginWithProfile(requestType, genericCallbacks, granttype, username, password, profileid, pin);
    }


    /* When profileFailure() is invoked , call refreshToken() and then reinvoke the getProfiles API

     */
    public void refreshToken(CallbackHelper.GenericCallbacks genericCallbacks, String requestType, String refreshToken) {
        new CallbackHelper().refreshToken(requestType, genericCallbacks, refreshToken);
    }


}
