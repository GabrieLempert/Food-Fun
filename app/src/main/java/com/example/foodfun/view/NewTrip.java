package com.example.foodfun.view;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.foodfun.CustomizeTrip;
import com.example.foodfun.Final;
import com.example.foodfun.FragmentAdapter;
import com.example.foodfun.R;
import com.example.foodfun.create_trip;
import com.example.foodfun.model.Ingredients;
import com.example.foodfun.model.Meal;
import com.example.foodfun.model.Supplies;
import com.example.foodfun.model.Trip;
import com.example.foodfun.model.TypeOfMeal;
import com.example.foodfun.model.UserFoodFun;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NewTrip extends AppCompatActivity implements create_trip.FragmentAListener, CustomizeTrip.FragmentCustomizeTripListener , Final.FinalFragmentListener {
    ArrayList<Meal> mealsInAccount;
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    FragmentAdapter fragmentAdapter;
    UserFoodFun user;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    Trip newTrip;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_trip);
        getSupportActionBar().hide();
        firebaseAuth=FirebaseAuth.getInstance();
        user=new UserFoodFun(null,null,null);
        this.newTrip=new Trip();
        addHardCoded(newTrip);
        mealsInAccount=new ArrayList<Meal>();
        tabLayout=findViewById(R.id.tab_layout);
        viewPager2=findViewById(R.id.view_pager);
        FragmentManager fm =getSupportFragmentManager();
        fragmentAdapter=new FragmentAdapter(fm,getLifecycle());
        viewPager2.setAdapter(fragmentAdapter);
        tabLayout.addTab(tabLayout.newTab().setText("New Trip"));
        tabLayout.addTab(tabLayout.newTab().setText("Customize Trip"));
        tabLayout.addTab(tabLayout.newTab().setText("Final"));
        getUser();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
                if(tab.getPosition()==3)
                    getTrip();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if(tab.getPosition()==3)
                    getTrip();
            }
        });






        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

    }


    public void addHardCoded(Trip newTrip){
        //Ingredients
        Ingredients salt=new Ingredients("salt");
        Ingredients pepper=new Ingredients("pepper");
        Ingredients canolaOil=new Ingredients("canola oil");
        Ingredients oliveOil=new Ingredients("olive oil");
        Ingredients eggs=new Ingredients("eggs");
        Ingredients tomato=new Ingredients("tomato");
        Ingredients sugar=new Ingredients("sugar");
        Ingredients pasta=new Ingredients("pasta");
        Ingredients chickenBreast=new Ingredients("chicken breast");
        Ingredients rice=new Ingredients("rice");
        Ingredients potato=new Ingredients("potato");


        //supplies
        Supplies pan=new Supplies("pan");
        Supplies pot=new Supplies("pot");
        Supplies fork=new Supplies("fork");
        Supplies plate=new Supplies("plate");
        Supplies knife=new Supplies("knife");
        Supplies cup=new Supplies("cup");
        Supplies trashBag=new Supplies("trash bag");
        Supplies teaPot=new Supplies("tea pot");
        Supplies gas=new Supplies("gas");


        Meal shakshuka=new Meal("shakshuka", TypeOfMeal.BREAKFAST);
        Meal chickenWithRice=new Meal("chicken with rice",TypeOfMeal.DINNER);
        Meal pastaWithTomatoes=new Meal("pasta italian",TypeOfMeal.LUNCH);
        Meal potatoInFire=new Meal("potato in fire",TypeOfMeal.DINNER);

        shakshuka.addIngredient(salt);
        shakshuka.addIngredient(pepper);
        shakshuka.addIngredient(tomato);
        shakshuka.addIngredient(eggs);
        shakshuka.addIngredient(canolaOil);

        chickenWithRice.addIngredient(salt);
        chickenWithRice.addIngredient(pepper);
        chickenWithRice.addIngredient(chickenBreast);
        chickenWithRice.addIngredient(rice);

        pastaWithTomatoes.addIngredient(salt);
        pastaWithTomatoes.addIngredient(pepper);
        pastaWithTomatoes.addIngredient(tomato);
        pastaWithTomatoes.addIngredient(oliveOil);
        pastaWithTomatoes.addIngredient(pasta);

        potatoInFire.addIngredient(potato);

    }

    @Override
    public void onInputASent(CharSequence name) {
        try {
            if (name.toString().isEmpty())
                throw new Exception();
            this.newTrip.setName(name.toString());
        }catch (Exception e){
            Toast.makeText(NewTrip.this,"You need to Enter a name",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onInputDaysSent(int days) {
        this.newTrip.setNumberOfDays(days);
    }

    @Override
    public void onInputPeopleSent(int people) {
        this.newTrip.setNumberOfPeople(people);
    }

    @Override
    public void getMeal(Meal addMeal) {
        addMeal.setNumberOfPeople(this.newTrip.getNumberOfPeople());
        if(this.newTrip.addMeal(addMeal))
            Toast.makeText(this,"Added this meal",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addMeal(Meal newMeal) {
        this.newTrip.addMeal(newMeal);
        for (int i = 0; i < newTrip.getMeals().size(); i++) {
            for (int j = 0; j < newTrip.getMeals().get(i).getIngredients().size(); j++) {
                newTrip.addGeneralIngredients(newTrip.getMeals().get(i).getIngredients().get(i));
            }
        }

    }

    @Override
    public void changeTab() {
        viewPager2.setCurrentItem(2);
    }


    public void getUser(){
        database=FirebaseDatabase.getInstance();
        Query refForUser=database.getReference("Users");
        int index=firebaseAuth.getCurrentUser().getEmail().indexOf("@");
        String userEmail=firebaseAuth.getCurrentUser().getEmail().substring(0,index);
        refForUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()) {
                        if(dataSnapshot.exists()) {
                            String currentEmail = dataSnapshot.child("email").getValue(String.class);
                            if (currentEmail.equals(firebaseAuth.getCurrentUser().getEmail())) {
                                user.setEmail(dataSnapshot.child("email").getValue(String.class));
                            }
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(NewTrip.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }





    public Trip getNewTrip() {
        return this.newTrip;
    }

    @Override
    public Trip getTrip() {
        return getNewTrip();
    }

    @Override
    public void changeTab(int position) {
        viewPager2.setCurrentItem(position);
    }


    @Override
    public void saveToFireBase() {
        DatabaseReference tripRef = database.getReference("Users");
        int index = user.getEmail().indexOf("@");
        String currentMail = user.getEmail().substring(0, index);
        tripRef.child(currentMail).child("Trips").child(newTrip.getName()).setValue(newTrip);
    }
}


