package com.tahasanli.secretgallery.views;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;
import com.tahasanli.secretgallery.LoginFragment;
import com.tahasanli.secretgallery.Photo;
import com.tahasanli.secretgallery.R;
import com.tahasanli.secretgallery.databinding.AddscreenBinding;
import com.tahasanli.secretgallery.models.AddScreenViewModel;


public class AddScreen extends Fragment {
    private AddscreenBinding binding;
    private AddScreenViewModel model;

    public static final String IDKey = "INTENTID";

    private Photo currentPhoto;

    private ActivityResultLauncher<Intent> resultIntent;
    private ActivityResultLauncher<String> resultString;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = AddscreenBinding.inflate(inflater, container, false);

        model = new ViewModelProvider(requireActivity()).get(AddScreenViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.button2.setOnClickListener(this::addPhoto);
        binding.AddScreenImageAddView.setOnClickListener(this::selectPhoto);

        int id = getArguments() == null ? -1: AddScreenArgs.fromBundle(getArguments()).getIndex();
        if( id > -1){
            binding.AddScreenName.setText(MainScreen.instance.photos[id].name);
            binding.AddScreenName.setEnabled(false);
            binding.AddScreenDate.setText(MainScreen.instance.photos[id].date);
            binding.AddScreenDate.setEnabled(false);
            binding.AddScreenSelectText.setVisibility(View.INVISIBLE);
            binding.AddScreenImage.setImageBitmap(MainScreen.instance.photos[id].photo);
            binding.AddScreenImageAddView.setOnClickListener(null);
            binding.button2.setVisibility(View.INVISIBLE);
        }

        resultIntent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == Activity.RESULT_OK){
                    Intent intentFromResult = result.getData();
                    if(intentFromResult != null) {
                        Uri data = intentFromResult.getData();
                        try{
                            if(Build.VERSION.SDK_INT >= 28) {
                                ImageDecoder.Source source = ImageDecoder.createSource(getActivity().getContentResolver(), data);

                                currentPhoto.photo = ImageDecoder.decodeBitmap(source);
                            }
                            else{
                                currentPhoto.photo = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data);
                            }
                            changePhoto();
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                }
            }
        });

        resultString = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if(result){
                    Intent galeryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    resultIntent.launch(galeryIntent);
                }
                else{
                    Toast.makeText(getActivity(), R.string.AddScreenPermissionNeed, Toast.LENGTH_LONG).show();
                }
            }
        });

        currentPhoto = new Photo();
    }

    public void changePhoto(){
        binding.AddScreenSelectText.setEnabled(false);
        binding.AddScreenImage.bringToFront();
        binding.AddScreenImage.setImageBitmap(this.currentPhoto.photo);
    }

    public void selectPhoto(View view) {
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)){
                Snackbar.make(view, R.string.AddScreenPermissionNeed,Snackbar.LENGTH_INDEFINITE).setAction(R.string.AddScreenGrantPermission, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resultString.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                }).show();
            }
            else{
                resultString.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            return;
        }
        else{
            Intent galeryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            resultIntent.launch(galeryIntent);
        }
    }



    public void addPhoto(View view) {
        currentPhoto.name = binding.AddScreenName.getText().toString();
        currentPhoto.date = binding.AddScreenDate.getText().toString();

        currentPhoto.InsertPhoto();

        NavDirections direc = AddScreenDirections.actionAddScreenToMainScreen();
        Navigation.findNavController(view).navigate(direc);
    }
}
