package dev.eliorba.insulinbook1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import dev.eliorba.insulinbook1.Models.User;

public class newUserActivity extends AppCompatActivity {


    TextInputLayout tfHeight;
    TextInputLayout tfWight;
    TextInputLayout tfLongInsulin;
    TextInputLayout tfAge ;
    MaterialButton  saveBtn;
    FirebaseFirestore db;

    RadioGroup rgGender;
    RadioButton selectedRadioButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_new_user);

        SharedPreferences preferences = getSharedPreferences("MyPrefs" , MODE_PRIVATE);
        String userId = preferences.getString("userId" , "");

        findView();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                User user = new User().setAge(Integer.parseInt(tfAge.getEditText().getText().toString()))
                        .setHeight(Integer.parseInt(tfHeight.getEditText().getText().toString()))
                        .setWight(Integer.parseInt(tfWight.getEditText().getText().toString()))
                        .setLongInsulin(Integer.parseInt(tfLongInsulin.getEditText().getText().toString()))
                        .setId(userId);

                uploadUserToDB(user);
            }
        });
    }
    private void findView() {
        tfWight =       findViewById(R.id.NewUser_TF_weight);
        tfHeight =      findViewById(R.id.NewUser_TF_height);
        tfLongInsulin = findViewById(R.id.NewUser_TF_longInsulin);
        tfAge =         findViewById(R.id.NewUser_TF_age);
        saveBtn =     findViewById(R.id.NewUser_BTN_add);
        rgGender = findViewById(R.id.NewUser_RG_gender);

    }

    public void uploadUserToDB(User user) {

        db = FirebaseFirestore.getInstance();

        if(user == null) {
            Toast.makeText(newUserActivity.this, "No user selected", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> item = new HashMap<>();
        item.put("UserId", user.getUserId());
        item.put("Age", user.getAge());
        item.put("Gender", user.getGender());
        item.put("LongInsulin", user.getLongInsulin());
        item.put("Hight", user.getHeight());
        item.put("Wight", user.getWight());

        db.collection("users")
                .add(item)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(newUserActivity.this, "successful", Toast.LENGTH_SHORT);

                        Intent intent = new Intent(newUserActivity.this , Main_Activity.class);
                        startActivity(intent);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(newUserActivity.this, "failed..", Toast.LENGTH_SHORT);
            }
        });

    }



}