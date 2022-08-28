package com.voitu.ficoapp.activities.ui.leaderboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.voitu.ficoapp.activities.ui.ranking.RankingViewModel;
import com.voitu.ficoapp.databinding.FragmentLeaderboardBinding;

public class LeaderboardFragment extends Fragment {

    private RankingViewModel mViewModel;
    private FragmentLeaderboardBinding binding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentLeaderboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mViewModel = new ViewModelProvider(this).get(RankingViewModel.class);

        System.out.println("Fanculo");



        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}