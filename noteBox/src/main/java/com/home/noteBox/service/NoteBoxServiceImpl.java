package com.home.noteBox.service;

import com.home.noteBox.api.request.NoteBoxRequest;
import com.home.noteBox.api.request.PublicStatus;
import com.home.noteBox.api.response.NoteBoxResponse;
import com.home.noteBox.api.response.NoteBoxUrlResponse;
import com.home.noteBox.model.NoteBox;
import com.home.noteBox.repository.NoteBoxRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteBoxServiceImpl implements NoteBoxService {

    private String host = "https://abc.com";
    private int publicListSize = 10;
    @Autowired
    private final NoteBoxRepositoryImpl repository;
    private final AtomicInteger idGenerator = new AtomicInteger(0);

    @Override
    public NoteBoxResponse getByHash(String hash) {
        NoteBox entity = repository.getByHash(hash);

        return new NoteBoxResponse(entity.getData(), entity.isPublic());
    }

    @Override
    public List<NoteBoxResponse> getFirstPublicNote() {
        List<NoteBox> list = repository.getListOfPublicAndAlive(publicListSize);
        return list.stream().map(entity ->
                        new NoteBoxResponse(entity.getData(), entity.isPublic()))
                .collect(Collectors.toList());
    }

    @Override
    public NoteBoxUrlResponse create(NoteBoxRequest request) {
        int hash = generateId();
        NoteBox entity = new NoteBox();
        entity.setData(request.getData());
        entity.setId(hash);
        entity.setHash(Integer.toHexString(hash));
        entity.setPublic(request.getPublicStatus() == PublicStatus.PUBLIC);
        entity.setLifeTime(LocalDateTime.now().plusSeconds(request.getExpirationTimeSeconds()));
        repository.add(entity);
        return new NoteBoxUrlResponse(host + "/" + entity.getHash());
    }

    private int generateId() {
        return idGenerator.getAndIncrement();
    }
}
