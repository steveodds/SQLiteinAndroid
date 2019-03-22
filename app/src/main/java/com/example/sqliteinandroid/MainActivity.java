package com.example.sqliteinandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    Button addBtn, readBtn, editBtn, deleteBtn;
    EditText idTxt, nameTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addBtn = findViewById(R.id.btnAdd);
        readBtn = findViewById(R.id.btnRead);
        editBtn = findViewById(R.id.btnEdit);
        deleteBtn = findViewById(R.id.btnDelete);
        idTxt = findViewById(R.id.txtIDVal);
        nameTxt = findViewById(R.id.txtNameVal);

    }
}
