package com.example.sqliteinandroid;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    Button addBtn, readBtn, editBtn, deleteBtn;
    EditText idTxt, nameTxt;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //DB_CODE
        dbHelper = new DBHelper(this);

        addBtn = findViewById(R.id.btnAdd);
        readBtn = findViewById(R.id.btnRead);
        editBtn = findViewById(R.id.btnEdit);
        deleteBtn = findViewById(R.id.btnDelete);
        idTxt = findViewById(R.id.txtIDVal);
        nameTxt = findViewById(R.id.txtNameVal);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                dbHelper.writeData(idTxt.getText().toString(),nameTxt.getText().toString());
            }
        });//end
        readBtn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                Cursor crs= dbHelper.readData(idTxt.getText().toString());
                if (crs.moveToFirst()){
                    do{
                        nameTxt.setText(crs.getString(0));
                    }
                    while(crs.moveToNext());
                }
                crs.close();
            }
        });//end
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                dbHelper.updateData(idTxt.getText().toString(),nameTxt.getText().toString());
            }
        });//end
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                dbHelper.deleteData(idTxt.getText().toString());
            }
        });//end
    }
}