package net.ncie.cutemap.act;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import net.ncie.cutemap.R;
import net.ncie.cutemap.fragment.MapFragment;
import net.ncie.cutemap.fragment.SettingFragment;
import net.ncie.cutemap.util.MyUtil;


public class MainAct extends AppCompatActivity {

    private boolean doubleBackToExitPressedOnce = false;
    private FragmentManager mFragmentManager;
    private Fragment[] fragments;
    private int lastFragment;
    private BottomNavigationView mNavigationView;


    private MapFragment mapFragment;
    private SettingFragment mSettingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        initUI();
        initFragment();
        initListener();

    }

    public void initUI(){
        mNavigationView = findViewById(R.id.main_navigation_bar);



    }

    private void initFragment() {
        mapFragment = new MapFragment(this);
        mSettingFragment = new SettingFragment(this);

        fragments = new Fragment[]{mapFragment,mSettingFragment};
        mFragmentManager = getSupportFragmentManager();
        //默认显示HomeFragment
        mFragmentManager.beginTransaction()
                .replace(R.id.main_page_controller, mapFragment)
                .show(mapFragment)
                .commit();
    }

    private void initListener() {
        mNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.map:
                        if (lastFragment != 0) {
                            MainAct.this.switchFragment(lastFragment, 0);
                            lastFragment = 0;

                        }
                        return true;
                    case R.id.setting:
                        if (lastFragment != 1) {
                            MainAct.this.switchFragment(lastFragment, 1);
                            lastFragment = 1;

                        }
                        return true;

                }
                return false;
            }
        });
    }

    private void switchFragment(int lastFragment, int index) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.hide(fragments[lastFragment]);

        if (!fragments[index].isAdded()){
            transaction.add(R.id.main_page_controller,fragments[index]);
        }
        transaction.show(fragments[index]).commitAllowingStateLoss();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;

        MyUtil.MyToast(this,"Press the Back key again to exit the app");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}