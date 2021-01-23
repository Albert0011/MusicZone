package com.glitchstacks.musiczone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddPlaylist2 extends AppCompatActivity {

    private Button buttonNext, buttonAdd;
    private RecyclerView recyclerViewTrack, recyclerViewArtist;
    private DatabaseReference mDatabase;
    private TextView textViewSeePlaylist;
    private SearchView searchViewArtist, searchViewTrack;

    private ArrayList<Track> tracklist = new ArrayList<>();
    private ArrayList<Artist> artistlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_playlist_2);

        // Hook
        buttonNext = findViewById(R.id.buttonNext);
        buttonAdd = findViewById(R.id.buttonAdd);
        textViewSeePlaylist = findViewById(R.id.textViewPlaylist);
        recyclerViewArtist = findViewById(R.id.recylcerViewArtist);
        recyclerViewTrack = findViewById(R.id.recyclerViewTrack);
        searchViewArtist = findViewById(R.id.searchViewArtist);
        searchViewTrack = findViewById(R.id.searchViewTrack);

        // Database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if(searchViewArtist!=null){

            searchViewArtist.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    SearchArtist(AddPlaylist2.this, newText);
                    return true;
                }
            });
        }

        if(searchViewTrack != null){
            searchViewTrack.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    SearchTrack(AddPlaylist2.this, newText);
                    return true;
                }
            });
        }

    }


    private void SearchTrack(Context context, String s) {

        String baseUrl2 = "https://api.spotify.com/v1/";
        String baseUrl = "https://api.spotify.com/v1/me";
        String query = s.replaceAll(" ", "%20");
        String qParam = "search?q="+query+"&type=track";

        final String authorization = "Bearer BQAWtvXL-nmH7ANOWQXYFPw7bkOxFxUpqj39kNdTmDyMIrGT4bcoo36sOZmqllPNxevrjDN1SEdtgtmXnLc6MOp9GUJfsY5tT19aQbBMV0kJ7PerSiBz8twOBlOZH8_XG8txW01wX7INczKK_AuMcf3zj0sGjl6grQ";

        String url = baseUrl2 + qParam;

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    getValueTrack(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", authorization);
                return headers;
            }
        };

        // Access the RequestQueue through your requestMaker
        requestMaker.getInstance(context).addToRequestQueue(req);
    }


    private void SearchArtist(Context context, String s) {

        String query = s.replaceAll(" ", "%20");

        String baseUrl2 = "https://api.spotify.com/v1/";
        String baseUrl = "https://api.spotify.com/v1/me";

        //String qParam = "search?q=Raissa&type=artist";
        String qParam = "search?q="+query+"&type=artist";


        final String authorization = "Bearer BQAWtvXL-nmH7ANOWQXYFPw7bkOxFxUpqj39kNdTmDyMIrGT4bcoo36sOZmqllPNxevrjDN1SEdtgtmXnLc6MOp9GUJfsY5tT19aQbBMV0kJ7PerSiBz8twOBlOZH8_XG8txW01wX7INczKK_AuMcf3zj0sGjl6grQ";

        String url = baseUrl2 + qParam;

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    getValueArtist(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", authorization);
                return headers;
            }
        };

        // Access the RequestQueue through your requestMaker
        requestMaker.getInstance(context).addToRequestQueue(req);
    }

    public void getValueTrack(JSONObject object) throws JSONException {
        JSONObject track = object;

        JSONArray items = null;

        tracklist.clear();

        if (track != null) {
            Log.d("RESTAPI WORKED", "RestAPIWorked");

            JSONObject tracks = track.getJSONObject("tracks");
            items = tracks.getJSONArray("items");

            Integer itemlength = items.length();

            for (int i = 0; i < itemlength; i++) {
                String currentTrackLink = items.getJSONObject(i).getJSONObject("external_urls").getString("spotify");
                Log.d("linktrack", currentTrackLink);

                String currentTrackName = items.getJSONObject(i).getString("name");

                Track currentTrack = new Track(currentTrackLink,currentTrackName);
                tracklist.add(currentTrack);

            }

            TrackAdapter trackAdapter = new TrackAdapter(tracklist);
            recyclerViewTrack.setHasFixedSize(true);
            recyclerViewTrack.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.HORIZONTAL));
            recyclerViewTrack.setAdapter(trackAdapter);

        } else {
            Log.d("Ray Christian", "Ray Christian");
        }

        Log.d("RESTAPI WORKED", String.valueOf(items));



    }


    public void getValueArtist(JSONObject object) throws JSONException {
        JSONObject artist = object;

        JSONArray items = null;

        artistlist.clear();

        if (artist != null) {
            Log.d("RESTAPI WORKED", "RestAPIWorked");

            JSONObject artists = artist.getJSONObject("artists");
            items = artists.getJSONArray("items");

            Integer itemlength = items.length();

            for (int i = 0; i < itemlength; i++) {
                String currentArtistLink = items.getJSONObject(i).getJSONObject("external_urls").getString("spotify");
                String currentArtistImage = items.getJSONObject(i).getJSONArray("images").getJSONObject(0).getString("url");
                String currentArtistName = items.getJSONObject(i).getString("name");

                Artist currentArtist = new Artist(currentArtistName,currentArtistImage,currentArtistLink);
                artistlist.add(currentArtist);

            }

            ArtistAdapter artistAdapter = new ArtistAdapter(artistlist);
            recyclerViewArtist.setHasFixedSize(true);
            recyclerViewArtist.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.HORIZONTAL));
            recyclerViewArtist.setAdapter(artistAdapter);

        } else {
            Log.d("Ray Christian", "Ray Christian");
        }

        Log.d("RESTAPI WORKED", String.valueOf(items));


    }
}