package com.example.shoaib.locationtask;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class AccountOptions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_options);



    }

public void gotoGoogleSignIn(View OnClick)
{

    Intent i= new Intent(AccountOptions.this, GoogleSignIn.class);
    startActivity(i);


}


    public void gotoFacebookSignIn(View OnClick)
    {

        Intent i= new Intent(AccountOptions.this, FacebookSignIn.class);
        startActivity(i);


    }

    public void gotoTwitterSignIn(View OnClick)
    {

        Intent i= new Intent(AccountOptions.this, TwitterSignIn.class);
        startActivity(i);


    }

}



