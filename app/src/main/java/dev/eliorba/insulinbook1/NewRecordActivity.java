package dev.eliorba.insulinbook1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import dev.eliorba.insulinbook1.Models.Food;
import dev.eliorba.insulinbook1.Utils.DataManager;


public class NewRecordActivity extends AppCompatActivity {

    TextInputLayout tfName;
    TextInputLayout tfSugarbefore;
    TextInputLayout tfSugarafter;
    TextInputLayout tfInsulinDose;
    TextInputLayout tfLongInsulin;
    TextInputLayout tfHight;
    TextInputLayout tfWight;
    MaterialButton btnNewRecord;
    MaterialButton btnChoose;
    ImageView mImageView;
    ProgressBar Pb;
    FirebaseFirestore db;
    //private static String URL ;

    private static final int PICK_IMAGE_REQUEST = 1;
    Uri mImageUri;
    private StorageReference  mStorageRef;
    private DatabaseReference mDatabaseRef;
    private DataManager dataManager = new DataManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_record);
        getSupportActionBar().hide();

        tfName          = findViewById(R.id.NewRecord_TF_name);
        tfSugarbefore   = findViewById(R.id.NewRecord_TF_SugarBeinput);
        tfSugarafter    = findViewById(R.id.NewRecord_TF_SuganAfter);
        tfInsulinDose   = findViewById(R.id.NewRecord_TF_InsulinDose);
        tfLongInsulin   = findViewById(R.id.NewRecord_TF_LongInsulin);
        tfHight         = findViewById(R.id.NewRecord_TF_Hight);
        tfWight         = findViewById(R.id.NewRecord_TF_Wight);
        btnNewRecord    = findViewById(R.id.NewRecord_BTN_add);
        btnChoose       = findViewById(R.id.NewRecord_BTN_choose);
        mImageView      = findViewById(R.id.newRecord_IMG);
        Pb              = findViewById(R.id.newRecord_PB);

        db = FirebaseFirestore.getInstance();
        mStorageRef     = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef    = FirebaseDatabase.getInstance().getReference("uploads");

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChoose();
            }
        });

        btnNewRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences preferences = getSharedPreferences("MyPrefs" , MODE_PRIVATE);
                String userName = preferences.getString("username" , " ") ;
                Food food = new Food()
                        .setTitle(tfName.getEditText().getText().toString())
                        .setSugarBefore(Integer.parseInt(tfSugarbefore.getEditText().getText().toString()))
                        .setSugarAfter(Integer.parseInt(tfSugarafter.getEditText().getText().toString()))
                        .setInsulinDose(Integer.parseInt(tfInsulinDose.getEditText().getText().toString()))
                        .setLongInsulin(Integer.parseInt(tfLongInsulin.getEditText().getText().toString()))
                        .setHight(Integer.parseInt(tfHight.getEditText().getText().toString()))
                        .setWight(Integer.parseInt(tfWight.getEditText().getText().toString()))
                        .setUserName(userName);
                if(mImageUri != null)
                {
                    dataManager.uploadImageToStorageDB(NewRecordActivity.this , mImageUri , Pb , mImageView , food);
                }
                else{
                    Toast.makeText(NewRecordActivity.this, "Please select an image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openFileChoose() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();
            mImageView.setImageURI(mImageUri);
        }
    }


    }


