package com.example.suitcaseapplicationbyoutule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.suitcaseapplicationbyoutule.statusbar.StatusBarUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    MaterialTextView registerText, signInText;

    TextInputEditText emailField, passwordField;
    MaterialButton signUpBTN;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        StatusBarUtil.setStatusColor(this, getColor(R.color.black));

        // Assigning views to variables_____________________________________________________________
        emailField = findViewById(R.id.registerEmailTextInputEditText);
        passwordField = findViewById(R.id.registerPasswordTextInputEditText);
        signInText = findViewById(R.id.signInText);
        signUpBTN = findViewById(R.id.signUpBTN);
        registerText = findViewById(R.id.registerText);
        auth = FirebaseAuth.getInstance();

        registerText.setText(getResources().getString(R.string.register));

        // Styling the application's register text by applying color gradient_______________________
        TextPaint textPaint = registerText.getPaint();
        float width = textPaint.measureText(getResources().getString(R.string.app_name));

        Shader textShader = new LinearGradient(0, 0, width, registerText.getTextSize(),
                new int[]{
                        Color.parseColor("#EB00A1"),
                        Color.parseColor("#7885FF"),
                }, null, Shader.TileMode.CLAMP);
        registerText.getPaint().setShader(textShader);

        /* Switching to the ProfileCreationActivity when nextBTN is clicked and also carrying on all
           user input to the activity */
        signUpBTN.setOnClickListener(View -> {

            //Capturing user input
            final String email = emailField.getText().toString().trim();
            final String password = passwordField.getText().toString().trim();


            // Validating input
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Email Required", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Password Required", Toast.LENGTH_SHORT).show();
            } else {

                // Creating a progress dialog //
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(false);
                builder.setView(R.layout.account_creation_dialog_layout);
                AlertDialog dialog = builder.create();
                dialog.show();

                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    // Sign up is successful
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);

                                    startActivity(intent);
                                    finish();
                                    Toast.makeText(RegisterActivity.this, "Account Created",
                                            Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();

                                } else {

                                    // Signing up failed
                                    Toast.makeText(RegisterActivity.this, "Authentication Failed",
                                            Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            }
                        });

            }
        });

        //Switching to the LoginActivity when signIn text is clicked________________________________
        signInText.setOnClickListener(View -> {
            signInText.setTextColor(getColor(R.color.magenta_pink));
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            //Customer screen transition animation
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            finish();
        });
    }

}
