package com.fanavard.alisherafat.khatereha.app.events;

import com.fanavard.alisherafat.khatereha.app.models.Memory;

public class AddMemoryEvent {
    public Memory memory;

    public AddMemoryEvent(Memory memory) {
        this.memory = memory;
    }
}
