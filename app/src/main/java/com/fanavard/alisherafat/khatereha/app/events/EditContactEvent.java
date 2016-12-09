package com.fanavard.alisherafat.khatereha.app.events;

import com.fanavard.alisherafat.khatereha.app.models.Contact;

public class EditContactEvent {
    public Contact contact;

    public EditContactEvent(Contact contact) {
        this.contact = contact;
    }
}
