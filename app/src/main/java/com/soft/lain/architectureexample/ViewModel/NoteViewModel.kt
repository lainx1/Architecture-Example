package com.soft.lain.architectureexample.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.soft.lain.architectureexample.Model.Note
import com.soft.lain.architectureexample.Data.Repository.NoteRepository

class NoteViewModel(application: Application): AndroidViewModel(application) {
    var noteRepository: NoteRepository?= null
    var allNotes: LiveData<List<Note>> ? = null
    init {
        noteRepository = NoteRepository(application)
        allNotes =  noteRepository!!.getAllNotes()
    }

    fun insert(note: Note){
        noteRepository!!.insert(note)
    }
    fun update(note: Note){
        noteRepository!!.update(note)
    }
    fun delete(note: Note){
        noteRepository!!.delete(note)
    }
    fun deleteAllNotes(){
        noteRepository!!.deleteAllNotes()
    }

}