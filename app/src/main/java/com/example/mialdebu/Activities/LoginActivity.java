package com.example.mialdebu.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mialdebu.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient googleApiClient;


    private static final int SIGN_IN_CODE = 777 ;

    private EditText userMail,userPassword;
    private Button BtnLogin,log_Regbtn;
    private ProgressBar loginProgress;


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDataRef;


    private Intent HomeActivity;
    private  Intent MainActivity;

    SignInButton signInButton ;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        userMail = findViewById(R.id.login_Mail);
        userPassword = findViewById(R.id.Login_Password);
        BtnLogin = findViewById(R.id.LogInBtn);
        loginProgress = findViewById(R.id.Login_Progress);
        HomeActivity = new Intent(this, HomeActivity.class);
        log_Regbtn = findViewById(R.id.Log_RegBtn);

        MainActivity = new Intent(this,MainActivity.class);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        //iniciacion de la...


        //...autenticacion de google
        signInButton =  findViewById(R.id.log_Google);




        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, SIGN_IN_CODE);
                loginProgress.setVisibility(View.VISIBLE);
                BtnLogin.setVisibility(View.INVISIBLE);
                signInButton.setVisibility(View.INVISIBLE);
                log_Regbtn.setVisibility(View.INVISIBLE);

                googleApiClient.connect();
            }
        });


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user !=null){

                    updateUI();
                }

            }
        };

        log_Regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent registerActivity = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(registerActivity);


            }
        });

        loginProgress.setVisibility(View.INVISIBLE);
        BtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginProgress.setVisibility(View.VISIBLE);
                BtnLogin.setVisibility(View.INVISIBLE);

                final String mail = userMail.getText().toString();
                final String userPasword = userPassword.getText().toString();

                if(mail.isEmpty() || userPasword.isEmpty()){

                    showmessage("Por favor verifique los datos ingresados");
                    BtnLogin.setVisibility(View.VISIBLE);
                    loginProgress.setVisibility(View.INVISIBLE);

                }
                else
                {

                     signIn(mail,userPasword);

                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        getSupportActionBar().setTitle("Inicio De Sesion");

        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStop() {
        super.onStop();

        if(mAuthListener != null){

            mAuth.removeAuthStateListener(mAuthListener);


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SIGN_IN_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {
            fireBaseAuthWithGoogle(result.getSignInAccount());
        } else
            Toast.makeText(this, "No se pudo Iniciar Sesion", Toast.LENGTH_SHORT).show();



    }

    private void fireBaseAuthWithGoogle(GoogleSignInAccount signInAccount) {

        AuthCredential credential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);

        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "No se ha podido conectar", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void signIn(String mail, String userPasword) {

        mAuth.signInWithEmailAndPassword(mail,userPasword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    loginProgress.setVisibility(View.INVISIBLE);
                    BtnLogin.setVisibility(View.INVISIBLE);
                    updateUI();


                }
                else
                    showmessage(task.getException().getMessage());
                BtnLogin.setVisibility(View.VISIBLE);
                loginProgress.setVisibility(View.INVISIBLE);


            }
        });

    }

    private void updateUI() {


        startActivity(HomeActivity);
        finish();

    }

    private void showmessage(String s) {

        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

}


