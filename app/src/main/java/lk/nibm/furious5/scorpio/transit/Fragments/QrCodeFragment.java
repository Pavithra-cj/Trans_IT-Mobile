package lk.nibm.furious5.scorpio.transit.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import lk.nibm.furious5.scorpio.transit.Activities.BuyTicketActivity;
import lk.nibm.furious5.scorpio.transit.Activities.LoginActivity;
import lk.nibm.furious5.scorpio.transit.Activities.MainActivity;
import lk.nibm.furious5.scorpio.transit.databinding.FragmentQrCodeBinding;

public class QrCodeFragment extends Fragment {

    private FragmentQrCodeBinding binding;

    private static final String ARG_LATITUDE = "latitude";
    private static final String ARG_LONGITUDE = "longitude";
    private static final String ARG_TOKEN = "token";

    private double latitude;
    private double longitude;
    private String token;
    private String qrData;

    private static final String TAG = "VolleyExample";
    private RequestQueue requestQueue;

    public static QrCodeFragment newInstance(double latitude, double longitude, String token) {
        QrCodeFragment fragment = new QrCodeFragment();
        Bundle args = new Bundle();
        args.putDouble(ARG_LATITUDE, latitude);
        args.putDouble(ARG_LONGITUDE, longitude);
        args.putString(ARG_TOKEN, token);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            latitude = getArguments().getDouble(ARG_LATITUDE);
            longitude = getArguments().getDouble(ARG_LONGITUDE);
            token = getArguments().getString(ARG_TOKEN);
        }

    }

    private void showToast(String message) {

        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentQrCodeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.btnQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initialize the IntentIntegrator and start the QR code scanning process
                IntentIntegrator intentIntegrator = IntentIntegrator.forSupportFragment(QrCodeFragment.this);
                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.setPrompt("Scan the Station QR code.");
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                intentIntegrator.initiateScan(); // Start QR code scanning
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null)
        {
            String contents = intentResult.getContents();
            if (contents != null)
            {
                qrData = intentResult.getContents();
                requestApi();
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void requestApi() {
        // Initialize the RequestQueue
        requestQueue = Volley.newRequestQueue(requireContext());

        // API endpoint URL
        String apiUrl = "https://transit.alexlanka.com/api/v1/tickets/book/qr-scan";

        JSONObject postData = new JSONObject();
        try {
            postData.put("qr_id", qrData);
            postData.put("longitude", String.valueOf(longitude));
            postData.put("latitude", String.valueOf(latitude));
            showToast("JSON data: " +postData.toString());
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
                        showToast(message);
                        showToast(response.toString());
                        Log.d(TAG, "Response: " + response.toString());

                        if(message.equals("Qr Code Scan Successfully!"))
                        {
                            Intent intent = new Intent(getActivity(), BuyTicketActivity.class);
                            intent.putExtra("departure_station", response.getJSONObject("data").getJSONObject("departure_station").getString("station_name"));

                            // Extract arrival stations names
                            JSONArray arrivalStationsArray = response.getJSONObject("data").getJSONArray("arrival_stations");
                            String[] arrivalStationsNames = new String[arrivalStationsArray.length()];
                            for (int i = 0; i < arrivalStationsArray.length(); i++) {
                                arrivalStationsNames[i] = arrivalStationsArray.getJSONObject(i).getString("station_name");
                            }

                            intent.putExtra("arrival_stations", arrivalStationsArray.toString());
                            startActivity(intent);
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