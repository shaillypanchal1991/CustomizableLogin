package com.sample.customizableloginsample.views;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import com.sample.customizableloginsample.R;
import com.sample.customizableloginsample.databinding.ActivityHomeBinding;
import com.sample.customizableloginsample.fragments.AccountFragment;
import com.sample.customizableloginsample.fragments.HomeFragment;
import com.sample.customizableloginsample.fragments.SearchFragment;
import com.sample.customizableloginsample.fragments.SettingsFragment;
import com.sample.customizableloginsample.helper.BottomNavigationBehavior;
import com.sample.customizableloginsample.utils.StringConstants;
import com.sample.loginkit.init.RootLoginController;
import com.sample.loginkit.utils.LogUtils;


import java.util.HashMap;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {//, AnalyticsServiceManager.AnalyticsEventInterface {

    ActivityHomeBinding binding;
    private String TAG = HomeActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.WHITE);


        super.onCreate(savedInstanceState);


        initializeLoginKit();

        binding = DataBindingUtil.setContentView(HomeActivity.this, R.layout.activity_home);
        binding.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) binding.navigation.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());
        loadFragment(new HomeFragment());

    }

    private void initializeLoginKit() {

        RootLoginController rootLoginController = RootLoginController.init();
        rootLoginController.customConfiguration.isLoggingEnable = true;
        rootLoginController.customConfiguration.retryCount = 2;
        rootLoginController.customConfiguration.domainURL = StringConstants.DOMAIN_URL;
        rootLoginController.loadLoginKit(this, rootLoginController);
        //AnalyticsServiceManager.getInstance().setAnalyticsEventListener(this);


    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    fragment = new HomeFragment();
                    loadFragment(fragment);
                    return true;

                case R.id.navigation_search:

                    fragment = new SearchFragment();
                    loadFragment(fragment);
                    return true;

                case R.id.navigation_account:

                    fragment = new AccountFragment();
                    loadFragment(fragment);
                    return true;

                case R.id.navigation_settings:

                    fragment = new SettingsFragment();
                    loadFragment(fragment);
                    return true;


            }

            return false;
        }
    };

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    private void loadFragment(Fragment fragment) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    /*@Override
    public void pushEvent(String name, HashMap<String, String> eventProperties) {
        LogUtils.debug(TAG, "Analytics Event : " + name + " Params :" + eventProperties.toString());
    }*/
}
