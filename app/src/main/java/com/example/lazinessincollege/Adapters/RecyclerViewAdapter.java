package com.example.lazinessincollege.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lazinessincollege.R;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "MEU RecyclerViewAdapter";
    private ArrayList<String> mFotosUrls = new ArrayList<>();
    private Context mContext;

    public RecyclerViewAdapter(ArrayList<String> fotosUrls, Context context) {
        mFotosUrls = fotosUrls;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: criado");

        View view =
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.item_image, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(mContext)
                .asBitmap()
                .load(mFotosUrls.get(position))
                .into(holder.foto);
        holder.foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "OnBindViewHolder: Click na iamgem");
            }
        });

    }

    @Override
    public int getItemCount() {
        return mFotosUrls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView foto;

        public ViewHolder(View itemView) {
            super(itemView);
            foto = itemView.findViewById(R.id.iv_item);
        }
    }
}
