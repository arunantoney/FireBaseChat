package com.inapp.firebasechat.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import com.inapp.firebasechat.R;
import com.inapp.firebasechat.model.User;
import com.inapp.firebasechat.utils.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Inappian on 3/15/2016.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvRegister, tvSIgnIn;
    private EditText etEmail, etPwd;
    private EditText etRegEmail, etRegPwd, etName, etPhone;
    private LinearLayout llSignIn, llRegister;
    private Button btnSignIn;
    private Context context;
    private Firebase ref, userRef;
    private Firebase.AuthStateListener mAuthStateListener;
    private String name;
    private Button btnRegister;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        context = this;
        init();

    }

    private void init() {
        etEmail = (EditText) findViewById(R.id.et_email);
        etPwd = (EditText) findViewById(R.id.et_password);
        etRegEmail = (EditText) findViewById(R.id.et_reg_email);
        etRegPwd = (EditText) findViewById(R.id.et_reg_pwd);
        etPhone = (EditText) findViewById(R.id.et_reg_phone);
        etName = (EditText) findViewById(R.id.et_reg_name);
        tvSIgnIn = (TextView) findViewById(R.id.tv_signin);
        tvRegister = (TextView) findViewById(R.id.tv_register);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        llSignIn = (LinearLayout) findViewById(R.id.layout_login);
        llRegister = (LinearLayout) findViewById(R.id.layou_Register);

        btnSignIn.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        tvSIgnIn.setOnClickListener(this);

        ref = new Firebase(Constants.BASE_URl);
        userRef = ref.child("User");
        userRef.keepSynced(true);

        mAuthStateListener = new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {

                setAuthenticatedUser(authData);
            }
        };
        ref.addAuthStateListener(mAuthStateListener);
    }

    private void setAuthenticatedUser(AuthData authData) {
        if (authData != null)
            name = authData.getUid();
    }

    @Override
    public void onClick(View v) {
        if (v == btnSignIn) {
            loginWithPassword();
        } else if (v == btnRegister) {
            createUser();
        } else if (v == tvSIgnIn) {
            llSignIn.setVisibility(View.VISIBLE);
            llRegister.setVisibility(View.GONE);
        } else if (v == tvRegister) {
            llSignIn.setVisibility(View.GONE);
            llRegister.setVisibility(View.VISIBLE);
        }

        //
    }

    private void createUser() {
        ref.createUser(etRegEmail.getText().toString(), etRegPwd.getText().toString(), new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                System.out.println("Successfully created user account with uid: " + result.get("uid"));
                User user = new User(etName.getText().toString(), etRegEmail.getText().toString(), etPhone.getText().toString(), result.get("uid").toString());
                HashMap<String, Object> userDetail = new HashMap<>();
                Map<String, Object> hashtaghMap = new ObjectMapper().convertValue(user, Map.class);
                userDetail.put(result.get("uid").toString(), hashtaghMap);
                userRef.updateChildren(userDetail);
showUserListActivity();
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                // there was an error
            }
        });
    }

    private void showUserListActivity() {
        Intent intent=new Intent(context,UserListActivity.class);
        startActivity(intent);
    }

    private void loginWithPassword() {
        ref.authWithPassword(etEmail.getText().toString(), etPwd.getText().toString(), new AuthResultHandler("password"));
    }

    private class AuthResultHandler implements Firebase.AuthResultHandler {

        private final String provider;

        public AuthResultHandler(String provider) {
            this.provider = provider;
        }

        @Override
        public void onAuthenticated(AuthData authData) {

            Log.i("------", provider + " auth successful");
         //   setAuthenticatedUser(authData);
            showUserListActivity();
        }

        @Override
        public void onAuthenticationError(FirebaseError firebaseError) {

        }
    }
}
