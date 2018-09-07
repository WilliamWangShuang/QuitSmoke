package com.example.william.quitsmokeappclient.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import com.example.william.quitsmokeappclient.R;

public class PartnerMainFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.partner_main_fragment, container, false);
    }
}
