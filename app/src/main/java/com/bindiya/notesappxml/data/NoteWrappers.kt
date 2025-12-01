package com.bindiya.notesappxml.data

import com.bindiya.notesappxml.core.model.Note
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun NoteEntity.toDomain(): Note {
    val type = object : TypeToken<List<String>>() {}.type
    val tags = Gson().fromJson<List<String>>(this.tagsJson ?: "[]", type)
    return Note(id, title, body, tags, createdAt)
}

fun Note.toEntity(): NoteEntity {
    val tagsJson = Gson().toJson(this.tags)
    return NoteEntity(id = id, title = title, body = body, tagsJson = tagsJson, createdAt = createdAt)
}
