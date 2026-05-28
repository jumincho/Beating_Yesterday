package com.jumincho.beatingyesterday.ui.productivity;

import android.content.Intent;
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

public class ProductivityFragment extends Fragment {

    private ProductivityViewModel productivityViewModel;
    Button currentBtn;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        productivityViewModel = new ViewModelProvider(this).get(ProductivityViewModel.class);
        View root = inflater.inflate(R.layout.fragment_productivity, container, false);
        Button btn1 = (Button) root.findViewById(R.id.leftMenu);
        Button btn2 = (Button) root.findViewById(R.id.rightMenu);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentBtn = (Button) v;
                MainActivity activity = (MainActivity) getActivity();
                if (activity == null) {
                    return;
                }
                String buttonName = currentBtn.getText().toString();
                if (buttonName.equals("TODO LIST")) {
                    Intent intent = new Intent(activity.getApplicationContext(), TodoActivity.class);
                    startActivity(intent);
                } else if (buttonName.equals("공부(업무) 타이머")) {
                    Intent intent = new Intent(activity.getApplicationContext(), TimerActivity.class);
                    startActivity(intent);
                }
            }
        };
        btn1.setOnClickListener(listener);
        btn2.setOnClickListener(listener);

        return root;
    }
}
