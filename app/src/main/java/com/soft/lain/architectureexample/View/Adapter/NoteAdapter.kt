package com.soft.lain.architectureexample.View.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.soft.lain.architectureexample.Model.Note
import com.soft.lain.architectureexample.R
import kotlinx.android.synthetic.main.note_item.view.*

class NoteAdapter : ListAdapter<Note, NoteAdapter.NoteHolder>(DIFF_CALLBACK) {
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Note>(){
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.title.equals(newItem.title) && oldItem.description.equals(newItem.description)
                        && oldItem.priority == newItem.priority
            }
        }
    }
    var clickListener: OnItemClickListener?=null

    class NoteHolder(view: View) : RecyclerView.ViewHolder(view) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteHolder(view)
    }
    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        val currentNote = getItem(position)
        holder.itemView.text_view_title.text = currentNote.title
        holder.itemView.text_view_priority.text = currentNote.priority.toString()
        holder.itemView.text_view_description.text = currentNote.description
        holder.itemView.setOnClickListener { clickListener!!.onItemClick(note = currentNote) }
    }
    fun getNoteAtPosition(position: Int): Note {
        return getItem(position)
    }
    public interface OnItemClickListener{
        fun onItemClick(note: Note)
    }
}