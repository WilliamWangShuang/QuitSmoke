package com.example.william.quitsmokeappclient.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.william.quitsmokeappclient.R;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainFragment extends Fragment {
    private View vMainFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        vMainFragment = inflater.inflate(R.layout.fragment_main, container, false);

        return vMainFragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
