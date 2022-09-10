package com.tahasanli.secretgallery;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.tahasanli.secretgallery.databinding.ActivityMainBinding;
import com.tahasanli.secretgallery.databinding.LoginfragmentBinding;
import com.tahasanli.secretgallery.views.CreatePassword;
import com.tahasanli.secretgallery.views.MainScreen;

public class LoginFragment extends Fragment {
    public static SharedPreferences prefs;
    public static final String PasswordPrefKey = "PASSWORDSHAREDPREFERENCESKEY";
    public static LoginFragment instance;

    private NavController navCont;

    public LoginfragmentBinding binding;

    public SQLiteDatabase db;
    public static final String DBTableName = "Photos";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = LoginfragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.button.setOnClickListener(this::getIn);
        binding.MainActivityCreatePassword.setOnClickListener(this::createPassword);

        navCont = Navigation.findNavController(binding.getRoot());

        instance = this;

        db = getActivity().openOrCreateDatabase(getString(R.string.app_name), Activity.MODE_PRIVATE, null);

        prefs = getActivity().getSharedPreferences(getString(R.string.app_name), Activity.MODE_PRIVATE);
        if(prefs.getString(PasswordPrefKey,"").matches("")) {
            NavDirections direc = LoginFragmentDirections.actionLoginFragmentToCreatePassword();
            navCont.navigate(direc);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        binding.editTextTextPassword.setText("");
    }

    public void createPassword(View view) {
        NavDirections direc = LoginFragmentDirections.actionLoginFragmentToCreatePassword();
        navCont.navigate(direc);
    }

    public void getIn(View view) {
        if(prefs.getString(PasswordPrefKey, "").toString().equals(binding.editTextTextPassword.getText().toString())){
            NavDirections direc = LoginFragmentDirections.actionLoginFragmentToMainScreen();
            navCont.navigate(direc);
        }
        else
            Toast.makeText(getActivity(), R.string.MainActivityWrongPass,Toast.LENGTH_LONG).show();
    }
}
