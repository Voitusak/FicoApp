package com.voitu.ficoapp.activities.ui.ranking;

import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.voitu.ficoapp.R;
import com.voitu.ficoapp.activities.RanksAdapter;
import com.voitu.ficoapp.controller.Common;
import com.voitu.ficoapp.databinding.FragmentRankingBinding;
import com.voitu.ficoapp.model.Tag;
import com.voitu.ficoapp.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RankingFragment extends Fragment {

    private FragmentRankingBinding binding;
    private Tag selectedTag;
    private Spinner s;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        RankingViewModel rankingViewModel =
                new ViewModelProvider(this).get(RankingViewModel.class);

        binding = FragmentRankingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        s = binding.usersSpinner;
        final SwipeRefreshLayout pullToRefresh = binding.rankRefresh;
        pullToRefresh.setOnRefreshListener(() -> {
            rankingViewModel.updateData(); // your code
            pullToRefresh.setRefreshing(false);
        });

        List<Tag> tags = new ArrayList<>();


        ArrayAdapter<Tag> spinnerArrayAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.spinner_text, tags);
        rankingViewModel.getTags().observe(getViewLifecycleOwner(), o -> {
            tags.clear();
            tags.addAll(o);
            spinnerArrayAdapter.notifyDataSetChanged();
        });
        s.setAdapter(spinnerArrayAdapter);

        ListView listView = binding.rankListView;
        RanksAdapter adapter = new RanksAdapter(requireActivity(), new ArrayList<>());
        List<User> userList = new ArrayList<>();
        rankingViewModel.getUsers().observe(getViewLifecycleOwner(), o ->{
            userList.clear();
            userList.addAll(o);
            adapter.setNames(o);
            adapter.notifyDataSetChanged();
        });


        // Here, you set the data in your ListView
        listView.setAdapter(adapter);

        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Tag tag = ((Tag)s.getItemAtPosition(position));
                rankingViewModel.updateData();
                System.out.println(tag.getName());
                selectedTag = tag;
                adapter.setTag(tagToPair(selectedTag));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                s.setSelection(0);
                selectedTag = ((Tag)s.getItemAtPosition(0));
                adapter.setTag(tagToPair(selectedTag));
                adapter.notifyDataSetChanged();
            }
        });

        binding.addRank.setOnClickListener(view -> Common.startAddTagsActivity(getActivity()));
        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<Pair<String, Integer>> tagToPair(Tag selectedTag) {
        try {
            return selectedTag.getWinnersIds().stream()
                    .collect(Collectors.toMap(Function.identity(),
                            s -> 1, Integer::sum))
                    .entrySet().stream().map(e -> new Pair<>(e.getKey(), e.getValue()))
                    .sorted(Comparator.comparing(a -> a.second,
                            (a, b) -> Integer.compare(b, a)))
                    .collect(Collectors.toList());
        }catch(Exception e){
            return Collections.emptyList();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}