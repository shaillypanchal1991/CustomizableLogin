package com.sample.customizableloginsample.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.sample.customizableloginsample.R;
import com.sample.customizableloginsample.app.ShellApplication;
import com.sample.customizableloginsample.databinding.ActivityLoginBinding;
import com.sample.customizableloginsample.storage.DataStore;
import com.sample.customizableloginsample.utils.NetworkUtils;
import com.sample.loginkit.analytics.AnalyticsServiceManager;
import com.sample.loginkit.init.RootLoginController;
import com.sample.loginkit.listeners.CallbackHelper;
import com.sample.loginkit.models.Login;
import com.sample.loginkit.models.Profile;
import com.sample.loginkit.network.error.CustomException;

import java.util.HashMap;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, CallbackHelper.GenericCallbacks,AnalyticsServiceManager.AnalyticsEventInterface {

    ActivityLoginBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.WHITE);
        super.onCreate(savedInstanceState);


        binding = DataBindingUtil.setContentView(LoginActivity.this, R.layout.activity_login);


        binding.btnSignIn.setOnClickListener(this);

        binding.editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isValidEmail(s.toString())) {
                    binding.txtinputEmail.setError("Enter a valid address");
                    binding.btnSignIn.setEnabled(false);
                } else if (binding.editTextPassword.getText().toString().equals("")) {
                    binding.btnSignIn.setEnabled(false);
                    binding.txtinputEmail.setError(null);
                } else {
                    binding.txtinputEmail.setError(null);

                }

            }
        });


        binding.editTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                binding.btnSignIn.setEnabled(false);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isPasswordLengthGreaterThan5(s.toString())) {
                    binding.btnSignIn.setEnabled(false);
                    binding.txtinputPassword.setError("Password is too short");
                } else if (isPasswordLengthGreaterThan120(s.toString())) {
                    binding.btnSignIn.setEnabled(false);
                    binding.txtinputPassword.setError("Password is too long");
                } else if (!isPasswordStrong(s.toString())) {
                    binding.btnSignIn.setEnabled(false);
                    binding.txtinputPassword.setError("Password is too weak");
                } else if (binding.txtinputEmail.getError() != null) {
                    binding.btnSignIn.setEnabled(false);
                    binding.txtinputPassword.setError(null);
                } else {

                    binding.txtinputPassword.setError(null);
                }
            }
        });


        String userName = DataStore.getInstance().fetchUserName();
        boolean isRemembered = DataStore.getInstance().fetchRememberMe();
        if (DataStore.getInstance().fetchRememberMe()) {
            binding.editTextEmail.setText(userName);
            binding.checkBoxRememberMe.setChecked(true);
        } else {
            binding.checkBoxRememberMe.setChecked(isRemembered);
        }



        AnalyticsServiceManager.getInstance().setAnalyticsEventListener(this);
    }


    @Override
    public void onClick(View v) {

        if (NetworkUtils.isNetworkConnected(this)) {
            ShellApplication.getCommonListener().loginwithCredentials(this, "Login", binding.editTextEmail.getText().toString(), binding.editTextPassword.getText().toString());
            HashMap<String,String> eventParams = new HashMap<String,String>();
            eventParams.put("username", binding.editTextEmail.getText().toString());
            eventParams.put("isRememberMe",String.valueOf(binding.checkBoxRememberMe.isChecked()));
            AnalyticsServiceManager.getInstance().pushAnalyticsEvent("login_attempted",eventParams);

        }
        else{
            Toast.makeText(this,"You are not connected to internet ",Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public void onLoginSuccess(Login liveData) {

        Log.e("sdasd", "zdff");
        DataStore.getInstance().storeUserSessionDetails(liveData);
        DataStore.getInstance().storeUserName(binding.editTextEmail.getText().toString());
        DataStore.getInstance().storePassword(binding.editTextPassword.getText().toString());
        DataStore.getInstance().storeRememberMeStatus(binding.checkBoxRememberMe.isChecked());
        Intent intent = new Intent(LoginActivity.this, ProfilesActivity.class);
        startActivity(intent);

        HashMap<String,String> eventParams = new HashMap<String,String>();
        eventParams.put("username", binding.editTextEmail.getText().toString());
        eventParams.put("isRememberMe",String.valueOf(binding.checkBoxRememberMe.isChecked()));
        eventParams.put("isProfile","false");
        eventParams.put("responseObject",liveData.toString());
        AnalyticsServiceManager.getInstance().pushAnalyticsEvent("login_success",eventParams);

        finish();


    }


    /*

    Callbacks of CallbackManager Class
     */
    @Override
    public void onLoginFailure(CustomException apiException) {

        Toast.makeText(this, "The username or password entered is incorrect",Toast.LENGTH_SHORT).show();

        HashMap<String,String> eventParams = new HashMap<String,String>();
        eventParams.put("username", binding.editTextEmail.getText().toString());
        eventParams.put("isRememberMe",String.valueOf(binding.checkBoxRememberMe.isChecked()));
        eventParams.put("isProfile","false");
        eventParams.put("errorResponse",apiException.getErrorCode()+ ": " +apiException.getErrorMessage(apiException.getErrorCode()));
        AnalyticsServiceManager.getInstance().pushAnalyticsEvent("login_failure",eventParams);
    }

    @Override
    public void onProfileSuccess(List<Profile> lstProfiles) {

    }

    @Override
    public void onProfileFailure(CustomException apiException) {

    }


    @Override
    public void onProfileLoginFailure(CustomException apiException) {

    }

    @Override
    public void onRefreshTokenSucess(Login data) {

    }

    @Override
    public void onRefreshTokenFailure(CustomException apiException) {

    }

    @Override
    public void onProfileLoginSuccess(Login data) {

    }



    /*

    Username and password validation checks
     */

    public final boolean isValidEmail(String target) {
        if (target == null || target.equals("")) {
            binding.btnSignIn.setEnabled(false);
            return false;
        } else {
            binding.btnSignIn.setEnabled(true);
            //android Regex to check the email address Validation
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();

        }
    }


    public boolean isPasswordLengthGreaterThan5(String target) {
        if (target == null || target.equals("")) {
            binding.btnSignIn.setEnabled(false);
            return false;
        } else {

            return target.length() > 5;
        }

    }

    public boolean isPasswordLengthGreaterThan120(String target) {
        if (target == null || target.equals("")) {
            binding.btnSignIn.setEnabled(false);
            return false;
        } else {
            binding.btnSignIn.setEnabled(true);
            return target.length() > 120;
        }
    }

    public boolean isPasswordStrong(String target) {
        if (target == null || target.equals("")) {
            binding.btnSignIn.setEnabled(false);
            return false;
        } else {
            binding.btnSignIn.setEnabled(true);
            if (target.equalsIgnoreCase("password") || target.equalsIgnoreCase("qwerty")) {
                return false;
            } else {
                return true;
            }
        }
    }

    @Override
    public void pushEvent(String name, HashMap<String, String> eventProperties) {
        // do the needful
    }
}
