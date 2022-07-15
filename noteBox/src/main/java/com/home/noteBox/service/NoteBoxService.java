package com.home.noteBox.service;

import com.home.noteBox.api.request.NoteBoxRequest;
import com.home.noteBox.api.response.NoteBoxResponse;
import com.home.noteBox.api.response.NoteBoxUrlResponse;

import java.util.List;

public interface NoteBoxService {
    NoteBoxResponse getByHash(String hash);
    List<NoteBoxResponse> getFirstPublicNote();
    NoteBoxUrlResponse create(NoteBoxRequest request);
}
