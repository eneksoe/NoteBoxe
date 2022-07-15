package com.home.noteBox.repository;

import com.home.noteBox.model.NoteBox;

import java.util.List;

public interface NoteBoxRepository {
    NoteBox getByHash(String hash);
    List<NoteBox> getListOfPublicAndAlive(int amount);
    void add(NoteBox entity);
}
