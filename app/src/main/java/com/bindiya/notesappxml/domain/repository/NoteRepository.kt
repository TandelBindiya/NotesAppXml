package com.bindiya.notesappxml.domain.repository

import androidx.paging.PagingData
import com.bindiya.notesappxml.core.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    suspend fun getNoteById(id: Long): Note?
    suspend fun insertNote(note: Note): Long
    suspend fun updateNote(note: Note)
    suspend fun deleteNote(id: Long)
    fun getAllUniqueTags(): Flow<List<String>>
    fun getNotesPaged(query: String?, tag: String?): Flow<PagingData<Note>>
}