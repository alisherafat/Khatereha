package com.fanavard.alisherafat.khatereha.memories;


import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import com.activeandroid.Model;
import com.afollestad.materialdialogs.MaterialDialog;
import com.fanavard.alisherafat.khatereha.R;
import com.fanavard.alisherafat.khatereha.app.configs.FileConfigs;
import com.fanavard.alisherafat.khatereha.app.events.AddMemoryEvent;
import com.fanavard.alisherafat.khatereha.app.events.EditMemoryEvent;
import com.fanavard.alisherafat.khatereha.app.models.Contact;
import com.fanavard.alisherafat.khatereha.app.models.Item;
import com.fanavard.alisherafat.khatereha.app.models.Memory;
import com.fanavard.alisherafat.khatereha.app.utils.Utils;
import com.fanavard.alisherafat.khatereha.result.ResultActivity;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddEditMemoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@RuntimePermissions
public class AddEditMemoryFragment extends Fragment {
    private final int REQUEST_TAKE_TEXT = 4654;
    private final int REQUEST_RECORD_AUDIO = 1235;
    private final int REQUEST_TAKE_AUDIO = 1368;
    private final int REQUEST_RECORD_VIDEO = 9969;
    private final int REQUEST_TAKE_VIDEO = 2963;
    private final int REQUEST_CAPTURE_IMAGE = 1823;
    private final int REQUEST_TAKE_IMAGE = 1023;

    @BindView(R.id.edtTitle) EditText edtTitle;
    @BindView(R.id.edtDate) EditText edtDate;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    private MemoryItemAdapter adapter;
    private Memory memory;
    private List<Item> items;
    private boolean add;
    private Contact contact;
    private Calendar myCalendar = Calendar.getInstance();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private Utils utils;

