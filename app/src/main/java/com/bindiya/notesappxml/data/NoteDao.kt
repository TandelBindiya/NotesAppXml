package com.bindiya.notesappxml.data

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("""
        SELECT * FROM notes
        WHERE ((:query IS NULL)
              OR (title LIKE '%' || :query || '%' OR body LIKE '%' || :query || '%'))
          AND (:tag IS NULL OR tagsJson LIKE '%' || :tag || '%')
        ORDER BY createdAt DESC
    """)
    fun pagingSourceFilteredByTag(query: String?, tag: String?): PagingSource<Int, NoteEntity>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: NoteEntity): Long

    @Update
    suspend fun update(entity: NoteEntity)

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getById(id: Long): NoteEntity?

    @Query("SELECT tagsJson FROM notes")
    fun observeAllTagsJson(): Flow<List<String>>
}
