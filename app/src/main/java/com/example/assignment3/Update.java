package com.example.assignment3;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Update extends AppCompatActivity {

    EditText etweb2,eturl2,etpass2;
    Button btnupdate, btndelete;
    String id,name,url,pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update);
        init();
        getintentdata();
        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String updatedName = etweb2.getText().toString().trim();
                String updatedUrl = eturl2.getText().toString().trim();
                String updatedPass = etpass2.getText().toString().trim();
                MyDatabase mydb=new MyDatabase(Update.this);
                mydb.updateData(id, updatedName, updatedUrl, updatedPass);
                setResult(RESULT_OK);
                finish();
            }
        });
        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog();
            }
        });

    }
    public void init(){
        etweb2=findViewById(R.id.etweb2);
        eturl2=findViewById(R.id.eturl2);
        etpass2=findViewById(R.id.etpass2);
        btnupdate=findViewById(R.id.btnupdate);
        btndelete=findViewById(R.id.btndelete);
    }

    void getintentdata(){
        if(getIntent().hasExtra("id")&&getIntent().hasExtra("name")&&getIntent().hasExtra("url")
        &&getIntent().hasExtra("password")){
            //GET INTENT DATA
            id=getIntent().getStringExtra("id");
            name=getIntent().getStringExtra("name");
            url=getIntent().getStringExtra("url");
            pass=getIntent().getStringExtra("password");
            //SET INTENT DATA
            etweb2.setText(name);
            eturl2.setText(url);
            etpass2.setText(pass);
        }
    }
    void confirmDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Delete "+name+"?");
        builder.setMessage("Are you sure you want to delete "+name+"?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               // MyDatabase mydb=new MyDatabase(Update.this);
                //mydb.DeleteRow(id);
                moveToRecycleBin();
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.create().show();

    }
    void moveToRecycleBin() {
        MyDatabase mydb = new MyDatabase(Update.this);
        mydb.moveToRecycleBin(id, name, url, pass);
        Toast.makeText(Update.this, name + " moved to Recycle Bin", Toast.LENGTH_SHORT).show();
        finish();
    }

}