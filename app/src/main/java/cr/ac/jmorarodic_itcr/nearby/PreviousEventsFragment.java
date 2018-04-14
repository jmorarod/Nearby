package cr.ac.jmorarodic_itcr.nearby;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class PreviousEventsFragment extends Fragment {


    public PreviousEventsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View RootView = inflater.inflate(R.layout.fragment_previous_events, container, false);

        ArrayList<EventCalendarItem> eventos = new ArrayList<>();

        for(int i =0; i<10;i++) {
            EventCalendarItem e = new EventCalendarItem(R.drawable.health, "Titulo de noticia", "14 Abril 2018");
            eventos.add(e);
        }

        //Carga en listview
        final ListView listView = (ListView) RootView.findViewById(R.id.previousEventList);
        EventCalendarAdapter eventMainAdapter = new EventCalendarAdapter(getActivity().getApplicationContext(),R.layout.list_item_event_calendar,eventos);
        listView.setAdapter(eventMainAdapter);


        return RootView;
    }

}
