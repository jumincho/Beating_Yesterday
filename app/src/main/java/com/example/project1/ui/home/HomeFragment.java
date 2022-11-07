package com.example.project1.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.project1.MainActivity;
import com.example.project1.R;
import com.example.project1.ui.QuestionActivity;
import com.example.project1.ui.left.Frag1;
import com.example.project1.ui.left.LeftViewModel;
import com.example.project1.ui.right.MyTimer;
import com.example.project1.ui.right.TimerActivity;

import org.w3c.dom.Text;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {
    public static HomeFragment newInstance() {
        HomeFragment frag = new HomeFragment();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }
    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        homeViewModel.setData();
        homeViewModel.setTextView(root);
        SharedPreferences sf = getActivity().getSharedPreferences("Yesterday_kcalScore", Context.MODE_PRIVATE);
        int tmp = sf.getInt("Kcal", 0);
        TextView tv1_y = (TextView)root.findViewById(R.id.kcalTextView);
        tv1_y.append(tmp + "");
        SharedPreferences sf2 = getActivity().getSharedPreferences("yesterday_interval", Context.MODE_PRIVATE);
        HomeViewModel.yesterdayInterval = sf2.getLong("interval", 0);
        long tmp1 = HomeViewModel.yesterdayInterval;
        TextView tv2_y = (TextView)root.findViewById(R.id.studyTimeTextView);
        tv2_y.append(tmp1 + "초");
        SharedPreferences sf1 = getActivity().getSharedPreferences("Today_kcalScore", Context.MODE_PRIVATE);
        HomeViewModel.todayKcalSCore = sf1.getInt("Kcal", 0);
        tmp = HomeViewModel.todayKcalSCore;
        TextView tv1 = (TextView)root.findViewById(R.id.today_KcalTextView);
        tv1.append(tmp + "");
        SharedPreferences sf3 = getActivity().getSharedPreferences("today_interval",Context.MODE_PRIVATE);
        HomeViewModel.todayInterval = sf3.getLong("interval", 0);
        tmp1 = HomeViewModel.todayInterval;
        TextView tv2 = (TextView)root.findViewById(R.id.today_StudytimeTextView);
        tv2.append(tmp1 + "초");
        Button btn = root.findViewById(R.id.editButton);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData(root);
                setData();
                homeViewModel.setBmiLevel();
                Toast.makeText(getActivity(), "수정하였습니다.", Toast.LENGTH_SHORT).show();
                homeViewModel.setTextView(root);
            }
        };
        Button btn1 = root.findViewById(R.id.button2);
        View.OnClickListener listener1  = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv1_y.setText(tv1.getText().toString());
                tv2_y.setText(tv2.getText().toString());
                tv1.setText("칼로리 : 0");
                tv2.setText("공부시간 : 0초");
                SharedPreferences sf = getActivity().getSharedPreferences("Yesterday_kcalScore", Context.MODE_PRIVATE);
                SharedPreferences sf1 = getActivity().getSharedPreferences("Today_kcalScore", Context.MODE_PRIVATE);
                SharedPreferences sf2 = getActivity().getSharedPreferences("yesterday_interval", Context.MODE_PRIVATE);
                SharedPreferences sf3 = getActivity().getSharedPreferences("today_interval",Context.MODE_PRIVATE);
                SharedPreferences.Editor e = sf.edit();
                SharedPreferences.Editor e1 = sf1.edit();
                SharedPreferences.Editor e2 = sf2.edit();
                SharedPreferences.Editor e3 = sf3.edit();
                if((sf.getInt("Kcal", 0) < sf1.getInt("Kcal", 0)) && sf2.getLong("interval", 0) < sf3.getLong("interval", 0) ) {
                    ImageView y = (ImageView) root.findViewById(R.id.imageView);
                    ImageView t = (ImageView) root.findViewById(R.id.imageView2);
                    y.setImageResource(R.drawable.loseimg);
                    t.setImageResource(R.drawable.winimg);
                    Toast.makeText(getActivity(), "승부결과 : 어제의 나를 이겼습니다." , Toast.LENGTH_SHORT).show();
                }
                else {
                    ImageView y = (ImageView) root.findViewById(R.id.imageView);
                    ImageView t = (ImageView) root.findViewById(R.id.imageView2);
                    y.setImageResource(R.drawable.winimg);
                    t.setImageResource(R.drawable.loseimg);
                    Toast.makeText(getActivity(), "승부결과 : 어제의 내가 이겼습니다." , Toast.LENGTH_SHORT).show();
                }
                e.putInt("Kcal", sf1.getInt("Kcal", 0));
                e2.putLong("interval", sf3.getLong("interval", 0));
                e1.clear();
                e3.clear();
                e.commit(); e1.commit(); e2.commit(); e3.commit();
                SharedPreferences sf4 = getActivity().getSharedPreferences("TextViewData", MODE_PRIVATE);
                SharedPreferences.Editor e4 = sf4.edit();
                e4.clear();
                e4.putString("BreakFast", "아침");
                e4.putString("Lunch", "점심");
                e4.putString("Dinner", "저녁");
                e4.commit();

                Toast.makeText(getActivity(), "오늘의 나와 경쟁을 시작합니다." , Toast.LENGTH_SHORT).show();
            }
        };
        btn.setOnClickListener(listener);
        btn1.setOnClickListener(listener1);
        return root;
    }
    public void updateData(View root) {
        EditText nameEdit = (EditText) root.findViewById(R.id.nameEdit);
        EditText genderEdit = (EditText) root.findViewById(R.id.genderEdit);
        EditText ageEdit = (EditText) root.findViewById(R.id.ageEdit);
        EditText heightEdit = (EditText) root.findViewById(R.id.heightEdit);
        EditText weightEdit = (EditText) root.findViewById(R.id.weightEdit);
        HomeViewModel.name = nameEdit.getText().toString();
        HomeViewModel.gender = genderEdit.getText().toString();
        HomeViewModel.age = Integer.parseInt(ageEdit.getText().toString());
        HomeViewModel.height = Float.parseFloat((heightEdit.getText().toString()));
        HomeViewModel.weight = Float.parseFloat((weightEdit.getText().toString()));
        HomeViewModel.BMI = (HomeViewModel.weight / (HomeViewModel.height/100) / (HomeViewModel.height/100));
        HomeViewModel.BMI = (float)(Math.round(HomeViewModel.BMI * 100) / 100.0);

        SharedPreferences sf = getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sf.edit();
        e.putString("Name",HomeViewModel.name); // key, value를 이용하여 저장하는 형태
        e.putString("Gender",HomeViewModel.gender); // key, value를 이용하여 저장하는 형태
        e.putInt("Age",HomeViewModel.age); // key, value를 이용하여 저장하는 형태
        e.putFloat("Height",HomeViewModel.height);
        e.putFloat("Weight",HomeViewModel.weight);// key, value를 이용하여 저장하는 형태
        e.commit();
    }
    public void setData() {
        SharedPreferences sf = getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        MainActivity.name = sf.getString("Name", "이름없음");
        MainActivity.gender = sf.getString("Gender", "성별설정안함");
        MainActivity.age = sf.getInt("Age", 0 );
        MainActivity.height = sf.getFloat("Height", 0);
        MainActivity.weight = sf.getFloat("Weight", 0);
    }

}