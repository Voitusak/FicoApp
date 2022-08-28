package com.voitu.ficoapp.activities.ui.account;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.voitu.ficoapp.databinding.FragmentAccountBinding;
import com.voitu.ficoapp.model.User;

public class AccountFragment extends Fragment {

    private FragmentAccountBinding binding;
    private User user;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AccountViewModel accountViewModel =
                new ViewModelProvider(this).get(AccountViewModel.class);

        binding = FragmentAccountBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        accountViewModel.getUser(u ->{
            this.user = u;
            System.out.println(this.user.toJson());

            fillField();
        });

        return root;
    }

    private void fillField() {
        this.binding.accountAlias.setText(this.user.getAlias());
        this.binding.accountEmail.setText(this.user.getEmail());
        this.binding.accountEmail.setSingleLine();
        this.binding.accountName.setText(this.user.getName());
        this.binding.accountSurname.setText(this.user.getSurname());
        this.binding.accountRole.setText(this.user.getRole().getName());
        this.binding.accountRole.setTextColor(requireActivity()
                .getResources().getColor(this.user.getRole().getColor()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}