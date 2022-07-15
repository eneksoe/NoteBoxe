package com.home.noteBox.service;

import com.home.noteBox.api.request.NoteBoxRequest;
import com.home.noteBox.api.request.PublicStatus;
import com.home.noteBox.api.response.NoteBoxResponse;
import com.home.noteBox.exception.NotFoundEntityException;
import com.home.noteBox.model.NoteBox;
import com.home.noteBox.repository.NoteBoxRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class NoteBoxServiceImplTest {

    @MockBean
    private NoteBoxRepositoryImpl noteBoxRepositoryImpl;

    @Autowired
    private NoteBoxServiceImpl noteBoxServiceImpl;

    @Test
    public void notExistHash() {
        when(noteBoxRepositoryImpl.getByHash(anyString())).thenThrow(NotFoundEntityException.class);
        assertThrows(NotFoundEntityException.class, () -> noteBoxServiceImpl.getByHash("sdfhfhg"));
    }

    @Test
    void testGetFirstPublicNote() {
        when(noteBoxRepositoryImpl.getListOfPublicAndAlive(anyInt())).thenReturn(new ArrayList<>());
        assertTrue(noteBoxServiceImpl.getFirstPublicNote().isEmpty());
        verify(noteBoxRepositoryImpl).getListOfPublicAndAlive(anyInt());
    }

    @Test
    void testGetFirstPublicNote2() {
        NoteBox noteBox = new NoteBox();
        noteBox.setData("Data");
        noteBox.setHash("Hash");
        noteBox.setId(1);
        noteBox.setLifeTime(LocalDateTime.of(1, 1, 1, 1, 1));
        noteBox.setPublic(true);

        ArrayList<NoteBox> noteBoxList = new ArrayList<>();
        noteBoxList.add(noteBox);
        when(noteBoxRepositoryImpl.getListOfPublicAndAlive(anyInt())).thenReturn(noteBoxList);
        List<NoteBoxResponse> actualFirstPublicNote = noteBoxServiceImpl.getFirstPublicNote();
        assertEquals(1, actualFirstPublicNote.size());
        NoteBoxResponse getResult = actualFirstPublicNote.get(0);
        assertEquals("Data", getResult.getData());
        assertTrue(getResult.isPublic());
        verify(noteBoxRepositoryImpl).getListOfPublicAndAlive(anyInt());
    }

    @Test
    void testGetFirstPublicNote3() {
        NoteBox noteBox = new NoteBox();
        noteBox.setData("Data");
        noteBox.setHash("Hash");
        noteBox.setId(1);
        noteBox.setLifeTime(LocalDateTime.of(1, 1, 1, 1, 1));
        noteBox.setPublic(true);

        NoteBox noteBox1 = new NoteBox();
        noteBox1.setData("Data");
        noteBox1.setHash("Hash");
        noteBox1.setId(1);
        noteBox1.setLifeTime(LocalDateTime.of(1, 1, 1, 1, 1));
        noteBox1.setPublic(true);

        ArrayList<NoteBox> noteBoxList = new ArrayList<>();
        noteBoxList.add(noteBox1);
        noteBoxList.add(noteBox);
        when(noteBoxRepositoryImpl.getListOfPublicAndAlive(anyInt())).thenReturn(noteBoxList);
        List<NoteBoxResponse> actualFirstPublicNote = noteBoxServiceImpl.getFirstPublicNote();
        assertEquals(2, actualFirstPublicNote.size());
        NoteBoxResponse getResult = actualFirstPublicNote.get(0);
        assertTrue(getResult.isPublic());
        NoteBoxResponse getResult1 = actualFirstPublicNote.get(1);
        assertTrue(getResult1.isPublic());
        assertEquals("Data", getResult1.getData());
        assertEquals("Data", getResult.getData());
        verify(noteBoxRepositoryImpl).getListOfPublicAndAlive(anyInt());
    }

    @Test
    void testCreate() {
        doNothing().when(noteBoxRepositoryImpl).add((NoteBox) any());

        NoteBoxRequest noteBoxRequest = new NoteBoxRequest();
        noteBoxRequest.setData("Data");
        noteBoxRequest.setExpirationTimeSeconds(1L);
        noteBoxRequest.setPublicStatus(PublicStatus.PUBLIC);
        noteBoxServiceImpl.create(noteBoxRequest);
        verify(noteBoxRepositoryImpl).add((NoteBox) any());
    }

    @Test
    void testCreate2() {
        doNothing().when(noteBoxRepositoryImpl).add((NoteBox) any());
        NoteBoxRequest noteBoxRequest = mock(NoteBoxRequest.class);
        when(noteBoxRequest.getPublicStatus()).thenReturn(PublicStatus.UNLISTED);
        when(noteBoxRequest.getData()).thenReturn("Data");
        when(noteBoxRequest.getExpirationTimeSeconds()).thenReturn(1L);
        doNothing().when(noteBoxRequest).setData((String) any());
        doNothing().when(noteBoxRequest).setExpirationTimeSeconds(anyLong());
        doNothing().when(noteBoxRequest).setPublicStatus((PublicStatus) any());
        noteBoxRequest.setData("Data");
        noteBoxRequest.setExpirationTimeSeconds(1L);
        noteBoxRequest.setPublicStatus(PublicStatus.PUBLIC);
        noteBoxServiceImpl.create(noteBoxRequest);
        verify(noteBoxRepositoryImpl).add((NoteBox) any());
        verify(noteBoxRequest).getPublicStatus();
        verify(noteBoxRequest).getData();
        verify(noteBoxRequest).getExpirationTimeSeconds();
        verify(noteBoxRequest).setData((String) any());
        verify(noteBoxRequest).setExpirationTimeSeconds(anyLong());
        verify(noteBoxRequest).setPublicStatus((PublicStatus) any());
    }

    @Test
    public void getExistHash() {
        NoteBox entity = new NoteBox();
        entity.setHash("1");
        entity.setData("11");
        entity.setPublic(true);

        when(noteBoxRepositoryImpl.getByHash("1")).thenReturn(entity);

        NoteBoxResponse expected = new NoteBoxResponse("11", true);
        NoteBoxResponse actual = noteBoxServiceImpl.getByHash("1");

        assertEquals(expected, actual);
    }

}