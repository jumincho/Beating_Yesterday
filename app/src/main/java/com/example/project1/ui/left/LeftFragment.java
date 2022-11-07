package com.example.project1.ui.left;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager2.adapter.FragmentViewHolder;

import com.example.project1.MainActivity;
import com.example.project1.R;
import com.example.project1.ui.QuestionActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class LeftFragment extends Fragment {

    private LeftViewModel leftViewModel;
    Button currentBtn;

    public static LeftFragment newInstance() {
        LeftFragment frag = new LeftFragment();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        leftViewModel = new ViewModelProvider(this).get(LeftViewModel.class);
        View root = inflater.inflate(R.layout.fragment_left, container, false);
        Button btn1 = (Button) root.findViewById(R.id.leftMenu);
        Button btn2 = (Button) root.findViewById(R.id.rightMenu);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentBtn = (Button)v;
                MainActivity activity = (MainActivity)getActivity();
                String buttonName = currentBtn.getText().toString();
                if(buttonName.equals("식단관리")) {
                    activity.FragmentView(Frag1.newInstance());
                }
                else if(buttonName.equals("체중관리")) {
                    activity.FragmentView(Frag2.newInstance());
                }
            }
        };
        btn1.setOnClickListener(listener);
        btn2.setOnClickListener(listener);
        MainActivity activity = (MainActivity)getActivity();
        activity.FragmentView(Frag1.newInstance());
        return root;
    }
}