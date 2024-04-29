package com.example.assignment3;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment3.MyDatabase;

import java.util.ArrayList;

public class RecycleBin extends AppCompatActivity implements URLAdaptor.RecycleBinActionListener {

    RecyclerView recyclerViewRecycleBin;
    MyDatabase myDatabase;
    URLAdaptor adapter;
    ArrayList<String> web, url, pass, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_bin);

        recyclerViewRecycleBin = findViewById(R.id.recyclerViewRecycleBin);
        myDatabase = new MyDatabase(this);

        web = new ArrayList<>();
        url = new ArrayList<>();
        pass = new ArrayList<>();
        id = new ArrayList<>();

        displayRecycleBinData();

        adapter = new URLAdaptor(RecycleBin.this, this, web, url, id, pass, true, this);
        recyclerViewRecycleBin.setAdapter(adapter);
        recyclerViewRecycleBin.setLayoutManager(new LinearLayoutManager(this));
    }

    private void displayRecycleBinData() {
        Cursor cursor = myDatabase.getRecycleBinData();
        if (cursor != null) {
            int columnIndexID = cursor.getColumnIndex(MyDatabase.COLUMN_ID);
            int columnIndexName = cursor.getColumnIndex(MyDatabase.COLUMN_DELETED_NAME);
            int columnIndexURL = cursor.getColumnIndex(MyDatabase.COLUMN_DELETED_URL);
            int columnIndexPassword = cursor.getColumnIndex(MyDatabase.COLUMN_DELETED_PASSWORD);

            while (cursor.moveToNext()) {
                id.add(cursor.getString(columnIndexID));
                web.add(cursor.getString(columnIndexName));
                url.add(cursor.getString(columnIndexURL));
                pass.add(cursor.getString(columnIndexPassword));
            }
            cursor.close();
        }
    }

    // Method to restore item from recycle bin to main table
    @Override
    public void onRestoreItem(int position) {
        String itemID = id.get(position);
        String name = web.get(position);
        String itemURL = url.get(position);
        String itemPassword = pass.get(position);

        myDatabase.restoreFromRecycleBin(itemID, name, itemURL, itemPassword);
        Toast.makeText(this, "Item restored from recycle bin", Toast.LENGTH_SHORT).show();
        removeItem(position);
    }

    // Method to permanently delete item from recycle bin
    @Override
    public void onPermanentlyDeleteItem(int position) {
        String itemID = id.get(position);
        myDatabase.deleteFromRecycleBin(itemID);
        Toast.makeText(this, "Item permanently deleted from recycle bin", Toast.LENGTH_SHORT).show();
        removeItem(position);
    }

    // Method to remove item from lists and notify adapter
    private void removeItem(int position) {
        id.remove(position);
        web.remove(position);
        url.remove(position);
        pass.remove(position);
        adapter.notifyItemRemoved(position);
    }
    // Implementing the DatabaseCallback interface method

}
