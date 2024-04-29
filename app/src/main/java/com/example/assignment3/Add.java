package com.example.assignment3;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Add extends AppCompatActivity {

    EditText etweb,eturl,etpass;
    Button btnsave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add);
        init();

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDatabase mydb=new MyDatabase(Add.this);
                mydb.add(etweb.getText().toString().trim(), eturl.getText().toString().trim(),etpass.getText().toString().trim());

            }
        });

    }

    public void init(){
        etweb=findViewById(R.id.etweb);
        eturl=findViewById(R.id.eturl);
        etpass=findViewById(R.id.etpass);
        btnsave=findViewById(R.id.btnsave);
    }
}