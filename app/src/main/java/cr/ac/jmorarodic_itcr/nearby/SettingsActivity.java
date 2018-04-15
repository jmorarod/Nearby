package cr.ac.jmorarodic_itcr.nearby;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Carga lista vertical categorias
        //Ejemplo de datos
        ArrayList<CategoriaItem> Categories = new ArrayList<>();

        for (int i = 0; i < 10; i++)
        {
            CategoriaItem s = new CategoriaItem("SubCategoria",R.drawable.photo);
            Categories.add(s);
        }

        //carga en recyclerview
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.listCategorieView);
        recyclerView.setLayoutManager(layoutManager);


        CategorieSettingsAdapter adapterC = new CategorieSettingsAdapter(Categories, this);
        recyclerView.setAdapter(adapterC);


        ArrayList<CategoriaItem> subCategories = new ArrayList<>();

        for (int i = 0; i < 10; i++)
        {
            CategoriaItem s = new CategoriaItem("SubCategoria",R.drawable.photo);
            subCategories.add(s);
        }

        final ListView listView = (ListView) findViewById(R.id.listSubCategorie);
        SubCategorieAdapter groupMainAdapter = new SubCategorieAdapter(this,R.layout.list_item_settings_subcategorie,subCategories);
        listView.setAdapter(groupMainAdapter);

    }

    public void onClickBack(View view)
    {
        Intent intent = new Intent(this,IndexActivity.class);
        startActivity(intent);
    }
}
