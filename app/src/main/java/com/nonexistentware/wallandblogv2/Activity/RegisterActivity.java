package com.nonexistentware.wallandblogv2.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nonexistentware.wallandblogv2.R;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference fUserDatabase;

    private ImageView userImg;
    private TextView createUserBtn, toLogin;
    private EditText userMail, userPass, userName;

    ProgressBar progressBar;

    Uri pickedImageUri;

    static int PReqCode = 1;
    static int REQUESCODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        createUserBtn = findViewById(R.id.user_reg_btn);

        userImg = findViewById(R.id.user_image_reg);

        userMail = findViewById(R.id.user_email_reg);
        userPass = findViewById(R.id.user_pass_reg);
        userName = findViewById(R.id.user_name_reg);

        toLogin = findViewById(R.id.to_login_screen);

        progressBar = findViewById(R.id.register_progress);

        mAuth = FirebaseAuth.getInstance();
        fUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        userImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 29) {
                    checkAndRequestForPermission();
                } else {
                    openGallery();
                }
            }
        });

        createUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUserBtn.setVisibility(View.INVISIBLE);

                final String name = userName.getText().toString().trim();
                final String mail = userMail.getText().toString().trim();
                final String pass = userPass.getText().toString().trim();

                if (!TextUtils.isEmpty(mail) && !TextUtils.isEmpty(pass)
                && !TextUtils.isEmpty(name)) {
                    createNewUser(name, mail, pass);
                } else {
                    Toast.makeText(getApplicationContext(), "Name, email and password fields can't be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        progressBar.setVisibility(View.INVISIBLE);

    }

    private void createNewUser(final String name, final String mail, String pass) {
        createUserBtn.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.INVISIBLE);
                            fUserDatabase.child(mAuth.getUid()).child("email").setValue(mail);
                            fUserDatabase.child(mAuth.getUid()).child("name").setValue(name);
                            createProfileWithImage(name, pickedImageUri, mAuth.getCurrentUser());
                            Toast.makeText(RegisterActivity.this, "Account successfully created", Toast.LENGTH_SHORT).show();
                        } else {
                            progressBar.setVisibility(View.INVISIBLE);
                            createUserBtn.setVisibility(View.VISIBLE);
                            Toast.makeText(RegisterActivity.this, "ERROR" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateUserInfo(final String name, FirebaseUser currentUser) {
        final UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .setPhotoUri(pickedImageUri)
                .build();
        currentUser.updateProfile(profileUpdate)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
//                            updateUi();
                        }
                    }
                });
    }

    private void createProfileWithImage(final String email, final Uri pickedImageUri, final FirebaseUser currentUser) {
        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("users_photos");
        final StorageReference imageFilePath = mStorage.child(pickedImageUri.getLastPathSegment());
        imageFilePath.putFile(pickedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(final Uri uri) {
                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(email)
                                .setPhotoUri(uri)
                                .build();

                        currentUser.updateProfile(profileUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // user info updated successfully
//                                            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
//                                            showMessage("Register Complete");
                                                updateUi();
                                        }
                                    }
                                });
                    }
                });
            }
        });
    }

    private void updateUi() {
        progressBar.setVisibility(View.VISIBLE);
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESCODE);
    }

    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(RegisterActivity.this, "Please accept for required permission", Toast.LENGTH_SHORT).show();

            } else {
                ActivityCompat.requestPermissions(RegisterActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }

        } else
            openGallery();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null) {

            // the user has successfully picked an image
            // we need to save its reference to a Uri variable
            pickedImageUri = data.getData();
            userImg.setImageURI(pickedImageUri);
        }
    }
}