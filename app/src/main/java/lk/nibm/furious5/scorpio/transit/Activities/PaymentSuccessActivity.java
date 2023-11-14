package lk.nibm.furious5.scorpio.transit.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import lk.nibm.furious5.scorpio.transit.R;
import lk.nibm.furious5.scorpio.transit.databinding.ActivityDetailsPackageBinding;
import lk.nibm.furious5.scorpio.transit.databinding.ActivityPaymentSuccessBinding;

public class PaymentSuccessActivity extends AppCompatActivity {

    private ActivityPaymentSuccessBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentSuccessBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentSuccessActivity.this, PackagesActivity.class);
                startActivity(intent);
            }
        });

    }
}