package cr.ac.jmorarodic_itcr.nearby;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

public class SubcategoriasActivity extends AppCompatActivity {
    ArrayList<Integer> subCategorias = new ArrayList<Integer>();
    ArrayList<CategoriaItem> subCategoriasItems;

    public void onClickContinuar(View view){
        //TODO: crear Usuario
        SharedPreferences sharedPreferences =  getApplicationContext().getSharedPreferences("cr.ac.jmorarodic_itcr.nearby.sharedpreferences",MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("logged",true).apply();
        Intent intent = new Intent(this,IndexActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcategorias);
        final GridView gridView = findViewById(R.id.gridViewSubCategorias);
        subCategoriasItems = new ArrayList<>();
        //TODO: cargar las categorias del backend y crear los Categoria Item para pasarlos al adapter

        CategoriaAdapter categoriaAdapter=new CategoriaAdapter(this,R.layout.grid_view_items,subCategoriasItems);
        gridView.setAdapter(categoriaAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                subCategorias.add(position);
                //adapter.removeitem(position)
                //adapter.notifyDataSetChanged
            }
        });
    }
}

