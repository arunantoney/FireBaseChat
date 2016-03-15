package com.inapp.firebasechat.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.inapp.firebasechat.R;
import com.inapp.firebasechat.utils.Constants;

import java.util.Map;

/**
 * Created by Inappian on 3/15/2016.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText tvName, tvPwd;
    private Button btnSignIn;
    private Context context;
    private Firebase ref,userRef;
    private Firebase.AuthStateListener mAuthStateListener;
    private String name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        context=this;
        init();

    }

    private void init() {
        tvName= (EditText) findViewById(R.id.tv_name);
        tvPwd= (EditText) findViewById(R.id.tv_password);
        btnSignIn= (Button) findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(this);
        ref=new Firebase(Constants.BASE_URl);
        userRef=ref.child("User");
        mAuthStateListener = new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {

                setAuthenticatedUser(authData);
            }
        };
        ref.addAuthStateListener(mAuthStateListener);
    }

    private void setAuthenticatedUser(AuthData authData) {
        if(authData!=null)
        name = authData.getUid();
    }

    @Override
    public void onClick(View v) {
      loginWithPassword();
        //createUser();
    }

    private void createUser() {
        ref.createUser(tvName.getText().toString(), tvPwd.getText().toString(), new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                System.out.println("Successfully created user account with uid: " + result.get("uid"));
            }
            @Override
            public void onError(FirebaseError firebaseError) {
                // there was an error
            }
        });
    }

    private void loginWithPassword() {
        ref.authWithPassword(tvName.getText().toString(), tvPwd.getText().toString(), new AuthResultHandler("password"));
    }

    private class AuthResultHandler implements Firebase.AuthResultHandler {

        private final String provider;

        public AuthResultHandler(String provider) {
            this.provider = provider;
        }

        @Override
        public void onAuthenticated(AuthData authData) {

            Log.i("------", provider + " auth successful");
            setAuthenticatedUser(authData);
        }

        @Override
        public void onAuthenticationError(FirebaseError firebaseError) {

        }
    }
}
