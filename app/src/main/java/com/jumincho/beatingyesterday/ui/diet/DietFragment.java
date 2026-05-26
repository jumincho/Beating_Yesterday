package com.jumincho.beatingyesterday.ui.diet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.jumincho.beatingyesterday.MainActivity;
import com.jumincho.beatingyesterday.R;

public class DietFragment extends Fragment {

    private DietViewModel dietViewModel;
    Button currentBtn;

    public static DietFragment newInstance() {
        DietFragment frag = new DietFragment();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dietViewModel = new ViewModelProvider(this).get(DietViewModel.class);
        View root = inflater.inflate(R.layout.fragment_diet, container, false);
        Button btn1 = (Button) root.findViewById(R.id.leftMenu);
        Button btn2 = (Button) root.findViewById(R.id.rightMenu);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentBtn = (Button) v;
                MainActivity activity = (MainActivity) getActivity();
                String buttonName = currentBtn.getText().toString();
                if (buttonName.equals("식단관리")) {
                    activity.showFragment(DietInputFragment.newInstance());
                } else if (buttonName.equals("체중관리")) {
                    activity.showFragment(WeightManagementFragment.newInstance());
                }
            }
        };
        btn1.setOnClickListener(listener);
        btn2.setOnClickListener(listener);
        return root;
    }
}
