package com.wunmansho.memeroller;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.wunmansho.util.utils;

import java.util.List;


public class PhotoGalleryAdapter extends RecyclerView.Adapter<PhotoGalleryAdapter.PersonViewHolder> {

    private final CustomItemClickListener listener;
    private final List<PicturesInfo> picturesinfo;
    private Context context;
    PhotoGalleryAdapter(Context context, List<PicturesInfo> picturesinfo, CustomItemClickListener listener) {
        this.picturesinfo = picturesinfo;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.photo_gallery_item, viewGroup, false);
        final PersonViewHolder pvh = new PersonViewHolder(v);
        //  v.setOnClickListener(v1 -> listener.onItemClick(v1, pvh.getPosition()));
        v.setOnClickListener(v1 -> listener.onItemClick(v1, pvh.getLayoutPosition()));
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
        //  personViewHolder.personName.setText(picturesinfo.get(i).name);
        //  personViewHolder.personAge.setText(picturesinfo.get(i).age);
        //  personViewHolder.personPhoto.setImageResource(picturesinfo.get(i).photoId);
        RequestOptions options = new RequestOptions();
        //  options.centerCrop().override(96, 96).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true);
        options.centerCrop().override(96, 96).diskCacheStrategy(DiskCacheStrategy.NONE);


        //   Uri image = Uri.parse(picturesinfo.get(i).photoId);
        String image = picturesinfo.get(i).photoId;
        String fileExt = utils.splitFileExt(image);

      //  Context context = ApplicationContextProvider.getContext();

        Glide.with(context)
                .load(image)
                .apply(options)
                .into(personViewHolder.personPhoto);
        //    personViewHolder.personPhoto.setImageURI(Uri.parse(picturesinfo.get(i).photoId));
    }

    @Override
    public int getItemCount() {
        return picturesinfo.size();
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        final CardView cv;
        //    TextView personName;
        //    TextView personAge;
        final ImageView personPhoto;

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            //       personName = (TextView)itemView.findViewById(R.id.person_name);
            //       personAge = (TextView)itemView.findViewById(R.id.person_age);
            personPhoto = itemView.findViewById(R.id.person_photo);
        }
    }
}
