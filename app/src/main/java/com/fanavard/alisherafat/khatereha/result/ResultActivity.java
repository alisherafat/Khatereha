package com.fanavard.alisherafat.khatereha.result;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.fanavard.alisherafat.khatereha.R;

/**
 * This activity is used to Host @{@link RecordFragment} and @{@link AddTextFragment}
 */
public class ResultActivity extends AppCompatActivity implements RecordFragment.OnRecordFinishListener,
        AddTextFragment.OnSubmitTextListener {
    public static final String TYPE = "action";
    public static final String DEFAULT_TEXT = "default_text";
    public static final int RECORD_VOICE = 1;
    public static final int GET_TEXT = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        switch (getIntent().getExtras().getInt(TYPE)) {
            case RECORD_VOICE:
                Fragment recordFragment = new RecordFragment();
                startFragment(recordFragment);
                setTitle("Record Voice");
                break;
            case GET_TEXT: {
                Fragment fragment = AddTextFragment.newInstance(getIntent().getStringExtra(DEFAULT_TEXT));
                startFragment(fragment);
                setTitle("Add Text");
                break;
            }
        }
    }

    private void startFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
    }

    @Override
    public void onRecordFinish(String path) {
        Intent intent = new Intent();
        intent.putExtra("key", path);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onTextSubmit(String text) {
        Intent intent = new Intent();
        intent.putExtra("key", text);
        setResult(RESULT_OK, intent);
        finish();
    }

}
