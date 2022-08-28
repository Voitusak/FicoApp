package com.voitu.ficoapp.activities;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.voitu.ficoapp.R;
import com.voitu.ficoapp.controller.Common;
import com.voitu.ficoapp.databinding.FragmentFirstBinding;
import com.voitu.ficoapp.model.AuthManager;
import com.voitu.ficoapp.model.DataSource;
import com.voitu.ficoapp.model.DataSourceImpl;
import com.voitu.ficoapp.model.Role;
import com.voitu.ficoapp.model.Tag;
import com.voitu.ficoapp.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private DataSource db;
    private AuthManager auth;
    private User currentUser;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        this.db = new DataSourceImpl();
        this.auth = new AuthManager();
        return binding.getRoot();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonSecond.setOnClickListener(view1 -> NavHostFragment.findNavController(FirstFragment.this)
                .navigate(R.id.action_FirstFragment_to_SecondFragment));
        binding.firstBack.setOnClickListener(v -> Common.startMainActivity(getActivity()));

        List<Tag> tags = new ArrayList<>();
        List<User> users = new ArrayList<>();
        ArrayAdapter<Tag> tagsAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.spinner_text, tags);
        ArrayAdapter<User> userAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.spinner_text, users);

        binding.addUsersSpinner.setAdapter(userAdapter);
        binding.addTagSpinner.setAdapter(tagsAdapter);
        String currentUserId = this.auth.getCurrentUser().getUid();
        this.db.getUser(currentUserId, u->{
            this.currentUser = u;
        });
        this.db.getTags(t -> {
            tags.clear();
            tags.addAll(t);
            tagsAdapter.notifyDataSetChanged();
        });
        this.db.getUsers(u->{
            users.clear();
            users.addAll(u.stream().filter(us -> !us.getId().equals(currentUserId))
            .collect(Collectors.toList()));
            userAdapter.notifyDataSetChanged();
        });

        binding.addNewRank.setOnClickListener(v->{
            if(this.currentUser.getRole().getPermissionLevel() < Role.PIU_TOBIA.getPermissionLevel()){
                Common.showError("Not allowed :(",getActivity());
                return;
            }
            User u = (User) binding.addUsersSpinner.getSelectedItem();
            Tag t = (Tag) binding.addTagSpinner.getSelectedItem();
            new AlertDialog.Builder(getActivity())
                    .setTitle("New Tag")
                    .setMessage("Are you sure?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes,
                            (dialog, whichButton) -> {
                                db.addTagWinner(t,u, c -> Common.showError("Added", getActivity()));
                            })
                    .setNegativeButton(android.R.string.no, null).show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}