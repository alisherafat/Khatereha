package com.fanavard.alisherafat.khatereha.contacts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.fanavard.alisherafat.khatereha.R;

/**
 * To host @{@link AddEditContactFragment} in Add and Edit mode
 */
public class AddEditContactActivity extends AppCompatActivity {
    public static final String TYPE = "type";
    public static final String CONTACT_ID = "contact_id";
    public static final int TYPE_ADD = 1;
    public static final int TYPE_EDIT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_contact);
        Fragment fragment = null;
        switch (getIntent().getIntExtra(TYPE, -1)) {
            case TYPE_ADD:
                fragment = AddEditContactFragment.newInstance(-1, true);
                setTitle("New Contact");
                break;
            case TYPE_EDIT:
                fragment = AddEditContactFragment.newInstance(
                        getIntent().getLongExtra(CONTACT_ID, -1), false);
                setTitle("Edit Contact");
                break;

        }

        if (fragment == null) {
            throw new IllegalStateException("type not found");
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment).commit();
    }
}
