package com.example.aolanrewaju.didyoufeelit;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class DidYouFeelItMainActivity extends AppCompatActivity {

    private String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2016-01-01&endtime=2016-05-02&minfelt=50&minmagnitude=5";
    private String LOG_TAG = DidYouFeelItMainActivity.class.getName();
    private TextView mLocationOffset;
    private TextView mCdi;
    private TextView mPeopleThatFeltIt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_did_you_feel_it_main);

        //create and execute an instance of {@link EarthQuakeAsyncTask
        new EarthQuakeAsyncTask().execute(USGS_REQUEST_URL);
    }

    private void UpdateUI(final Event event){
        //reference "Location Offset" TextView
        mLocationOffset = findViewById(R.id.location_offset);
        String offsetTracker = String.format("M %.1f - %s", event.getmMagnitude(),event.getmPlace());
        mLocationOffset.setText(offsetTracker);

        //reference the "CDI" TextView
        mCdi = findViewById(R.id.cdi);
        String cdiContent = String.format("%.1f",event.getmCdi());
        mCdi.setText(cdiContent);

        //attach a listener to the CDI TextView
        mCdi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send an Intent to a browser
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(event.getmUrl()));

                //resolve an activity
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(DidYouFeelItMainActivity.this,"No Intent available to handle action",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //reference the "people that felt it" detail TextView
        mPeopleThatFeltIt = findViewById(R.id.feelItCount);
        String feltItContent = String.format("%d people felt it",event.getmFelt());
        mPeopleThatFeltIt.setText(feltItContent);
    }

    private class EarthQuakeAsyncTask extends AsyncTask<String, Void, Event>{

        @Override
        protected Event doInBackground(String... urls) {

            //don't perform any request operation if the urls is empty or the first element is null
            if(urls.length < 1 | urls[0] == null)
                return null;

            //create an instance of an Earthquake @link Event
            Event event = Utils.fetchData(urls[0]);
            return event;
        }

        @Override
        protected void onPostExecute(Event event){
            UpdateUI(event);
        }
    }
}
