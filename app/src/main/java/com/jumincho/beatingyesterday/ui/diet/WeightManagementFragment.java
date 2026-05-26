package com.jumincho.beatingyesterday.ui.diet;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.jumincho.beatingyesterday.MainActivity;
import com.jumincho.beatingyesterday.R;
import com.jumincho.beatingyesterday.ui.home.HomeViewModel;

import static android.content.Context.MODE_PRIVATE;

public class WeightManagementFragment extends Fragment {

    static int stdKcal;
    static int kcalScore;
    Button currentBtn;

    public static WeightManagementFragment newInstance() {
        WeightManagementFragment frag = new WeightManagementFragment();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_weight_management, container, false);
        Button btn1 = (Button) root.findViewById(R.id.leftMenu);
        TextView tv1 = (TextView) root.findViewById(R.id.calTextView);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentBtn = (Button) v;
                MainActivity activity = (MainActivity) getActivity();
                String buttonName = currentBtn.getText().toString();
                if (buttonName.equals("식단 입력")) {
                    activity.showFragment(DietInputFragment.newInstance());
                } else if (buttonName.equals("체중 관리")) {
                    activity.showFragment(WeightManagementFragment.newInstance());
                }
            }
        };
        btn1.setOnClickListener(listener);
        if (HomeViewModel.gender.equals("남자")) {
            stdKcal = 2700;
        } else {
            stdKcal = 2000;
        }

        if (HomeViewModel.bmiLevel == 1) {
            tv1.setText("당신의 체중은 저체중입니다.\n");
            kcalScore = DietViewModel.total - stdKcal;
        } else if (HomeViewModel.bmiLevel == 2) {
            tv1.setText("당신의 체중은 정상체중입니다.\n");
            kcalScore = stdKcal - DietViewModel.total;
            if (kcalScore >= 0) {
                kcalScore = -kcalScore;
            }
        } else {
            tv1.setText("당신의 체중은 과체중입니다.\n");
            kcalScore = stdKcal - DietViewModel.total;
        }
        SharedPreferences sf = getActivity().getSharedPreferences("Today_kcalScore", MODE_PRIVATE);
        SharedPreferences.Editor e = sf.edit();
        e.putInt("Kcal", kcalScore);
        e.commit();
        tv1.append("오늘의 칼로리 점수는 " + kcalScore + "입니다.");
        return root;
    }
}
