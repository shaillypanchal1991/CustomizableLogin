package com.sample.customizableloginsample.views;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.widget.EditText;

import com.sample.customizableloginsample.R;
import com.sample.customizableloginsample.adapters.ProfileRecyclerViewAdapter;
import com.sample.customizableloginsample.app.ShellApplication;
import com.sample.customizableloginsample.databinding.ActivityProfilesBinding;
import com.sample.customizableloginsample.storage.DataStore;
import com.sample.customizableloginsample.utils.AutoFitGridLayoutManager;
import com.sample.customizableloginsample.utils.GridItemSpacingDecorator;
import com.sample.loginkit.listeners.CallbackHelper;
import com.sample.loginkit.models.Login;
import com.sample.loginkit.models.Profile;
import com.sample.loginkit.network.error.CustomException;

import java.util.List;

public class ProfilesActivity extends AppCompatActivity implements CallbackHelper.GenericCallbacks, ProfileRecyclerViewAdapter.profileClickListener {
    ActivityProfilesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(ProfilesActivity.this, R.layout.activity_profiles);
        binding.setLifecycleOwner(this);


        binding.recyclerView.setLayoutManager(new AutoFitGridLayoutManager(this, 200));

        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        binding.recyclerView.addItemDecoration(new GridItemSpacingDecorator(2, dpToPx(10), true));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());

        makeProfilesRequest();
       // ShellApplication.getCommonListener().loadProfiles(this, "Profiles", DataStore.getInstance().fetchUserSessionDetails().getAccessToken());


    }

    public  void makeProfilesRequest(){
        ShellApplication.getCommonListener().loadProfiles(this, "Profiles", DataStore.getInstance().fetchUserSessionDetails().getAccessToken());


    }

    private void populateData(List<Profile> profiles) {


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


        ShellApplication.getCommonListener().loginWithProfile(this,"ProfileLogin","password", DataStore.getInstance().fetchUserName(), DataStore.getInstance().fetchPassword(), profile.getId(), pin) ;

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


        ShellApplication.getCommonListener().refreshToken(this,"RefreshToken",DataStore.getInstance().fetchUserSessionDetails().getRefreshToken());

    }

    @Override
    public void onProfileLoginSuccess(Login data) {
        Intent in = new Intent(ProfilesActivity.this, HomeActivity.class);
        startActivity(in);
    }


    @Override
    public void onProfileLoginFailure(CustomException apiException) {

    }

    @Override
    public void onRefreshTokenFailure(CustomException apiException) {


    }

    @Override
    public void onRefreshTokenSucess(Login data) {
        DataStore.getInstance().storeUserSessionDetails(data);
      makeProfilesRequest();


    }
}
