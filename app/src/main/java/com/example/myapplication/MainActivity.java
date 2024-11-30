package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText edtNumber, edtName, edtFirstName;
    Button btnAdd, btnDelete, btnShowAll, btnNext, btnPrev, btnFirst, btnShowPlayer;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtNumber = findViewById(R.id.edtNumber); // Field for Player Number
        edtName = findViewById(R.id.edtName);     // Field for Player Name
        edtFirstName = findViewById(R.id.edtFirstName); // Field for Player First Name
        btnAdd = findViewById(R.id.btnAdd);
        btnDelete = findViewById(R.id.btnDelete);
        btnShowAll = findViewById(R.id.btnShowAll);
        btnNext = findViewById(R.id.btnNext);
        btnPrev = findViewById(R.id.btnPrev);
        btnFirst = findViewById(R.id.btnFirst);
        btnShowPlayer = findViewById(R.id.btnShowPlayer);

        // Initialize Database Helper
        dbHelper = new DatabaseHelper(this);

        btnAdd.setOnClickListener(v -> addPlayer());
        btnDelete.setOnClickListener(v -> deletePlayer());
        btnShowAll.setOnClickListener(v -> showAllPlayers());
        btnNext.setOnClickListener(v -> showNextPlayer());
        btnPrev.setOnClickListener(v -> showPreviousPlayer());
        btnFirst.setOnClickListener(v -> showFirstPlayer());
        btnShowPlayer.setOnClickListener(v -> showPlayerByNumber());
    }

    private void addPlayer() {
        String numberText = edtNumber.getText().toString();
        String name = edtName.getText().toString();
        String firstName = edtFirstName.getText().toString();

        if (numberText.isEmpty() || name.isEmpty() || firstName.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int number;
        try {
            number = Integer.parseInt(numberText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid number", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isAdded = dbHelper.addPlayer(number, name, firstName);
        if (isAdded) {
            Toast.makeText(this, "Player added successfully!", Toast.LENGTH_SHORT).show();
            clearFields();
        } else {
            Toast.makeText(this, "Player with this number already exists!", Toast.LENGTH_SHORT).show();
        }
    }

    private void deletePlayer() {
        String numberText = edtNumber.getText().toString();

        if (numberText.isEmpty()) {
            Toast.makeText(this, "Please enter the player's number to delete", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isDeleted = dbHelper.deletePlayer(Integer.parseInt(numberText));
        if (isDeleted) {
            Toast.makeText(this, "Player deleted successfully!", Toast.LENGTH_SHORT).show();
            clearFields();
        } else {
            Toast.makeText(this, "Player not found!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showPlayerByNumber() {
        String numberText = edtNumber.getText().toString();

        if (numberText.isEmpty()) {
            Toast.makeText(this, "Please enter the player's number", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] player = dbHelper.getPlayerByNumber(Integer.parseInt(numberText));
        if (player != null) {
            edtName.setText(player[0]);
            edtFirstName.setText(player[1]);
            Toast.makeText(this, "Player found!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No player found with this number!", Toast.LENGTH_SHORT).show();
            clearFields();
        }
    }

    private void clearFields() {
        edtNumber.setText("");
        edtName.setText("");
        edtFirstName.setText("");
    }

    private void showAllPlayers() {
        String allPlayers = dbHelper.getAllPlayers();
        if (allPlayers.isEmpty()) {
            Toast.makeText(this, "No players found", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("All Players");
        builder.setMessage(allPlayers);
        builder.setPositiveButton("Close", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void showNextPlayer() {
        String[] nextPlayer = dbHelper.getNextPlayer();
        if (nextPlayer != null) {
            edtNumber.setText(nextPlayer[0]);
            edtName.setText(nextPlayer[1]);
            edtFirstName.setText(nextPlayer[2]);
        } else {
            Toast.makeText(this, "No next player", Toast.LENGTH_SHORT).show();
        }
    }

    private void showPreviousPlayer() {
        String[] prevPlayer = dbHelper.getPreviousPlayer();
        if (prevPlayer != null) {
            edtNumber.setText(prevPlayer[0]);
            edtName.setText(prevPlayer[1]);
            edtFirstName.setText(prevPlayer[2]);
        } else {
            Toast.makeText(this, "No previous player", Toast.LENGTH_SHORT).show();
        }
    }

    private void showFirstPlayer() {
        String[] firstPlayer = dbHelper.getFirstPlayer();
        if (firstPlayer != null) {
            edtNumber.setText(firstPlayer[0]);
            edtName.setText(firstPlayer[1]);
            edtFirstName.setText(firstPlayer[2]);
        } else {
            Toast.makeText(this, "No players in the database", Toast.LENGTH_SHORT).show();
        }
    }
}
