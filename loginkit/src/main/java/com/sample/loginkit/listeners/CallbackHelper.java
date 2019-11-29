package com.sample.loginkit.listeners;

import android.util.Log;

import com.sample.loginkit.models.Login;
import com.sample.loginkit.models.Profile;
import com.sample.loginkit.network.apiUtils.DataRepository;
import com.sample.loginkit.network.error.CustomException;
import com.sample.loginkit.network.generic.DataWrapper;
import com.sample.loginkit.network.generic.GenericRefreshTokenHandler;
import com.sample.loginkit.network.generic.GenericRequestHandler;
import com.sample.loginkit.network.interactor.LoginInteractor;
import com.sample.loginkit.network.interactor.ProfileInteractor;
import com.sample.loginkit.network.interactor.ProfileLoginInteractor;
import com.sample.loginkit.network.interactor.RefreshTokenInteractor;
import com.sample.loginkit.utils.LogUtils;
import com.sample.loginkit.utils.RequestType;
import com.sample.loginkit.utils.StringConstants;

import java.util.List;

/*


This generic class is responsible for fetching the generic response of all the APIs received in
the GenericRequestHandler class and converting the generic response
to model wise response data for the activities in form of callbacks.


 */
public class CallbackHelper implements GenericRequestHandler.IResponseStatus, GenericRefreshTokenHandler.ITokenResponseStatus {

    private GenericCallbacks _generalCallback;
    private RequestType _requestType;


    /* Should be called to perform login
    @param requestType : APIName
    @param genericCallback : Interface instance
    @param username : Email id entered by user
    @param password : Password entered by user

     */
    public void loginwithCredentials(RequestType requestType, CallbackHelper.GenericCallbacks genericCallback, String username, String password) {

        _generalCallback = genericCallback;
        _requestType = requestType;

        LoginInteractor.createInstance(StringConstants.APIKEY, StringConstants.PASSWORD, username, password).onAuthRequest(CallbackHelper.this, DataRepository.getInstance().getApiRequest());

    }

    /* Should be called to perform loading of profiles of a particular user

     */

    public void loadProfiles(RequestType requestType, CallbackHelper.GenericCallbacks genericCallback, String accessToken) {

        _generalCallback = genericCallback;
        _requestType = requestType;
        ProfileInteractor.createInstance("bearer " + accessToken).onProfilesRequest(CallbackHelper.this, DataRepository.getInstance().getApiRequest());

    }

    /* Should be called when user clicks on a profile and needs to login with that particula profile

     */
    public void loginWithProfile(RequestType requestType, CallbackHelper.GenericCallbacks genericCallback, String granttype, String username, String password, String profileid, int pin) {

        _generalCallback = genericCallback;
        _requestType = requestType;

        if (pin != 0) {
            ProfileLoginInteractor.createInstance("password", username, password, profileid, pin).onProfileLoginRequest(this, DataRepository.getInstance().getApiRequest());
        } else {
            ProfileLoginInteractor.createInstance("password", username, password, profileid).onProfileLoginRequest(this, DataRepository.getInstance().getApiRequest());

        }
        //ProfileInteractor.createInstance("bearer "+accessToken).onProfilesRequest(ProfilesLoginHelper.this, DataRepository.getInstance().getApiRequest());

    }


    /* When the user receives callback onProfileFailure(), he should refresh the token by calling refreshToken
@param refreshToken : received in Login API with accesstoken
     */
    public void refreshToken(RequestType requestType, CallbackHelper.GenericCallbacks genericCallbacks, String refreshToken) {
        _generalCallback = genericCallbacks;
        _requestType = requestType;
        RefreshTokenInteractor.createInstance(refreshToken, "refresh_token").onRefreshTokenRequest(this);

    }


    /* This overriden method is triggered everytime an API is triggered and handles the response according
       to the model data and sets callbacks
        @param RequestType : The type of API eg: Login,GetProfiles etc to differentiate between the generic response received
     */
    @Override
    public void onResponseReceived(DataWrapper liveData) {

        switch (_requestType.name()) {

            case "LOGIN_WITH_CREDENTIALS":


                if (liveData.getData() != null) {
                    LogUtils.debug("Login Successful", "Login Successful");
                    _generalCallback.onLoginSuccess((Login) liveData.getData());
                } else {
                    LogUtils.debug("Login Failed", "Login Failed");
                    _generalCallback.onLoginFailure(liveData.getApiException());
                }


                break;

            case "LOAD_PROFILES":
                if (liveData.getData() != null) {
                    LogUtils.debug("Loading Profiles Successful", "Loading profiles  Successful");
                    _generalCallback.onProfileSuccess((List<Profile>) liveData.getData());
                } else {
                    _generalCallback.onProfileFailure(liveData.getApiException());
                    LogUtils.debug("lOADING pROFILES fAILED", "Loading pROFiles Failed");
                }
                break;

            case "LOGIN_WTH_PROFILE_ID":
                if (liveData.getData() != null) {
                    LogUtils.debug("Login with Profile ID Successful", "Login with Profile ID Successful");
                    _generalCallback.onProfileLoginSuccess((Login) liveData.getData());
                } else {
                    LogUtils.debug("Login with Profile ID Failed", "Login with Profile ID Failed");
                    _generalCallback.onProfileLoginFailure(liveData.getApiException());
                }
                break;

            case "REFRESH_TOKEN":
                if (liveData.getData() != null) {
                    LogUtils.debug("Refresh token Successful", "Refresh Token Successful");
                    _generalCallback.onRefreshTokenSucess((Login) liveData.getData());
                } else {
                    LogUtils.debug("Refresh Token Failed", "Refresh Token  Failed");
                    _generalCallback.onRefreshTokenFailure(liveData.getApiException());
                }
                break;

            default:
                break;


        }


    }


    @Override
    public void refreshTokenResponse(DataWrapper liveData) {
        if (liveData.getData() != null) {
            _generalCallback.onRefreshTokenSucess((Login) liveData.getData());
        } else {
            _generalCallback.onRefreshTokenFailure(liveData.getApiException());
        }
    }

    /*
    Common interface between SHell app and Login Kit library for API Response Callbacks
     */
    public interface GenericCallbacks {

        public void onLoginSuccess(Login liveData);


        public void onLoginFailure(CustomException apiException);


        public void onProfileSuccess(List<Profile> lstProfiles);


        public void onProfileFailure(CustomException apiException);

        public void onProfileLoginSuccess(Login data);

        public void onProfileLoginFailure(CustomException apiException);

        public void onRefreshTokenSucess(Login data);

        public void onRefreshTokenFailure(CustomException apiException);
    }
}
