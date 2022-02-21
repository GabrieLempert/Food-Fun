package com.example.foodfun.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.foodfun.R;
import com.example.foodfun.model.Trip;
import com.example.foodfun.model.UserFoodFun;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    Button createTrip;
    Button viewTrips;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    TextView userName;
    Button logOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        logOut=findViewById(R.id.log_out);
        createTrip=findViewById(R.id.New_Trip);
        viewTrips=findViewById(R.id.My_Trips);
        progressBar=findViewById(R.id.progressBar2);
        firebaseAuth=FirebaseAuth.getInstance();
        userName=findViewById(R.id.User_Name_1);
        userName.setText("Hello "+firebaseAuth.getCurrentUser().getEmail());
        createNewTrip();
        viewDataBase();
        logOut.setOnClickListener(e->{
            firebaseAuth.signOut();
            startActivity(new Intent(getApplicationContext(), LogInActivity.class));
            finish();
        });

    }


    public void createNewTrip(){
        createTrip.setOnClickListener(e->{
            progressBar.setVisibility(View.VISIBLE);
            startActivity(new Intent(getApplicationContext(), NewTrip.class));
            finish();
        });
    }
    public void viewDataBase(){
        viewTrips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TripHistory.class));
                finish();

            }
        });
    }





}