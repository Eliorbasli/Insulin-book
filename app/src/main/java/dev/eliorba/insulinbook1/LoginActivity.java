package dev.eliorba.insulinbook1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dev.eliorba.insulinbook1.Models.Food;
import dev.eliorba.insulinbook1.Models.User;
import dev.eliorba.insulinbook1.Utils.DataManager;

public class LoginActivity extends AppCompatActivity {

    SignInButton btnSignIn;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 123;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;

    ArrayList<String> UserList;
    String AccountID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        DataManager dataManager = new DataManager();

        setContentView(R.layout.activity_main);

        btnSignIn = findViewById(R.id.btnSignIn);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        RequestGoogleSignIn();

        btnSignIn.setOnClickListener(view -> {
            signIn();
        });
    }




    private void RequestGoogleSignIn(){
        GoogleSignInOptions gso;
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                //Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken() , account.getId());

                SharedPreferences.Editor editor = getApplicationContext()
                        .getSharedPreferences("MyPrefs" , MODE_PRIVATE)
                        .edit();
                editor.putString("username" ,account.getDisplayName());
                editor.putString("userEmail" , account.getEmail());
                editor.putString("userId" , account.getId());

                Toast.makeText(LoginActivity.this, account.getId(), Toast.LENGTH_SHORT).show();

                AccountID = account.getId();


                editor.putString("userPhoto" , account.getPhotoUrl().toString());
                editor.apply();

            } catch (ApiException e) {

                Toast.makeText(LoginActivity.this, "Authentication Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    //if auth google success go to next activity
    private void firebaseAuthWithGoogle(String idToken , String userid) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            checkNewUser(userid);
//                            Intent intent = new Intent(LoginActivity.this , User_profile_Activity.class);
//                            startActivity(intent);
//                            finish();

                        } else {

                            Toast.makeText(LoginActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void checkNewUser(String userid) {

        //UsersFromDB();

        if(1 == 0 ) {   // list conatin userId
            //if(UserIdList.contains(AccountID))
            Intent intent = new Intent(LoginActivity.this , User_profile_Activity.class);
            startActivity(intent);
            finish();
        }
        //if is new user go to NewActivity and add user to DB;
        else{
            Intent intent = new Intent(LoginActivity.this , newUserActivity.class);
            startActivity(intent);

        }
    }


    private void UsersFromDB() {

        db.collection("users").orderBy("title", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if(error != null){

                            Log.e("Firestore error" , error.getMessage());
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()){

                            if(dc.getType() == DocumentChange.Type.ADDED){

                                UserList.add(dc.getDocument().toString());
                            }

                        }
                    }
                });
    }


}



