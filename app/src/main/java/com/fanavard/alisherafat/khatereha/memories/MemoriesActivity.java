package com.fanavard.alisherafat.khatereha.memories;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fanavard.alisherafat.khatereha.R;

public class MemoriesActivity extends AppCompatActivity {
    public static final String ACTION = "action";
    public static final String CONATCT_ID = "contact_id";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memories);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                MemoryListFragment.newInstance(getIntent().getLongExtra(CONATCT_ID,-1))).commit();
        setTitle("Memories");
    }
}
