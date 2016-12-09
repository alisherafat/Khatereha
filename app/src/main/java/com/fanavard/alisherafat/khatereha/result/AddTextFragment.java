package com.fanavard.alisherafat.khatereha.result;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.fanavard.alisherafat.khatereha.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * to get Text from user to show in memory items
 */
public class AddTextFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    @BindView(R.id.edtBody) EditText edtBody;
    @BindView(R.id.imgSubmit) Button imgSubmit;
    private String dafaultText;

    private OnSubmitTextListener mListener;

    public AddTextFragment() {
        // Required empty public constructor
    }

    public static AddTextFragment newInstance(String param1) {
        AddTextFragment fragment = new AddTextFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            dafaultText = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_text, container, false);
        ButterKnife.bind(this, view);
        edtBody.setText(dafaultText);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSubmitTextListener) {
            mListener = (OnSubmitTextListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @OnClick({R.id.edtBody, R.id.imgSubmit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edtBody:
                break;
            case R.id.imgSubmit:
                String body = edtBody.getText().toString().trim();
                if (body.length() < 1) return;
                if (mListener != null) {
                    mListener.onTextSubmit(body);
                }
                break;
        }
    }

    public interface OnSubmitTextListener {
        void onTextSubmit(String text);
    }
}
