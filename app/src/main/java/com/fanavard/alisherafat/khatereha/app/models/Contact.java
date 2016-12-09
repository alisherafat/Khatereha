package com.fanavard.alisherafat.khatereha.app.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.io.Serializable;
import java.util.List;

/**
 * Model Layer which represent Contact<br/>
 * extended from @{@link Model} in order to use ORM
 */
@Table(name = "contacts")
public class Contact extends Model implements Serializable {
    @Column(name = "name", index = true)
    public String name;

    @Column(name = "img_uri")
    public String imgUri;

    @Column(name = "number")
    public String number;

    public boolean hasImage() {
        if (imgUri == null || imgUri.isEmpty()) {
            return false;
        }
        return true;
    }

    public List<Memory> getMemories() {
        return new Select().from(Memory.class).where("contact_id=?", getId()).orderBy("timestamp asc").execute();
    }

    public int getMemoryCount() {
        return new Select().from(Memory.class).where("contact_id=?", getId()).count();
    }

    public static List<Contact> getAll() {
        return new Select().from(Contact.class).execute();
    }
}
