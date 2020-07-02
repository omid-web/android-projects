package com.example.pokedex;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PokemonActivity extends AppCompatActivity {
    private TextView nameTextView, numberTextView, type1TextView, type2TextView, descriptionTextView;
    private String url;
    private Boolean caught = false;
    private Button catchButton;
    private ImageView spriteImageView;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon);

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        url = getIntent().getStringExtra("url");
        nameTextView = findViewById(R.id.pokemon_name);
        numberTextView = findViewById(R.id.pokemon_number);
        type1TextView = findViewById(R.id.pokemon_type1);
        type2TextView = findViewById(R.id.pokemon_type2);
        descriptionTextView = findViewById(R.id.pokemon_description);
        catchButton = findViewById(R.id.pokemon_catch);
        spriteImageView = findViewById(R.id.pokemon_sprite);

        load();
    }

    public void load() {
        type1TextView.setText("");
        type2TextView.setText("");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    nameTextView.setText(response.getString("name"));
                    numberTextView.setText(String.format("#%03d", response.getInt("id")));

                    JSONArray typeEntries = response.getJSONArray("types");
                    for (int i = 0; i < typeEntries.length(); i++) {
                        JSONObject typeEntry = typeEntries.getJSONObject(i);
                        int slot = typeEntry.getInt("slot");
                        String type = typeEntry.getJSONObject("type").getString("name");

                        if (slot == 1) {
                            type1TextView.setText(type);
                        } else if (slot == 2) {
                            type2TextView.setText(type);
                        }
                    }

                    JSONObject spriteUrls = response.getJSONObject("sprites");
                    String front_default = spriteUrls.getString("front_default");
                    new DownloadSpriteTask().execute(front_default);

                    String url_description = response.getJSONObject("species").getString("url");
                    description(url_description);

                } catch (JSONException e) {
                    Log.e("omid", "pokemon json error", e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("omid", "pokemon details error");
            }
        });

        requestQueue.add(request);
    }

    public void toggleCatch(View v) {
        if (caught) {
            caught = false;
            catchButton.setText("Catch!");
        } else {
            caught = true;
            catchButton.setText("Release!");
        }
    }

    /*
    //TODO: save state
    getPreferences(Context.MODE_PRIVATE).edit().putString("course", "cs50").commit();
    String course = getPreferences(Context.MODE_PRIVATE).getString("course", "cs50");
    //course is equal to "cs50"
    */

    private class DownloadSpriteTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                return BitmapFactory.decodeStream(url.openStream());
            } catch (IOException e) {
                Log.e("omid", "Download sprite error", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            spriteImageView.setImageBitmap(bitmap);
        }
    }

    private void description(String url) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response_description) {
                try {
                    JSONArray entries = response_description.getJSONArray("flavor_text_entries");
                    for (int j = 0; j < entries.length(); j++) {
                        JSONObject entry = entries.getJSONObject(j);
                        String lang = entry.getJSONObject("language").getString("name");
                        if (lang.equals("en")) {
                            descriptionTextView.setText(entry.getString("flavor_text"));
                            break;
                        }
                    }
                } catch (JSONException e) {
                    Log.e("omid", "Description json error", e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("omid", "pokemon species error", error);
            }
        });

        requestQueue.add(request);
    }

}
