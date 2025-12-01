package com.bindiya.notesappxml.domain.usecases

import com.bindiya.notesappxml.core.model.Note
import com.bindiya.notesappxml.domain.repository.NoteRepository
import javax.inject.Inject

class InsertNoteUseCase @Inject constructor(private val repo: NoteRepository) {
   suspend operator fun invoke(note: Note) = repo.insertNote(note)
}