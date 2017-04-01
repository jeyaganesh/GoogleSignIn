package com.devlup.jeyaganesh.signin;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiActivity;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private SignInButton btnsignin;
    private Button btnsignout;
    private TextView txtstatus, txtname, txtemail;
    private GoogleApiClient gac;
    private ImageView i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnsignin = (SignInButton) findViewById(R.id.btbsignin);
        btnsignout = (Button) findViewById(R.id.btnsignout);
        i = (ImageView) findViewById(R.id.imageView);
        txtemail = (TextView) findViewById(R.id.txtemail);
        txtname = (TextView) findViewById(R.id.txtname);
        btnsignin.setOnClickListener(this);
        btnsignin.setSize(SignInButton.SIZE_STANDARD);
        btnsignout.setOnClickListener(this);
        btnsignout.setVisibility(View.GONE);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();
        gac = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    private void signout() {
        Auth.GoogleSignInApi.signOut(gac).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        //txtstatus.setText("signout success");
                        Snackbar.make(findViewById(R.id.content), "signout success", Snackbar.LENGTH_LONG)
                                .show();
                        btnsignout.setVisibility(View.GONE);
                    }
                }
        );
    }

    private void signin() {
        Intent signinIntent = Auth.GoogleSignInApi.getSignInIntent(gac);
        startActivityForResult(signinIntent, 1);
    }

    protected void onActivityResult(int requestcode, int resultcode, Intent data) {
        super.onActivityResult(requestcode, resultcode, data);
        if (requestcode == 1) {
            GoogleSignInResult gsr = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(gsr);
        }
    }

    private void handleSignInResult(GoogleSignInResult gsr) {
        if (gsr.isSuccess()) {
            GoogleSignInAccount acct = gsr.getSignInAccount();
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            //  i.setImageURI(null);
            //  txtstatus.setText(personPhoto.toString());
            Picasso.with(this).load(personPhoto).into(i);
            i.setImageURI(personPhoto);
            txtemail.setText(acct.getEmail().toString());
            txtname.setText(acct.getDisplayName().toString());
            btnsignout.setVisibility(View.VISIBLE);
            Snackbar.make(findViewById(R.id.content), "authentication success", Snackbar.LENGTH_LONG)
                    .show();

        } else
            //    txtstatus.setText("authentication failure");
            // Snackbar snackbar=new Snackbar.make(CoordinatorLayout,
            Snackbar.make(findViewById(R.id.content), "authentication failure", Snackbar.LENGTH_LONG)
                    .show();
        //snackbar.show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btbsignin:
                signin();
                break;
            case R.id.btnsignout:
                signout();
                break;

        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
