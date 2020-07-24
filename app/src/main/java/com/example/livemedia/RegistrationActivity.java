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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegistrationActivity extends AppCompatActivity {
    String regUserEmail,regUserPassword;
    EditText registerEmail,registerPassword;
    TextView haveanaccount;
    Button registerButton;
    FirebaseAuth mAuth;
    ProgressDialog loadingBar;
    DatabaseReference rootref;
    String currentuserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initializefields();
        haveanaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoLoginPage();
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
    }

    private void createAccount() {
        regUserEmail=registerEmail.getText().toString();
        regUserPassword=registerPassword.getText().toString();
        if(TextUtils.isEmpty(regUserEmail)||TextUtils.isEmpty(regUserPassword)){
            Toast.makeText(this,"Please Fill in the details",Toast.LENGTH_SHORT).show();
        }else {
            loadingBar.setTitle("Creating new Account");
            loadingBar.setMessage("please wait....");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();
            mAuth.createUserWithEmailAndPassword(regUserEmail,regUserPassword).
                    addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegistrationActivity.this,
                                        "Account created successfully",Toast.LENGTH_LONG)
                                        .show();
                                loadingBar.dismiss();
                                creteProfile();
                                gotohomeScreen();
                            }else{
                                String message= task.getException().toString();
                                Toast.makeText(RegistrationActivity.this,
                                        "Error: "+message,Toast.LENGTH_LONG).show();
                                loadingBar.dismiss();
                            }
                        }
                    });

        }
    }

    private void creteProfile() {
        currentuserId=mAuth.getCurrentUser().getUid();
        rootref.child("users").child(currentuserId).setValue("");
        HashMap<String,String> profileMap=new HashMap<>();
        profileMap.put("userId",currentuserId);
        profileMap.put("emailId",regUserEmail);
        profileMap.put("password",regUserPassword);
        rootref.child("users").child(currentuserId).setValue(profileMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegistrationActivity.this,"Profile Created",Toast.LENGTH_SHORT).show();

                        }else {
                            String message= task.getException().toString();
                            Toast.makeText(RegistrationActivity.this,"Errror: "+message,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void gotohomeScreen() {
        Intent gotohomescreen = new Intent(RegistrationActivity.this,MainActivity.class);
        gotohomescreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(gotohomescreen);
        finish();
    }

    private void gotoLoginPage() {
        Intent gotologinactivity = new Intent(RegistrationActivity.this,LoginActivity.class);
        startActivity(gotologinactivity);

    }

    private void initializefields() {
        registerEmail=findViewById(R.id.emaileditext);
        registerPassword=findViewById(R.id.passwordedittext);
        registerButton=findViewById(R.id.registerbutton);
        haveanaccount=findViewById(R.id.accountavailabletextview);
        loadingBar = new ProgressDialog(this);
        rootref= FirebaseDatabase.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();
    }
}
