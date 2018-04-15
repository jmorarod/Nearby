package cr.ac.jmorarodic_itcr.nearby;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by karizp on 14/04/2018.
 */

public class CategorieSettingsAdapter extends RecyclerView.Adapter<CategorieSettingsAdapter.ViewHolder>{

    private ArrayList<CategoriaItem> categoriaItems = new ArrayList<>();
    private Context context;


    public CategorieSettingsAdapter(ArrayList<CategoriaItem> categoriaItems, Context context) {
        this.categoriaItems = categoriaItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_categorie_settings,
                parent ,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.name.setText(categoriaItems.get(position).getNombre());
        holder.image.setImageResource(categoriaItems.get(position).getImageID());
        holder.remove.setImageResource(R.drawable.ic_remove);


    }



    @Override
    public int getItemCount() {
        return categoriaItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView image;
        TextView name;
        ImageView remove;


        public ViewHolder(View itemView) {
            super(itemView);
            this.image = itemView.findViewById(R.id.imgCategorie);
            this.name = itemView.findViewById(R.id.txtCategorie);
            this.remove = itemView.findViewById(R.id.imgSub);
        }
    }
}
