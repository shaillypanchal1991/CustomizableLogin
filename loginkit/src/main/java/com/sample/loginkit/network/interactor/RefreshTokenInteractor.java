package com.sample.loginkit.network.interactor;

import com.sample.loginkit.BuildConfig;
import com.sample.loginkit.models.Login;
import com.sample.loginkit.network.apiUtils.APIRequestInterface;
import com.sample.loginkit.network.apiUtils.DataRepository;
import com.sample.loginkit.network.generic.GenericRefreshTokenHandler;
import com.sample.loginkit.utils.StringConstants;

import retrofit2.Call;


/*
 *
 * Interactor classes are for implementing business logic .
 * Creates API requests
 * */
public class RefreshTokenInteractor extends GenericRefreshTokenHandler<Login> {

    private APIRequestInterface refreshTokenService = DataRepository.getInstance().getApiRequest();
    private String refreshToken, granttype;

    public static RefreshTokenInteractor createInstance(String token, String granttype) {

        RefreshTokenInteractor refreshTokenInteractor = new RefreshTokenInteractor();
        refreshTokenInteractor.refreshToken = token;
        refreshTokenInteractor.granttype = granttype;

        return refreshTokenInteractor;
    }

    public void onRefreshTokenRequest(GenericRefreshTokenHandler.ITokenResponseStatus status) {

        doRequest(status);
    }


    @Override
    protected Call<Login> makeRequest() {
        return refreshTokenService.refreshToken(StringConstants.BASIC_PREFIX + StringConstants.APIKEY, granttype, refreshToken);
    }
}
