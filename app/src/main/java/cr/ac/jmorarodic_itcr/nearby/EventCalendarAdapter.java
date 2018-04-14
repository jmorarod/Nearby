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

public class EventCalendarAdapter extends ArrayAdapter<EventCalendarItem>{

    ArrayList<EventCalendarItem> eventos = new ArrayList<>();

    public EventCalendarAdapter(Context context, int listViewId, ArrayList<EventCalendarItem> eventos)
    {
        super(context,listViewId,eventos);
        this.eventos=eventos;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        View v = convertView;

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.list_item_event_calendar,null);

        ImageView imageCategorie = v.findViewById(R.id.imgEventCalendar);
        TextView date = v.findViewById(R.id.txtDate);
        TextView title = v.findViewById(R.id.txtTitle);


        if(eventos.get(position)!=null)
        {

            imageCategorie.setImageResource(eventos.get(position).getImage());
            date.setText(eventos.get(position).getDate());
            title.setText(eventos.get(position).getTitle());


        }



        return v;
    }

}
