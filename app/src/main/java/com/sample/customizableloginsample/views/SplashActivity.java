package com.sample.customizableloginsample.views;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.sample.customizableloginsample.R;
import com.sample.customizableloginsample.databinding.ActivityMainBinding;
import com.sample.customizableloginsample.storage.DataStore;
import com.sample.loginkit.init.CustomConfiguration;
import com.sample.loginkit.init.RootLoginController;

import java.util.HashMap;


public class SplashActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.WHITE);
        super.onCreate(savedInstanceState);


        initializeLoginKit();


        if (DataStore.getInstance().fetchUserSessionDetails() == null) {

            Intent i = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(i);
            finish();

        } else {

            Intent i = new Intent(SplashActivity.this, ProfilesActivity.class);
            startActivity(i);
            finish();

        }

        binding = DataBindingUtil.setContentView(SplashActivity.this, R.layout.activity_main);

   /*  new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {

                Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(i);
                binding.progressCircular.setVisibility(View.GONE);
                finish();
            }
        }, 2000);*/


    }

    private void initializeLoginKit() {

        RootLoginController rootLoginController = RootLoginController.init();
        rootLoginController.customConfiguration.isLoggingEnable = true;
        rootLoginController.customConfiguration.retryCount = 2;
        rootLoginController.customConfiguration.uiComponents.backgroundColor = "#ffffff";
        rootLoginController.customConfiguration.domainURL = "https://usermgt-staging.shared-svc.bellmedia.ca";

        rootLoginController.loadLoginKit(this, rootLoginController);

        // AnalyticsServiceManager.getInstance().setAnalyticsEventListener(this);


    }


}
