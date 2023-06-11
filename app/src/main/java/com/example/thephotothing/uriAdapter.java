package com.example.thephotothing;

import static android.content.ContentValues.TAG;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class uriAdapter extends RecyclerView.Adapter<uriAdapter.ViewHolder> {
    private ArrayList<modelClass> imguris;
    private Context context;



    public uriAdapter(Context context, ArrayList<modelClass> imguris) {
        this.context = context;
        this.imguris=imguris;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ind_img,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull uriAdapter.ViewHolder holder, int position) {

        Picasso.with(context).load(imguris.get(position).getImg()).into(holder.img,new com.squareup.picasso.Callback(){

            @Override
            public void onSuccess() {
                Toast.makeText(context, "Success rc", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError() {
                Toast.makeText(context, "failer rc", Toast.LENGTH_SHORT).show();
            }
        });
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Individual download not available", Toast.LENGTH_SHORT).show();
            }
        });



    }

    @Override
    public int getItemCount() {
        return imguris.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView img;
        private ImageButton imageButton;
        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.back_img);
            imageButton = itemView.findViewById(R.id.but_img);

        }
    }
}