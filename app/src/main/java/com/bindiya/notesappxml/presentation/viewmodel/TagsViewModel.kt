package com.bindiya.notesappxml.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bindiya.notesappxml.data.NoteRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TagsViewModel @Inject constructor(
    private val repo: NoteRepositoryImpl
) : ViewModel() {

    val tags: StateFlow<List<String>> =
        repo.getAllUniqueTags()
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}
