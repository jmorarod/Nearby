package cr.ac.jmorarodic_itcr.nearby;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 */
public class PreviousEventsFragment extends Fragment {

    ArrayList<Bitmap> mbitmaps = new ArrayList<>();
    ArrayList<EventCalendarItem> eventItems = new ArrayList<>();
    JSONObject jsonResponse;
    JSONObject jsonRequestBody = new JSONObject();
    StringRequest jsonRequest;
    StringRequest jsonRequest2;
    String api_key;
    RecyclerView recyclerView;
    ListView listView;
    EventCalendarAdapter adapterC;
    Activity activity;





    public PreviousEventsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View RootView = inflater.inflate(R.layout.fragment_previous_events, container, false);

        ArrayList<EventCalendarItem> eventos = new ArrayList<>();

        //Carga en listview
        listView = (ListView) RootView.findViewById(R.id.previousEventList);

        //final ListView listView = (ListView) RootView.findViewById(R.id.previousEventList);
        //EventCalendarAdapter eventMainAdapter = new EventCalendarAdapter(getActivity().getApplicationContext(),R.layout.list_item_event_calendar,eventos);
        //listView.setAdapter(eventMainAdapter);


        return RootView;
    }






}
