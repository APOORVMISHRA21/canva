package com.example.mycanva;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.mycanva.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.inputEmail.addTextChangedListener(emailWatcher);

        mAuth = FirebaseAuth.getInstance();

        binding.signUp.setOnClickListener(v -> {
            if(isValidEmail(binding.inputEmail.getText().toString())){
                performSignUp(binding.inputEmail.getText().toString(), binding.inputPassword.getText().toString());
            }
        });
    }

    private TextWatcher emailWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(!isValidEmail(s.toString())) {
                binding.inputEmailLayout.setBoxStrokeColor(getResources().getColor(R.color.red));
            }
            else {
                binding.inputPasswordLayout.setBoxStrokeColor(getResources().getColor(R.color.green));
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        @Override
        public void afterTextChanged(Editable s) { }
    };

    public static boolean isValidEmail(String email) {
        String expression = "^[\\w\\.]+@([\\w]+\\.)+[A-Z]{2,7}$";
        CharSequence inputString = email;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputString);

        return matcher.matches();

    }

    public void performSignUp(String email, String password){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("AUTH---STATUS", "%%%%%%%%%%%%%%%%%  createUserWithEmail:success %%%%%%%%%%%%%%%%%%%%");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d("AUTH---CRED", "%%%%%%%%%%%%%%%%      Email : " + user.getEmail() + "  Name: " + user.getDisplayName()+"       %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%") ;
                            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("AUTH--STATUS", "#################    createUserWithEmail:failure  #################################3", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}