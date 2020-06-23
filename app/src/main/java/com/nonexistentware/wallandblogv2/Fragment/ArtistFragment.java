package com.nonexistentware.wallandblogv2.Fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nonexistentware.wallandblogv2.R;
import com.squareup.picasso.Picasso;


public class ArtistFragment extends Fragment {

    private ImageView userImage;
    private TextView userMail, userName, removeAccountBtn, removeUserData, signoutBtn;

    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseUser currentUser;
    private DatabaseReference reference;

    private String emailId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View itemView = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_artist, container, false);

        userImage = itemView.findViewById(R.id.user_profile_image);
        userMail = itemView.findViewById(R.id.user_profile_mail);
        userName = itemView.findViewById(R.id.user_profile_name);

//        removeAccountBtn = findViewById(R.id.user_remove_account); //remove only account
        removeUserData = itemView.findViewById(R.id.user_remove_data); //remove only user data, not account
        signoutBtn = itemView.findViewById(R.id.user_sign_out);

//        progressBar = findViewById(R.id.user_profile_progress);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference();

//        emailId = getIntent().getStringExtra("userId");

//        auth = FirebaseAuth.getInstance();
//        user = auth.getCurrentUser();
//        firebaseDatabase = FirebaseDatabase.getInstance();
//        firebaseStorage = FirebaseStorage.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
//        storageReference = FirebaseStorage.getInstance().getReference();


        if (auth.getCurrentUser() != null) {
//            reference = database.getReference().child(Common.STR_CLOUD_NOTE).child(auth.getCurrentUser().getUid());
            reference = FirebaseDatabase.getInstance().getReference()
                    .child("Users").child(auth.getCurrentUser().getUid());
        }

        signoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        signoutBtn.setVisibility(View.INVISIBLE);
                        Picasso.with(getContext())
                                .load(R.drawable.user_account_white)
                                .into(userImage);
                    }
                });

            }
        });

        loadUserData();
//        checkUserOnline();

        return itemView;
    }

    private void loadUserData() {
        if (auth.getCurrentUser() != null) {
            userMail.setText(currentUser.getEmail());
            userName.setText(currentUser.getDisplayName());
            Picasso.with(getContext())
                    .load(currentUser.getPhotoUrl())
                    .into(userImage);
        } else {
            if (auth.getCurrentUser() == null) {
                userName.setText("Offline");
                userMail.setText("Offline");
                Picasso.with(getContext())
                        .load(R.drawable.user_account_white)
                        .into(userImage);
            }
        }
//

    }

    private void checkUserOnline() {
        if (auth.getCurrentUser() == null) {
            userName.setText("");
            userMail.setText("");
            Picasso.with(getContext())
                    .load(R.drawable.user_account_white)
                    .into(userImage);
        }
    }

    @Override
    public void onStart() {
        super.onStart();


    }
}
