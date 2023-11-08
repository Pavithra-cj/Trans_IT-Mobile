package lk.nibm.furious5.scorpio.transit.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import lk.nibm.furious5.scorpio.transit.Model.User;
import lk.nibm.furious5.scorpio.transit.R;
import lk.nibm.furious5.scorpio.transit.Utilities.Constants;
import lk.nibm.furious5.scorpio.transit.Utilities.PreferenceManager;
import lk.nibm.furious5.scorpio.transit.databinding.ActivityLoginBinding;

import com.android.volley.RequestQueue;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private static final String TAG = "VolleyExample";
    private RequestQueue requestQueue;
    private String email, password;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferenceManager preferenceManager = new PreferenceManager(getApplicationContext());

        if (preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        binding = ActivityLoginBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        setListners();
    }

    private void setListners() {

        binding.txtCreateNewAccount.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), SignupActivity.class)));
        binding.btnSignIn.setOnClickListener(v -> {
            if (isValidSignInDetails()) {
                email = binding.inputEmail.getText().toString();
                password = binding.inputPassword.getText().toString();
                login();
            }
        });

    }

    private void loading(Boolean isLoading) {

        if (isLoading) {
            binding.btnSignIn.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.btnSignIn.setVisibility(View.VISIBLE);
        }

    }

    private void showToast(String message) {

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

    }

    private Boolean isValidSignInDetails() {

        if (binding.inputEmail.getText().toString().trim().isEmpty()) {
            showToast("Enter Email");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches()) {
            showToast("Enter valid Email");
            return false;
        } else if (binding.inputPassword.getText().toString().trim().isEmpty()) {
            showToast("Enter Password");
            return false;
        } else {
            return true;
        }

    }

    private void login() {
        loading(true);
        // Initialize the RequestQueue
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        // API endpoint URL
        String apiUrl = "https://transit.alexlanka.com/api/v1/user/auth";

        JSONObject postData = new JSONObject();
        try {
            postData.put("email", email);
            postData.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Make a POST request using Volley
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, apiUrl, postData,
                response -> {
                    loading(false);
//                    binding.inputEmail.setText("");
//                    binding.inputPassword.setText("");
                    try {
                        String message = response.getString("token");
                        Log.d(TAG, "Response: " + response.toString());
                        if (message.equals(response.getString("token"))) {
                            // Handle success, navigate to the next activity
                            showToast("Login successful");
//                            token = response.getString("token");
//                            showToast(token);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("Token", response.getString("token"));
                            startActivity(intent);
                        } else {
                            showToast("Login failed: " + response.getString("errorMessage"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
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
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        // Add the request to the RequestQueue
        requestQueue.add(request);
    }

}