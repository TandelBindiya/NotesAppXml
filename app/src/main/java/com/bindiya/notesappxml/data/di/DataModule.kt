package com.bindiya.notesappxml.data.di

import android.content.Context
import androidx.room.Room
import com.bindiya.notesappxml.data.NoteDao
import com.bindiya.notesappxml.data.NoteDatabase
import com.bindiya.notesappxml.data.NoteRepositoryImpl
import com.bindiya.notesappxml.domain.repository.NoteRepository
import com.bindiya.notesappxml.domain.usecases.DeleteNoteUseCase
import com.bindiya.notesappxml.domain.usecases.GetNotesUseCase
import com.bindiya.notesappxml.domain.usecases.InsertNoteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext ctx: Context): NoteDatabase =
        Room.databaseBuilder(ctx, NoteDatabase::class.java, "notes_xml.db")
            .build()

    @Provides
    fun provideNoteDao(db: NoteDatabase): NoteDao = db.noteDao()

    @Provides
    fun provideNoteRepository(dao: NoteDao): NoteRepository = NoteRepositoryImpl(dao)

    @Provides
    fun provideDeleteNoteUseCase(repository: NoteRepository): DeleteNoteUseCase =
        DeleteNoteUseCase(repository)

    @Provides
    fun provideGetNotesUseCase(repository: NoteRepository): GetNotesUseCase =
        GetNotesUseCase(repository)

    @Provides
    fun provideInsertNoteUseCase(repository: NoteRepository): InsertNoteUseCase =
        InsertNoteUseCase(repository)

}
