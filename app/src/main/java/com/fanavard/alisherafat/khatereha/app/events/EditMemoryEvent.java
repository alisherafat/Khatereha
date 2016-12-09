package com.fanavard.alisherafat.khatereha.app.events;

import com.fanavard.alisherafat.khatereha.app.models.Memory;

public class EditMemoryEvent {
    public Memory memory;

    public EditMemoryEvent(Memory memory) {
        this.memory = memory;
    }
}
