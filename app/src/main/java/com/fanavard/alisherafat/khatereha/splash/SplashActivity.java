package com.fanavard.alisherafat.khatereha.splash;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.fanavard.alisherafat.khatereha.R;
import com.fanavard.alisherafat.khatereha.app.interfaces.Loggable;
import com.fanavard.alisherafat.khatereha.app.models.Contact;
import com.fanavard.alisherafat.khatereha.app.preferences.PreferenceHelper;
import com.fanavard.alisherafat.khatereha.main.MainActivity;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * Launcher activity to retrieve Contacts and load them into application data base
 */
@RuntimePermissions
public class SplashActivity extends AppCompatActivity implements Loggable {

    private PreferenceHelper preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        preferences = PreferenceHelper.getInstance(this);
        if (preferences.getBoolean(getString(R.string.p_contacts_loaded))) {
            launchMainActivity();
        } else {
            SplashActivityPermissionsDispatcher.fetchContactsWithCheck(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        SplashActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    /**
     * fetching contacts with permission on a separate thread
     */
    @NeedsPermission(Manifest.permission.READ_CONTACTS)
    public void fetchContacts() {
        new FetchContactsAndAddToDB().execute();
    }

    private void launchMainActivity() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }

    @Override
    public void log(String m) {
        Log.d("app", m);
    }

    /**
     * this class is used to get contacts on a separate thread.
     */
    private class FetchContactsAndAddToDB extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER};

            Cursor people = getContentResolver().query(uri, projection, null, null, null);

            int indexName = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int indexNumber = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

            if (people.moveToFirst()) {
                do {
                    Contact contact = new Contact();
                    contact.name = people.getString(indexName);
                    contact.number = people.getString(indexNumber);
                    contact.save();
                } while (people.moveToNext());
            }
            preferences.putBoolean(getString(R.string.p_contacts_loaded), true);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            launchMainActivity();
        }
    }
}
