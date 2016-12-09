package com.fanavard.alisherafat.khatereha.memories;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.activeandroid.Model;
import com.fanavard.alisherafat.khatereha.R;
import com.fanavard.alisherafat.khatereha.app.events.EditMemoryEvent;
import com.fanavard.alisherafat.khatereha.app.models.Contact;
import com.fanavard.alisherafat.khatereha.app.models.Memory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MemoryListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MemoryListFragment extends Fragment {
    private Contact contact;
    private List<Memory> items;
    private MemoryListAdapter adapter;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    public MemoryListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MemoryListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MemoryListFragment newInstance(long contactId) {
        MemoryListFragment fragment = new MemoryListFragment();
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
            items = contact.getMemories();
            adapter = new MemoryListAdapter(getContext(), items);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_memory_list, container, false);
        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Subscribe
    public void onEditMemory(EditMemoryEvent memoryEvent) {
        int index = items.indexOf(memoryEvent.memory);
        adapter.notifyItemChanged(index);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
