package cr.ac.jmorarodic_itcr.nearby;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SubCategorieFragment extends Fragment {


    public SubCategorieFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View RootView = inflater.inflate(R.layout.fragment_sub_categorie, container, false);

        ArrayList<CategoriaItem> categorias = new ArrayList<>();

        for(int i=0;i<10;i++)
        {
            CategoriaItem c = new CategoriaItem("Categoria",R.drawable.health);
            categorias.add(c);
        }

        final GridView gridView = (GridView) RootView.findViewById(R.id.gridSubCategorie);
        CategorieSettingsAdapterNew categorieSettingsAdapterNew = new CategorieSettingsAdapterNew(getActivity().getApplicationContext(),R.layout.list_item_categorie_new,categorias);


        gridView.setAdapter(categorieSettingsAdapterNew);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ImageView check =view.findViewById(R.id.imgCheck);

                if (check.getVisibility()==View.INVISIBLE)
                {
                    check.setVisibility(View.VISIBLE);
                }
                else
                {
                    check.setVisibility(View.INVISIBLE);
                }
            }
        });

        return RootView;
    }

}
