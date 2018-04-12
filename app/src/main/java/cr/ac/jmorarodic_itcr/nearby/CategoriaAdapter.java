package cr.ac.jmorarodic_itcr.nearby;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by josem on 11/4/2018.
 */

public class CategoriaAdapter extends ArrayAdapter<CategoriaItem> {

    ArrayList<CategoriaItem> categorias = new ArrayList<>();

    public CategoriaAdapter(Context context, int textViewId, ArrayList<CategoriaItem> categorias){
        super(context,textViewId,categorias);
        this.categorias = categorias;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.grid_view_items, null);

        ImageView imageView = v.findViewById(R.id.categoriaImg);


        if(categorias.get(position).getImagen() != null)
            imageView.setImageBitmap(categorias.get(position).getImagen());

        return v;

    }
}
