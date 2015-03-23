package com.laioffer.intern_project.internproject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class RestaurantListActivity extends Activity {

    public final static String RESTAURANT_LOC = "com.laioffer.internproject.RESTAURANT_LOC";

    private RestaurantListFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_restaurant_list);
        fragment = (RestaurantListFragment) getFragmentManager().findFragmentByTag("fragtag");
    }

    /**
     * Use this method to instantiate the action bar, and add your items to it.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_restaurant_list, menu);

        // It should return true if you have added items to it and want the menu
        // to be displayed.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            // Implement the action here!
            return true;
        } else if (id == R.id.action_location) {
            switchToMapView(restaurant_loc);
        } else if (id == R.id.action_recommend) {
            recommendRestaurants();
        }

        return super.onOptionsItemSelected(item);
    }

    private void recommendRestaurants() {
        // It is asynchronously updating the result so as not to block UI thread
        RecommendRestaurantsAsyncTask t = new RecommendRestaurantsAsyncTask();
        t.execute("RecommendRestaurants");
    }

    // Create the intent and switch to map view activity.
    private void switchToMapView(ArrayList<LatLng> restaurant_loc) {
        Intent intent = new Intent(this, RestaurantMapActivity.class);

        // Fake some loc data here.
        for (int i = 0; i < 5; ++i) {
            restaurant_loc.add(i, new LatLng(i, i));
        }

        intent.putParcelableArrayListExtra(RESTAURANT_LOC, restaurant_loc);
        startActivity(intent);
    }

    // Store the locations of all restaurants.
    private ArrayList<LatLng> restaurant_loc = new ArrayList<>();

    private class RecommendRestaurantsAsyncTask extends AsyncTask<String, Void, List<Restaurant>> {

        @Override
        protected List<Restaurant> doInBackground(String... params) {
            return RestaurantApiClient.post(params);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(List<Restaurant> restaurants) {
            super.onPostExecute(restaurants);
            fragment.getRestaurantListAdapter().updateRestaurants(restaurants);
            fragment.getRestaurantListAdapter().notifyDataSetChanged();
        }
    }
}
