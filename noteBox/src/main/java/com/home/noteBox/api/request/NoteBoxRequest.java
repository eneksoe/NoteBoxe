package com.home.noteBox.api.request;

import lombok.Data;

@Data
public class NoteBoxRequest {
    private String data;
    private long expirationTimeSeconds;
    private PublicStatus publicStatus;

}
