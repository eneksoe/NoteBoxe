package com.home.noteBox.controller;

import com.home.noteBox.api.request.NoteBoxRequest;
import com.home.noteBox.api.response.NoteBoxResponse;
import com.home.noteBox.api.response.NoteBoxUrlResponse;
import com.home.noteBox.service.NoteBoxService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
public class NoteBoxController {

    private final NoteBoxService service;

    @GetMapping("/{hash}")
    public NoteBoxResponse getByHash(@PathVariable String hash) {
        return service.getByHash(hash);
    }

    @GetMapping("/")
    public Collection<NoteBoxResponse> getPublicPasteList() {
        return service.getFirstPublicNote();
    }

    @PostMapping("/")
    public NoteBoxUrlResponse add(@RequestBody NoteBoxRequest request){
       return service.create(request);
    }
}
