package com.example.suitcaseapplicationbyoutule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextPaint;
import android.widget.TextView;

import com.example.suitcaseapplicationbyoutule.statusbar.StatusBarUtil;
import com.google.android.material.textview.MaterialTextView;

public class MainActivity extends AppCompatActivity {

    MaterialTextView applicationName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatusBarUtil.setStatusColor(this, getColor(R.color.black));

        applicationName = findViewById(R.id.applicationNameText);
        applicationName.setText(getResources().getString(R.string.app_name));

        //This code below helps style the application's name by applying color gradient.
        //TextPaint object helps style the text assigned to it
        TextPaint textPaint = applicationName.getPaint();

        /*Calculates the width of the text in the TextView by measuring the width of the text string
          when drawn with the current text size and font */
        float width = textPaint.measureText(getResources().getString(R.string.app_name));

        /*Creating a LinearGradient shader, which is a way to apply a gradient color effect to
          the text */
        Shader textShader = new LinearGradient(0, 0, width, applicationName.getTextSize(),

            new int[] {

                    Color.parseColor("#EB00A1"),
                    Color.parseColor("#7885FF"),

            }, null, Shader.TileMode.CLAMP);

        /*Creates LinearGradient shader as the shader for the TextPaint associated with the
          applicationName TextView */
        applicationName.getPaint().setShader(textShader);

        /*Creating a Handler object for a task that is do be executed later after the application
        launches and in this case the task is for the application to switch from the current
        MainActivity to the LoginActivity after a postDelayed time of 2 seconds */
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);

                //Customer screen transition animation
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        }, 2000);

    }
}