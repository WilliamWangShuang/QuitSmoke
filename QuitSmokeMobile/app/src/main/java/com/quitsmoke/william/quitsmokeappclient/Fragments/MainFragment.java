package com.quitsmoke.william.quitsmokeappclient.Fragments;

import android.app.Activity;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quitsmoke.william.quitsmokeappclient.R;

import clientservice.QuitSmokeClientUtils;
import clientservice.factory.SwitchMainAdapter;

public class MainFragment extends Fragment {
    private View vMainFragment;
    private FragmentActivity myContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        vMainFragment = inflater.inflate(R.layout.fragment_main, container, false);

        return vMainFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        TabLayout.Tab smokerTab = tabLayout.newTab();
        TabLayout.Tab supporterTab = tabLayout.newTab();
        smokerTab.setCustomView(R.layout.tab_item);
        ((TextView)smokerTab.getCustomView().findViewById(R.id.tabTextView)).setText("Quitter");
        supporterTab.setCustomView(R.layout.tab_item);
        ((TextView)supporterTab.getCustomView().findViewById(R.id.tabTextView)).setText("Supporter");
        tabLayout.addTab(smokerTab);
        tabLayout.addTab(supporterTab);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager);
        final SwitchMainAdapter adapter = new SwitchMainAdapter
                (myContext.getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
