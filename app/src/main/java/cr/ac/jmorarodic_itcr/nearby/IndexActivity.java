package cr.ac.jmorarodic_itcr.nearby;

import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

public class IndexActivity extends AppCompatActivity {

    private BottomNavigationView mainNav;
    private FrameLayout mainFrame;

    private HomeFragment homeFragment;
    private GroupFragment groupFragment;
    private CalendarFragment calendarFragment;
    private NotificationFragment notificationFragment;
    private ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        mainNav = (BottomNavigationView) findViewById(R.id.main_menu);
        mainFrame = (FrameLayout) findViewById(R.id.mainFrame);

        homeFragment = new HomeFragment();
        groupFragment = new GroupFragment();
        calendarFragment = new CalendarFragment();
        notificationFragment = new NotificationFragment();
        profileFragment = new ProfileFragment();

        setFragment(homeFragment);

        mainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.tab_home:
                        Toast.makeText(IndexActivity.this,"HOME",Toast.LENGTH_LONG).show();
                        setFragment(homeFragment);
                        break;
                    case R.id.tab_group:
                        Toast.makeText(IndexActivity.this,"GROUP",Toast.LENGTH_LONG).show();
                        setFragment(groupFragment);
                        break;
                    case R.id.tab_calendar:
                        Toast.makeText(IndexActivity.this,"CALENDAR",Toast.LENGTH_LONG).show();
                        setFragment(calendarFragment);
                        break;
                    case R.id.tab_notification:
                        Toast.makeText(IndexActivity.this,"NOTIFICATION",Toast.LENGTH_LONG).show();
                        setFragment(notificationFragment);
                        break;
                    case R.id.tab_profile:
                        Toast.makeText(IndexActivity.this,"PROFILE",Toast.LENGTH_LONG).show();
                        setFragment(profileFragment);
                        break;

                }
                return true;
            }
        });
    }

   private void setFragment(Fragment fragment)
   {
       FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
       fragmentTransaction.replace(R.id.mainFrame, fragment);
       fragmentTransaction.commit();
   }
}
