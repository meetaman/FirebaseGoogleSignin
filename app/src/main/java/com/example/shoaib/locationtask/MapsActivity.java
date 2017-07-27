package com.example.shoaib.locationtask;

import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private DatabaseReference lDatabase;
    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "MainActivity";

    int valueentered=0;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

        mAuth = FirebaseAuth.getInstance();
        lDatabase = FirebaseDatabase.getInstance().getReference();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        final         EditText username= (EditText) findViewById(R.id.edit_username);
        final       EditText pass= (EditText) findViewById(R.id.edit_password);
        final       Button signin= (Button) findViewById(R.id.button_signin);
        final        Button signup= (Button) findViewById(R.id.button_signup);
        final String emailtext = username.getText().toString();
        final String passwordtext = pass.getText().toString();
        final TextView Email= (TextView) findViewById(R.id.textView_email);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
//        signin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            signinuser(emailtext,passwordtext);
//            }
//        });

//        signup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            signupuser(emailtext,passwordtext);
//
//            }
//        });



        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String emailtodb = username.getText().toString().trim();

                String passwordtodb = pass.getText().toString().trim();

                //Toast.makeText(MainActivity.this,"Sign up button clickeddd"+emailtodb+passwordtodb, Toast.LENGTH_LONG).show();


                if(!TextUtils.isEmpty(emailtodb) && !TextUtils.isEmpty(passwordtodb)){



                    mAuth.createUserWithEmailAndPassword(emailtodb,passwordtodb).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){


        Toast.makeText(MapsActivity.this, "Signup Successful",
                Toast.LENGTH_SHORT).show();



                            } else {


                                Log.w(TAG, "signInWithEmail:failed", task.getException());
                                Toast.makeText(MapsActivity.this, "Signup Failed",
                                        Toast.LENGTH_SHORT).show();//

                            }

                        }
                    });


                } else {

                    Toast.makeText(MapsActivity.this,"email and password required", Toast.LENGTH_LONG).show();
                }





            }
        });



        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String emailtodb = username.getText().toString().trim();

                String passwordtodb = pass.getText().toString().trim();

                //Toast.makeText(MainActivity.this,"Sign up button clickeddd"+emailtodb+passwordtodb, Toast.LENGTH_LONG).show();


                if(!TextUtils.isEmpty(emailtodb) && !TextUtils.isEmpty(passwordtodb)){



                    mAuth.signInWithEmailAndPassword(emailtodb,passwordtodb).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){


                                Toast.makeText(MapsActivity.this, "Signup Successful",
                                        Toast.LENGTH_SHORT).show();

                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (user != null) {
                                    // Name, email address, and profile photo Url
                                    String checkname = user.getDisplayName();
                                    String checkemail = user.getEmail();
                                    Uri photoUrl = user.getPhotoUrl();
                                    Email.setText(checkemail);
                                    // The user's ID, unique to the Firebase project. Do NOT use this value to
                                    // authenticate with your backend server, if you have one. Use
                                    // FirebaseUser.getToken() instead.
                                    String uid = user.getUid();
                                }



                            } else {


                                Log.w(TAG, "signInWithEmail:failed", task.getException());
                                Toast.makeText(MapsActivity.this, "Signup Failed",
                                        Toast.LENGTH_SHORT).show();//

                            }

                        }
                    });


                } else {

                    Toast.makeText(MapsActivity.this,"email and password required", Toast.LENGTH_LONG).show();
                }





            }
        });





        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String checkname = user.getDisplayName();
            String checkemail = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = user.getUid();
        }


    }




    public void signinuser(String email, String password)
    {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(MapsActivity.this, "Signin Successful",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        else {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(MapsActivity.this, "Signin Failed",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });


//        @Override
//        public void onComplete(@NonNull Task<AuthResult> task) {
//        if (task.isSuccessful()) {
//            // Sign in success, update UI with the signed-in user's information
//            Log.d(TAG, "signInWithEmail:success");
//            FirebaseUser user = mAuth.getCurrentUser();
//            updateUI(user);
//        } else {
//            // If sign in fails, display a message to the user.
//            Log.w(TAG, "signInWithEmail:failure", task.getException());
//            Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
//                    Toast.LENGTH_SHORT).show();
//            updateUI(null);
//        }
//
//        // [START_EXCLUDE]
//        if (!task.isSuccessful()) {
//            mStatusTextView.setText(R.string.auth_failed);
//        }
//        hideProgressDialog();
//        // [END_EXCLUDE]
//    }
//


    }


    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient != null && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mGoogleMap=googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1);
        //mLocationRequest.setFastestInterval(1);
        mLocationRequest.setSmallestDisplacement(0);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}

    @Override
    public void onLocationChanged(Location location)
    {
//        Toast.makeText(this, "Location Changed " + location.getLatitude()
//                + location.getLongitude(), Toast.LENGTH_SHORT).show();

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

        //move map camera
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(30));


    }


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {


                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }


        }
    }

}