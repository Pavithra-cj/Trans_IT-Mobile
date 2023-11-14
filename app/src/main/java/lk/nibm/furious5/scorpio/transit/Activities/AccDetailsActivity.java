package lk.nibm.furious5.scorpio.transit.Activities;

import static lk.nibm.furious5.scorpio.transit.Activities.PackagesActivity.EXTRA_TOKEN;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import lk.nibm.furious5.scorpio.transit.Adapters.ItemAdapter;
import lk.nibm.furious5.scorpio.transit.Model.PackageItem;
import lk.nibm.furious5.scorpio.transit.R;
import lk.nibm.furious5.scorpio.transit.databinding.ActivityAccDetailsBinding;
import lk.nibm.furious5.scorpio.transit.databinding.ActivityPaymentSuccessBinding;

public class AccDetailsActivity extends AppCompatActivity {

    private ActivityAccDetailsBinding binding;

    private static final String ARG_TOKEN = "token";
    private RequestQueue requestQueue;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAccDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(ARG_TOKEN)) {
            String token = intent.getStringExtra(ARG_TOKEN);
            this.token = token;
        } else {
            showToast("Token not found");
        }

        requestApi();

    }

    private void showToast(String message) {

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

    }

    private void requestApi() {
        // Initialize the RequestQueue
        requestQueue = Volley.newRequestQueue(this);

        // API endpoint URL
        String apiUrl = "https://transit.alexlanka.com/api/v1/my/account";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, apiUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String message = response.getString("message");
                            String errorMsg = response.getString("errorMessage");

                            if (message.equals("Fetch data successfully!"))
                            {
                                showToast(message);

                                JSONObject jsonObject = response.getJSONObject("data");

                                binding.txtUsername.setText(jsonObject.getString("name"));
                                binding.txtEmail.setText(jsonObject.getString("email"));
                                binding.txtNumber.setText(jsonObject.getString("mobile"));
                                binding.joinDate.setText(jsonObject.getString("join_date"));
                                binding.txtCPoints.setText(jsonObject.getString("credit_points"));

                            }else {
                                showToast(errorMsg);
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