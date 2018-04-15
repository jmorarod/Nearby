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
 * Created by karizp on 14/04/2018.
 */

public class SubCategorieAdapter extends ArrayAdapter<CategoriaItem> {
    ArrayList<CategoriaItem> subCategorie = new ArrayList<>();

    public SubCategorieAdapter(Context context, int textViewId, ArrayList<CategoriaItem> subCategorie){
        super(context,textViewId,subCategorie);
        this.subCategorie = subCategorie;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.list_item_settings_subcategorie, null);

        ImageView imageView = v.findViewById(R.id.imgCategorie);
        ImageView remove = v.findViewById(R.id.imgSub);
        TextView userText = v.findViewById(R.id.txtCategorie);

        if(subCategorie.get(position).getImagen() != null) {
            userText.setText(subCategorie.get(position).getNombre());
            imageView.setImageBitmap(subCategorie.get(position).getImagen());
            remove.setImageResource(R.drawable.ic_remove);
        }

        return v;

    }
}
