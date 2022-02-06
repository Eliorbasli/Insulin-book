package dev.eliorba.insulinbook1.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

import dev.eliorba.insulinbook1.Models.Food;
import dev.eliorba.insulinbook1.R;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.MyViewHolder> {

    Context context;
    private Activity activity;
    ArrayList<Food> foodArrayList;

    public FoodAdapter(Activity activity,  Context context, ArrayList<Food> foodArrayList) {
        this.activity = activity;
        this.context = context;
        this.foodArrayList = foodArrayList;
    }

    @NonNull
    @Override
    public FoodAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_food_item, parent , false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodAdapter.MyViewHolder holder, int position) {


        Food food = foodArrayList.get(position);

        holder.foodTitle.setText(food.getTitle());
        holder.InsulinDose.setText(String.valueOf(food.getInsulinDose()));

        holder.sugarBefore.setText(String.valueOf(food.getSugarBefore()));
        holder.sugarAfter.setText(String.valueOf(food.getSugarAfter()));

        //Image
        Glide.with(context).load(food.getImage())
                .into(holder.FoodImage);

    }

    @Override
    public int getItemCount() {
        return foodArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        MaterialTextView  foodTitle ;
        MaterialTextView sugarBefore;
        MaterialTextView sugarAfter;
        MaterialTextView InsulinDose;
        AppCompatImageView FoodImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            foodTitle = itemView.findViewById(R.id.Food_LBL_Title);
            sugarBefore = itemView.findViewById(R.id.Food_LBL_SugarBefore);
            sugarAfter = itemView.findViewById(R.id.Food_LBL_SugarAfter);
            InsulinDose = itemView.findViewById(R.id.Food_LBL_Insulin);
            FoodImage = itemView.findViewById(R.id.Food_IMG_image);
        }
    }
}

