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

public class HomeActivity extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View homeActivityScreen = inflater.inflate(R.layout.fragment_home_activity, container, false);

        //This code below helps style the application's name by applying color gradient.
        MaterialTextView listHubTextView = homeActivityScreen.findViewById(R.id.listHubTextView);
        TextPaint textPaint = listHubTextView.getPaint();
        float width = textPaint.measureText(getResources().getString(R.string.app_name));

        Shader textShader = new LinearGradient(0, 0, width, listHubTextView.getTextSize(),
                new int[] {
                        Color.parseColor("#EB00A1"),
                        Color.parseColor("#7885FF"),
                }, null, Shader.TileMode.CLAMP);
        listHubTextView.getPaint().setShader(textShader);

        /* Using ColorStateList resource file to define different colors for different states for the
        floatingBTN icon*/
        FloatingActionButton addBTN = homeActivityScreen.findViewById(R.id.addFabBTN);
        ColorStateList colorStateList = ContextCompat.getColorStateList(requireContext(),
                R.color.fab_icon_tint);
        addBTN.setImageTintList(colorStateList);

        //floatingBTN ActionLister
        addBTN.setOnClickListener(View -> {
            // Create an Intent for the AddNewItemActivity The starting the activity
            Intent intent = new Intent(getContext(), AddNewItemActivity.class);
            startActivity(intent);
        });


        return homeActivityScreen;
    }
}