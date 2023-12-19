package com.example.suitcaseapplicationbyoutule;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;

import android.Manifest;
import android.widget.Toast;

public class ShareItemDialog extends AppCompatDialogFragment {

    TextInputEditText contactNumberField;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.share_item_contact_info_dialogbox, null);

        contactNumberField = view.findViewById(R.id.contactNumberID);

        builder.setView(view)
                .setTitle("Contact Info")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Assigning variables
                        String contactNumber = contactNumberField.getText().toString().trim();
                        SharedPreferences preferences = requireActivity().getApplicationContext().getSharedPreferences(
                                "Item Details Preference", Context.MODE_PRIVATE);
                        String itemName = preferences.getString("itemName", "");
                        String itemPrice = preferences.getString("itemPrice", "");
                        //String itemDescription =  preferences.getString("itemDescription", "");
                        String itemImage = preferences.getString("itemImage", "");

                        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS)
                                == PackageManager.PERMISSION_GRANTED) {
                            // When permission is granted
                            String message = "Hey, hope you are doing awesome. Please consider adding this item to " +
                                    "your list" +
                                    "\n\nName: " + itemName +
                                    "\nPrice: " + itemPrice +
                                    "\nImage: " + itemImage;
                            sendSMS(contactNumber,message);
                        } else {
                            // When permission is not granted, request for permission
                            Toast.makeText(requireContext(), "No No No No", Toast.LENGTH_SHORT).show();
                            ActivityCompat.requestPermissions(requireActivity(),
                                    new String[]{Manifest.permission.SEND_SMS}, 100);
                        }
                    }
                });

        return builder.create();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        SharedPreferences preferences = requireActivity().getApplicationContext().getSharedPreferences(
                "Item Details Preference", Context.MODE_PRIVATE);
        String itemName = preferences.getString("itemName", "");
        String itemPrice = preferences.getString("itemPrice", "");
        //String itemDescription =  preferences.getString("itemDescription", "");
        String itemImage = preferences.getString("itemImage", "");

        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can now send SMS
                String contactNumber = contactNumberField.getText().toString().trim();

                String message = "Hey, hope you are doing awesome. Please consider adding this item to " +
                        "your list" +
                        "\n\nName: " + itemName +
                        "\nPrice: " + itemPrice +
                        "\nImage: " + itemImage;
                sendSMS(contactNumber,message);
            } else {
                // Permission denied, handle this case
                Toast.makeText(requireContext(), "Permission denied. Cannot send SMS.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void sendSMS(String contactNumber,String message) {

        // Checking if String is empty or not
        if (!contactNumber.isEmpty()) {
            // Initialize SMS Manager
            SmsManager smsManager = SmsManager.getDefault();
            // Send Message
            smsManager.sendTextMessage(contactNumber, null, message, null, null);
            // Display SMS toast
            Toast.makeText(requireContext(), "SMS sent", Toast.LENGTH_SHORT).show();
        } else {

            // When String is empty
            Toast.makeText(requireContext(), "Please enter contact number", Toast.LENGTH_SHORT).show();

        }
    }
}