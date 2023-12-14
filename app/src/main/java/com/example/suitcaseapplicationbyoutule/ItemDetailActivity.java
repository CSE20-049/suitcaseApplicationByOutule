package com.example.suitcaseapplicationbyoutule;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.suitcaseapplicationbyoutule.statusbar.StatusBarUtil;

public class ItemDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        StatusBarUtil.setStatusColor(this, getColor(R.color.black));

    }
}