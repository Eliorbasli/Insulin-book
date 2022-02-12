package dev.eliorba.insulinbook1.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collection;

import dev.eliorba.insulinbook1.Models.Food;
import dev.eliorba.insulinbook1.R;

public class FoodAdapter extends RecyclerView.Adapter<MyViewHolder> implements Filterable {

    Context context;
    private Activity activity;
    private ArrayList<Food> foodArrayList;
    private ArrayList<Food> foodArrayListAll;

    public FoodAdapter(Activity activity,  Context context, ArrayList<Food> foodArrayList) {
        this.activity = activity;
        this.context = context;
        this.foodArrayList = foodArrayList;
        this.foodArrayListAll = new ArrayList<>(foodArrayList);
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_food_item, parent , false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Food food = foodArrayList.get(position);
        holder.LBL_foodTitle.setText(food.getTitle());
        holder.InsulinDose.setText(String.valueOf(food.getInsulinDose()));
        holder.LongInsulin.setText(String.valueOf(food.getLongInsulin()));
        holder.sugarBefore.setText(String.valueOf(food.getSugarBefore()));
        holder.sugarAfter.setText(String.valueOf(food.getSugarAfter()));
        holder.LBL_uploadBY.setText("Upload by " +String.valueOf(food.getUserName()));
        holder.LBL_Hight.setText(String.valueOf(food.getHight()));
        holder.LBL_Wight.setText(String.valueOf(food.getWight()));

        //Image
        Glide.with(context).load(food.getImage()).into(holder.FoodImage);

    }

    @Override
    public int getItemCount() {
        return foodArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {

        //run on background thread
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            ArrayList <Food> filteredList = new ArrayList<>();

            if (constraint.toString().isEmpty()){
                filteredList.addAll(foodArrayListAll);

            }else{
                for(Food food: foodArrayListAll){
                    if(food.getTitle().toLowerCase().contains(constraint.toString().toLowerCase()) ){
                        filteredList.add(food);

                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        };

        //runs on a ui thread
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            foodArrayList.clear();
            foodArrayList.addAll((Collection<? extends Food>) results.values);
            notifyDataSetChanged();
        }
    };


}

