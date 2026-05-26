package com.jumincho.beatingyesterday;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.jumincho.beatingyesterday.ui.ProfileSetupActivity;
import com.jumincho.beatingyesterday.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {
    public static int questionFlag;
    public static String name;
    public static String gender;
    public static int age;
    public static float height;
    public static float weight;

    public static void refreshData() {
        questionFlag = ProfileSetupActivity.questionFlag;
        name = ProfileSetupActivity.name;
        gender = ProfileSetupActivity.gender;
        age = ProfileSetupActivity.age;
        height = ProfileSetupActivity.height;
        weight = ProfileSetupActivity.weight;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        SharedPreferences sf = getSharedPreferences("UserData", MODE_PRIVATE);
        questionFlag = sf.getInt("Flag", 0);
        name = sf.getString("Name", "이름없음");
        gender = sf.getString("Gender", "성별설정안함");
        age = sf.getInt("Age", 0);
        height = sf.getFloat("Height", 0);
        weight = sf.getFloat("Weight", 0);
        if (questionFlag == 0) {
            Intent intent = new Intent(getApplicationContext(), ProfileSetupActivity.class);
            startActivity(intent);
            finish();
        }
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_diet, R.id.navigation_home, R.id.navigation_productivity).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        showFragment(HomeFragment.newInstance());
    }

    public void showFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.commit();
    }
}
