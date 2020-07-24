package com.example.livemedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText emailLoginedittext,passwordloginedittext;
    Button loginbutton;
    TextView newUsertextview;
    FirebaseAuth mAuth;
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializefields();
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uselogin();
            }
        });
        newUsertextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoRegistrationpage();
            }
        });
    }

    private void gotoRegistrationpage() {
        Intent gotoregistration = new Intent(LoginActivity.this,RegistrationActivity.class);
        startActivity(gotoregistration);
    }

    private void uselogin() {
        String uEmail,uPassword;
        uEmail=emailLoginedittext.getText().toString();
        uPassword=passwordloginedittext.getText().toString();
        if(TextUtils.isEmpty(uEmail)||TextUtils.isEmpty(uPassword))
        {
            Toast.makeText(this,"Email-id or password field is empty",Toast.LENGTH_LONG).show();
        }else {
            loadingBar.setTitle("Logging in...");
            loadingBar.setMessage("Please wait... Logging In...");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();
            mAuth.signInWithEmailAndPassword(uEmail,uPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(LoginActivity.this, "Log in Successful", Toast.LENGTH_SHORT).show();
                        gotohomescreen();
                        loadingBar.dismiss();
                    }else {
                        String message=task.getException().toString();
                        Toast.makeText(LoginActivity.this,"Error is :"+ message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }

    private void gotohomescreen() {
        Intent gotohomescreen = new Intent(LoginActivity.this,MainActivity.class);
        gotohomescreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(gotohomescreen);
        finish();

    }

    private void initializefields() {
        mAuth=FirebaseAuth.getInstance();
        emailLoginedittext=findViewById(R.id.loginemaileditext);
        passwordloginedittext=findViewById(R.id.loginpasswordedittext);
        loginbutton=findViewById(R.id.loginbutton);
        newUsertextview=findViewById(R.id.registrationtextview);
        loadingBar=new ProgressDialog(this);
    }
}
