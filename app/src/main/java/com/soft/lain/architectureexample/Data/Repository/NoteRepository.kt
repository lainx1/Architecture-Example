package com.soft.lain.architectureexample.Data.Repository

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.soft.lain.architectureexample.Data.Dao.NoteDao
import com.soft.lain.architectureexample.Data.DataBase.NoteDatabase
import com.soft.lain.architectureexample.Model.Note

class NoteRepository (application: Application){
    private var noteDao: NoteDao?= null
    private var allNotes: LiveData<List<Note>> ?= null

    init {
        val noteDatabase: NoteDatabase = NoteDatabase.getInstance(application)
        noteDao = noteDatabase.noteDao()
        allNotes = noteDao!!.getAllNotes()
    }

    public fun insert(note: Note){
        InsertNoteAsyncTask(noteDao!!).execute(note)
    }

    public fun update(note: Note){
        UpdateNoteAsyncTask(noteDao!!).execute(note)
    }

    public fun delete(note: Note){
        DeleteNoteAsyncTask(noteDao!!).execute(note)
    }

    public fun deleteAllNotes(){
        DeleteAllNotesAsyncTask(noteDao!!).execute()
    }

    public fun getAllNotes():LiveData<List<Note>> = allNotes!!

    private class InsertNoteAsyncTask(noteDao: NoteDao): AsyncTask<Note, Unit, Unit>(){

        var noteDao: NoteDao?= null
        init {
            this.noteDao = noteDao
        }
        override fun doInBackground(vararg notes: Note?): Unit {
            noteDao!!.insert(notes[0]!!)
            return
        }
    }

    private class UpdateNoteAsyncTask(noteDao: NoteDao): AsyncTask<Note, Unit, Unit>(){

        var noteDao: NoteDao?= null
        init {
            this.noteDao = noteDao
        }
        override fun doInBackground(vararg notes: Note?): Unit {
            noteDao!!.update(notes[0]!!)
            return
        }
    }

    private class DeleteNoteAsyncTask(noteDao: NoteDao): AsyncTask<Note, Unit, Unit>(){

        var noteDao: NoteDao?= null
        init {
            this.noteDao = noteDao
        }
        override fun doInBackground(vararg notes: Note?): Unit {
            noteDao!!.delete(notes[0]!!)
            return
        }
    }

    private class DeleteAllNotesAsyncTask(noteDao: NoteDao): AsyncTask<Unit, Unit, Unit>(){

        var noteDao: NoteDao?= null
        init {
            this.noteDao = noteDao
        }
        override fun doInBackground(vararg params: Unit?): Unit {
            noteDao!!.deleteAllNotes()
            return
        }
    }

}