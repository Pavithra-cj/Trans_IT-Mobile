package lk.nibm.furious5.scorpio.transit.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import java.util.HashMap;
import java.util.Map;

import lk.nibm.furious5.scorpio.transit.Adapters.ItemAdapter;
import lk.nibm.furious5.scorpio.transit.Model.PackageItem;
import lk.nibm.furious5.scorpio.transit.R;

public class PackagesActivity extends AppCompatActivity {
    private static final String ARG_TOKEN = "token";
    private RecyclerView itemRecyclerView;
    private ItemAdapter itemAdapter;
    private ArrayList<PackageItem> packageList;
    private RequestQueue requestQueue;

    private String token;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packages);

        itemRecyclerView = findViewById(R.id.recyclerView);
        itemRecyclerView.setHasFixedSize(true);
        itemRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        packageList = new ArrayList<>();

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(ARG_TOKEN)) {
            String token = intent.getStringExtra(ARG_TOKEN);
            this.token = token;
            showToast("Token: " + token);
        } else {
            showToast("Token not found");
        }

        requestQueue = Volley.newRequestQueue(this);

        parseJSON();

    }

    private void showToast(String message) {

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

    }

    private void parseJSON()
    {
        String url = "https://transit.alexlanka.com/api/v1/packages";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");

                            for (int i = 0; i < jsonArray.length(); i++)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String pkgName = jsonObject.getString("name");
                                String credits = jsonObject.getString("credit_points");
                                String price = jsonObject.getString("price");

                                packageList.add(new PackageItem(pkgName, credits, price));

                                itemAdapter = new ItemAdapter(PackagesActivity.this, packageList);
                                itemRecyclerView.setAdapter(itemAdapter);

                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            // Override the getHeaders method to add the token to the request headers
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        requestQueue.add(request);
    }

}