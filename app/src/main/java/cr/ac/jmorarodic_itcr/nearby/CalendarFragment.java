package cr.ac.jmorarodic_itcr.nearby;


import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends Fragment {


    private PreviousEventsFragment previousEventsFragment;
    private NextEventsFragment nextEventsFragment;
    private InterestEventFragment interestEventFragment;
    private Button buttonNext;
    private Button buttonPrevious;
    private  Button buttonInteres;


    public CalendarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View RootView =inflater.inflate(R.layout.fragment_calendar, container, false);

        buttonNext = RootView.findViewById(R.id.btn_next);
        buttonPrevious = RootView.findViewById(R.id.btn_previous);
        buttonInteres = RootView.findViewById(R.id.btn_interes);

        previousEventsFragment = new PreviousEventsFragment();
        nextEventsFragment = new NextEventsFragment();
        interestEventFragment = new InterestEventFragment();


        setFragment(nextEventsFragment);

        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonNext.setBackgroundColor(getResources().getColor(android.R.color.white));
                buttonNext.setTextColor(getResources().getColor(R.color.colorPrimaryLight));

                buttonInteres.setBackgroundColor(getResources().getColor(android.R.color.white));
                buttonInteres.setTextColor(getResources().getColor(R.color.colorPrimaryLight));

                buttonPrevious.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                buttonPrevious.setTextColor(getResources().getColor(R.color.colorWhite));

                setFragment(previousEventsFragment);
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonPrevious.setBackgroundColor(getResources().getColor(android.R.color.white));
                buttonPrevious.setTextColor(getResources().getColor(R.color.colorPrimaryLight));

                buttonInteres.setBackgroundColor(getResources().getColor(android.R.color.white));
                buttonInteres.setTextColor(getResources().getColor(R.color.colorPrimaryLight));

                buttonNext.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                buttonNext.setTextColor(getResources().getColor(R.color.colorWhite));

                setFragment(nextEventsFragment);
            }
        });

        buttonInteres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonNext.setBackgroundColor(getResources().getColor(android.R.color.white));
                buttonNext.setTextColor(getResources().getColor(R.color.colorPrimaryLight));

                buttonPrevious.setBackgroundColor(getResources().getColor(android.R.color.white));
                buttonPrevious.setTextColor(getResources().getColor(R.color.colorPrimaryLight));

                buttonInteres.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
                buttonInteres.setTextColor(getResources().getColor(R.color.colorWhite));

                setFragment(interestEventFragment);
            }
        });




        //tabLayout.setOn;



        return RootView;
    }

    private void setFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.calendarFrame, fragment);
        fragmentTransaction.commit();
    }

}
