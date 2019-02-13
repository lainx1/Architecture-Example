package com.soft.lain.architectureexample.View.Activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.soft.lain.architectureexample.Model.Note
import com.soft.lain.architectureexample.View.Adapter.NoteAdapter
import com.soft.lain.architectureexample.ViewModel.NoteViewModel
import com.soft.lain.architectureexample.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val ADD_NOTE_REQUEST = 1
        const val UPDATE_NOTE_REQUEST = 2
    }

    private var noteViewModel: NoteViewModel?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)

        val adapter = NoteAdapter()
        recycler_view.adapter = adapter

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel::class.java)
        noteViewModel!!.allNotes!!.observe(this, Observer<List<Note>> {
            notes -> adapter.submitList(notes!!)
        })
        button_add_note.setOnClickListener {
            val intent = Intent(MainActivity@this , AddNoteActivity::class.java)
            startActivityForResult(intent, ADD_NOTE_REQUEST)
        }

        ItemTouchHelper(
                object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT)){
                    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                        return false
                    }
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        noteViewModel!!.delete(adapter.getNoteAtPosition(viewHolder.adapterPosition))
                        Toast.makeText(this@MainActivity, "Note deleted", Toast.LENGTH_SHORT).show()
                    }
                }
        ).attachToRecyclerView(recycler_view)
        adapter.clickListener = object : NoteAdapter.OnItemClickListener {
            override fun onItemClick(note: Note) {
                val intent = Intent(this@MainActivity, AddNoteActivity::class.java)
                intent.putExtra(AddNoteActivity.EXTRA_ID, note.id)
                intent.putExtra(AddNoteActivity.EXTRA_TITLE, note.title)
                intent.putExtra(AddNoteActivity.EXTRA_PRIORITY, note.priority)
                intent.putExtra(AddNoteActivity.EXTRA_DESCRIPTION, note.description)
                startActivityForResult(intent, UPDATE_NOTE_REQUEST)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_NOTE_REQUEST && resultCode == Activity.RESULT_OK){
            val title = data!!.getStringExtra(AddNoteActivity.EXTRA_TITLE)
            val description = data!!.getStringExtra(AddNoteActivity.EXTRA_DESCRIPTION)
            val priority = data!!.getIntExtra(AddNoteActivity.EXTRA_PRIORITY, 0)

            val note = Note(id = 0, title = title, description = description, priority = priority)
            noteViewModel!!.insert(note)

            Toast.makeText(this@MainActivity, "Note saved", Toast.LENGTH_SHORT).show()

        }else if(requestCode == UPDATE_NOTE_REQUEST && resultCode == Activity.RESULT_OK){
            val id = data!!.getIntExtra(AddNoteActivity.EXTRA_ID, -1)
            if (id == -1){
                Toast.makeText(this@MainActivity, "Note can't be updated", Toast.LENGTH_SHORT).show()
                return
            }
            val title = data!!.getStringExtra(AddNoteActivity.EXTRA_TITLE)
            val description = data!!.getStringExtra(AddNoteActivity.EXTRA_DESCRIPTION)
            val priority = data!!.getIntExtra(AddNoteActivity.EXTRA_PRIORITY, 0)
            val note = Note(id = id, title = title, description = description, priority = priority)

            noteViewModel!!.update(note = note)
            Toast.makeText(this@MainActivity, "Note updated", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this@MainActivity, "Note not saved", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            R.id.delete_all_notes -> {
                noteViewModel!!.deleteAllNotes()
                Toast.makeText(this@MainActivity, "All Notes Deleted", Toast.LENGTH_SHORT).show()
                return true
            }
            else ->{
                return super.onOptionsItemSelected(item)
            }
        }
    }
}
