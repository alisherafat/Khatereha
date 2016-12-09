package com.fanavard.alisherafat.khatereha.contacts;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fanavard.alisherafat.khatereha.R;
import com.fanavard.alisherafat.khatereha.app.events.AddContactEvent;
import com.fanavard.alisherafat.khatereha.app.events.DeleteContactEvent;
import com.fanavard.alisherafat.khatereha.app.events.EditContactEvent;
import com.fanavard.alisherafat.khatereha.app.interfaces.Loggable;
import com.fanavard.alisherafat.khatereha.app.models.Contact;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactsFragment extends Fragment implements Loggable {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private ContactAdapter adapter;
    private List<Contact> items;

    public ContactsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ContactsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactsFragment newInstance() {
        ContactsFragment fragment = new ContactsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_contacts, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_contact) {
            Intent intent = new Intent(getContext(), AddEditContactActivity.class);
            intent.putExtra(AddEditContactActivity.TYPE, AddEditContactActivity.TYPE_ADD);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        items = Contact.getAll();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ContactAdapter(getContext(), items);
        recyclerView.setAdapter(adapter);
        if (items.size() < 1) {
            Toast.makeText(getContext(), "there is no contact,try add to add", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void log(String m) {
        Log.d("app", m);
    }

    @Subscribe
    public void onNewContact(AddContactEvent contactEvent) {
        items.add(contactEvent.contact);
        adapter.notifyItemInserted(items.size() - 1);
    }

    @Subscribe
    public void onEditContact(EditContactEvent contactEvent) {
        int index = items.indexOf(contactEvent.contact);
        adapter.notifyItemChanged(index);
    }

    @Subscribe
    public void onDeleteContact(DeleteContactEvent contactEvent) {
        int index = items.indexOf(contactEvent.contact);
        items.remove(contactEvent.contact);
        adapter.notifyItemRemoved(index);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
