package com.voitu.ficoapp.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.voitu.ficoapp.R;
import com.voitu.ficoapp.controller.Common;
import com.voitu.ficoapp.databinding.FragmentSecondBinding;
import com.voitu.ficoapp.model.AuthManager;
import com.voitu.ficoapp.model.DataSource;
import com.voitu.ficoapp.model.DataSourceImpl;
import com.voitu.ficoapp.model.Role;
import com.voitu.ficoapp.model.User;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private DataSource db;
    private  AuthManager auth;
    private User currentUser;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        db = new DataSourceImpl();
        this.auth= new AuthManager();
        this.db.getUser(this.auth.getCurrentUser().getUid(), us->{
            this.currentUser= us;
        });
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonFirst.setOnClickListener(view1 -> NavHostFragment.findNavController(SecondFragment.this)
                .navigate(R.id.action_SecondFragment_to_FirstFragment));
        binding.secondBack.setOnClickListener(v -> Common.startMainActivity(getActivity()));
        binding.addNewRank.setOnClickListener(v -> addTag());
    }

    private void addTag(){
        TextView t = binding.newTagBox;
        if(this.currentUser.getRole().getPermissionLevel() < Role.GUEST.getPermissionLevel()){
            Common.showError("Not allowed :(",getActivity());
            return;
        }
        if (!t.getText().toString().isEmpty()){
            new AlertDialog.Builder(getActivity())
                    .setTitle("New Tag")
                    .setMessage("Are you sure?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes,
                            (dialog, whichButton) -> {
                                this.db.addTag(t.getText().toString(), s -> {
                                    Common.showError("Added",getActivity());
                                    t.setText("");
                                });
                            })
                    .setNegativeButton(android.R.string.no, null).show();
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}