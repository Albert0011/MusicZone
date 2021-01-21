//package com.glitchstacks.musiczone;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ListView;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;
//
//import org.json.JSONObject;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class RestAPI extends AppCompatActivity {
//
//    private Button button1, button2, button3;
//    private EditText editText;
//    private ListView listView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_rest_a_p_i);
//
//        // Hook
//        button1 = findViewById(R.id.button1);
//        button2 = findViewById(R.id.button2);
//        button3 = findViewById(R.id.button3);
//
//        editText = findViewById(R.id.editText);
//        listView = findViewById(R.id.listView);
//
//        button1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Instantiate the RequestQueue.
//                String url = "http://my-json-feed";
//
//                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
//                        (Request.Method.GET, url, null, new Response.Listener< JSONObject >() {
//
//                            @Override
//                            public void onResponse(JSONObject response) {
//                                getValue(response)
//                            }
//                        }, new Response.ErrorListener() {
//
//                            @Override
//                            public void onErrorResponse(VolleyError error) {

//
//                            }
//                        });
//
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    HashMap<String, String> headers = new HashMap<String, String>();
//                    headers.put("user-key", VariablesUsed.API_KEY);
//                    return headers;
//                }
//            }
//        });
//
//        button2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//
//        button3.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//
//
//    }
//
//    private void getValue(JSONObject response) {
//    }
//}