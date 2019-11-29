package com.sample.loginkit.network.apiUtils;

import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.sample.loginkit.init.RootLoginController;
import com.sample.loginkit.models.Login;
import com.sample.loginkit.models.Profile;
import com.sample.loginkit.network.NetworkConfiguration;

import com.sample.loginkit.network.generic.DataWrapper;
import com.sample.loginkit.utils.LogUtils;
import com.sample.loginkit.utils.StringConstants;

import java.util.List;

public class DataRepository {

    private String TAG = DataRepository.class.getName();

    private APIRequestInterface apiRequest;

    private static final DataRepository instance = new DataRepository();

    public DataRepository() {
        apiRequest = NetworkConfiguration.getRetrofitInstance().create(APIRequestInterface.class);

    }

    public static DataRepository getInstance() {
        return instance;
    }

    public APIRequestInterface getApiRequest() {
        return apiRequest;
    }


}
