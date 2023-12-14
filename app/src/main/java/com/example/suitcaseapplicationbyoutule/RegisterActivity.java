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

import java.security.PrivateKey;

public class RegisterActivity extends AppCompatActivity {

    MaterialTextView registerText, signInText;

    TextInputEditText firstNameField, lastNameField, emailField, contactField, passwordField;
    MaterialButton signUpBTN;

    private final User user = new User();

    DatabaseReference databaseReference = FirebaseDatabase.getInstance()
            .getReferenceFromUrl("https://suitcase-application-2-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        StatusBarUtil.setStatusColor(this, getColor(R.color.black));

        // Assigning views to variables_____________________________________________________________
        firstNameField = findViewById(R.id.registerFirstNameTextInputEditText);
        lastNameField = findViewById(R.id.registerLastNameTextInputEditText);
        emailField = findViewById(R.id.registerEmailTextInputEditText);
        contactField = findViewById(R.id.registerContactTextInputEditText);
        passwordField = findViewById(R.id.registerPasswordTextInputEditText);
        signInText = findViewById(R.id.signInText);
        signUpBTN = findViewById(R.id.signUpBTN);
        registerText = findViewById(R.id.registerText);

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
            String firstName = firstNameField.getText().toString().trim();
            String lastName = lastNameField.getText().toString().trim();
            String email = emailField.getText().toString().trim();
            String contact = contactField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();


            // Validating input
            if (firstName.isEmpty()) {
                Toast.makeText(this, "Firstname Required", Toast.LENGTH_SHORT).show();
            } else if (lastName.isEmpty()) {
                Toast.makeText(this, "Lastname Required", Toast.LENGTH_SHORT).show();
            } else if (email.isEmpty()) {
                Toast.makeText(this, "Email Required", Toast.LENGTH_SHORT).show();
            } else if (contact.isEmpty()) {
                Toast.makeText(this, "Contact Required", Toast.LENGTH_SHORT).show();
            } else if (password.isEmpty()) {
                Toast.makeText(this, "Password Required", Toast.LENGTH_SHORT).show();
            } else {

                // Populating User object
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setEmail(email);
                user.setContact(contact);
                user.setPassword(password);

                //Saving information to Firebase
                uploadUserData(user);

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
    private void uploadUserData(User user) {

        // Creating a progress dialog and showing it
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.account_creation_dialog_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        // Sanitize email to create a valid Firebase Database path
        String sanitizedEmail = user.getEmail().replace(".", "_");

        // Checking for existing users
        databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(sanitizedEmail)) {
                    Toast.makeText(RegisterActivity.this,
                            "Account already exist",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // Save user data to Firebase
                    databaseReference.child("Users").child(sanitizedEmail)
                            .child("firstName").setValue(user.getFirstName());
                    databaseReference.child("Users").child(sanitizedEmail)
                            .child("lastName").setValue(user.getLastName());
                    databaseReference.child("Users").child(sanitizedEmail)
                            .child("contact").setValue(user.getContact());
                    databaseReference.child("Users").child(sanitizedEmail)
                            .child("password").setValue(user.getPassword());
                    databaseReference.child("Users").child(sanitizedEmail);

                    // Account creation successful
                    Toast.makeText(RegisterActivity.this, "Account Created",
                            Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this ,
                            LoginActivity.class));
                    finish();
                    dialog.dismiss();

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RegisterActivity.this, error.toString(),
                        Toast.LENGTH_SHORT).show();
            }

        });

    }

}
