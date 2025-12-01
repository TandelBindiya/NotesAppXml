package com.bindiya.notesappxml.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.bindiya.notesappxml.core.model.Note
import com.bindiya.notesappxml.domain.repository.NoteRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val dao: NoteDao
) : NoteRepository {

    override fun getNotesPaged(query: String?, tag: String?): Flow<PagingData<Note>> {
        return Pager(
            config = PagingConfig(pageSize = 5, enablePlaceholders = false),
            pagingSourceFactory = { dao.pagingSourceFilteredByTag(query,tag) }
        ).flow.map { pagingData ->
            pagingData.map { entity -> entity.toDomain() }
        }
    }

    override suspend fun getNoteById(id: Long) = dao.getById(id)?.toDomain()
    override suspend fun insertNote(note: Note) = dao.insert(note.toEntity())
    override suspend fun updateNote(note: Note) = dao.update(note.toEntity())
    override suspend fun deleteNote(id: Long) = dao.delete(id)
    override fun getAllUniqueTags(): Flow<List<String>> {
        val listType = object : TypeToken<List<String>>() {}.type
        return dao.observeAllTagsJson()
            .map { jsonList ->
                jsonList
                    .asSequence()
                    .filter { !it.isNullOrBlank() }
                    .flatMap { json ->
                        try {
                            val parsed: List<String> = Gson().fromJson(json, listType)
                            parsed.asSequence()
                        } catch (e: Exception) {
                            emptySequence()
                        }
                    }
                    .map { it.trim() }
                    .filter { it.isNotEmpty() }
                    .toSet()
                    .toList()
                    .sortedBy { it.lowercase() }
            }
    }
}
