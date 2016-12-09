package com.fanavard.alisherafat.khatereha.memories.single;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.activeandroid.Model;
import com.fanavard.alisherafat.khatereha.R;
import com.fanavard.alisherafat.khatereha.app.models.Item;
import com.fanavard.alisherafat.khatereha.app.models.Memory;
import com.fanavard.alisherafat.khatereha.memories.AddEditMemoryActivity;
import com.fanavard.alisherafat.khatereha.memories.MemoryItemAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MemoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MemoryFragment extends Fragment {
    @BindView(R.id.txtTitle) TextView txtTitle;
    @BindView(R.id.txtDate) TextView txtDate;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    private Memory memory;
    private List<Item> items;
    private MemoryItemAdapter adapter;

    public MemoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MemoryFragment.
     */
    public static MemoryFragment newInstance(long memoryId) {
        MemoryFragment fragment = new MemoryFragment();
        Bundle args = new Bundle();
        args.putLong("memory_id", memoryId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            long id = getArguments().getLong("memory_id");
            memory = Model.load(Memory.class, id);
            items = memory.getItems();
            adapter = new MemoryItemAdapter(getContext(), items,false);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_memory,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.edit) {
            Intent intent = new Intent(getContext(), AddEditMemoryActivity.class);
            intent.putExtra(AddEditMemoryActivity.TYPE, AddEditMemoryActivity.TYPE_EDIT);
            intent.putExtra(AddEditMemoryActivity.CONTACT_ID, memory.contact.getId());
            intent.putExtra(AddEditMemoryActivity.MEMORY_ID, memory.getId());
            startActivity(intent);
            getActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_memory, container, false);
        ButterKnife.bind(this, view);
        txtTitle.setText(memory.title);
        txtDate.setText(memory.getDate());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // enable up scroll that makes it like timeline
        if (adapter.getItemCount() > 1) {
            recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, adapter.getItemCount() - 1);
        }
        return view;
    }

}
