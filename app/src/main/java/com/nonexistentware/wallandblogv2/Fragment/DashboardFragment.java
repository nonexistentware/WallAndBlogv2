package com.nonexistentware.wallandblogv2.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.nonexistentware.wallandblogv2.Activity.LoginActivity;
import com.nonexistentware.wallandblogv2.Activity.RegisterActivity;
import com.nonexistentware.wallandblogv2.R;

public class DashboardFragment extends Fragment {

    TextView loginBtn, registerBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View itemView = LayoutInflater.from(container.getContext())
                .inflate(R.layout.fragment_dashboard, container, false);

        loginBtn = itemView.findViewById(R.id.login_btn_dash);
        registerBtn = itemView.findViewById(R.id.register_btn_dash);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RegisterActivity.class));
            }
        });

        return itemView;
    }
}
