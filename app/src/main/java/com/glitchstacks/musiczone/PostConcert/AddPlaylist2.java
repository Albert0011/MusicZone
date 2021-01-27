package com.glitchstacks.musiczone.PostConcert;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.glitchstacks.musiczone.R;
import com.glitchstacks.musiczone.requestMaker;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddPlaylist2 extends AppCompatActivity implements ArtistListener, TrackListener {

    private Button buttonNext, buttonAdd;
    private RecyclerView recyclerViewTrack, recyclerViewArtist;
    private DatabaseReference mDatabase;
    private TextView textViewSeePlaylist;
    private SearchView searchViewArtist, searchViewTrack;
    private Dialog dialog;
    private String concertName, concertKey, concertMainGenre, concertDescription, concertDuration, concertDate, concertTime;
    private Uri concertImageUrl;
    ArtistAdapter artistAdapter;
    TrackAdapter trackAdapter;

    private ArrayList<Track> tracklist = new ArrayList<>();
    private ArrayList<Artist> artistlist = new ArrayList<>();
    private ArrayList<Performance> performanceList = new ArrayList<>();

    final String authorization = "Bearer BQB5pTeJAvsL0Qrloc7u8cXEqgjaMgWfYqbP0sr7l6YImRjAzWk3-MJ5PUkerSWOSr9bEUcjZgVnN_D_bXg6nKSgg9FFKgsQuY-r71blLIDwgjUAWiuQDCdOD1E4i_qVq0XWvzcrXOVl2yNVnhNOOc5VYxYCcWq0GQ";


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

        // getExtra
        concertName = getIntent().getStringExtra("concertName");
        concertKey = getIntent().getStringExtra("concertKey");
        concertMainGenre = getIntent().getStringExtra("concertMainGenre");
        concertDescription = getIntent().getStringExtra("concertDescription");
        concertDuration = getIntent().getStringExtra("concertDuration");
        concertDate = getIntent().getStringExtra("concertDate");
        concertTime = getIntent().getStringExtra("concertTime");
        concertImageUrl = getIntent().getParcelableExtra("concertimageUri");

        Log.d("IniAddPlaylist", "something is missing" + concertName + concertKey + concertMainGenre + concertDescription + concertDuration + concertDate + concertTime + (concertImageUrl == null));

        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (searchViewArtist != null) {

            searchViewArtist.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if (newText.isEmpty()) return false;
                    SearchArtist(AddPlaylist2.this, newText);
                    return true;
                }
            });
        }

        if (searchViewTrack != null) {
            searchViewTrack.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if (newText.isEmpty()) return false;
                    SearchTrack(AddPlaylist2.this, newText);
                    return true;
                }
            });
        }

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCurrentPerformance();
            }
        });

        textViewSeePlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialog();
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callNextScreen();
            }
        });

    }

    private void ShowDialog() {

        ArrayList dialogTrackList = new ArrayList<>();

        for (Performance p : performanceList) {
            List<Track> currentTrack = p.getTrackList();
            for (Track t : currentTrack) {
                dialogTrackList.add(t.getName());
            }
        }

        // inisialisasi dialog
        dialog = new Dialog(AddPlaylist2.this);

        // set dialog milik customer
        dialog.setContentView(R.layout.dialog_searchable_spinner);
        dialog.getWindow().setLayout(1000, 1500);

        // transparant
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();

        EditText editText = dialog.findViewById(R.id.editText);
        ListView listView = dialog.findViewById(R.id.listView);

        // Initialize array adapter
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (AddPlaylist2.this, android.R.layout.simple_list_item_1, dialogTrackList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                // Initialize a TextView for ListView each Item
                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                // Set the text color of TextView (ListView Item)
                tv.setTextColor(Color.BLACK);

                // Generate ListView Item using TextView
                return view;
            }
        };
        listView.setAdapter(adapter);
    }

    private void AddCurrentPerformance() {
        if (artistAdapter == null && trackAdapter == null) {
            Toast.makeText(AddPlaylist2.this, "Search for artist and track first!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (artistAdapter == null) {
            Toast.makeText(AddPlaylist2.this, "Search artist first!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (trackAdapter == null) {
            Toast.makeText(AddPlaylist2.this, "Search track first!", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Artist> selectedArtist = artistAdapter.getSelectedArtist();
        Integer selectedArtistCount = selectedArtist.size();

        if (selectedArtistCount == 0) {
            Toast.makeText(AddPlaylist2.this, "Please select Artist first", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedArtistCount > 1) {
            Toast.makeText(AddPlaylist2.this, "Please match artist and track one by one!", Toast.LENGTH_SHORT).show();
            return;
        }

        String artistName, trackTitle;
        Artist currentArtist = selectedArtist.get(0);

        List<Track> selectedTrack = trackAdapter.getSelectedTrack();

        Integer selectedTrackSize = selectedTrack.size();

        if (selectedTrackSize == 0) {
            Toast.makeText(AddPlaylist2.this, "Please select Track first", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedTrackSize > 1) {
            Toast.makeText(AddPlaylist2.this, "Please match track and artist one by one!", Toast.LENGTH_SHORT).show();
            return;
        }

        Track currentTrack = selectedTrack.get(0);

        // kalau blm ada playlist, lsg masukkin saja
        if (performanceList.size() == 0) {

            List<Track> currentTrackList = new ArrayList<>();
            currentTrackList.add(currentTrack);
            performanceList.add(new Performance(currentArtist, currentTrackList));

        } else {
            // kalau sudah ada
            int i = -1;
            for (Performance p : performanceList) {
                if (p.getArtist().getId().equals(currentArtist.getId())) {
                    i = performanceList.indexOf(p);
                    break;
                }
            }

            if(i == -1){
                List<Track> currentTrackList = new ArrayList<>();
                currentTrackList.add(currentTrack);
                performanceList.add(new Performance(currentArtist,currentTrackList));
            }else{
                performanceList.get(i).getTrackList().add(currentTrack);
            }
        }

        Toast.makeText(AddPlaylist2.this, currentTrack.getName() + " - " + currentArtist.getName() + " added to playlist!", Toast.LENGTH_SHORT).show();
    }

    private void SearchTrack(Context context, String s) {

        String baseUrl2 = "https://api.spotify.com/v1/";
        String baseUrl = "https://api.spotify.com/v1/me";
        String query = s.replaceAll(" ", "%20");
        String qParam = "search?q=" + query + "&type=track";

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
        String qParam = "search?q=" + query + "&type=artist";

        String url = baseUrl2 + qParam;

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    getValueArtist(response);
                } catch (JSONException e) {
                    Log.d("masuk exception e", "true");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error instanceof NetworkError){
                    Log.d("masuk volley exception", "false");
                }
                Log.d("masuk volley exception", "true");
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

        Log.d("makeRequest", "true");
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
                String currentTrackId = items.getJSONObject(i).getString("id");

                Track currentTrack = new Track(currentTrackLink, currentTrackName, currentTrackId);
                tracklist.add(currentTrack);

            }

            trackAdapter = new TrackAdapter(tracklist, this);
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
                String currentArtistImage = null;
                    try {
                        currentArtistImage = items.getJSONObject(i).getJSONArray("images").getJSONObject(0).getString("url");
                    }catch(JSONException e){
                        Log.d("ErrorImage", "Image not exist");
                    }
                    String currentArtistID = items.getJSONObject(i).getString("id");
                    String currentArtistName = items.getJSONObject(i).getString("name");
                    Artist currentArtist = new Artist(currentArtistName, currentArtistImage, currentArtistLink, currentArtistID);
                    artistlist.add(currentArtist);

            }

            artistAdapter = new ArtistAdapter(artistlist, this);
            recyclerViewArtist.setHasFixedSize(true);
            recyclerViewArtist.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.HORIZONTAL));
            recyclerViewArtist.setAdapter(artistAdapter);

        } else {
            Log.d("Ray Christian", "Ray Christian");
        }

        Log.d("RESTAPI WORKED", String.valueOf(items));


    }

    @Override
    public void onArtistAction(Boolean isSelected) {
        if (isSelected) {
            Toast.makeText(AddPlaylist2.this, "Something Selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onTrackAction(Boolean isSelected) {
        if (isSelected) {
            //Toast.makeText(AddPlaylist2.this, "Something selected", Toast.LENGTH_SHORT).show();
        }
    }

    public void callNextScreen() {

        if(performanceList.isEmpty()){
            //Toast.makeText(AddPlaylist2.this, "Add playlist first!", Toast.LENGTH_SHORT);
            return;
        }

        Intent intent = new Intent(getApplicationContext(), AddArea.class);

        if(concertName.isEmpty() || concertKey.isEmpty() || concertMainGenre.isEmpty() || concertDate.isEmpty() || concertDuration.isEmpty() || concertImageUrl == null || concertTime.isEmpty() ){
            Log.d("AddArea", "something is missing" + concertName + concertKey + concertMainGenre + concertDescription + concertDuration + concertDate + concertTime + (concertImageUrl == null));
            return;
        }

        intent.putExtra("concertName", concertName);
        intent.putExtra("concertKey", concertKey);
        intent.putExtra("concertMainGenre", concertMainGenre);
        intent.putExtra("concertDescription", concertDescription);
        intent.putExtra("concertDuration", concertDuration);
        intent.putExtra("concertDate", concertDate);
        intent.putExtra("concertTime", concertTime);
        intent.putExtra("concertimageUri", concertImageUrl);
        intent.putExtra("playlist", performanceList);
        startActivity(intent);

    }
}