package com.example.foodfun.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodfun.R;
import com.example.foodfun.model.Trip;
import com.example.foodfun.model.UserFoodFun;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TripHistory extends AppCompatActivity {
    TripHistoryListener listener;
    ListView ls;
    ArrayAdapter adapter;
    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
    UserFoodFun user;
    Button mainMenu;

    public interface TripHistoryListener{
        public void getTrip(Trip newTrip);
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_history);
        getSupportActionBar().hide();

        ls=findViewById(R.id.list_trip_history);
        firebaseAuth=FirebaseAuth.getInstance();
        user=new UserFoodFun(null,null,null);
        database=FirebaseDatabase.getInstance();
        setAdapter();
        if(!adapter.isEmpty())
            ls.setAdapter(adapter);
        else
            ls.setAdapter(adapter);
        Toast.makeText(TripHistory.this,"Didn't add any trips yet",Toast.LENGTH_SHORT).show();
        mainMenu=findViewById(R.id.main_menu);
        getTrip();
        goBack();
    }


    public void setAdapter() {
        ArrayList<String> tripsName = new ArrayList<>();
        String userName = getUser();
        DatabaseReference ref = database.getReference("Users").child(userName).child("Trips");
        //  if (!ref.getRef().getKey().isEmpty()) {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        tripsName.add(snapshot.getValue(Trip.class).toString());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //}

        this.adapter=new ArrayAdapter(TripHistory.this.getApplicationContext(), android.R.layout.simple_list_item_single_choice,android.R.id.text1,tripsName);


    }

    public String getUser(){
        database=FirebaseDatabase.getInstance();
        Query refForUser=database.getReference("Users");
        int index=firebaseAuth.getCurrentUser().getEmail().indexOf("@");
        String userEmail=firebaseAuth.getCurrentUser().getEmail().substring(0,index);
        refForUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()) {
                        String currentEmail =dataSnapshot.child("email").getValue(String.class);
                        if (currentEmail.equals(firebaseAuth.getCurrentUser().getEmail())) {
                            user.setGetUserID(dataSnapshot.child("getUserID").getValue(int.class));
                            user.setEmail(dataSnapshot.child("email").getValue(String.class));
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TripHistory.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        return userEmail;
    }


    public void getTrip(){
        String userEmail=getUser();
        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String tripName = ls.getSelectedItem().toString();
                DatabaseReference ref = database.getReference().child("Users").child(userEmail).child("Trips");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            if (dataSnapshot.exists()) {
                                if (dataSnapshot.getValue(Trip.class).getName().equals(tripName)) {
                                    Trip getTrip = dataSnapshot.getValue(Trip.class);
                                    listener = new TripHistoryListener() {
                                        @Override
                                        public void getTrip(Trip newTrip) {
                                            newTrip = getTrip;
                                        }
                                    };
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(TripHistory.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
    public void goBack(){
        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
    }

}
