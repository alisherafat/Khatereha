package com.fanavard.alisherafat.khatereha.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fanavard.alisherafat.khatereha.R;
import com.fanavard.alisherafat.khatereha.contacts.ContactsFragment;

/**
 * Host Activity to show @{@link ContactsFragment}
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, ContactsFragment.newInstance())
                .commit();
        setTitle("Contacts");
    }
}
