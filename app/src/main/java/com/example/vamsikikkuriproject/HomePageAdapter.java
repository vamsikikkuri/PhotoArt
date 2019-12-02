package com.example.vamsikikkuriproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;


public class HomePageAdapter extends RecyclerView.Adapter<HomePageAdapter.HomePageViewHolder> {
    private ArrayList<String> mImageDescriptions;
    private ArrayList<String> mImageUrls;
    private ArrayList<String> mImageCategories;
    private Context mContext;

    public HomePageAdapter(Context context, ArrayList<String> imageDescs, ArrayList<String> imageUrls, ArrayList<String> categories) {
        mContext = context;
        mImageDescriptions = imageDescs;
        mImageUrls = imageUrls;
        mImageCategories = categories;
    }

    @NonNull
    @Override
    public HomePageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_page_cards_list, viewGroup, false);
        return new HomePageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HomePageViewHolder homePageViewHolder, final int i) {
        final String description = mImageDescriptions.get(i);
        homePageViewHolder.imgDesc.setText(description);
        Glide.with(mContext)
                .load(mImageUrls.get(i))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        homePageViewHolder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                }).into(homePageViewHolder.img);


        homePageViewHolder.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                share.putExtra(Intent.EXTRA_SUBJECT, "Link to the Image");
                share.putExtra(Intent.EXTRA_TEXT, mImageUrls.get(i));
                mContext.startActivity(Intent.createChooser(share, "Link to Image"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImageDescriptions.size();
    }

    public class HomePageViewHolder extends RecyclerView.ViewHolder {
        private TextView imgDesc;
        private ImageView img;
        private ImageView shareButton;
        private ProgressBar progressBar;

        public HomePageViewHolder(@NonNull View itemView) {
            super(itemView);
            imgDesc = itemView.findViewById(R.id.imageDescription);
            img = itemView.findViewById(R.id.image);
            shareButton = itemView.findViewById(R.id.shareButton);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }
}
