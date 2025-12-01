package com.bindiya.notesappxml.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bindiya.notesappxml.core.model.Note
import com.bindiya.notesappxml.domain.usecases.DeleteNoteUseCase
import com.bindiya.notesappxml.domain.usecases.GetNotesUseCase
import com.bindiya.notesappxml.domain.usecases.InsertNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val getNotesUseCase: GetNotesUseCase,
    private val insertNoteUseCase: InsertNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase
) : ViewModel() {

    private val _query = MutableStateFlow<String?>(null)
    val query: StateFlow<String?> = _query.asStateFlow()

    private val _tag = MutableStateFlow<String?>(null)
    val selectedTag: StateFlow<String?> = _tag.asStateFlow()


    @OptIn(ExperimentalCoroutinesApi::class)
    val notes: StateFlow<PagingData<Note>> =
        combine(_query, _tag) { q, t -> Pair(q, t) }.flatMapLatest { (q, t) ->
            getNotesUseCase(q, t).cachedIn(viewModelScope)
        }.stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())

    fun setQuery(q: String?) {
        _query.value = q
    }

    fun filterByTag(tag: String?) {
        _tag.value = tag
    }

    fun insert(
        title: String,
        body: String,
        tags: String
    ): Job {
        val tags = tags.split(",").mapNotNull { it.trim().takeIf { it.isNotEmpty() } }
        val note = Note(title = title.trim(), body = body.trim(), tags = tags)
        return viewModelScope.launch { insertNoteUseCase(note) }
    }

    fun delete(id: Long) = viewModelScope.launch { deleteNoteUseCase(id) }

    fun clearSelectedTag() { _tag.value = null }

}
