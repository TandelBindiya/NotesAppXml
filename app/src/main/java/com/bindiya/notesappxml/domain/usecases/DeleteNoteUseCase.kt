package com.bindiya.notesappxml.domain.usecases

import com.bindiya.notesappxml.domain.repository.NoteRepository
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(
   private val repository: NoteRepository
) {
   suspend operator fun invoke(id: Long) {
      repository.deleteNote(id)
   }
}