package cr.ac.jmorarodic_itcr.nearby;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

public class ListItemProfileAdapter extends ArrayAdapter<ListItemProfile> {

    private ArrayList<ListItemProfile> lista = new ArrayList<>();

    public ListItemProfileAdapter(Context context, int listViewId, ArrayList<ListItemProfile> lista)
    {
        super(context,listViewId,lista);
        this.lista=lista;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        View v = convertView;

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.list_item_profile,null);

        ImageView imageCategorie = v.findViewById(R.id.imgItemProfile);
        TextView title = v.findViewById(R.id.txtTitle);


        if(lista.get(position)!=null)
        {
            imageCategorie.setImageResource(lista.get(position).getImage());
            title.setText(lista.get(position).getTitle());
        }

        return v;
    }
}