    public AddEditMemoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AddEditMemoryFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddEditMemoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddEditMemoryFragment newInstance(long memoryId, long contactId, boolean add) {
        AddEditMemoryFragment fragment = new AddEditMemoryFragment();
        Bundle args = new Bundle();
        args.putLong("memory_id", memoryId);
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
            long contactId = getArguments().getLong("contact_id");
            contact = Model.load(Contact.class, contactId);
            if (add) {
                memory = new Memory();
            } else {
                long id = getArguments().getLong("memory_id");
                memory = Model.load(Memory.class, id);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_edit_memory, container, false);
        ButterKnife.bind(this, view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        handleFragmentMode();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new MemoryItemAdapter(getContext(), items, true);
        recyclerView.setAdapter(adapter);

    }

    private void handleFragmentMode() {
        if (add) {
            items = new ArrayList<>();
        } else {
            items = memory.getItems();
            edtTitle.setText(memory.title);
            edtDate.setText(memory.getDate());
        }
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
                Item item = new Item();
                item.type = Item.TYPE_IMAGE;
                item.path = utils.getTimestampedName() + ".png";
                AddEditMemoryFragmentPermissionsDispatcher.copyFileWithCheck(
                        AddEditMemoryFragment.this,
                        new File(utils.getRealPathFromURI(data.getData())),
                        new File(FileConfigs.DIR_MEMORY_IMAGES + "/" + item.path)
                        , item);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (requestCode == REQUEST_RECORD_AUDIO) {
            Item item = new Item();
            item.type = Item.TYPE_AUDIO;
            String path = data.getStringExtra("key");
            if (path == null) {
                utils.toast("something went wrong!");
                return;
            }
            item.path = path;
            addItem(item);
        }

        if (requestCode == REQUEST_TAKE_TEXT) {
            String body = data.getStringExtra("key");
            Item item = new Item();
            item.type = Item.TYPE_TEXT;
            item.path = body;
            addItem(item);
        }

        if (requestCode == REQUEST_CAPTURE_IMAGE) {
            String fileName = utils.getTimestampedName() + ".png";
            File file = new File(FileConfigs.DIR_MEMORY_IMAGES + "/" + fileName);
            file.getParentFile().mkdirs();
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            try {
                OutputStream fOut = new FileOutputStream(file);
                photo.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
                fOut.flush(); // Not really required
                fOut.close(); // do not forget to close the stream
                Item item = new Item();
                item.type = Item.TYPE_IMAGE;
                item.path = fileName;
                addItem(item);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @NeedsPermission(
            {android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE})
    public void copyFile(File sourceFile, File destFile, Item item) {
        destFile.getParentFile().mkdirs();
        try {
            utils.copyFile(sourceFile, destFile);
            addItem(item);
        } catch (IOException e) {
            e.printStackTrace();
            utils.toast("something went wrong");
        }
    }

    private void addItem(Item item) {
        items.add(item);
        utils.log("item added successfully: " + item.path);
        adapter.notifyDataSetChanged();
        if (adapter.getItemCount() > 1) {
            recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, adapter.getItemCount() - 1);
        }
    }

    @OnClick(R.id.edtDate)
    public void onClick() {
        new DatePickerDialog(getContext(), onDateSetListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @OnClick({R.id.btnImage, R.id.btnAudio, R.id.btnVideo, R.id.btnSave, R.id.btnText})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnImage: {
                new MaterialDialog.Builder(getContext())
                        .items(new String[]{"From Gallery", "From camera"})
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                if (which == 0) {
                                    Intent intent = new Intent();
                                    intent.setType("image/*");
                                    intent.setAction(Intent.ACTION_GET_CONTENT);
                                    startActivityForResult(intent, REQUEST_TAKE_IMAGE);
                                } else {
                                    AddEditMemoryFragmentPermissionsDispatcher.captureImageWithCheck(AddEditMemoryFragment.this);
                                }
                            }
                        })
                        .show();
                break;
            }
            case R.id.btnAudio: {
                Intent intent = new Intent(getContext(), ResultActivity.class);
                intent.putExtra(ResultActivity.TYPE, ResultActivity.RECORD_VOICE);
                startActivityForResult(intent, REQUEST_RECORD_AUDIO);
                break;
            }
            case R.id.btnText: {
                Intent intent = new Intent(getContext(), ResultActivity.class);
                intent.putExtra(ResultActivity.TYPE, ResultActivity.GET_TEXT);
                intent.putExtra(ResultActivity.DEFAULT_TEXT, "");
                startActivityForResult(intent, REQUEST_TAKE_TEXT);
                break;
            }
            case R.id.btnVideo: {
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Video"), REQUEST_TAKE_VIDEO);
                break;
            }
            case R.id.btnSave:
                save();
                break;
        }
    }

    @NeedsPermission({Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void captureImage() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAPTURE_IMAGE);
    }

    private void save() {
        String title = edtTitle.getText().toString();
        String date = edtDate.getText().toString();
        if (!isValid(title, date)) {
            return;
        }
        memory.title = title;
        memory.setDateFromString(date);
        memory.contact = contact;
        memory.save();

        for (Item item : items) {
            item.memory = memory;
            item.save();
        }
        utils.toast("Successfully saved!");
        if (add) {
            EventBus.getDefault().post(new AddMemoryEvent(memory));
        } else {
            EventBus.getDefault().post(new EditMemoryEvent(memory));
        }
        getActivity().finish();
    }

    private boolean isValid(String title, String date) {
        if (date.isEmpty()) {
            edtDate.setError("this field is required");
            return false;
        }
        if (title.isEmpty()) {
            edtTitle.setError("this field is required");
            return false;
        }

        if (!date.contains("-")) {
            edtDate.setError("Bad format!");
            return false;
        }
        if (items.size() < 1) {
            utils.toast("at least one item is required");
            return false;
        }
        return true;
    }

    DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            edtDate.setText(sdf.format(myCalendar.getTime()));
        }
    };

}
