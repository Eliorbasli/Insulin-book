package dev.eliorba.insulinbook1.Adapters;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import dev.eliorba.insulinbook1.R;

public class MyViewHolder extends RecyclerView.ViewHolder {

    MaterialTextView LBL_foodTitle;
    MaterialTextView sugarBefore;
    MaterialTextView sugarAfter;
    MaterialTextView InsulinDose;
    MaterialTextView LongInsulin;
    MaterialTextView LBL_uploadBY;
    MaterialTextView LBL_Wight;
    MaterialTextView LBL_Hight;
    AppCompatImageView FoodImage;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        LBL_foodTitle = itemView.findViewById(R.id.Food_LBL_Title);
        sugarBefore = itemView.findViewById(R.id.Food_LBL_SugarBefore);
        sugarAfter = itemView.findViewById(R.id.Food_LBL_SugarAfter);
        InsulinDose = itemView.findViewById(R.id.Food_LBL_Insulin);
        FoodImage = itemView.findViewById(R.id.Food_IMG_image);
        LBL_uploadBY = itemView.findViewById(R.id.Food_LBL_uploadBY);
        LBL_Wight = itemView.findViewById(R.id.Food_LBL_Wight2);
        LBL_Hight = itemView.findViewById(R.id.Food_LBL_Hight2);
        LongInsulin = itemView.findViewById(R.id.Food_LBL_LongInsulin222);
    }


}
