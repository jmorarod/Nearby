package cr.ac.jmorarodic_itcr.nearby;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingsNewActivity extends AppCompatActivity {

    private CategorieFragment categorieFragment;
    private SubCategorieFragment subCategorieFragment;
    private Button categorie;
    private Button subcategorie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_new);

        categorieFragment = new CategorieFragment();
        subCategorieFragment = new SubCategorieFragment();

        categorie = findViewById(R.id.btn_categorie);
        subcategorie = findViewById(R.id.btn_subcategorie);

        setFragment(categorieFragment);

        categorie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categorie.setTextColor(getResources().getColor(R.color.colorWhite));
                categorie.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                subcategorie.setTextColor(getResources().getColor(R.color.colorPrimary));
                subcategorie.setBackgroundColor(getResources().getColor(R.color.colorWhite));

                setFragment(categorieFragment);

            }
        });

        subcategorie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subcategorie.setTextColor(getResources().getColor(R.color.colorWhite));
                subcategorie.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                categorie.setTextColor(getResources().getColor(R.color.colorPrimary));
                categorie.setBackgroundColor(getResources().getColor(R.color.colorWhite));

                setFragment(subCategorieFragment);
            }
        });


    }

    private void setFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.settingFrame, fragment);
        fragmentTransaction.commit();
    }
}
