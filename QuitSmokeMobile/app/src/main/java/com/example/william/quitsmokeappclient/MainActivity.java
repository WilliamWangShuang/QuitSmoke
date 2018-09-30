package com.example.william.quitsmokeappclient;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.william.quitsmokeappclient.Fragments.CalculateFrsFragment;
import com.example.william.quitsmokeappclient.Fragments.CreatePlanFragment;
import com.example.william.quitsmokeappclient.Fragments.GameFragment;
import com.example.william.quitsmokeappclient.Fragments.GameMainFragment;
import com.example.william.quitsmokeappclient.Fragments.MainFragment;
import com.example.william.quitsmokeappclient.Fragments.MapFragment;
import com.example.william.quitsmokeappclient.Fragments.PartnerMainFragment;
import com.example.william.quitsmokeappclient.Fragments.PlanHistoryFragment;
import com.example.william.quitsmokeappclient.Fragments.SmokerMainFragment;

import clientservice.QuitSmokeClientConstant;
import clientservice.QuitSmokeClientUtils;
import clientservice.webservice.receiver.CheckPlanReceiver;
import clientservice.webservice.receiver.ResetStreakReceiver;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // set check create plan receiver
    private CheckPlanReceiver checkPlanReceiver;
    private ResetStreakReceiver resetStreakReceiver;
    private ImageButton btnFrag_home_go;
    private ImageButton btnFrag_create_plan_go;
    private ImageButton btnFrag_frs_calc_go;
    private ImageButton btnFrag_map_go;
    private TextView tvLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // get fragment manager
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        // show fragment according to current user role. If only smoker, show smoker main page, vice versa. If both role, show main page with switch

        fragmentManager.beginTransaction().replace(R.id.content_frame, getMainPageByRole()).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // set buttons onclick events
        // home icon
        btnFrag_home_go = findViewById(R.id.btnFrag_home_go);
        btnFrag_home_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment nextFragment = getMainPageByRole();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, nextFragment).commit();
            }
        });
        // create plan icon
        btnFrag_create_plan_go = findViewById(R.id.btnFrag_create_plan_go);
        btnFrag_create_plan_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment nextFragment = new CreatePlanFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, nextFragment).commit();
            }
        });
        // calculate FRS
        btnFrag_frs_calc_go = findViewById(R.id.btnFrag_frs_calc_go);
        btnFrag_frs_calc_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment nextFragment = new CalculateFrsFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, nextFragment).commit();
            }
        });
        // free zone map
        btnFrag_map_go = findViewById(R.id.btnFrag_map_go);
        btnFrag_map_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment nextFragment = new MapFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, nextFragment).commit();
            }
        });

        // logout
        tvLogout = findViewById(R.id.logout);
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // clear value of email and pwd in shared preference
                SharedPreferences sharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("email", "");
                editor.putString("pwd", "");
                editor.commit();

                // go back to launch page
                Intent intent = new Intent(MainActivity.this, SurveyActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        changeDrawerItem(navigationView);
        // set notification channel
        createNotificationChannel();

        // set login account and password in Shared preference so that user do not need to login every time when open app until he log off
        SharedPreferences sharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String emailInPreference = sharedPreferences.getString("email", "");
        String pwdInPreference = sharedPreferences.getString("pwd", "");
        if (emailInPreference == null
                || "".equals(emailInPreference)
                || pwdInPreference == null
                || "".equals(pwdInPreference)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("email", QuitSmokeClientUtils.getEmail());
            editor.putString("pwd", QuitSmokeClientUtils.getPassword());
            editor.commit();
        }


        // start receiver
        checkPlanReceiver = new CheckPlanReceiver(this);
        // check if smoker has launched app in the same day. if yes, not start receiver to synchronize steaker's point
        if (QuitSmokeClientUtils.isIsSmoker()) {
            boolean isFirstLaunch = sharedPreferences.getBoolean(QuitSmokeClientUtils.getUid() + "isFirstLaunch", true);
            if (isFirstLaunch) {
                resetStreakReceiver = new ResetStreakReceiver(this);
            }
        }

        // set tool bar welcome message
        TextView tvSubtitleInNav = navigationView.getHeaderView(0).findViewById(R.id.tvSubtitle);
        tvSubtitleInNav.setText(getResources().getString(R.string.nav_header_subtitle) + " " + QuitSmokeClientUtils.getName());
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        // check if login account info is already stored in SharedPreference
        SharedPreferences sharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String emailInPreference = sharedPreferences.getString("email", "");
        String pwdInPreference = sharedPreferences.getString("pwd", "");
        // if user is in login status, go back to home page. Otherwise, go back to launch survey activity
        if (emailInPreference != null
                && !"".equals(emailInPreference)
                && pwdInPreference != null
                && !"".equals(pwdInPreference)) {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(MainActivity.this, SurveyActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();
        // declare a fragement
        Fragment nextFragment = null;

        if (id == R.id.calculate_frs) {
            // Handle map action
            nextFragment = new CalculateFrsFragment();
        } else if (id == R.id.home_fragment) {
            // go back home page
            nextFragment = getMainPageByRole();
        } else if (id == R.id.create_plan) {
            // go to create plan page
            nextFragment = new CreatePlanFragment();
        } else if (id == R.id.map) {
            nextFragment = new MapFragment();
        } else if (id == R.id.plan_history) {
            nextFragment = new PlanHistoryFragment();
        } else if (id == R.id.game) {
            nextFragment = new GameMainFragment();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, nextFragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // create notification channel
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= 26) {
            CharSequence name = getString(R.string.channel_name);
            String description = "";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(getString(R.string.channel_id), name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    // change fragment according to current user role
    private Fragment getMainPageByRole() {
        android.support.v4.app.Fragment fragment = null;
        if (QuitSmokeClientUtils.isIsSmoker() && !QuitSmokeClientUtils.isIsPartner()) {
            fragment = new SmokerMainFragment();
        } else if (!QuitSmokeClientUtils.isIsSmoker() && QuitSmokeClientUtils.isIsPartner()) {
            fragment = new PartnerMainFragment();
        } else if (QuitSmokeClientUtils.isIsSmoker() && QuitSmokeClientUtils.isIsPartner()) {
            fragment = new MainFragment();
        }

        return fragment;
    }

    // change drawer items according to current user role
    private void changeDrawerItem(NavigationView drawer) {
        Menu menu = drawer.getMenu();
        if (QuitSmokeClientUtils.isIsSmoker() && !QuitSmokeClientUtils.isIsPartner()) {

        } else if (!QuitSmokeClientUtils.isIsSmoker() && QuitSmokeClientUtils.isIsPartner()) {
            if(Build.VERSION.SDK_INT > 11) {
                invalidateOptionsMenu();
                menu.findItem(R.id.interaction_sub_menu).getSubMenu().findItem(R.id.create_plan).setVisible(false);
                menu.findItem(R.id.interaction_sub_menu).getSubMenu().findItem(R.id.write_report).setVisible(false);
                btnFrag_create_plan_go.setVisibility(GONE);
            }
        } else if (QuitSmokeClientUtils.isIsSmoker() && QuitSmokeClientUtils.isIsPartner()) {

        }
    }
}
