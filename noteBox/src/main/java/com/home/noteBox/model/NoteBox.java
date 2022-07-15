package com.home.noteBox.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NoteBox {
    private int id;
    private String data;
    private String hash;
    private LocalDateTime lifeTime;
    private boolean isPublic;
}
