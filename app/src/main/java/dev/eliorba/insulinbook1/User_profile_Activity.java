package dev.eliorba.insulinbook1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

public class User_profile_Activity extends AppCompatActivity {


    TextView    tvUserName;
    TextView    tvUserEmail;
    ImageView   userImageView;
    Button      btnSignOut;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);


        tvUserName = findViewById(R.id.userName);
        tvUserEmail = findViewById(R.id.userEmail);
        userImageView = findViewById(R.id.userImage);
        btnSignOut = findViewById(R.id.btnLogout);

        mAuth = FirebaseAuth.getInstance();

        SharedPreferences preferences = getSharedPreferences("MyPrefs" , MODE_PRIVATE);

        String userName = preferences.getString("username" , " ") ;
        String userEmail = preferences.getString("userEmail" , " ") ;
        String userPhotoUrl = preferences.getString("userPhoto" , " ") ;

        tvUserName.setText(userName);
        tvUserEmail.setText(userEmail);

        Glide.with(this).load(userPhotoUrl).into(userImageView);

        btnSignOut.setOnClickListener(view -> {
            mAuth.signOut();
            startActivity(new Intent(User_profile_Activity.this , MainActivity.class));
        });
    }
}