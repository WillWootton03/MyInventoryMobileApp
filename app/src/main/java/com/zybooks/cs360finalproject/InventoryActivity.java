package com.zybooks.cs360finalproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class InventoryActivity extends AppCompatActivity {

    private TableLayout tableLayout;
    private EditText inputName, inputQuantity;
    private Button addButton;
    private AppDatabase db;
    private String userId;
    private boolean editingRow;
    private String editingItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory);

        tableLayout = findViewById(R.id.tableLayout);
        inputName = findViewById(R.id.inputName);
        inputQuantity = findViewById(R.id.inputQuantity);
        addButton = findViewById(R.id.addButton);

        addButton.setOnClickListener(v -> {
            if (!editingRow) {
                addNewItem();
            } else {
                updateItem(editingItemId);
                // After item editing reset to give native look
                editingRow = false;
                editingItemId = "";
                addButton.setText(R.string.buttonAdd);
                inputName.setText("");
                inputQuantity.setText("");
            }
        });

        userId = getIntent().getStringExtra("USER_ID");

        db = AppDatabaseClient.getInstance(this);

        editingRow = false;

        loadTable();
    }

    private void loadTable() {
        // Refreshes table layout
        tableLayout.removeAllViews();

        List<InventoryItem> items = db.inventoryDAO().getItemsForUser(userId);

        // iterate through all items in the items list
        for (InventoryItem item : items) {
            TableRow row = new TableRow(this);

            // Set text view for the Item column in row
            TextView itemText = new TextView(this);
            itemText.setText(item.itemName);
            itemText.setPadding(8, 8, 8, 8);

            // Set the text view for the Quantity column in row
            TextView quantityText = new TextView(this);
            quantityText.setText(Integer.toString(item.itemQuantity));
            quantityText.setPadding(8, 8, 8, 8);

            // Set values for delete button
            Button deleteButton = new Button(this);
            deleteButton.setText(R.string.delete);

            row.setTag(item.id);

            // Set listener to delete buttons when clicked
            deleteButton.setOnClickListener(v ->
            {
                TableRow itemRow = (TableRow) v.getParent();

                db.inventoryDAO().deleteById((String) itemRow.getTag());
                loadTable();
            });

            // Set listener when a row is selected
            row.setOnClickListener(v -> {
                // If user is already editing this row, reset and return to add row
                if (editingRow){
                    editingRow = false;
                    editingItemId = "";
                    inputName.setText("");
                    inputQuantity.setText("");
                    addButton.setText(R.string.buttonAdd);
                } else {
                    editingRow = true;
                    editingItemId = item.id;
                    inputName.setText(item.itemName);
                    inputQuantity.setText(Integer.toString(item.itemQuantity));
                    addButton.setText(R.string.buttonUpdate);
                }
            });

            // Add row details to new row
            row.addView(itemText);
            row.addView(quantityText);
            row.addView(deleteButton);

            // Add row to table
            tableLayout.addView(row);

            // Reset Input texts for new items
            inputName.setText("");
            inputQuantity.setText("");
        }
    }


    private void addNewItem() {
        // Get the data from the input
        String name = inputName.getText().toString().trim();
        String quantity = inputQuantity.getText().toString();

        InventoryItem newItem = new InventoryItem();
        newItem.itemName = name;
        newItem.userOwnerId = userId;
        // Tries to convert items quantity from int to string
        try {
            newItem.itemQuantity = Integer.parseInt(quantity.trim());
        } catch (Exception e) {
            newItem.itemQuantity = 0;
        }
        db.inventoryDAO().insert(newItem);
        // Toast to say item added
        Toast.makeText(this,"Inserted " + newItem.itemName, Toast.LENGTH_SHORT).show();

        checkLowQuantity(newItem.id);

        loadTable();

        // Reset Input texts for new items
        inputName.setText("");
        inputQuantity.setText("");
    }

    private void updateItem(String editingItemId) {
        InventoryItem item = db.inventoryDAO().getItem(userId, editingItemId);

        // if that item cannot be found return null to avoid crashing
        if (item == null) return;

        item.itemName = inputName.getText().toString().trim();
        try {
            item.itemQuantity = Integer.parseInt(inputQuantity.getText().toString().trim());
        } catch (Exception e) {
            // If integer can't be parsed from input don't update quantity
        }

        Toast.makeText(this, "Updated item " + item.itemName,
                Toast.LENGTH_SHORT).show();

        db.inventoryDAO().update(item);

        checkLowQuantity(item.id);

        // refresh the table after updating
        loadTable();
    }

    private void checkLowQuantity(String itemId){

        InventoryItem item = db.inventoryDAO().getItem(userId, itemId);
        String phone = db.userDAO().getPhoneNumber(userId);

        // if user has given permission and quantity is low it attempts to send SMS to current number
        if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
                && item.itemQuantity < 5 && phone != null && !phone.isEmpty() ) {

            try {
                SmsManager smsManager = getSystemService(SmsManager.class);
                smsManager.sendTextMessage(
                        phone,
                        null,
                        "Item " + item.itemName + " Quantity is low at " + item.itemQuantity,
                        null,
                        null
                );
                // Shows toast to display that it accurately sent a text
                Toast.makeText(this, "SMS Send to " + phone,
                        Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Failed to send SMS", Toast.LENGTH_SHORT).show();
            }
            } else {
                Toast.makeText(this, "Could not send SMS", Toast.LENGTH_SHORT).show();
        }

    }
}


