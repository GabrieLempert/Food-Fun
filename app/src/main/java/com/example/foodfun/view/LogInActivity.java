package com.example.foodfun.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodfun.R;
import com.example.foodfun.model.UserFoodFun;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LogInActivity extends AppCompatActivity {
    FirebaseDatabase database;
    ProgressBar progressBar;
    Button log_in_button;
    Button register;
    TextInputEditText email;
    TextInputEditText password;
    TextInputLayout emailLayout;
    TextInputLayout passwordLayout;
    UserFoodFun user;


    private static final int RC_SIGN_IN =14 ;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        getSupportActionBar().hide();
        log_in_button=findViewById(R.id.Log_In_Button);
        register=findViewById(R.id.register_button);
        email=findViewById(R.id.User_Name_Log_In);
        password=findViewById(R.id.Password_Log_In);
        emailLayout=findViewById(R.id.User_Name_Log_In_Layout);
        passwordLayout=findViewById(R.id.Password_Log_In_Layout);
        mAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        progressBar=findViewById(R.id.progressBar);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                finish();
            }
        });

        createRequest();
        signInWithGoogle();
        log_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectUser();
            }
        });


    }
    public void createRequest(){
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_user_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient=GoogleSignIn.getClient(this,gso);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void signInWithGoogle(){
        findViewById(R.id.google_sign_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(LogInActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            getUser();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete( Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            int index=mAuth.getCurrentUser().getEmail().indexOf("@");
                            String userEmail=mAuth.getCurrentUser().getEmail().substring(0,index);
                            user=new UserFoodFun(userEmail,mAuth.getCurrentUser().getEmail(),"12");
                            database=FirebaseDatabase.getInstance();
                            DatabaseReference userRef=database.getReference("Users");
                            userRef.child(userEmail).setValue(user);
                            progressBar.setVisibility(View.VISIBLE);
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(LogInActivity.this,"something wet wrong",Toast.LENGTH_SHORT).show();

                        }
                    }
                });



    }

    public void connectUser(){
        if(password.getText().toString().isEmpty())
            passwordLayout.setError("didn't enter a password");
        if (email.getText().toString().isEmpty())
            emailLayout.setError("didn't enter a eMail");
        else {
            String userName = email.getText().toString();
            String passwordS = password.getText().toString();
            mAuth.signInWithEmailAndPassword(userName, passwordS).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        getUser();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LogInActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            });
        }
    }

    public void getUser(){
        user=new UserFoodFun(null,null,null);
        database=FirebaseDatabase.getInstance();
        Query refForUser=database.getReference("Users");
        int index=mAuth.getCurrentUser().getEmail().indexOf("@");
        String userEmail=mAuth.getCurrentUser().getEmail().substring(0,index);
        refForUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot dataSnapshot:snapshot.getChildren())
                        if (dataSnapshot.exists()) {
                            String currentEmail = dataSnapshot.child("email").getValue(String.class);
                            if (currentEmail.equals(mAuth.getCurrentUser().getEmail())) {
                                Integer userID = dataSnapshot.child("getUserID").getValue(int.class);
                                if (userID != null)
                                    user.setGetUserID(userID);
                                user.setEmail(dataSnapshot.child("email").getValue(String.class));
                            }
                        }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LogInActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

}
