package lk.nibm.furious5.scorpio.transit.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import lk.nibm.furious5.scorpio.transit.R;
import lk.nibm.furious5.scorpio.transit.databinding.ActivityBuyTicketBinding;

public class BuyTicketActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ActivityBuyTicketBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityBuyTicketBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String departureStationJson = intent.getStringExtra("departure_station");
        String arrivalStationsJson = intent.getStringExtra("arrival_stations");

        binding.textViewDepartureStation.setText(departureStationJson);

        // Parse JSON data and populate the dropdown list
        try {
            JSONObject departureStation = new JSONObject(departureStationJson);
            JSONArray arrivalStations = new JSONArray(arrivalStationsJson);

            // Handle departure station data
            String departureStationName = departureStation.getString("station_name");
            // TODO: Populate UI elements with departure station data if needed

            // Handle arrival stations data
            List<String> arrivalStationNames = new ArrayList<>();
            for (int i = 0; i < arrivalStations.length(); i++) {
                JSONObject station = arrivalStations.getJSONObject(i);
                String stationName = station.getString("station_name");
                arrivalStationNames.add(stationName);
            }

            // Populate the dropdown list with arrival station names
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, arrivalStationNames);
            Spinner spinner = binding.spinnerArrivalStations;
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(this);

        } catch (JSONException e) {
            e.printStackTrace();
            // Handle JSON parsing error
        }

    }

    private void showToast(String Message)
    {

        Toast.makeText(getApplicationContext(), Message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
        showToast(text);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}