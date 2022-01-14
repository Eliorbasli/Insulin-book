package dev.eliorba.insulinbook1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class NewRecordActivity extends AppCompatActivity {

    TextInputLayout tfName ;
    TextInputLayout tfSugarbefore;
    TextInputLayout tfSugarafter;
    TextInputLayout tfInsulinDose;
    TextInputLayout tfHight;
    TextInputLayout tfWight;
    MaterialButton btnNewRecord;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_record);
        getSupportActionBar().hide();

        tfName = findViewById(R.id.NewRecord_TF_name);
        tfSugarbefore = findViewById(R.id.NewRecord_TF_SugarBeinput);
        tfSugarafter = findViewById(R.id.NewRecord_TF_SuganAfter);
        tfInsulinDose = findViewById(R.id.NewRecord_TF_InsulinDose);
        tfHight = findViewById(R.id.NewRecord_TF_Hight);
        tfWight = findViewById(R.id.NewRecord_TF_Wight);
        btnNewRecord = findViewById(R.id.NewRecord_BTN_add);

        btnNewRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db = FirebaseFirestore.getInstance();
                                // = tfSugarbefore.getText().toString();
                String title = tfName.getEditText().getText().toString();
                String SugarBefore = tfSugarbefore.getEditText().getText().toString();
                String Sugarafter = tfSugarafter.getEditText().getText().toString();
                String InsulinDose = tfInsulinDose.getEditText().getText().toString();
                String Hight = tfHight.getEditText().getText().toString();
                String Wight = tfWight.getEditText().getText().toString();

                Map<String, Object> item = new HashMap<>();
                item.put("title", title);
                item.put("Sugarbefore", SugarBefore);
                item.put("Sugarafter", Sugarafter);
                item.put("InsulinDose", InsulinDose);
                item.put("Hight", Hight);
                item.put("Wight", Wight);

                db.collection("items")
                        .add(item)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(NewRecordActivity.this, "succceful", Toast.LENGTH_SHORT);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(NewRecordActivity.this, "failed..", Toast.LENGTH_SHORT);
                    }
                });


            }
        });
   }

}