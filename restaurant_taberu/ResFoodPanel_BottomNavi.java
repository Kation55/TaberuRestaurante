package com.example.restaurant_taberu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.example.restaurant_taberu.resFoodPanel.ResHomeFragment;
import com.example.restaurant_taberu.resFoodPanel.ResOrderFragment;
import com.example.restaurant_taberu.resFoodPanel.ResPendingOrderFragment;
import com.example.restaurant_taberu.resFoodPanel.ResProfileFragment;


public class ResFoodPanel_BottomNavi extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res_food_panel__bottom_navigation);
        BottomNavigationView navigationView = findViewById(R.id.res_bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()){
            case R.id.resHome:
                fragment = new ResHomeFragment();
                break;
            case R.id.Orders:
                fragment=new ResOrderFragment();
                break;
            case R.id.resProfile:
                fragment = new ResProfileFragment();
                break;
        }
        return loadcheffragment(fragment);
    }

    private boolean loadcheffragment(Fragment fragment) {

        if (fragment != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,fragment).commit();
            return true;
        }
        return false;
    }
}