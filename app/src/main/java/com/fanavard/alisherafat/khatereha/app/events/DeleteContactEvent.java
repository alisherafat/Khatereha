package com.fanavard.alisherafat.khatereha.app.events;

import com.fanavard.alisherafat.khatereha.app.models.Contact;

public class DeleteContactEvent {
    public Contact contact;

    public DeleteContactEvent(Contact contact) {
        this.contact = contact;
    }
}
