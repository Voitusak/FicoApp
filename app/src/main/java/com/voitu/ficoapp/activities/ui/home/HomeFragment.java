package com.voitu.ficoapp.activities.ui.home;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.Timestamp;
import com.voitu.ficoapp.R;
import com.voitu.ficoapp.activities.UpdatesAdapter;
import com.voitu.ficoapp.controller.Common;
import com.voitu.ficoapp.databinding.FragmentHomeBinding;
import com.voitu.ficoapp.model.Role;
import com.voitu.ficoapp.model.UpdateInfo;
import com.voitu.ficoapp.model.UpdateInfoImpl;
import com.voitu.ficoapp.model.User;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    public static final String NICE_TRY = "Nice try";
    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    private Spinner s;
    private User selectedUser;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        //final TextView textView = binding.fragmentTitle;
        ListView listView = binding.listView;
        List<UpdateInfo> list = new ArrayList<>();
        UpdatesAdapter adapter = new UpdatesAdapter(requireActivity(), list);

        // Here, you set the data in your ListView
        listView.setAdapter(adapter);
        homeViewModel.getStatus().observe(getViewLifecycleOwner(), o -> {
            list.clear();
            list.addAll(o);
            sortList(list);
            adapter.notifyDataSetChanged();
        });
        s = binding.usersSpinner;
        final SwipeRefreshLayout pullToRefresh = binding.privaterefresh;
        pullToRefresh.setOnRefreshListener(() -> {
            homeViewModel.updateData(((User) s.getSelectedItem()).getId()); // your code
            pullToRefresh.setRefreshing(false);
        });


        List<User> userList = new ArrayList<>();

        ArrayAdapter<User> spinnerArrayAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.spinner_text, userList);
        homeViewModel.getUsers().observe(getViewLifecycleOwner(), o -> {
            userList.clear();
            userList.addAll(o);
            adapter.setNames(o);
            adapter.notifyDataSetChanged();
            spinnerArrayAdapter.notifyDataSetChanged();
        });
        s.setAdapter(spinnerArrayAdapter);
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                User us = ((User)s.getItemAtPosition(position));
                homeViewModel.updateData(us.getId());
                System.out.println(((User)s.getItemAtPosition(position)).toJson().toString());
                selectedUser = us;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                s.setSelection(0);
                selectedUser = ((User)s.getItemAtPosition(0));
            }
        });
        Button b = binding.addPoints;
        EditText box = binding.pointsbox;
        b.setOnClickListener(view -> {
            if(!box.getText().toString().isEmpty()){
                addPoints(box.getText().toString());
                box.setText("");
            }
        });
        return root;
    }
    private boolean checkIfLoggedUserSelected(String id){
        return id.equals(this.homeViewModel.getLoggedUser().getId());
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sortList(List<UpdateInfo> list) {
        list.sort((a,b) -> b.getDate().compareTo(a.getDate()));
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addPoints(String points) {
        if(checkIfLoggedUserSelected(this.selectedUser.getId())){
            Common.showError(NICE_TRY, getActivity());
            return;
        }
        if(this.homeViewModel.getLoggedUser().getRole().getPermissionLevel() != Role.MEMBER.getPermissionLevel() &&
                this.homeViewModel.getLoggedUser().getRole().getPermissionLevel() <=
                this.selectedUser.getRole().getPermissionLevel()){
            Common.showError("Not allowed :(",getActivity());
            return;
        }
        long savingPoints = 0L;
        try{
            savingPoints = Long.parseLong(points);
        } catch (Exception e){
            return;
        }
        if(this.homeViewModel.getLoggedUser().getStatus().getPoints() - savingPoints < 0 ){
            return;
        }

        UpdateInfo i = new UpdateInfoImpl(Timestamp.now().toDate(),savingPoints,
                this.homeViewModel.getLoggedUser().getId());
        UpdateInfo iMinus = new UpdateInfoImpl(Timestamp.now().toDate(),-savingPoints,
                this.selectedUser.getId());
        homeViewModel.addPoints(i, this.selectedUser.getId());
        homeViewModel.addPoints(iMinus, this.homeViewModel.getLoggedUser().getId());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}