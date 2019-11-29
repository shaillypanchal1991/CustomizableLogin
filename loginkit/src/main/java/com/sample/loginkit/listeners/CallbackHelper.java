package com.sample.loginkit.listeners;

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
import com.sample.loginkit.utils.StringConstants;

import java.util.List;

/*


This generic class is responsible for fetching the generic response of all the APIs received in
the GenericRequestHandler class and converting the generic response
to model wise response data for the activities in form of callbacks.


 */
public class CallbackHelper implements GenericRequestHandler.IResponseStatus, GenericRefreshTokenHandler.ITokenResponseStatus {

    private GenericCallbacks _generalCallback;
    private String _requestType;


    /* Should be called to perform login
    @param requestType : APIName
    @param genericCallback : Interface instance
    @param username : Email id entered by user
    @param password : Password entered by user

     */
    public void loginwithCredentials(String requestType, CallbackHelper.GenericCallbacks genericCallback, String username, String password) {

        _generalCallback = genericCallback;
        _requestType = requestType;

        LoginInteractor.createInstance(StringConstants.APIKEY, StringConstants.PASSWORD, username, password).onAuthRequest(CallbackHelper.this, DataRepository.getInstance().getApiRequest());

    }

    /* Should be called to perform loading of profiles of a particular user

     */

    public void loadProfiles(String requestType, CallbackHelper.GenericCallbacks genericCallback, String accessToken) {

        _generalCallback = genericCallback;
        _requestType = requestType;
        ProfileInteractor.createInstance("bearer " + accessToken).onProfilesRequest(CallbackHelper.this, DataRepository.getInstance().getApiRequest());

    }

    /* Should be called when user clicks on a profile and needs to login with that particula profile

     */
    public void loginWithProfile(String requestType, CallbackHelper.GenericCallbacks genericCallback, String granttype, String username, String password, String profileid, int pin) {

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
    public void refreshToken(String requestType, CallbackHelper.GenericCallbacks genericCallbacks, String refreshToken) {
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

        if (_requestType.equals("Login")) {

            if (liveData.getData() != null) {
                _generalCallback.onLoginSuccess((Login) liveData.getData());
            } else {
                _generalCallback.onLoginFailure(liveData.getApiException());
            }


        }

        else if (_requestType.equals("Profiles")) {

            if (liveData.getData() != null) {
                _generalCallback.onProfileSuccess((List<Profile>) liveData.getData());
            } else {
                _generalCallback.onProfileFailure(liveData.getApiException());
            }
        }

        else if (_requestType.equals("ProfileLogin")) {

            if (liveData.getData() != null) {
                _generalCallback.onProfileLoginSuccess((Login) liveData.getData());
            } else {
                _generalCallback.onProfileLoginFailure(liveData.getApiException());
            }
        }

        else if (_requestType.equals("RefreshToken")) {

            if (liveData.getData() != null) {
                _generalCallback.onRefreshTokenSucess((Login) liveData.getData());
            } else {
                _generalCallback.onRefreshTokenFailure(liveData.getApiException());
            }
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
