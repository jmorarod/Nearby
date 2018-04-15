package cr.ac.jmorarodic_itcr.nearby;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cr.ac.jmorarodic_itcr.nearby.R;

/**
 * Created by karizp on 11/04/2018.
 */

public class CategorieMainAdapter extends RecyclerView.Adapter<CategorieMainAdapter.ViewHolder>
{

    private String[] mCategories;
    private Integer[] mImages;
    private Context mContext;
    private Bitmap[] mImagesBitmaps;

    public CategorieMainAdapter(String[] mCategories, Integer[] mImages, Context mContext, Bitmap[] mImagesBitmaps) {
        this.mCategories = mCategories;
        this.mImages = mImages;
        this.mContext = mContext;
        this.mImagesBitmaps = mImagesBitmaps;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_categories_main,
                parent ,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.name.setText(mCategories[position]);
        //holder.image.setImageResource(mImages[position]);
        holder.image.setImageBitmap(mImagesBitmaps[position]);

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, mCategories[position], Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCategories.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView image;
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            this.image = itemView.findViewById(R.id.imgCategorie);
            this.name = itemView.findViewById(R.id.txtCategorie);
        }
    }

}
