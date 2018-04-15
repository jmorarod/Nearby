package cr.ac.jmorarodic_itcr.nearby;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View RootView = inflater.inflate(R.layout.fragment_profile, container, false);

        ArrayList<ListItemProfile> listaGrupos = new ArrayList<>();

        for(int i =0; i<10;i++) {
            ListItemProfile l = new ListItemProfile(R.drawable.sports, "Titulo de noticia");
            listaGrupos.add(l);
        }

        //Carga en listview
        final ListView listView = (ListView) RootView.findViewById(R.id.listGroup);
        ListItemProfileAdapter listItemProfile = new ListItemProfileAdapter(getActivity().getApplicationContext(),R.layout.list_item_profile,listaGrupos);
        listView.setAdapter(listItemProfile);


        ArrayList<ListItemProfile> listaEventos = new ArrayList<>();

        for(int i =0; i<10;i++) {
            ListItemProfile l = new ListItemProfile(R.drawable.photo, "Titulo de noticia");
            listaEventos.add(l);
        }

        //Carga en listview
        final ListView listView2 = (ListView) RootView.findViewById(R.id.listEvent);
        ListItemProfileAdapter listItemProfile2 = new ListItemProfileAdapter(getActivity().getApplicationContext(),R.layout.list_item_profile,listaEventos);
        listView2.setAdapter(listItemProfile2);

        ImageView edit = RootView.findViewById(R.id.imgEdit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(),EditProfileActivity.class);
                startActivity(intent);
            }
        });

        ImageView settings = RootView.findViewById(R.id.imgSettings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(),SettingsNewActivity.class);
                startActivity(intent);
            }
        });


        return RootView;
    }



}
