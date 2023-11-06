package lk.nibm.furious5.scorpio.transit.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import lk.nibm.furious5.scorpio.transit.Activities.BuyTicketActivity;
import lk.nibm.furious5.scorpio.transit.databinding.FragmentQrCodeBinding;

public class QrCodeFragment extends Fragment {

    private FragmentQrCodeBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                binding.txtTest.setText(intentResult.getContents());
                Intent intent = new Intent(getActivity(), BuyTicketActivity.class);
                intent.putExtra("QRCodeData", intentResult.getContents());
                startActivity(intent);
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


}