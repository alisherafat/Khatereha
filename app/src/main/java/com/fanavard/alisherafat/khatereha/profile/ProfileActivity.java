package com.fanavard.alisherafat.khatereha.profile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fanavard.alisherafat.khatereha.R;

public class ProfileActivity extends AppCompatActivity {
    public static final String CONTACT_ID = "contact";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container,
                ProfileFragment.newInstance(getIntent().getLongExtra(CONTACT_ID,-1)))
                .commit();
        setTitle("Profile");
    }
}
