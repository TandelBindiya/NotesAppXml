package com.bindiya.notesappxml.core.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val title: String,
    val body: String,
    val tags: List<String>,
    val createdAt: String = "Nov 30, 2025"
)