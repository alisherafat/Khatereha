package com.fanavard.alisherafat.khatereha.contacts;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanavard.alisherafat.khatereha.R;
import com.fanavard.alisherafat.khatereha.app.configs.FileConfigs;
import com.fanavard.alisherafat.khatereha.app.models.Contact;
import com.fanavard.alisherafat.khatereha.profile.ProfileActivity;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private List<Contact> items;
    private Context context;

    public ContactAdapter(Context context, List<Contact> items) {
        this.context = context;
        this.items = items;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        Contact item = items.get(position);
        vh.txtName.setText(item.name);
        vh.txtNumber.setText(item.number);
        if (item.hasImage()) {
            vh.img.setImageURI(Uri.fromFile(new File(FileConfigs.DIR_CONTACT_IMAGES + "/" + item.imgUri)));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View view;
        @BindView(R.id.cardView) CardView cardView;
        @BindView(R.id.img) ImageView img;
        @BindView(R.id.txtName) TextView txtName;
        @BindView(R.id.txtNumber) TextView txtNumber;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, ProfileActivity.class);
            intent.putExtra(ProfileActivity.CONTACT_ID, items.get(getAdapterPosition()).getId());
            context.startActivity(intent);
            Log.d("app","id:" + items.get(getAdapterPosition()).getId());
        }
    }

}