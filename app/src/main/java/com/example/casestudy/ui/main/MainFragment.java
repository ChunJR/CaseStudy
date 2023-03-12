package com.example.casestudy.ui.main;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.casestudy.R;
import com.example.casestudy.databinding.FragmentMainBinding;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;
    private FragmentMainBinding binding;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        observeDataChange();
        initView();
    }

    private void observeDataChange() {
        mViewModel.getState().observe(getViewLifecycleOwner(), mainViewState -> {
            if (mainViewState instanceof MainViewState.onLoading) {
                binding.progressBar.setVisibility(View.VISIBLE);
            } else if (mainViewState instanceof MainViewState.onSuccess) {
                binding.progressBar.setVisibility(View.GONE);
                String jsonString = ((MainViewState.onSuccess) mainViewState).getJsonString();
                binding.tvResult.setText(jsonString);
            } else if (mainViewState instanceof MainViewState.onError) {
                binding.progressBar.setVisibility(View.GONE);
                String message = ((MainViewState.onError) mainViewState).getMessage();
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        binding.btnSubmit.setOnClickListener(v -> {
            binding.tvResult.setText(""); // Reset
            mViewModel.onSubmitClick(binding.etComment.getText().toString());
        });
    }
}