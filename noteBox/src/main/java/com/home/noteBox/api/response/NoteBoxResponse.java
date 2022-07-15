package com.home.noteBox.api.response;

import com.home.noteBox.api.request.PublicStatus;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class NoteBoxResponse {
    private final String data;
    private final boolean isPublic;
}
