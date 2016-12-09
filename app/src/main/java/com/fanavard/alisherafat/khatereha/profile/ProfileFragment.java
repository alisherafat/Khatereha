package com.fanavard.alisherafat.khatereha.profile;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.Model;
import com.fanavard.alisherafat.khatereha.R;
import com.fanavard.alisherafat.khatereha.app.configs.FileConfigs;
import com.fanavard.alisherafat.khatereha.app.events.AddMemoryEvent;
import com.fanavard.alisherafat.khatereha.app.events.DeleteContactEvent;
import com.fanavard.alisherafat.khatereha.app.events.EditContactEvent;
import com.fanavard.alisherafat.khatereha.app.interfaces.Loggable;
import com.fanavard.alisherafat.khatereha.app.models.Contact;
import com.fanavard.alisherafat.khatereha.contacts.AddEditContactActivity;
import com.fanavard.alisherafat.khatereha.memories.AddEditMemoryActivity;
import com.fanavard.alisherafat.khatereha.memories.MemoriesActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements Loggable {
    @BindView(R.id.img) ImageView img;
    @BindView(R.id.txtName) TextView txtName;
    @BindView(R.id.txtNumber) TextView txtNumber;
    @BindView(R.id.txtMemCount) TextView txtMemCount;
    private Contact contact;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(long contactId) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putLong("contact_id", contactId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        if (getArguments() != null) {
            long id = getArguments().getLong("contact_id");
            contact = Model.load(Contact.class, id);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        displayData();
        return view;
    }

    private void displayData() {
        txtName.setText(contact.name);
        txtNumber.setText(contact.number);
        txtMemCount.setText("Memories Count: " + contact.getMemoryCount());
        if (contact.hasImage()) {
            img.setImageURI(Uri.fromFile(new File(FileConfigs.DIR_CONTACT_IMAGES + "/" + contact.imgUri)));
        }
    }

    @OnClick({R.id.btnDelete, R.id.btnEdit, R.id.btnAddMem, R.id.btnShowMem})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnDelete:
                EventBus.getDefault().post(new DeleteContactEvent(contact));
                contact.delete();
                Toast.makeText(getContext(), "Contact removed!", Toast.LENGTH_SHORT).show();
                getActivity().finish();
                break;
            case R.id.btnEdit: {
                Intent intent = new Intent(getContext(), AddEditContactActivity.class);
                intent.putExtra(AddEditContactActivity.TYPE, AddEditContactActivity.TYPE_EDIT);
                intent.putExtra(AddEditContactActivity.CONTACT_ID, contact.getId());
                startActivity(intent);
                break;
            }
            case R.id.btnAddMem: {
                Intent intent = new Intent(getContext(), AddEditMemoryActivity.class);
                intent.putExtra(AddEditMemoryActivity.TYPE, AddEditMemoryActivity.TYPE_ADD);
                intent.putExtra(AddEditMemoryActivity.CONTACT_ID, contact.getId());
                startActivity(intent);
                break;
            }
            case R.id.btnShowMem: {
                if (contact.getMemoryCount() < 1) {
                    Toast.makeText(getContext(), "There is no item to see", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(getContext(), MemoriesActivity.class);
                intent.putExtra(MemoriesActivity.CONATCT_ID, contact.getId());
                startActivity(intent);
                break;
            }
        }
    }

    @Subscribe
    public void onEditContact(EditContactEvent contactEvent) {
        contact = contactEvent.contact;
        displayData();
    }

    @Subscribe
    public void onNewMem(AddMemoryEvent memoryEvent) {
        displayData();
    }

    @Override
    public void log(String m) {
        Log.d("app", m);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
