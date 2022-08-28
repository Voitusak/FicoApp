package com.voitu.ficoapp.activities.ui.main;

import static com.voitu.ficoapp.controller.Common.convertNumber;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.voitu.ficoapp.activities.FinalActivity;
import com.voitu.ficoapp.controller.Common;
import com.voitu.ficoapp.databinding.FragmentMainBinding;
import com.voitu.ficoapp.model.AuthManager;
import com.voitu.ficoapp.model.DataSourceImpl;
import com.voitu.ficoapp.model.FicoPointsStatus;
import com.voitu.ficoapp.model.Role;


public class MainFragment extends Fragment {

    private FragmentMainBinding binding;

    private View background;
    private TextView pointsView;
    private TextView percentageView;
    private ProgressBar progressBar;

    private boolean firstLoading = true;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MainViewModel homeViewModel =
                new ViewModelProvider(this).get(MainViewModel.class);
        binding = FragmentMainBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        this.background = binding.wallpaper;
        this.pointsView = binding.points;
        this.percentageView = binding.percentage;
        this.progressBar = binding.progressBar;
        final SwipeRefreshLayout pullToRefresh = binding.mainrefresh;
        pullToRefresh.setOnRefreshListener(() -> {
            homeViewModel.updateData(); // your code
            pullToRefresh.setRefreshing(false);
        });
        //final TextView textView = binding.fragmentTitle;
         homeViewModel.getStatus().observe(getViewLifecycleOwner(), this::updateView);
        return root;
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    public void updateView(FicoPointsStatus s) {
        long points  = s.getPoints();
        long maxPoints = s.getThreshold();
        pointsView.setText(convertNumber(points));
        double percentage = maxPoints !=0 ? ((double)(points*100) / maxPoints) : 0;
        System.out.println("Points->" + points + "  max ->" + maxPoints + " percentage ->" + percentage);
        percentageView.setText(Common.formatter.format(percentage) + "%");
        progressBar.setProgress((int) percentage);
        if(points >= maxPoints){
            new DataSourceImpl().getUser(new AuthManager().getCurrentUser().getUid(),u ->{
                if(u.getRole().equals(Role.PIU_TOBIA)) {
                    Intent intent = new Intent(getActivity(), FinalActivity.class);
                    startActivity(intent);
                }
            });
        }
        firstLoading = false;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}