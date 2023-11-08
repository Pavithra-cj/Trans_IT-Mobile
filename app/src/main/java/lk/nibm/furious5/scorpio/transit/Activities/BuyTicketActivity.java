package lk.nibm.furious5.scorpio.transit.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import lk.nibm.furious5.scorpio.transit.R;

public class BuyTicketActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_ticket);

        String qrCodeData = getIntent().getStringExtra("QRCodeData");
        if (qrCodeData != null) {
            showToast("Qr code Data :" + qrCodeData);
        } else {
            showToast("No Data");
        }

    }

    private void showToast(String Message)
    {

        Toast.makeText(getApplicationContext(), Message, Toast.LENGTH_SHORT).show();

    }

}