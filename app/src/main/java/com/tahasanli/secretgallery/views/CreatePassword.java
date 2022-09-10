package com.tahasanli.secretgallery.views;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.tahasanli.secretgallery.LoginFragment;
import com.tahasanli.secretgallery.MainActivity;
import com.tahasanli.secretgallery.databinding.CreatepasswordBinding;

public class CreatePassword extends Fragment {
    public CreatepasswordBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = CreatepasswordBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.button.setOnClickListener(this::savePassword);
    }

    public void savePassword(View view) {
        if(binding.password.getText().toString().equals(binding.verifyPassword.getText().toString()))
        {
            LoginFragment.instance.db.execSQL("DROP TABLE IF EXISTS " + LoginFragment.DBTableName );
            LoginFragment.instance.db.execSQL("CREATE TABLE IF NOT EXISTS " + LoginFragment.DBTableName + "(id INTEGER PRIMARY KEY, name VARCHAR, date VARCHAR, image BLOB)");
            LoginFragment.prefs.edit().putString(LoginFragment.PasswordPrefKey, binding.password.getText().toString()).apply();

            Navigation.findNavController(view).navigate(CreatePasswordDirections.actionCreatePasswordToLoginFragment());
        }
        else{
            Toast.makeText( getContext(), "Password dont match!", Toast.LENGTH_SHORT).show();
            binding.password.setText("");
            binding.verifyPassword.setText("");
        }
    }
}
