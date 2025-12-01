package com.bindiya.notesappxml.domain.usecases

import com.bindiya.notesappxml.domain.repository.NoteRepository
import javax.inject.Inject

class GetNotesUseCase @Inject constructor(private val repo: NoteRepository) {
   operator fun invoke(query: String?, tag: String?) = repo.getNotesPaged(query,tag)
}

