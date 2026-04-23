package com.zybooks.cs360finalproject;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;


public class PermissionsActivity extends AppCompatActivity {

    private TextView permissionStatus;

    private Button viewInventory;

    private Button requestButton;

    private ActivityResultLauncher<String> permissionLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.permissions);

        permissionStatus = findViewById(R.id.permissionStatus);
        requestButton = findViewById(R.id.requestPermissionButton);
        viewInventory = findViewById(R.id.viewInventory);



        // if already has permissions skip and go straight to inventory
        if (hasPermission()) {
            goToInventory();
            return;
        }

        // Launcher to handle Permissions
        permissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted){
                        goToInventory();
                    } else {
                        permissionStatus.setText(R.string.noPermission);
                    }
                }
        );
        // Allows for permissions to be launched when pressing request
        requestButton.setOnClickListener(v -> requestPermission());

        // Allows user to skip allowing permission
        viewInventory.setOnClickListener(v -> goToInventory());
    }

    // Checks if permission has already been set
    private boolean hasPermission() {
        return checkSelfPermission(Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED;
    }

    // Launches the permission launched used in the onCreate function
    private void requestPermission() {
        permissionLauncher.launch(Manifest.permission.SEND_SMS);
    }

    private void goToInventory(){
        Intent intent = new Intent(this, InventoryActivity.class);
        intent.putExtra("USER_ID", getIntent().getStringExtra("USER_ID"));
        startActivity(intent);
        finish();
    }

}
