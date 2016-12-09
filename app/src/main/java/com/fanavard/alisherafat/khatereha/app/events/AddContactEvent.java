package com.fanavard.alisherafat.khatereha.app.events;

import com.fanavard.alisherafat.khatereha.app.models.Contact;

public class AddContactEvent {
    public Contact contact;

    public AddContactEvent(Contact contact) {
        this.contact = contact;
    }
}
