package com.example.suitcaseapplicationbyoutule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.TextPaint;
import android.widget.TextView;

import com.example.suitcaseapplicationbyoutule.statusbar.StatusBarUtil;
import com.google.android.material.textview.MaterialTextView;

public class LoginActivity extends AppCompatActivity {

    MaterialTextView loginText, signUpText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StatusBarUtil.setStatusColor(this, getColor(R.color.black));

        loginText = findViewById(R.id.loginText);
        signUpText = findViewById(R.id.signUpText);
        loginText.setText(getResources().getString(R.string.login));

        //This code below helps style the application's login text by applying color gradient.
        TextPaint textPaint = loginText.getPaint();
        float width = textPaint.measureText(getResources().getString(R.string.app_name));

        Shader textShader = new LinearGradient(0, 0, width, loginText.getTextSize(),
                new int[] {
                        Color.parseColor("#EB00A1"),
                        Color.parseColor("#7885FF"),
                }, null, Shader.TileMode.CLAMP);
        loginText.getPaint().setShader(textShader);

        //Switching to the RegisterActivity activity when signUp text is clicked
        signUpText.setOnClickListener(View -> {

            signUpText.setTextColor(getColor(R.color.magenta_pink));
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            //Customer screen transition animation
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        });
    }
}