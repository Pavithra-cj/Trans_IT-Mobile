package lk.nibm.furious5.scorpio.transit.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import lk.nibm.furious5.scorpio.transit.Activities.AccDetailsActivity;
import lk.nibm.furious5.scorpio.transit.Activities.PackagesActivity;
import lk.nibm.furious5.scorpio.transit.R;
import lk.nibm.furious5.scorpio.transit.databinding.FragmentHomeBinding;
import lk.nibm.furious5.scorpio.transit.databinding.FragmentQrCodeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private static final String ARG_TOKEN = "token";

    private String token;

    public static HomeFragment newInstance(String token) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TOKEN, token);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
        {
            token = getArguments().getString(ARG_TOKEN);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.imgcredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), PackagesActivity.class);
                intent.putExtra(ARG_TOKEN, token);
                startActivity(intent);
            }
        });

        binding.imgProp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), AccDetailsActivity.class);
                intent.putExtra(ARG_TOKEN, token);
                startActivity(intent);
            }
        });

        return view;
    }

    private void showToast(String message) {

        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();

    }

}