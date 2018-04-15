package cr.ac.jmorarodic_itcr.nearby;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by karizp on 15/04/2018.
 */

public class CategorieSettingsAdapterNew extends ArrayAdapter<CategoriaItem> {

    ArrayList<CategoriaItem> categorias = new ArrayList<>();

    public CategorieSettingsAdapterNew(Context context, int textViewId, ArrayList<CategoriaItem> categorias){
        super(context,textViewId,categorias);
        this.categorias = categorias;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.list_item_categorie_new, null);

        ImageView imageView = v.findViewById(R.id.imgCategorie);
        TextView textView = v.findViewById(R.id.txtCategorie);
        ImageView imageCheck = v.findViewById(R.id.imgCheck);


        if(categorias.get(position).getNombre() != null)
        {
            imageView.setImageResource(categorias.get(position).getImageID());
            textView.setText(categorias.get(position).getNombre());
            imageCheck.setImageResource(R.drawable.ic_check);
            imageCheck.setBackgroundColor(ContextCompat.getColor(v.getContext(),R.color.cardview_shadow_start_color));
            imageCheck.setVisibility(View.INVISIBLE);
        }

        return v;

    }
}
