package com.fanavard.alisherafat.khatereha.memories;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanavard.alisherafat.khatereha.R;
import com.fanavard.alisherafat.khatereha.app.models.Memory;
import com.fanavard.alisherafat.khatereha.memories.single.MemoryActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MemoryListAdapter extends RecyclerView.Adapter<MemoryListAdapter.ViewHolder> {
    private List<Memory> items;
    private Context context;

    public MemoryListAdapter(Context context, List<Memory> items) {
        this.context = context;
        this.items = items;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_memory, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Memory item = items.get(position);
        holder.txtTitle.setText(item.title);
        holder.txtDate.setText(item.getDate());
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View view;
        @BindView(R.id.txtTitle) TextView txtTitle;
        @BindView(R.id.txtDate) TextView txtDate;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, MemoryActivity.class);
            intent.putExtra(MemoryActivity.MEMORY_ID, items.get(getAdapterPosition()).getId());
            context.startActivity(intent);
        }
    }

}