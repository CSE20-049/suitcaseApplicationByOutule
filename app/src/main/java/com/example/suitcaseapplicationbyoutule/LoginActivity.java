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
import android.widget.Toast;

import com.example.suitcaseapplicationbyoutule.model.User;
import com.example.suitcaseapplicationbyoutule.statusbar.StatusBarUtil;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    MaterialTextView loginText, signUpText;
    MaterialButton signInBTN;

    TextInputEditText emailField, passwordField;

    //Creating a DatabaseReference class to access firebase's Realtime Database
    DatabaseReference databaseReference = FirebaseDatabase.getInstance()
            .getReferenceFromUrl("https://suitcase-application-2-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StatusBarUtil.setStatusColor(this, getColor(R.color.black));

        loginText = findViewById(R.id.loginText);
        signUpText = findViewById(R.id.signUpText);
        signInBTN =  findViewById(R.id.signInBTN);
        emailField = findViewById(R.id.loginEmailTextInputEditText);
        passwordField = findViewById(R.id.loginPasswordTextInputEditText);

        loginText.setText(getResources().getString(R.string.login));

        // This code below helps style the application's login text by applying color gradient____//
        TextPaint textPaint = loginText.getPaint();
        float width = textPaint.measureText(getResources().getString(R.string.app_name));

        Shader textShader = new LinearGradient(0, 0, width, loginText.getTextSize(),
                new int[] {
                        Color.parseColor("#EB00A1"),
                        Color.parseColor("#7885FF"),
                }, null, Shader.TileMode.CLAMP);
        loginText.getPaint().setShader(textShader);

        // SignUpText ActionLister________________________________________________________________//
        signUpText.setOnClickListener(View -> {
            signUpText.setTextColor(getColor(R.color.magenta_pink));
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            //Customer screen transition animation
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        });

        // Handling logging actions on click______________________________________________________//
        signInBTN.setOnClickListener(View -> {
            final String email = emailField.getText().toString().trim();
            final String password = passwordField.getText().toString().trim();

            // Accounting for empty fields //
            if (email.isEmpty()) {
                Toast.makeText(this, "Email Required", Toast.LENGTH_SHORT).show();
            } else if (password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Password Required", Toast.LENGTH_SHORT).show();
            } else {

                // Creating a progress dialog //
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setCancelable(false);
                builder.setView(R.layout.loading_dialog_layout);
                AlertDialog dialog = builder.create();
                dialog.show();

                // Sanitize the email address to create a valid Firebase Database path
                String sanitizedEmail = email.replace(".", "_");

                databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        //Checking if the sanitized email address exists in the Firebase database
                        if (snapshot.hasChild(sanitizedEmail)) {
                            DataSnapshot userSnapshot = snapshot.child(sanitizedEmail);

                            if (userSnapshot.child("password").exists()) {
                                String storedPassword = userSnapshot.child("password")
                                        .getValue(String.class);

                                if (password.equals(storedPassword)) {
                                    Intent intent = new Intent(LoginActivity.this,
                                            FragmentNavigatorActivity.class);

                                    // Put the User's sanitized Email on the intent object
                                    intent.putExtra("email", sanitizedEmail);

                                    // Start the activity
                                    startActivity(intent);
                                    finish();
                                    Toast.makeText(LoginActivity.this, "Login Successful",
                                            Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Wrong Password",
                                            Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "No password found for the user",
                                        Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "No account found",
                                    Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

}