package lk.nibm.furious5.scorpio.transit.Activities;

import static lk.nibm.furious5.scorpio.transit.Activities.PackagesActivity.EXTRA_CREDIT;
import static lk.nibm.furious5.scorpio.transit.Activities.PackagesActivity.EXTRA_ID;
import static lk.nibm.furious5.scorpio.transit.Activities.PackagesActivity.EXTRA_PACKAGE;
import static lk.nibm.furious5.scorpio.transit.Activities.PackagesActivity.EXTRA_PRICE;
import static lk.nibm.furious5.scorpio.transit.Activities.PackagesActivity.EXTRA_TOKEN;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import lk.nibm.furious5.scorpio.transit.R;
import lk.nibm.furious5.scorpio.transit.databinding.ActivityDetailsPackageBinding;
import lk.nibm.furious5.scorpio.transit.databinding.ActivityPackagesBinding;

public class DetailsPackageActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ActivityDetailsPackageBinding binding;
    private String paymentMethod;

    private String pkgName;
    private String credits;
    private String price;
    private String token;
    private String id;

    private static final String TAG = "VolleyExample";
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsPackageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();

        pkgName = intent.getStringExtra(EXTRA_PACKAGE);
        credits = intent.getStringExtra(EXTRA_CREDIT);
        price = intent.getStringExtra(EXTRA_PRICE);
        token = intent.getStringExtra(EXTRA_TOKEN);
        id = intent.getStringExtra(EXTRA_ID);

        binding.txtpkgName.setText(pkgName);
        binding.txtCredits.setText(credits);
        binding.txtPrice.setText(price);

        showToast("TOKEN :" +token);
        showToast("ID :" +id);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.payment_methods, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.paymentMethod.setAdapter(adapter);
        binding.paymentMethod.setOnItemSelectedListener(this);

        binding.btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestApi();
            }
        });

    }

    private void showToast(String message) {

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        paymentMethod = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void requestApi() {
        // Initialize the RequestQueue
        requestQueue = Volley.newRequestQueue(this);

        // API endpoint URL
        String apiUrl = "https://transit.alexlanka.com/api/v1/packages/buy";

        JSONObject postData = new JSONObject();
        try {
            postData.put("package_id", id);
            postData.put("payment_type", paymentMethod);
        } catch (JSONException e) {
            e.printStackTrace();
            showToast("Error creating JSON data");
            return;
        }

        // Make a POST request using Volley
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, apiUrl, postData,
                response -> {
                    try {
                        String message = response.getString("message");
                        String errorMsg = response.getString("errorMessage");
                        Log.d(TAG, "Response: " + response.toString());

                        if (message.equals("Payment successful!"))
                        {
                            Intent intent = new Intent(this, PaymentSuccessActivity.class);
                            startActivity(intent);
                        }else {
                            showToast(errorMsg);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        showToast("Error parsing API response");
                    }
                },
                error -> {
                    // Handle API error responses here
                    if (error instanceof NetworkError) {
                        Log.e(TAG, "Network Error");
                    } else if (error instanceof ServerError) {
                        Log.e(TAG, "Server Error");
                    } else if (error instanceof AuthFailureError) {
                        Log.e(TAG, "Authentication Failure Error");
                    } else if (error instanceof ParseError) {
                        Log.e(TAG, "Parse Error");
                    } else if (error instanceof NoConnectionError) {
                        Log.e(TAG, "No Connection Error");
                    } else if (error instanceof TimeoutError) {
                        Log.e(TAG, "Timeout Error");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // Set the Bearer token in the headers
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        // Add the request to the RequestQueue
        requestQueue.add(request);
    }

}