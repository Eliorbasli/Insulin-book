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


public class NewRecordActivity extends AppCompatActivity {

    TextInputLayout tfName;
    TextInputLayout tfSugarbefore;
    TextInputLayout tfSugarafter;
    TextInputLayout tfInsulinDose;
    TextInputLayout tfHight;
    TextInputLayout tfWight;
    MaterialButton btnNewRecord;
    MaterialButton btnChoose;
    Slider slider;
    ImageView mImageView;
    ProgressBar Pb;
    FirebaseFirestore db;
    StorageReference storageReference;
    private static String URL ;

    private static final int PICK_IMAGE_REQUEST = 1;
    Uri mImageUri;
    Uri downloadUrl;

    private StorageReference  mStorageRef;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_record);
        getSupportActionBar().hide();

        tfName          = findViewById(R.id.NewRecord_TF_name);
        tfSugarbefore   = findViewById(R.id.NewRecord_TF_SugarBeinput);
        tfSugarafter    = findViewById(R.id.NewRecord_TF_SuganAfter);
        tfInsulinDose   = findViewById(R.id.NewRecord_TF_InsulinDose);
        tfHight         = findViewById(R.id.NewRecord_TF_Hight);
        tfWight         = findViewById(R.id.NewRecord_TF_Wight);
        btnNewRecord    = findViewById(R.id.NewRecord_BTN_add);
        btnChoose       = findViewById(R.id.NewRecord_BTN_choose);
        mImageView      = findViewById(R.id.newRecord_IMG);
        Pb              = findViewById(R.id.newRecord_PB);
        slider          = findViewById(R.id.NewRecored_slider);


        SharedPreferences preferences = getSharedPreferences("MyPrefs" , MODE_PRIVATE);
        String userIdPre = preferences.getString("userId" , "");

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

                uploadImageToStorageDB();
            }
        });
    }


    private void uploadToFireStoreDB() {

        String title        = tfName.getEditText().getText().toString();
        Integer SugarBefore = Integer.parseInt(tfSugarbefore.getEditText().getText().toString());
        Integer Sugarafter  = Integer.parseInt(tfSugarafter.getEditText().getText().toString());
        Integer InsulinDose = Integer.parseInt(tfInsulinDose.getEditText().getText().toString());
        Integer Hight       = Integer.parseInt(tfHight.getEditText().getText().toString());
        Integer Wight       = Integer.parseInt(tfWight.getEditText().getText().toString());

        SharedPreferences preferences = getSharedPreferences("MyPrefs" , MODE_PRIVATE);
        String userId = preferences.getString("userId" , "");


        if(mImageUri != null){


        }else{
            Toast.makeText(this, "No Photo selected", Toast.LENGTH_SHORT).show();
        }

        Map<String, Object> item = new HashMap<>();
        item.put("title", title);
        item.put("Sugarbefore", SugarBefore);
        item.put("Sugarafter", Sugarafter);
        item.put("InsulinDose", InsulinDose);
        item.put("Hight", Hight);
        item.put("Wight", Wight);
        item.put("Image" ,URL);
        item.put("userId" , userId );



        db.collection("items")
                .add(item)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(NewRecordActivity.this, "successful", Toast.LENGTH_SHORT);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(NewRecordActivity.this, "failed..", Toast.LENGTH_SHORT);
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


    //upload only the image and file name to storage firebase in /image folder and get URL
    //file name is current date and time.
    public void uploadImageToStorageDB () {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
        Date now = new Date();
        String fileName = formatter.format(now);

        storageReference = FirebaseStorage.getInstance().getReference("images/" + fileName);

        storageReference.putFile(mImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Pb.setProgress(0);
                            }
                        },500);

                        mImageView.setImageURI(null);

                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Toast.makeText(NewRecordActivity.this, "Successfully Uploaded", Toast.LENGTH_SHORT).show();
                                downloadUrl = uri; // uri = URL = HTTP to image in storage
                                URL = uri.toString();
                                uploadToFireStoreDB();
                            }
                        });
//                                .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(NewRecordActivity.this, "Failed to uploaded", Toast.LENGTH_SHORT).show();
//                            }
//                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(NewRecordActivity.this, "Failed to uploaded", Toast.LENGTH_SHORT).show();

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount() );
                Pb. setProgress((int) progress);
            }
        });

    }

    }


