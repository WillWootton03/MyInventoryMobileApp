package com.zybooks.cs360finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Base64;
import android.widget.Toast;

import java.util.UUID;


public class MainActivity extends AppCompatActivity {

    boolean isLoginMode = true;

    // Login Screen Vars
    Button primaryButton;
    Button secondaryButton;
    TextView loginTitle;

    EditText inputUsername;
    EditText inputPassword;
    EditText inputPhone;

    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        primaryButton = findViewById(R.id.primaryButton);
        secondaryButton = findViewById(R.id.secondaryButton);
        loginTitle = findViewById(R.id.pageTitle);
        inputUsername = findViewById(R.id.usernameField);
        inputPassword = findViewById(R.id.passwordField);
        inputPhone = findViewById(R.id.phoneField);

        db = AppDatabaseClient.getInstance(this);

        updateUI();

        // Updates is LoginMode when clicked and calls update UI
        secondaryButton.setOnClickListener(v -> {
            // Sets isLoginMode to opposite when button clicked
            isLoginMode = !isLoginMode;

            updateUI();
        });

        primaryButton.setOnClickListener(v -> {
            if (isLoginMode){
                loginUser();
            } else {
                registerUser();
            }

        });

    }

    // Updates UI dynamically depending on page user wants to see
    private void updateUI(){
        inputUsername.setText("");
        inputPassword.setText("");
        inputPhone.setText("");
        if (isLoginMode) {
            loginTitle.setText(getString(R.string.login));
            primaryButton.setText(getString(R.string.login));
            secondaryButton.setText(getString(R.string.registerSwitch));
            inputPhone.setVisibility(TextView.GONE);
        } else {
            loginTitle.setText(getString(R.string.register));
            primaryButton.setText(getString(R.string.register));
            secondaryButton.setText(getString(R.string.loginSwitch));
            inputPhone.setVisibility(TextView.VISIBLE);
        }
    }

    // When logging in the user is switched to the permissions screen to grant permissions for access
    // to rest of app
    private void loginUser(){
        // find the user row in the users table to check plaintext password against hashed
        User user = db.userDAO().findByUsername(inputUsername.getText().toString());

        if (user != null) {
            // Get the salt from user row and decode it
            byte[] salt = Base64.decode(user.salt, Base64.DEFAULT);

            // Verifies input password with hashed password and salt stored in db
            if (PasswordUtil.verifyPassword(inputPassword.getText().toString(),
                    user.passwordHash, salt)) {

                    // Sends user to permissions screen
                    Intent intent = new Intent(MainActivity.this, PermissionsActivity.class);
                    intent.putExtra("USER_ID", user.userId);
                    startActivity(intent);
                }
            // Incorrect input details
            } else {
                loginTitle.setText(R.string.incorrectLogin);
            }
        }


    private void registerUser() {

        User existing = db.userDAO().findByUsername(inputUsername.getText().toString().trim());

        if (existing != null) {
            Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
            return;
        }

        String plaintextPassword = inputPassword.getText().toString().trim();

        // generate unique salt and hash given plaintext password
        byte[] salt = PasswordUtil.generateSalt();
        String hash = PasswordUtil.hashPassword(plaintextPassword, salt);

        User user = new User();
        user.username = inputUsername.getText().toString().trim();
        user.passwordHash = hash;
        user.salt = Base64.encodeToString(salt, Base64.DEFAULT);
        user.phoneNumber = inputPhone.getText().toString().trim();

        // adds the user using the DAO to the users table
        db.userDAO().insert(user);

        // create a toast displaying new user registered
        Toast.makeText(this,
                "Welcome " + user.username + "! Account successfully created." ,
                Toast.LENGTH_SHORT).show();

        // Sends user to permissions screen
        Intent intent = new Intent(MainActivity.this, PermissionsActivity.class);
        intent.putExtra("USER_ID", user.userId);
        startActivity(intent);
        finish();
    }

}