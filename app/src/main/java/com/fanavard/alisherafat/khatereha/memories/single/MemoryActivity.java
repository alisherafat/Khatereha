package com.fanavard.alisherafat.khatereha.memories.single;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fanavard.alisherafat.khatereha.R;

public class MemoryActivity extends AppCompatActivity {
    public static final String MEMORY_ID = "memory_id";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                MemoryFragment.newInstance(getIntent().getLongExtra(MEMORY_ID, -1))).commit();
        setTitle("Memory");
    }
}
