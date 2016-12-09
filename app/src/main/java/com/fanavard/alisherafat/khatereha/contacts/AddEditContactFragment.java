package com.fanavard.alisherafat.khatereha.contacts;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.activeandroid.Model;
import com.fanavard.alisherafat.khatereha.R;
import com.fanavard.alisherafat.khatereha.app.configs.FileConfigs;
import com.fanavard.alisherafat.khatereha.app.events.AddContactEvent;
import com.fanavard.alisherafat.khatereha.app.events.EditContactEvent;
import com.fanavard.alisherafat.khatereha.app.models.Contact;
import com.fanavard.alisherafat.khatereha.app.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddEditContactFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@RuntimePermissions
public class AddEditContactFragment extends Fragment {

    private final int REQUEST_TAKE_IMAGE = 1651;
    @BindView(R.id.img) ImageView img;
    @BindView(R.id.edtName) EditText edtName;
    @BindView(R.id.edtNumber) EditText edtNumber;

    private Utils utils;
    private Contact contact;
    private boolean add;

    public AddEditContactFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddEditContactFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddEditContactFragment newInstance(long contactId, boolean add) {
        AddEditContactFragment fragment = new AddEditContactFragment();
        Bundle args = new Bundle();
        args.putLong("contact_id", contactId);
        args.putBoolean("add", add);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        utils = Utils.getInstance(getContext());
        if (getArguments() != null) {
            add = getArguments().getBoolean("add");
            if (add) {
                contact = new Contact();
            } else {
                long id = getArguments().getLong("contact_id");
                contact = Model.load(Contact.class, id);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AddEditContactFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }


    @NeedsPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void copyFile(File sourceFile, File destFile,String fileName) {
        destFile.getParentFile().mkdirs();
        try {
            utils.copyFile(sourceFile, destFile);
            contact.imgUri = fileName;
            img.setImageURI(Uri.fromFile(destFile));
        } catch (IOException e) {
            e.printStackTrace();
            utils.toast("something went wrong. image not saved");
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_edit_contact, container, false);
        ButterKnife.bind(this, view);

        if (!add) {
            edtName.setText(contact.name);
            edtNumber.setText(contact.number);
        }
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != getActivity().RESULT_OK || data == null) {
            // there is no data to retrieve in this case
            return;
        }
        if (requestCode == REQUEST_TAKE_IMAGE) {
            try {
                String fileName = utils.getTimestampedName() + ".png";

                AddEditContactFragmentPermissionsDispatcher.copyFileWithCheck(
                        AddEditContactFragment.this,
                        new File(utils.getRealPathFromURI(data.getData())),
                        new File(FileConfigs.DIR_CONTACT_IMAGES + "/" + fileName)
                        , fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick(R.id.btnSave)
    public void onClick() {
        String name = edtName.getText().toString();
        String number = edtNumber.getText().toString();
        if (isValid(name, number)) {
            contact.name = name;
            contact.number = number;
            contact.save();
            utils.toast("saved");
            if (add) {
                EventBus.getDefault().post(new AddContactEvent(contact));
            } else {
                EventBus.getDefault().post(new EditContactEvent(contact));
            }
            getActivity().finish();
        }
    }

    @OnClick(R.id.btnSetImage)
    public void onSetImageClick() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,REQUEST_TAKE_IMAGE);
    }

    private boolean isValid(String name, String number) {

        if (name.isEmpty()) {
            edtName.setError("this field should not be empty");
            return false;
        }

        if (number.isEmpty()) {
            edtNumber.setError("this field should not be empty");
            return false;
        }
        return true;
    }


}
