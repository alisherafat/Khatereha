package com.fanavard.alisherafat.khatereha.memories;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.fanavard.alisherafat.khatereha.R;

public class AddEditMemoryActivity extends AppCompatActivity {

    public static final String TYPE = "type";
    public static final String MEMORY_ID = "memory_id";
    public static final String CONTACT_ID = "contact_id";
    public static final int TYPE_ADD = 1;
    public static final int TYPE_EDIT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_memory);

        Fragment fragment = null;
        switch (getIntent().getIntExtra(TYPE, -1)) {
            case TYPE_ADD:
                fragment = AddEditMemoryFragment.newInstance(-1,
                        getIntent().getLongExtra(CONTACT_ID,-1),
                        true);
                setTitle("New Memory");
                break;
            case TYPE_EDIT:
                fragment = AddEditMemoryFragment.newInstance(
                        getIntent().getLongExtra(MEMORY_ID, -1),
                        getIntent().getLongExtra(CONTACT_ID,-1),
                        false);
                setTitle("Edit Memory");
                break;

        }

        if (fragment == null) {
            throw new IllegalStateException("type not found");
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment).commit();

    }
}
