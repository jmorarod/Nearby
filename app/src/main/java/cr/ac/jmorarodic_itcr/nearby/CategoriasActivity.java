package cr.ac.jmorarodic_itcr.nearby;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

public class CategoriasActivity extends AppCompatActivity {
    ArrayList<Integer> categorias = new ArrayList<Integer>();
    ArrayList<CategoriaItem> categoriasItems;
    public void onClickGrid(View view){

    }
    public void onClickContinuar(View view){
        Intent intent = new Intent(this,SubcategoriasActivity.class);
        intent.putExtra("Categorias",categorias.toArray());
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);

        final GridView gridView = findViewById(R.id.gridViewCategorias);
        categoriasItems = new ArrayList<>();
        //TODO: cargar las categorias del backend y crear los Categoria Item para pasarlos al adapter

        CategoriaAdapter categoriaAdapter=new CategoriaAdapter(this,R.layout.grid_view_items,categoriasItems);
        gridView.setAdapter(categoriaAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                categorias.add(position);
                //adapter.removeitem(position)
                //adapter.notifyDataSetChanged
            }
        });
    }
}
