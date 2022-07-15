package com.home.noteBox.repository;

import com.home.noteBox.exception.NotFoundEntityException;
import com.home.noteBox.model.NoteBox;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class NoteBoxRepositoryImpl implements NoteBoxRepository {

    private final Map<String, NoteBox> vault = new ConcurrentHashMap<>();

    @Override
    public NoteBox getByHash(String hash) {
        NoteBox entity = vault.get(hash);
        if (entity == null) {
            throw new NotFoundEntityException("NoteBox not found with hash = " + hash);
        }
        return entity;
    }

    @Override
    public List<NoteBox> getListOfPublicAndAlive(int amount) {
        LocalDateTime now = LocalDateTime.now();

        return vault.values().stream()
                .filter(NoteBox::isPublic)
                .filter(noteBox -> noteBox.getLifeTime().isAfter(now))
                .sorted(Comparator.comparing(NoteBox::getId).reversed())
                .limit(amount)
                .collect(Collectors.toList());
    }

    @Override
    public void add(NoteBox entity) {
        vault.put(entity.getHash(), entity);
    }
}
