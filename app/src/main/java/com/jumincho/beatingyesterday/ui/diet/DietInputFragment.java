package com.jumincho.beatingyesterday.ui.diet;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.jumincho.beatingyesterday.MainActivity;
import com.jumincho.beatingyesterday.R;
import com.jumincho.beatingyesterday.data.FoodCalorieApi;
import com.jumincho.beatingyesterday.ui.home.HomeViewModel;

import static android.content.Context.MODE_PRIVATE;


public class DietInputFragment extends Fragment {
    View root;
    Button currentBtn;
    static TextView tv;

    public static DietInputFragment newInstance() {
        DietInputFragment frag = new DietInputFragment();
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
        root = inflater.inflate(R.layout.fragment_diet_input, container, false);
        Button rfButton = (Button) root.findViewById(R.id.refreshButton);
        Button initButton = (Button) root.findViewById(R.id.InitButton);
        Button btn2 = (Button) root.findViewById(R.id.rightMenu);
        Button btn3 = (Button) root.findViewById(R.id.searchButton);
        RadioButton morning = (RadioButton) root.findViewById(R.id.morning);
        RadioButton lunch = (RadioButton) root.findViewById(R.id.lunch);
        RadioButton dinner = (RadioButton) root.findViewById(R.id.dinner);
        RadioGroup rGroup = (RadioGroup) root.findViewById(R.id.rGroup);
        init();
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentBtn = (Button) v;
                MainActivity activity = (MainActivity) getActivity();
                String buttonName = currentBtn.getText().toString();
                if (buttonName.equals("체중 관리")) {
                    activity.showFragment(WeightManagementFragment.newInstance());
                } else if (buttonName.equals("등록")) {
                    if (!(morning.isChecked() || lunch.isChecked() || dinner.isChecked())) {
                        Toast.makeText(getActivity(), "아침, 점심, 저녁을 선택해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (morning.isChecked()) {
                        tv = (TextView) root.findViewById(R.id.textView);
                    } else if (lunch.isChecked()) {
                        tv = (TextView) root.findViewById(R.id.textView1);
                    } else if (dinner.isChecked()) {
                        tv = (TextView) root.findViewById(R.id.textView2);
                    }
                    EditText et = (EditText) root.findViewById(R.id.foodNameText);
                    String foodName = et.getText().toString();
                    if (foodName.equals("")) {
                        Toast.makeText(getActivity(), "음식을 입력해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    et.setText("");
                    FoodCalorieApi.getJson(foodName);
                    saveTextView();
                    saveTotal();
                } else if (buttonName.equals("초기화")) {
                    reset();
                    Toast.makeText(getActivity(), "초기화하였습니다.", Toast.LENGTH_SHORT).show();
                } else if (buttonName.equals("갱신")) {
                    renewal();
                    SharedPreferences sf = getActivity().getSharedPreferences("Today_kcalScore", MODE_PRIVATE);
                    SharedPreferences.Editor e = sf.edit();
                    e.putInt("Kcal", WeightManagementFragment.kcalScore);
                    e.commit();
                    Toast.makeText(getActivity(), "갱신하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        };
        rfButton.setOnClickListener(listener);
        initButton.setOnClickListener(listener);
        btn2.setOnClickListener(listener);
        btn3.setOnClickListener(listener);
        return root;
    }

    public static void setTextView(String s) {
        tv.setText(s);
    }

    public static String getTextView() {
        return tv.getText().toString();
    }

    public void saveTotal() {
        SharedPreferences sf = getActivity().getSharedPreferences("Kcal", MODE_PRIVATE);
        SharedPreferences.Editor e = sf.edit();
        e.putInt("total", DietViewModel.total);
        e.commit();
    }

    public void saveTextView() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("TextViewData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        TextView tv1 = (TextView) root.findViewById(R.id.textView);
        TextView tv2 = (TextView) root.findViewById(R.id.textView1);
        TextView tv3 = (TextView) root.findViewById(R.id.textView2);
        editor.putString("BreakFast", tv1.getText().toString());
        editor.putString("Lunch", tv2.getText().toString());
        editor.putString("Dinner", tv3.getText().toString());
        editor.commit();
    }

    @Override
    public void onPause() {
        super.onPause();
        saveTextView();
        saveTotal();
    }

    public void init() {
        SharedPreferences sf = getActivity().getSharedPreferences("TextViewData", MODE_PRIVATE);
        TextView tv1 = (TextView) root.findViewById(R.id.textView);
        TextView tv2 = (TextView) root.findViewById(R.id.textView1);
        TextView tv3 = (TextView) root.findViewById(R.id.textView2);
        tv1.setText(sf.getString("BreakFast", "아침"));
        tv2.setText(sf.getString("Lunch", "점심"));
        tv3.setText(sf.getString("Dinner", "저녁"));
        SharedPreferences sf1 = getActivity().getSharedPreferences("Kcal", MODE_PRIVATE);
        DietViewModel.total = sf1.getInt("total", 0);
    }

    public void reset() {
        TextView tv1 = (TextView) root.findViewById(R.id.textView);
        TextView tv2 = (TextView) root.findViewById(R.id.textView1);
        TextView tv3 = (TextView) root.findViewById(R.id.textView2);
        tv1.setText("아침");
        tv2.setText("점심");
        tv3.setText("저녁");
        DietViewModel.total = 0;
        SharedPreferences sf1 = getActivity().getSharedPreferences("Today_kcalScore", MODE_PRIVATE);
        SharedPreferences.Editor e1 = sf1.edit();
        e1.clear();
        e1.commit();
        saveTextView();
    }

    public void refresh() {
        HomeViewModel.yesterKcalScore = WeightManagementFragment.kcalScore;
        SharedPreferences sf = getActivity().getSharedPreferences("Yesterday_kcalScore", MODE_PRIVATE);
        SharedPreferences.Editor e = sf.edit();
        SharedPreferences sf1 = getActivity().getSharedPreferences("Today_kcalScore", MODE_PRIVATE);
        SharedPreferences.Editor e1 = sf1.edit();
        e.putInt("Kcal", WeightManagementFragment.kcalScore);
        e1.clear();
        e.commit();
        e1.commit();
    }

    public void renewal() {
        refresh();
        TextView tv1 = (TextView) root.findViewById(R.id.textView);
        TextView tv2 = (TextView) root.findViewById(R.id.textView1);
        TextView tv3 = (TextView) root.findViewById(R.id.textView2);
        tv1.setText("아침");
        tv2.setText("점심");
        tv3.setText("저녁");
        DietViewModel.total = 0;
    }
}
