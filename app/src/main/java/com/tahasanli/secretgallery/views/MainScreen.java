package com.tahasanli.secretgallery.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import com.tahasanli.secretgallery.MainScreenAdapter;
import com.tahasanli.secretgallery.Photo;
import com.tahasanli.secretgallery.databinding.MainscreenBinding;

public class MainScreen extends Fragment {
    private MainscreenBinding binding;

    public static MainScreen instance;
    public Photo[] photos;

    private MainScreenAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        photos = new Photo[0];
        instance = this;

        binding = MainscreenBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.MainScreenButton.setOnClickListener(this::addNewPhoto);

        binding.MainScreenRecycler.setLayoutManager(new GridLayoutManager(getContext(),2));
        binding.MainScreenRecycler.setAdapter(adapter = new MainScreenAdapter());
    }

    @Override
    public void onResume() {
        super.onResume();

        photos = Photo.GetAllPhotos().toArray(new Photo[0]);
        adapter.notifyDataSetChanged();
    }

    public void addNewPhoto(View view) {
        NavDirections direc = MainScreenDirections.actionMainScreenToAddScreen();
        Navigation.findNavController(view).navigate(direc);

    }
}
