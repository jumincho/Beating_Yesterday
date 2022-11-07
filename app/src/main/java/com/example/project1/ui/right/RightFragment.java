package com.example.project1.ui.right;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.project1.MainActivity;
import com.example.project1.R;
import com.example.project1.ui.left.Frag1;
import com.example.project1.ui.left.Frag2;

public class RightFragment extends Fragment {

    private RightViewModel rightViewModel;
    Button currentBtn;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        rightViewModel = new ViewModelProvider(this).get(RightViewModel.class);
        View root = inflater.inflate(R.layout.fragment_right, container, false);
        Button btn1 = (Button) root.findViewById(R.id.leftMenu);
        Button btn2 = (Button) root.findViewById(R.id.rightMenu);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentBtn = (Button)v;
                MainActivity activity = (MainActivity)getActivity();
                String buttonName = currentBtn.getText().toString();
                if(buttonName.equals("TODO LIST")) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), TodoActivity.class);
                    startActivity(intent);
                }
                else if(buttonName.equals("공부(업무) 타이머")) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), TimerActivity.class);
                    startActivity(intent);
                }
            }
        };
        btn1.setOnClickListener(listener);
        btn2.setOnClickListener(listener);

        return root;
    }
}