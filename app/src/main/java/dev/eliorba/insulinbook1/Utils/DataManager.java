package dev.eliorba.insulinbook1.Utils;


import android.app.Activity;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import dev.eliorba.insulinbook1.Models.Food;

public class DataManager {

    StorageReference storageReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<String> UserIDList = new ArrayList<String>();
    private static String URL ;

    //read all items from firebase database , all item save in foodArrayList
//    public void EventChangeListener(ArrayList foodArrayList , Adapter foodAdapter) {
//        db.collection("items").orderBy("title", Query.Direction.ASCENDING)
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//
//                        if(error != null){
//
//                            Log.e("Firestore error" , error.getMessage());
//                            return;
//                        }
//                        for (DocumentChange dc : value.getDocumentChanges()){
//
//                            if(dc.getType() == DocumentChange.Type.ADDED){
//
//                                foodArrayList.add(dc.getDocument().toObject(Food.class));
//                            }
//                            //foodAdapter.notifyDataSetChanged();
//                        }
//                    }
//                });
//    }

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


    public void uploadToFireStoreDB(Activity activity , Food food , Uri mImageUri) {
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
        item.put("LongInsulin" , food.getLongInsulin());
        item.put("Hight", food.getHight());
        item.put("Wight", food.getWight());
        item.put("UserName" , food.getUserName());
        item.put("Image" ,URL);

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

    public String getURL(){
        return URL;
    }




}
