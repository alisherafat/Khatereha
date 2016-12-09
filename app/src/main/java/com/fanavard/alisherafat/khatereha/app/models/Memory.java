package com.fanavard.alisherafat.khatereha.app.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Model Layer which represent Memory Entity<br/>
 * extended from @{@link Model} in order to use ORM
 */

@Table(name = "memories")
public class Memory extends Model implements Serializable {

    public static SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");


    @Column(name = "title")
    public String title;

    @Column(name = "contact_id")
    public Contact contact;

    @Column(name = "timestamp", index = true)
    private Date date;

    public String getDate() {
        return dateFormater.format(this.date);
    }

    public void setDateFromString(String date) {
        try {
            this.date = dateFormater.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            this.date = new Date();
        }
    }

    public List<Item> getItems() {
        return new Select().from(Item.class).where("memory_id=?", getId()).execute();
    }
}
