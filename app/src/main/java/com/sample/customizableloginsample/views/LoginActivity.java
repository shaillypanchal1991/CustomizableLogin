package com.sample.customizableloginsample.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.sample.customizableloginsample.R;
import com.sample.customizableloginsample.app.ShellApplication;
import com.sample.customizableloginsample.databinding.ActivityLoginBinding;
import com.sample.customizableloginsample.storage.DataStore;
import com.sample.loginkit.listeners.CallbackHelper;
import com.sample.loginkit.models.Login;
import com.sample.loginkit.models.Profile;
import com.sample.loginkit.network.error.CustomException;

import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, CallbackHelper.GenericCallbacks {

    ActivityLoginBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = DataBindingUtil.setContentView(LoginActivity.this,R.layout.activity_login);


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
    }





    @Override
    public void onClick(View v) {


        ShellApplication.getCommonListener().loginwithCredentials(this, "Login",binding.editTextEmail.getText().toString(), binding.editTextPassword.getText().toString());


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


    }


    /*

    Callbacks of CallbackManager Class
     */
    @Override
    public void onLoginFailure(CustomException apiException) {
        Log.e("sdasd", "zdff");
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
}
