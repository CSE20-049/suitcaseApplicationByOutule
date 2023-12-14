package com.example.suitcaseapplicationbyoutule;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;

public class PurchasedActivity extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View purchasedActivityScreen = inflater.inflate(R.layout.fragment_purchased_activity, container, false);

        //This code below helps style the application's name by applying color gradient.
        MaterialTextView purchasedTextView = purchasedActivityScreen.findViewById(R.id.purchasedTextView);
        TextPaint textPaint = purchasedTextView.getPaint();
        float width = textPaint.measureText(getResources().getString(R.string.app_name));

        Shader textShader = new LinearGradient(0, 0, width, purchasedTextView.getTextSize(),
                new int[] {
                        Color.parseColor("#EB00A1"),
                        Color.parseColor("#7885FF"),
                }, null, Shader.TileMode.CLAMP);
        purchasedTextView.getPaint().setShader(textShader);


        return purchasedActivityScreen;
    }
}