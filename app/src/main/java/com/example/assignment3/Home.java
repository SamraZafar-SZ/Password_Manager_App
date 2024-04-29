package com.example.assignment3;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Home extends AppCompatActivity {

    RecyclerView rvurl;
    FloatingActionButton btnadd,btnRecycleBin;
    MyDatabase mydb;
    ArrayList<String> web,url,pass,id;
    URLAdaptor adaptor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        rvurl=findViewById(R.id.rvurl);
        btnadd=findViewById(R.id.btnadd);
        btnRecycleBin=findViewById(R.id.btnRecycleBin);

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Home.this,Add.class);
                startActivity(intent);
            }
        });

        btnRecycleBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, RecycleBin.class);
                startActivity(intent);
            }
        });


        mydb=new MyDatabase(Home.this);
        web=new ArrayList<>();
        pass=new ArrayList<>();
        url=new ArrayList<>();
        id=new ArrayList<>();

        displayData();
        adaptor=new URLAdaptor(Home.this,this,web,url,id,pass,false,null);
        rvurl.setAdapter(adaptor);
        rvurl.setLayoutManager(new LinearLayoutManager(Home.this));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Reload data from the database
            displayData();
            // Notify the adapter that the data has changed
            adaptor.notifyDataSetChanged();
        }
    }

    void displayData(){
        id.clear();
        web.clear();
        url.clear();
        pass.clear();
        Cursor cursor=mydb.readAllData();
        if(cursor.getCount()==0){
            Toast.makeText(this,"NO DATA FOUND",Toast.LENGTH_SHORT).show();
        }
        else{
            while(cursor.moveToNext()){
                id.add(cursor.getString(0));
                web.add(cursor.getString(1));
                url.add(cursor.getString(2));
                pass.add(cursor.getString(3));
            }
        }
        cursor.close();
    }
    public void updateDataAndNotifyAdapter() {
        // Update the dataset
        adaptor.notifyDataSetChanged();
        displayData();
        // Notify the adapter about the dataset change

    }
    // Method to delete an item
    void onDeleteItem(String itemId) {
        // Delete item from database
        mydb.DeleteRow(itemId);

        // Update the dataset and notify the adapter
        displayData();
        adaptor.notifyDataSetChanged();
    }
    @Override
    protected void onResume() {
        super.onResume();
        // Refresh data and update UI
        displayData();
        adaptor.notifyDataSetChanged();
    }

}