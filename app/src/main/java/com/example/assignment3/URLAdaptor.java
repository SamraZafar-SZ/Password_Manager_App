package com.example.assignment3;// URLAdaptor.java

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment3.R;
import com.example.assignment3.Update;

import java.util.ArrayList;

public class URLAdaptor extends RecyclerView.Adapter<URLAdaptor.MyViewHolder> {

    private Context context;
    private ArrayList<String> name, url, password, id;
    private Activity activity;
    private boolean isRecycleBinAdapter; // Flag to determine the adapter type
    private RecycleBinActionListener listener;

    URLAdaptor(Activity activity, Context context, ArrayList<String> name, ArrayList<String> url, ArrayList<String> id, ArrayList<String> password, boolean isRecycleBinAdapter, RecycleBinActionListener listener) {
        this.activity = activity;
        this.context = context;
        this.name = name;
        this.id = id;
        this.url = url;
        this.password = password;
        this.isRecycleBinAdapter = isRecycleBinAdapter;
        this.listener = listener;
    }

    @NonNull
    @Override
    public URLAdaptor.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull URLAdaptor.MyViewHolder holder, int position) {
        holder.tvid.setText(String.valueOf(id.get(position)));
        holder.tvweb.setText(String.valueOf(name.get(position)));
        holder.tvpassword.setText(String.valueOf(password.get(position)));
        holder.tvurl.setText(String.valueOf(url.get(position)));

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRecycleBinAdapter) {
                    showOptionsDialog(position);
                } else {
                    // Handle click for non-recycle bin adapter
                    Intent intent = new Intent(context, Update.class);
                    intent.putExtra("id", String.valueOf(id.get(position)));
                    intent.putExtra("name", String.valueOf(name.get(position)));
                    intent.putExtra("url", String.valueOf(url.get(position)));
                    intent.putExtra("password", String.valueOf(password.get(position)));
                    activity.startActivityForResult(intent, 1);
                }
            }
        });
    }

    private void showOptionsDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose an action");
        builder.setItems(new CharSequence[]{"Restore", "Delete Permanently"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        listener.onRestoreItem(position);
                        break;
                    case 1:
                        listener.onPermanentlyDeleteItem(position);
                        break;
                }
            }
        });
        builder.create().show();
    }

    @Override
    public int getItemCount() {
        return id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvid, tvurl, tvpassword, tvweb;
        LinearLayout layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvid = itemView.findViewById(R.id.tvid);
            tvweb = itemView.findViewById(R.id.tvweb);
            tvpassword = itemView.findViewById(R.id.tvpassword);
            tvurl = itemView.findViewById(R.id.tvurl);
            layout = itemView.findViewById(R.id.layout);
        }
    }

    // Interface for handling recycle bin actions
    public interface RecycleBinActionListener {
        void onRestoreItem(int position);

        void onPermanentlyDeleteItem(int position);
    }
}
