package com.example.mialdebu.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mialdebu.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegisterActivity extends AppCompatActivity {


    ImageView userPhoto;
    static int PReqCode = 1;
    static int REQUESCODE = 1;
    Uri  pickedImgUri;

    private EditText userMail, userPassword, userPassword2, username,DNI;
    private ProgressBar loadingProgress;
    private Button regBtn;

    private FirebaseAuth mAuth;
     FirebaseDatabase  mDatabase;
    private DatabaseReference mDataRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        //inu view

        DNI = findViewById(R.id.regDNI);
        userMail = findViewById(R.id.regMail);
        username = findViewById(R.id.regName);
        userPassword = findViewById(R.id.regPassword);
        userPassword2 = findViewById(R.id.regConfirmPassword);
        userPhoto = findViewById(R.id.regUserPhoto);
        loadingProgress = findViewById(R.id.progressBar);
        regBtn = findViewById(R.id.regButton);
        loadingProgress.setVisibility(View.INVISIBLE);


        mDatabase = FirebaseDatabase.getInstance();
        mDataRef = mDatabase.getReference().child("usuarios");
        mAuth = FirebaseAuth.getInstance();


        setupActionBar();
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                regBtn.setVisibility(View.INVISIBLE);
                loadingProgress.setVisibility(View.VISIBLE);
                final String Email = userMail.getText().toString();
                final String Password = userPassword.getText().toString();
                final String Password2 = userPassword2.getText().toString();
                final String userName = username.getText().toString();
                final String nDNI = DNI.getText().toString();

                if(Email.isEmpty() || userName.isEmpty() || Password.isEmpty() || !Password.equals(Password2)  || nDNI.isEmpty())
                {

                    showMessage("Porfavor Revise los datos ingresados");
                    regBtn.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.INVISIBLE);

                }
                else {

                    if (Password.length() < 6) {
                        showMessage("La contraseña debe tener como minimo 6 digitos");
                        regBtn.setVisibility(View.VISIBLE);
                        loadingProgress.setVisibility(View.INVISIBLE);


                    } else
                    {


                        CreateUserAcc(Email, userName, Password,nDNI);
                    }
                }

            }
        });

        userPhoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick (View view){

                if(Build.VERSION.SDK_INT >=22){

                    checkAndRequestForPermission();

                }
                else{
                    openGallery();
                }


            }

        });
    }

    private void setupActionBar() {

        ActionBar actionBar = getSupportActionBar();;


        if (actionBar != null)
        {

            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Registro");


        }


    }

    private void CreateUserAcc(String email, final String userName, String password, final String DNI)
    {


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isComplete()){
                            showMessage("Cuenta creada");

                            UpdateAccInfo(userName,pickedImgUri,mAuth.getCurrentUser());
                            FirebaseUser currentUser= mAuth.getCurrentUser();
                            if (currentUser != null) {
                                mDataRef.child(currentUser.getUid()).child("DNI").setValue(DNI);
                            }
                            UpdateUi();

                        }
                        else
                        {

                            showMessage("Fallo Creando la cuenta" + task.getException().getMessage());
                            regBtn.setVisibility(View.VISIBLE);
                            loadingProgress.setVisibility(View.INVISIBLE);

                        }
                    }
                });

    }

    private void UpdateAccInfo(final String userName, Uri pickedImgUri, final FirebaseUser currentUser) {

        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("users_photos");
        final StorageReference imageFilePath = mStorage.child(pickedImgUri.getLastPathSegment());
        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                //la imagen se subio correctamente

                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {//

                        //el archivo uri se descargo
                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(userName)
                                .setPhotoUri(uri)
                                .build();

                        currentUser.updateProfile(profileUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            //se actualizo la informacion del usuario
                                            showMessage("Registro Completo");
                                            UpdateUi();

                                        }
                                    }
                                });


                    }
                });



            }
        });


    }

    private void UpdateUi() {



        Intent homeActivity = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(homeActivity);
        finish();

    }

    private void showMessage(String message) {

        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();

    }

    private void openGallery() {
        //TODO: open gallery intent and wait for user to pick an image!

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESCODE);

    }

    private void checkAndRequestForPermission() {
        if(ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                if (ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)){

                    Toast.makeText(RegisterActivity.this, "Acepte por permisos requeridos", Toast.LENGTH_SHORT).show();
                }
                else {
                ActivityCompat.requestPermissions(RegisterActivity.this,
                                                            new String []{Manifest.permission.READ_EXTERNAL_STORAGE},
                                                            PReqCode);



                }
        }
        else{

            openGallery();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == REQUESCODE && data != null){

            // the user has uploaded the photo
            pickedImgUri = data.getData();
            userPhoto.setImageURI(pickedImgUri);


        }

    }
}
