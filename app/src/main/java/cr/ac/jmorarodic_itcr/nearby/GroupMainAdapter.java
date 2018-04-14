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

public class GroupMainAdapter extends ArrayAdapter<GroupItem> {

    ArrayList<GroupItem> grupos = new ArrayList<>();

    public GroupMainAdapter(Context context, int listViewId, ArrayList<GroupItem> grupos)
    {
        super(context,listViewId,grupos);
        this.grupos=grupos;
    }

@Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        View v = convertView;

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.list_item_group_main,null);

            ImageView imageCategorie = v.findViewById(R.id.imgCategorie);
            CircleImageView imageProfile = v.findViewById(R.id.profile_image);
            TextView username = v.findViewById(R.id.txtUser);
            TextView stars = v.findViewById(R.id.txtPuntuacion);
            TextView title = v.findViewById(R.id.txtTitulo);
            TextView descrip = v.findViewById(R.id.txtDescrip);


            if(grupos.get(position)!=null)
            {
                imageCategorie.setImageResource(grupos.get(position).getImageCategorie());
                imageProfile.setImageResource(grupos.get(position).getImageProfile());
                username.setText(grupos.get(position).getUsername());
                stars.setText(grupos.get(position).getStars());
                title.setText(grupos.get(position).getTitle());
                descrip.setText(grupos.get(position).getDescription());

                ImageView imageView = v.findViewById(R.id.imageStar);
                imageView.setImageResource(R.drawable.ic_star);
            }



            return v;
    }
}
