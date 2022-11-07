package com.example.project1.ui.left;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project1.MainActivity;
import com.example.project1.R;
import com.example.project1.ui.home.HomeViewModel;

import org.w3c.dom.Text;

import static android.content.Context.MODE_PRIVATE;

public class Frag2 extends Fragment {

    static int stdKcal;
    static int kcalScore;
    Button currentBtn;

    public static Frag2 newInstance() {
        Frag2 frag2 = new Frag2();
        Bundle args = new Bundle();
        frag2.setArguments(args);
        return frag2;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.leftfragment_frag2, container, false);
        Button btn1 = (Button) root.findViewById(R.id.leftMenu);
        TextView tv1 = (TextView) root.findViewById(R.id.calTextView);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentBtn = (Button)v;
                MainActivity activity = (MainActivity)getActivity();
                String buttonName = currentBtn.getText().toString();
                if(buttonName.equals("식단 입력")) {
                    activity.FragmentView(Frag1.newInstance());
                }
                else if(buttonName.equals("체중 관리")) {
                    activity.FragmentView(Frag2.newInstance());
                }
            }
        };
        btn1.setOnClickListener(listener);
        if(HomeViewModel.gender.equals("남자")) {
            stdKcal = 2700;
        }
        else { stdKcal = 2000; }

        if(HomeViewModel.bmiLevel == 1) {
            tv1.setText("당신의 체중은 저체중입니다.\n");
            kcalScore = LeftViewModel.total - stdKcal;
        }
        else if(HomeViewModel.bmiLevel == 2) {
            tv1.setText("당신의 체중은 정상체중입니다.\n");
            kcalScore = stdKcal - LeftViewModel.total;
            if(kcalScore >= 0) { kcalScore = -kcalScore; }
        }
        else {
            tv1.setText("당신의 체중은 과체중입니다.\n");
            kcalScore = stdKcal - LeftViewModel.total;
        }
        SharedPreferences sf = getActivity().getSharedPreferences("Today_kcalScore", MODE_PRIVATE);
        SharedPreferences.Editor e = sf.edit();
        e.putInt("Kcal", kcalScore);
        e.commit();
        tv1.append("오늘의 칼로리 점수는 " + kcalScore + "입니다.");
        return root;
    }


}