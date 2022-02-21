package com.example.foodfun.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodfun.R;
import com.example.foodfun.model.UserFoodFun;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    EditText userName;
    EditText emailAddress;
    EditText password;
    Button register;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    UserFoodFun user;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        getSupportActionBar().hide();
        userName=findViewById(R.id.User_Name_1);
        password=findViewById(R.id.Password);
        emailAddress=findViewById(R.id.Email_1);
        register=findViewById(R.id.RegisterButton);
        firebaseAuth=FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progressBar_Register);
        if(firebaseAuth.getCurrentUser()!= null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }else {
            clickOnButton();
        }

    }

    public void createAccount(FirebaseAuth firebaseAuth){
        String userName=getUserName().getText().toString().trim();
        String email = getEmailAddress().getText().toString().trim();
        String password=getPassword().getText().toString().trim();
        if(TextUtils.isEmpty(userName)||TextUtils.isEmpty(email)||TextUtils.isEmpty(password))
            return ;
        if (password.length()<8)
            return ;

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "User created successfully", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }
                else{
                    Toast.makeText(RegisterActivity.this, "Error"+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    public void clickOnButton() {

        getRegister().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount(firebaseAuth);
                progressBar.setVisibility(View.VISIBLE);
                setUser();

            }
        });
    }


    public void setUser(){
        String userName=getUserName().getText().toString().trim();
        String email = getEmailAddress().getText().toString().trim();
        String password=getPassword().getText().toString().trim();
        if(TextUtils.isEmpty(userName)||TextUtils.isEmpty(email)||TextUtils.isEmpty(password))
            return ;
        if (password.length()<8)
            return ;
        this.user=new UserFoodFun(userName,email,password);
        this.database=FirebaseDatabase.getInstance();
        DatabaseReference userRef=database.getReference("Users");
        int index=email.indexOf("@");
        String userEmail=email.substring(0,index);
        userRef.child(userEmail).setValue(user);
    }
    public EditText getUserName() {
        return userName;
    }

    public EditText getEmailAddress() {
        return emailAddress;
    }
    public EditText getPassword() {
        return password;
    }
    public Button getRegister() {
        return register;
    }

}
