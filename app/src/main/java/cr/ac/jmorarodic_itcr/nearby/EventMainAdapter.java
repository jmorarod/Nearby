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

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by karizp on 13/04/2018.
 */

public class EventMainAdapter extends ArrayAdapter<EventItem> {

    ArrayList<EventItem> eventos = new ArrayList<>();

    public EventMainAdapter(Context context, int listViewId, ArrayList<EventItem> eventos)
    {
        super(context,listViewId,eventos);
        this.eventos=eventos;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        View v = convertView;

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.list_item_events_main,null);

        ImageView imageCategorie = v.findViewById(R.id.imgCategorie);
        CircleImageView imageProfile = v.findViewById(R.id.profile_image);
        TextView date = v.findViewById(R.id.txtFecha);
        TextView username = v.findViewById(R.id.txtUser);
        TextView stars = v.findViewById(R.id.txtPuntuacion);
        TextView title = v.findViewById(R.id.txtTitulo);
        TextView descrip = v.findViewById(R.id.txtDescrip);


        if(eventos.get(position)!=null)
        {

            imageCategorie.setImageBitmap(eventos.get(position).getImageCategorie());
            imageProfile.setImageResource(eventos.get(position).getImageProfile());
            date.setText(eventos.get(position).getDate());
            username.setText(eventos.get(position).getUsername());
            stars.setText(eventos.get(position).getStars());
            title.setText(eventos.get(position).getTitle());
            descrip.setText(eventos.get(position).getDescription());

            ImageView imageView = v.findViewById(R.id.imageStar);
            imageView.setImageResource(R.drawable.ic_star);
        }



        return v;
    }
}
