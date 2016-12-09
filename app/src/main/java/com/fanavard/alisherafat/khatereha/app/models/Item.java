package com.fanavard.alisherafat.khatereha.app.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;
/**
 * Model Layer which represent Item in a memory<br/>
 * extended from @{@link Model} in order to use ORM
 */
@Table(name = "memory_items")
public class Item extends Model implements Serializable {
    public static final short TYPE_TEXT = 1;
    public static final short TYPE_IMAGE = 2;
    public static final short TYPE_AUDIO = 3;
    public static final short TYPE_VIDEO = 4;

    @Column(name = "type")
    public int type;

    @Column(name = "memory_id")
    public Memory memory;

    @Column(name = "path")
    public String path;


    public boolean isImageReady() {
        if (type != TYPE_IMAGE && path == null && path.isEmpty()) {
            return false;
        }
        return true;
    }
}