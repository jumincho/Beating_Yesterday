package com.example.project1.ui.right;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.project1.MainActivity;
import com.example.project1.R;
import com.example.project1.ui.left.Frag1;


public class rFrag2 extends Fragment {
    Button currentBtn;

    public static rFrag2 newInstance() {
        rFrag2 fragment = new rFrag2();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.rightfragment_frag2, container, false);
        Button btn1 = (Button) root.findViewById(R.id.leftMenu);
        Button btn2 = (Button) root.findViewById(R.id.rightMenu);
        View.OnClickListener listener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                currentBtn = (Button)v;
                MainActivity activity = (MainActivity)getActivity();
                String buttonName = currentBtn.getText().toString();
                if(buttonName.equals("일정관리")) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), TodoActivity.class);
                    startActivity(intent);
                }
            }
        };
        btn1.setOnClickListener(listener);
        return root;
    }
}