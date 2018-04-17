package cr.ac.jmorarodic_itcr.nearby;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by josem on 12/4/2018.
 */

public class ComentarioAdapter extends ArrayAdapter<ComentarioItem> {
    ArrayList<ComentarioItem> comentarios = new ArrayList<>();

    public ComentarioAdapter(Context context, int textViewId, ArrayList<ComentarioItem> comentarios){
        super(context,textViewId,comentarios);
        this.comentarios = comentarios;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.list_item_comment, null);

        ImageView imageView = v.findViewById(R.id.profile_image);
        TextView userText = v.findViewById(R.id.txtUsername);
        TextView commentText = v.findViewById(R.id.txtComment);
        userText.setText(comentarios.get(position).getUserName());
        commentText.setText(comentarios.get(position).getComentario());
        if(comentarios.get(position).getUserImage() != null)
            imageView.setImageBitmap(comentarios.get(position).getUserImage());
        else
            imageView.setImageResource(comentarios.get(position).getUserImageResource());


        return v;

    }
}
