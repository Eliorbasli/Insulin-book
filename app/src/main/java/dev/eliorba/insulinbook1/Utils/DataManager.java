package dev.eliorba.insulinbook1.Utils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import dev.eliorba.insulinbook1.LoginActivity;
import dev.eliorba.insulinbook1.Models.Food;
import dev.eliorba.insulinbook1.Models.User;
import dev.eliorba.insulinbook1.User_profile_Activity;
import dev.eliorba.insulinbook1.newUserActivity;

public class DataManager {

    StorageReference storageReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static String URL ;

    public void uploadImageToStorageDB(Activity activity ,Uri mImageUri, ProgressBar Pb , ImageView mImageView , Food food) {

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
                                Toast.makeText(activity, "Successfully Uploaded", Toast.LENGTH_SHORT).show();
                                URL = uri.toString();// uri = URL = HTTP to image in storage
                                uploadToFireStoreDB(activity , food , mImageUri);
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(activity, "Failed to uploaded", Toast.LENGTH_SHORT).show();

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount() );
                Pb. setProgress((int) progress);
            }
        });

    }


    private void uploadToFireStoreDB(Activity activity , Food food , Uri mImageUri) {

        db = FirebaseFirestore.getInstance();

        if(mImageUri != null){


        }else{
            Toast.makeText(activity, "No Photo selected", Toast.LENGTH_SHORT).show();
        }

        Map<String, Object> item = new HashMap<>();
        item.put("title", food.getTitle());
        item.put("Sugarbefore", food.getSugarBefore());
        item.put("Sugarafter", food.getSugarAfter());
        item.put("InsulinDose", food.getInsulinDose());
        item.put("Hight", food.getHight());
        item.put("Wight", food.getWight());
        item.put("Image" ,URL);
      // item.put("userId" , userId );



        db.collection("items")
                .add(item)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(activity, "successful", Toast.LENGTH_SHORT);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, "failed..", Toast.LENGTH_SHORT);
            }
        });

    }


//    public void uploadUserToDB(Activity activity , User user) {
//
//        db = FirebaseFirestore.getInstance();
//
//
//        String userId = preferences.getString("userId" , "");
//
//        if(user == null) {
//            Toast.makeText(activity, "No user selected", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        Map<String, Object> item = new HashMap<>();
//        item.put("UserId", user.getId());
//        item.put("Age", user.getAge());
//        item.put("Gender", user.getGender());
//        item.put("LongInsulin", user.getLongInsulin());
//        item.put("Hight", user.getHeight());
//        item.put("Wight", user.getWight());
//
//        // item.put("userId" , userId );
//
//
//
//        db.collection("users")
//                .add(item)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Toast.makeText(activity, "successful", Toast.LENGTH_SHORT);
//
//                        Intent intent = new Intent(activity , User_profile_Activity.class);
//                        activity.startActivity(intent);
//
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(activity, "failed..", Toast.LENGTH_SHORT);
//            }
//        });
//
//    }






    public String getURL(){
        return URL;
    }




}
