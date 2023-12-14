package com.example.suitcaseapplicationbyoutule;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.TextPaint;

import com.example.suitcaseapplicationbyoutule.statusbar.StatusBarUtil;
import com.google.android.material.textview.MaterialTextView;

public class AddNewItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_item);
        StatusBarUtil.setStatusColor(this, getColor(R.color.black));

    }
}