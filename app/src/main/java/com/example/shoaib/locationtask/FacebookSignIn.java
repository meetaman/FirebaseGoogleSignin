package com.example.shoaib.locationtask;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;




public class FacebookSignIn extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "FacebookLogin";





    private TextView mStatusTextView;

    private TextView mDetailTextView;


    // [START declare_auth]

    private FirebaseAuth mAuth;

    // [END declare_auth]



    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_facebook_sign_in);
        mAuth = FirebaseAuth.getInstance();



// Views

        mStatusTextView = (TextView) findViewById(R.id.textView_status);

        mDetailTextView = (TextView) findViewById(R.id.textView_detail);

        findViewById(R.id.button_fbsignout).setOnClickListener(this);



        // [START initialize_auth]

        // Initialize Firebase Auth


        // [END initialize_auth]



        // [START initialize_fblogin]

        // Initialize Facebook Login button

        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.button_fbsignin);

        loginButton.setReadPermissions("email", "public_profile");

        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {

            @Override

            public void onSuccess(LoginResult loginResult) {

                Log.d(TAG, "facebook:onSuccess:" + loginResult);

                handleFacebookAccessToken(loginResult.getAccessToken());

            }



            @Override

            public void onCancel() {

                Log.d(TAG, "facebook:onCancel");

                // [START_EXCLUDE]


                // [END_EXCLUDE]

            }



            @Override

            public void onError(FacebookException error) {

                Log.d(TAG, "facebook:onError", error);

                // [START_EXCLUDE]


                // [END_EXCLUDE]

            }

        });

        // [END initialize_fblogin]
//
//        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
//
//            @Override
//
//            public void onSuccess(LoginResult loginResult) {
//
//                Log.d(TAG, "facebook:onSuccess:" + loginResult);
//
//                handleFacebookAccessToken(loginResult.getAccessToken());
//
//            }
//
//
//
//            @Override
//
//            public void onCancel() {
//
//                Log.d(TAG, "facebook:onCancel");
//
//                // [START_EXCLUDE]
//
//
//                // [END_EXCLUDE]
//
//            }
//
//
//
//            @Override
//
//            public void onError(FacebookException error) {
//
//                Log.d(TAG, "facebook:onError", error);
//
//                // [START_EXCLUDE]
//
//
//                // [END_EXCLUDE]
//
//            }
//
//        });
//
//        // [END initialize_fblogin]


    }

    @Override
    public void onClick(View view) {

    }


    // [START on_start_check_user]

    @Override

    public void onStart() {

        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.

        FirebaseUser currentUser = mAuth.getCurrentUser();

    }

    // [END on_start_check_user]



    // [START on_activity_result]

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);



        // Pass the activity result back to the Facebook SDK

        mCallbackManager.onActivityResult(requestCode, resultCode, data);

    }

    // [END on_activity_result]


    // [START auth_with_facebook]

    private void handleFacebookAccessToken(AccessToken token) {

        Log.d(TAG, "handleFacebookAccessToken:" + token);

        // [START_EXCLUDE silent]


        // [END_EXCLUDE]



        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());

        mAuth.signInWithCredential(credential)

                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override

                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information

                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = mAuth.getCurrentUser();

                            Toast.makeText(FacebookSignIn.this, "Authentication successful",

                                    Toast.LENGTH_SHORT).show();


                        } else {

                            // If sign in fails, display a message to the user.

                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                            Toast.makeText(FacebookSignIn.this, "Authentication failed.",

                                    Toast.LENGTH_SHORT).show();


                        }



                        // [START_EXCLUDE]


                        // [END_EXCLUDE]

                    }

                });

    }

    // [END auth_with_facebook]



    public void signOut() {

        mAuth.signOut();

        LoginManager.getInstance().logOut();


    }





public void SignOutButton(View v)
{

    signOut();

}


}
