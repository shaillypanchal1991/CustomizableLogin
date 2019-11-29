package com.sample.customizableloginsample.views;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.service.autofill.Dataset;
import android.text.InputType;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.sample.customizableloginsample.R;
import com.sample.customizableloginsample.adapters.ProfileRecyclerViewAdapter;
import com.sample.customizableloginsample.app.ShellApplication;
import com.sample.customizableloginsample.databinding.ActivityProfilesBinding;
import com.sample.customizableloginsample.storage.DataStore;
import com.sample.customizableloginsample.utils.AutoFitGridLayoutManager;
import com.sample.customizableloginsample.utils.GridItemSpacingDecorator;
import com.sample.customizableloginsample.utils.NetworkUtils;
import com.sample.loginkit.analytics.AnalyticsServiceManager;
import com.sample.loginkit.listeners.CallbackHelper;
import com.sample.loginkit.models.Login;
import com.sample.loginkit.models.Profile;
import com.sample.loginkit.network.apiUtils.DataRepository;
import com.sample.loginkit.network.error.CustomException;

import java.util.HashMap;
import java.util.List;

public class ProfilesActivity extends AppCompatActivity implements CallbackHelper.GenericCallbacks, ProfileRecyclerViewAdapter.profileClickListener {
    ActivityProfilesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.WHITE);
        super.onCreate(savedInstanceState);


        binding = DataBindingUtil.setContentView(ProfilesActivity.this, R.layout.activity_profiles);
        binding.setLifecycleOwner(this);


        binding.recyclerView.setLayoutManager(new AutoFitGridLayoutManager(this, 200));

        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        binding.recyclerView.addItemDecoration(new GridItemSpacingDecorator(2, dpToPx(10), true));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());

        makeProfilesRequest();


    }

    public void makeProfilesRequest() {

        if (NetworkUtils.isNetworkConnected(this)) {
            ShellApplication.getCommonListener().loadProfiles(this, "Profiles", DataStore.getInstance().fetchUserSessionDetails().getAccessToken());

        } else {
            Toast.makeText(this, "You are not connected to internet.", Toast.LENGTH_SHORT).show();
        }
    }

    private void populateData(List<Profile> profiles) {


        DataStore.getInstance().cacheProfileDetails(profiles);
        ProfileRecyclerViewAdapter profileRecyclerViewAdapter = new ProfileRecyclerViewAdapter(profiles, ProfilesActivity.this);

        binding.setProfileViewAdapter(profileRecyclerViewAdapter);


    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));

    }

    @Override
    public void onProfileClicked(final Profile profile) {


        if (profile.getHasPin()) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Enter PIN");

            final EditText input = new EditText(this);

            input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            builder.setView(input);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    loginwithProfileID(profile, Integer.valueOf(input.getText().toString()));

                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        } else {
            loginwithProfileID(profile, 0);
        }
    }


    public void loginwithProfileID(Profile profile, int pin) {
        //LogUtils.debug(TAG, "Profile Nick Name : " + profile.getNickname());

        if (NetworkUtils.isNetworkConnected(this)) {
            ShellApplication.getCommonListener().loginWithProfile(this, "ProfileLogin", "password", DataStore.getInstance().fetchUserName(), DataStore.getInstance().fetchPassword(), profile.getId(), pin);
        } else {
            Toast.makeText(this, "You are not connected to internet ", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onLoginSuccess(Login liveData) {

    }

    @Override
    public void onLoginFailure(CustomException apiException) {

    }

    @Override
    public void onProfileSuccess(List<Profile> lstProfiles) {

        populateData(lstProfiles);
    }

    @Override
    public void onProfileFailure(CustomException apiException) {

        if (NetworkUtils.isNetworkConnected(this)) {
            ShellApplication.getCommonListener().refreshToken(this, "RefreshToken", DataStore.getInstance().fetchUserSessionDetails().getRefreshToken());
        } else {
            Toast.makeText(this, "You are not connected to internet ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onProfileLoginSuccess(Login data) {

        Intent in = new Intent(ProfilesActivity.this, HomeActivity.class);
        startActivity(in);


        HashMap<String,String> eventParams = new HashMap<String,String>();
        eventParams.put("username", DataStore.getInstance().fetchUserName());
        eventParams.put("isRememberMe",String.valueOf(DataStore.getInstance().fetchRememberMe()));
        eventParams.put("isProfile","true");
        eventParams.put("responseObject",data.toString());
        AnalyticsServiceManager.getInstance().pushAnalyticsEvent("login_success",eventParams);

        finish();
    }


    @Override
    public void onProfileLoginFailure(CustomException apiException) {
        HashMap<String,String> eventParams = new HashMap<String,String>();
        eventParams.put("username", DataStore.getInstance().fetchUserName());
        eventParams.put("isRememberMe",String.valueOf(DataStore.getInstance().fetchRememberMe()));
        eventParams.put("isProfile","true");
        eventParams.put("errorResponse",apiException.getErrorCode() +"");
        AnalyticsServiceManager.getInstance().pushAnalyticsEvent("login_failure",eventParams);

        Toast.makeText(this,"Unable to fetch profiles",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefreshTokenFailure(CustomException apiException) {

        Toast.makeText(this,"There seems to be some error",Toast.LENGTH_SHORT).show();
        HashMap<String,String> eventParams = new HashMap<String,String>();
        eventParams.put("username", DataStore.getInstance().fetchUserName());
        eventParams.put("error", apiException.getErrorMessage(apiException.getErrorCode()));

        AnalyticsServiceManager.getInstance().pushAnalyticsEvent("refresh_session_failure",eventParams);

    }

    @Override
    public void onRefreshTokenSucess(Login data) {

        DataStore.getInstance().storeUserSessionDetails(data);
        makeProfilesRequest();


        HashMap<String,String> eventParams = new HashMap<String,String>();
        eventParams.put("username", DataStore.getInstance().fetchUserName());
        AnalyticsServiceManager.getInstance().pushAnalyticsEvent("refresh_session_success",eventParams);


    }
}
