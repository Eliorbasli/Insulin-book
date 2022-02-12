package dev.eliorba.insulinbook1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
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
import dev.eliorba.insulinbook1.Utils.DataManager;

public class Main_Activity extends AppCompatActivity{

    TextView    tvUserName;
    ImageView   userImageView;
    Button      btnSignOut;
    FloatingActionButton btnAdd;
    //androidx.appcompat.widget.SearchView SearchView;
    FirebaseAuth mAuth;

    private RecyclerView recyclerView;

    ArrayList<Food> foodArrayList;
    ArrayList<Food> foodArrayListForSearch;
    FoodAdapter foodAdapter;
    FirebaseFirestore db;
    SearchView searchView;
    DataManager dataManager ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        getSupportActionBar().hide();
        dataManager = new DataManager();
        findView();

        recyclerView.setLayoutManager(new LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false));
        recyclerView.setHasFixedSize(true);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        foodArrayList = new ArrayList<Food>();
        foodArrayListForSearch = new ArrayList<Food>();

        //read from firebase database and save in foodArrayList
        EventChangeListener();

        initMenu();
        foodAdapter = new FoodAdapter(this, Main_Activity.this , foodArrayList);
        recyclerView.setAdapter(foodAdapter);

        btnSignOut.setOnClickListener(view -> {
            mAuth.signOut();
            startActivity(new Intent(Main_Activity.this , LoginActivity.class));
        });

        btnAdd.setOnClickListener( view -> {
            Intent intent = new Intent(Main_Activity.this , NewRecordActivity.class);
            startActivity(intent);
        });

        // Search Option, if search is empty show all items else show all item contain the chars in searchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                foodAdapter = new FoodAdapter(Main_Activity.this, Main_Activity.this , foodArrayList);
                recyclerView.setAdapter(foodAdapter);
                return false;
            }


            //this function check of chars in search filed exist in foodArrayList, if yes , insert them to foodArrayListforSearch
            //and update the recycleView of result.
            @Override
            public boolean onQueryTextChange(String newText) {
                if(!searchView.toString().isEmpty()){
                    foodArrayListForSearch.clear();
                    for(int i = 0 ; i < foodArrayList.size() ; i++){
                        if (foodArrayList.get(i).getTitle().toLowerCase().contains(searchView.getQuery().toString().toLowerCase())){
                            foodArrayListForSearch.add(foodArrayList.get(i));
                        }
                        foodAdapter = new FoodAdapter(Main_Activity.this, Main_Activity.this , foodArrayListForSearch);
                        recyclerView.setAdapter(foodAdapter);
                    }
                }
                else{
                    foodAdapter = new FoodAdapter(Main_Activity.this, Main_Activity.this , foodArrayList);
                    recyclerView.setAdapter(foodAdapter);
                }
                return false;
            }
        });

    }


    private void initMenu (){

        SharedPreferences preferences = getSharedPreferences("MyPrefs" , MODE_PRIVATE);
        String userName = preferences.getString("username" , " ") ;
        String userPhotoUrl = preferences.getString("userPhoto" , " ") ;
        tvUserName.setText(userName);

        Glide.with(this).load(userPhotoUrl).into(userImageView);
    }

    private void findView(){
        tvUserName      = findViewById(R.id.userName);
        userImageView   = findViewById(R.id.userImage);
        btnSignOut      = findViewById(R.id.btnLogout);
        btnAdd          = findViewById(R.id.btnAdd);
        recyclerView    = findViewById(R.id.RC_items);
        searchView = findViewById(R.id.userProfile_searchView);

    }

    //read all items from firebase database , all item save in foodArrayList
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