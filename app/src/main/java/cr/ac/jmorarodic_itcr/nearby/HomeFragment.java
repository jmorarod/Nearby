package cr.ac.jmorarodic_itcr.nearby;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /* Inflate the layout for this fragment*/

        View RootView = inflater.inflate(R.layout.fragment_home, container, false);

        String[] mCategories = {"Salud", "Tecnología","Naturaleza","Aprendizaje", "Fotografía","Deportes"};
        Integer[] mImages = {R.drawable.health,R.drawable.technology,R.drawable.nature,R.drawable.learn,R.drawable.photo,R.drawable.sports};



        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL,false);
        RecyclerView recyclerView = (RecyclerView) RootView.findViewById(R.id.listMainCategorieView);
        recyclerView.setLayoutManager(layoutManager);


        CategorieMainAdapter adapterC = new CategorieMainAdapter(mCategories,mImages,getActivity().getApplicationContext());
        recyclerView.setAdapter(adapterC);



        return RootView;




    }

}
