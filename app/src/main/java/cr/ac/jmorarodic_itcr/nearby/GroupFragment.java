package cr.ac.jmorarodic_itcr.nearby;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class GroupFragment extends Fragment {


    public GroupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View RootView = inflater.inflate(R.layout.fragment_group, container, false);


        //Carga lista vertical categorias
        //Ejemplo de datos
        String[] mCategories = {"Salud", "Tecnología","Naturaleza","Aprendizaje", "Fotografía","Deportes"};
        Integer[] mImages = {R.drawable.health,R.drawable.technology,R.drawable.nature,R.drawable.learn,R.drawable.photo,R.drawable.sports};

        //carga en recyclerview
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL,false);
        final RecyclerView recyclerView = (RecyclerView) RootView.findViewById(R.id.listMainCategorieView);
        recyclerView.setLayoutManager(layoutManager);


        CategorieMainAdapter adapterC = new CategorieMainAdapter(mCategories,mImages,getActivity().getApplicationContext());
        recyclerView.setAdapter(adapterC);


        //Carga eventos disponibles
        //Solo son ejemplos de eventos
        ArrayList<GroupItem> groupItems = new ArrayList<>();
        GroupItem g = new GroupItem("Username", "4.5", "Titulo 1" , "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur",
                R.drawable.sports,R.drawable.profile_default);

        groupItems.add(g);

        g = new GroupItem("Username", "4.5", "Titulo 1" , "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur",
                R.drawable.photo,R.drawable.profile_default);

        groupItems.add(g);

        g = new GroupItem("Username", "4.5", "Titulo 1" , "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur",
                R.drawable.technology,R.drawable.profile_default);

        groupItems.add(g);

        //Carga en listview
        final ListView listView = (ListView) RootView.findViewById(R.id.listMainGroup);
        GroupMainAdapter groupMainAdapter = new GroupMainAdapter(getActivity().getApplicationContext(),R.layout.list_item_group_main,groupItems);
        listView.setAdapter(groupMainAdapter);



        return RootView;
    }

}
