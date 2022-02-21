package com.example.foodfun;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodfun.model.Ingredients;
import com.example.foodfun.model.Trip;
import com.example.foodfun.view.MainActivity;
import com.example.foodfun.view.TripHistory;

import java.util.ArrayList;

public class ShowTrip extends AppCompatActivity implements TripHistory.TripHistoryListener {
    Trip newTrip;
    ArrayAdapter adapterForIngredient;
    ArrayAdapter adapterForSupplies;
    ArrayAdapter getAdapterForIngredientGeneral;
    ListView getLsIngredientsGeneral;
    ListView lsIngredients;
    ListView lsSupplies;
    Button save;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.show_trip);
        getLsIngredientsGeneral=findViewById(R.id.general_ingredient_list);
        lsIngredients=findViewById(R.id.general_ingredient_list);
        lsSupplies=findViewById(R.id.general_ingredient_list);
        setLsIngredients();
        lsIngredients.setAdapter(adapterForIngredient);
        setLsSupplies();
        lsSupplies.setAdapter(adapterForSupplies);
        setLsIngredientsGeneral();
        getLsIngredientsGeneral.setAdapter(getAdapterForIngredientGeneral);
        save=findViewById(R.id.BackToTrips);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TripHistory.class));
                finish();

            }
        });
    }

    @Override
    public void getTrip(Trip newTrip) {
        this.newTrip=newTrip;
    }

    public void setLsIngredients() {
        ArrayList<String> listIngredients = new ArrayList<String>();
        if (!listIngredients.contains("Water bottles " + newTrip.getWaterBottles()))
            listIngredients.add("Water bottles " + newTrip.getWaterBottles());
        if (!newTrip.getMeals().isEmpty()) {
            ArrayList<Ingredients> ingredientsArrayList = newTrip.allIngredients();
            for (int i = 0; i < ingredientsArrayList.size(); i++) {
                listIngredients.add(newTrip.calculateIngredients(ingredientsArrayList.get(i)));
            }
            adapterForIngredient = new ArrayAdapter(ShowTrip.this.getApplicationContext(), android.R.layout.simple_list_item_2, android.R.id.text1, listIngredients);
        }
    }

    public void setLsSupplies() {
        ArrayList<String> listSupplies=new ArrayList<>();

        for (int i = 0; i <newTrip.getMeals().size(); i++) {
            for (int j = 0; j < newTrip.getMeals().get(i).getSupplies().size();j++) {
                newTrip.setSupplies(newTrip.getMeals().get(i).getSupplies().get(j));
            }
        }
        for (int i = 0; i <newTrip.getSupplies().size() ; i++) {
            String outPut=newTrip.getSupplies().get(i).getName();
            listSupplies.add(outPut);
        }
        adapterForSupplies=new ArrayAdapter(ShowTrip.this.getApplicationContext(), android.R.layout.simple_gallery_item,android.R.id.text2,listSupplies);

    }


    public  void setLsIngredientsGeneral(){
        ArrayList<String> listIngredients=new ArrayList<String>();
        for (int i = 0; i < newTrip.getMeals().size(); i++) {
            for (int j = 0; j < newTrip.getMeals().get(i).getIngredients().size(); j++) {
                String outPut=newTrip.addGeneralIngredients(newTrip.getMeals().get(i).getIngredients().get(i));
                listIngredients.add(outPut);
            }
        }
        getAdapterForIngredientGeneral=new ArrayAdapter(ShowTrip.this.getApplicationContext().getApplicationContext(), android.R.layout.simple_gallery_item,android.R.id.text2,listIngredients);
    }

}
