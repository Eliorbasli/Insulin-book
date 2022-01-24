package dev.eliorba.insulinbook1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import dev.eliorba.insulinbook1.Adapters.FoodAdapter;
import dev.eliorba.insulinbook1.Models.Food;

public class User_profile_Activity extends AppCompatActivity {


    TextView    tvUserName;
    TextView    tvUserEmail;
    ImageView   userImageView;
    Button      btnSignOut;
    FloatingActionButton btnAdd;

    FirebaseAuth mAuth;

    RecyclerView recyclerView;
    ArrayList<Food> foodArrayList;
    FoodAdapter foodAdapter;
    FirebaseFirestore db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        getSupportActionBar().hide();


        tvUserName = findViewById(R.id.userName);
        tvUserEmail = findViewById(R.id.userEmail);
        userImageView = findViewById(R.id.userImage);
        btnSignOut = findViewById(R.id.btnLogout);
        btnAdd = findViewById(R.id.btnAdd);

        recyclerView = findViewById(R.id.RC_items);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        foodArrayList = new ArrayList<Food>();

        foodAdapter = new FoodAdapter(User_profile_Activity.this , foodArrayList);

        recyclerView.setAdapter(foodAdapter);

        EventChangeListener();

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


        btnAdd.setOnClickListener( view -> {
            Intent intent = new Intent(User_profile_Activity.this , NewRecordActivity.class);
            startActivity(intent);
        });












    }

    private void EventChangeListener() {

        db.collection("items").orderBy("title", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if(error != null){

                            Log.e("Firestore error" , error.getMessage());
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()){

                            if(dc.getType() == DocumentChange.Type.ADDED){

                               foodArrayList.add(dc.getDocument().toObject(Food.class));
                            }

                            foodAdapter.notifyDataSetChanged();

                        }


                    }
                });
    }
}