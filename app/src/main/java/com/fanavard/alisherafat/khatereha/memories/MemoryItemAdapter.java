package com.fanavard.alisherafat.khatereha.memories;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanavard.alisherafat.khatereha.R;
import com.fanavard.alisherafat.khatereha.app.configs.FileConfigs;
import com.fanavard.alisherafat.khatereha.app.custom.AudioPlayer;
import com.fanavard.alisherafat.khatereha.app.models.Item;

import java.io.File;
import java.util.List;

public class MemoryItemAdapter extends RecyclerView.Adapter<MemoryItemAdapter.ViewHolder> {
    private final int TYPE_TEXT = 1, TYPE_IMAGE = 2, TYPE_AUDIO = 3;
    private List<Item> items;
    private Context context;
    private MediaPlayer mediaPlayer;
    private String currentPlayingName = "";
    private AudioPlayer currenAudioPlayer;
    private boolean edit;

    public MemoryItemAdapter(Context context, List<Item> items, boolean edit) {
        this.context = context;
        this.items = items;
        this.edit = edit;
        mediaPlayer = new MediaPlayer();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_TEXT:
                return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_memory_text, parent, false), viewType);
            case TYPE_IMAGE:
                return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_memory_image, parent, false), viewType);
            case TYPE_AUDIO:
                return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_memory_audio, parent, false), viewType);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_TEXT:
                handleTextItem(viewHolder, position);
                break;
            case TYPE_IMAGE:
                handleImageItem(viewHolder, position);
                break;
            case TYPE_AUDIO:
                handleAudioItem(viewHolder, position);
                break;
        }
        if (edit) {
            viewHolder.btnDelete.setVisibility(View.VISIBLE);
        }
    }


    private void handleAudioItem(ViewHolder viewHolder, int position) {
    }

    private void handleTextItem(ViewHolder viewHolder, int position) {
        Item item = items.get(position);
        viewHolder.txtItem.setText(item.path);
    }

    private void handleImageItem(final ViewHolder viewHolder, int position) {

        Item item = items.get(position);
        if (item.isImageReady()) {
            File file = new File(FileConfigs.DIR_MEMORY_IMAGES + "/" + item.path);
            if (file.exists()) {
                viewHolder.imgItem.setImageURI(Uri.fromFile(file));
            }
        }
    }

    private void deleteItem(final int position) {
        Item item = items.get(position);
        if (item.getId() != null) {
            item.delete();
        }
        Log.d("app", item.getId() + " removed");
        items.remove(position);
        notifyItemRemoved(position);
    }

    public void releaseMediaPlayer() {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                // mediaPlayer.release();
                mediaPlayer.reset();
                mediaPlayer = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).type;
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View view;
        private ImageView imgItem, imgMenu;
        private TextView txtItem;
        private AudioPlayer audioPlayer;
        private Button btnEdit, btnDelete;

        public ViewHolder(View view, int viewType) {
            super(view);
            this.view = view;
            switch (viewType) {
                case TYPE_TEXT:
                    txtItem = (TextView) view.findViewById(R.id.txtItem);
                    break;
                case TYPE_IMAGE:
                    imgItem = (ImageView) view.findViewById(R.id.imgItem);
                    break;
                case 3:
                    audioPlayer = (AudioPlayer) view.findViewById(R.id.audioStreamer);
                    audioPlayer.setOnTogglePlayClickListener(this);
                    break;
            }
            if (edit) {
                btnDelete = (Button) view.findViewById(R.id.btnDelete);
                btnDelete.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Item item = items.get(position);
            switch (view.getId()) {
                case R.id.btnDelete:
                    deleteItem(position);
                    break;
                case R.id.myAudioPlayerFabTogglePlay: {
                    if (item.path.equals(currentPlayingName)) {
                        audioPlayer.togglePlayPause();
                        return;
                    }
                    audioPlayer.attachMediaPlayer(mediaPlayer);
                    try {
                        if (currenAudioPlayer != null && mediaPlayer.isPlaying()) {
                            currenAudioPlayer.togglePlayPause();
                        }
                        audioPlayer.setLocalPath(FileConfigs.DIR_AUDIOS + "/" + item.path);
                        currentPlayingName = item.path;
                        currenAudioPlayer = audioPlayer;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
        }
    }

}