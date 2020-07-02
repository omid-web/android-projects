package com.example.pokedex;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PokedexAdapter extends RecyclerView.Adapter<PokedexAdapter.PokedexViewHolder> implements Filterable {
    @Override
    public Filter getFilter() {
        return new PokemonFilter();
    }

    private class PokemonFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            final List<Pokemon> filteredPokemon = new ArrayList<>();

            if (charSequence != null) {
                if (pokemonList != null && pokemonList.size() > 0) {
                    String[] searchSubstringArray = charSequence.toString().toLowerCase().trim().split("[\\s,;]+");

                    for (final Pokemon pokemon1 : pokemonList) {
                        boolean matchesSearch = true;

                        for (String searchSubString : searchSubstringArray) {
                            matchesSearch = pokemon1.getName().toLowerCase().contains(searchSubString);
                        }

                        if (matchesSearch) {
                            filteredPokemon.add(pokemon1);
                        }
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredPokemon;
            results.count = filteredPokemon.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            pokemonList = (List<Pokemon>) filterResults.values;
            notifyDataSetChanged();
        }
    }

    public static class PokedexViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout containerView;
        public TextView textView;

        public PokedexViewHolder(@NonNull View itemView) {
            super(itemView);

            containerView = itemView.findViewById(R.id.pokedex_row);
            textView = itemView.findViewById(R.id.pokedex_row_text_view);

            containerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Pokemon current = (Pokemon) containerView.getTag();
                    Intent intent = new Intent(view.getContext(), PokemonActivity.class);
                    intent.putExtra("url", current.getUrl());

                    view.getContext().startActivity(intent);
                }
            });
        }
    }

    private List<Pokemon> pokemonList = new ArrayList<>();
    private RequestQueue requestQueue;

    PokedexAdapter(Context applicationContext) {
        requestQueue = Volley.newRequestQueue(applicationContext);
        loadPokemon();
    }

    private void loadPokemon() {
        String url = "https://pokeapi.co/api/v2/pokemon?limit=151";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray results = response.getJSONArray("results");
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject result = results.getJSONObject(i);
                        String name = result.getString("name");
                        pokemonList.add(new Pokemon(
                                name.substring(0, 1).toUpperCase() + name.substring(1),
                                result.getString("url")
                        ));
                    }

                    notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.e("omid", "JSON error", e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("omid", "pokemon list error", error);
            }
        });

        requestQueue.add(request);
    }

    @NonNull
    @Override
    public PokedexViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pokedex_row, parent, false);

        return new PokedexViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PokedexViewHolder holder, int position) {
        Pokemon current = pokemonList.get(position);
        holder.textView.setText(current.getName());
        holder.containerView.setTag(current);
    }

    @Override
    public int getItemCount() {
        return pokemonList.size();
    }
}
