package com.kartikeydixit231.studyplanner.adapter

import android.app.AlertDialog
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kartikeydixit231.studyplanner.R
import data.Note
import utils.StorageHelper
class NoteAdapter(
    private val noteList: ArrayList<Note>
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    inner class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNote: TextView = view.findViewById(R.id.tvNote)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = noteList[position]

        // Note text
        holder.tvNote.text = note.text

        // Strike-through and color
        holder.tvNote.paint.isStrikeThruText = note.completed
        holder.tvNote.setTextColor(if (note.completed) Color.GRAY else Color.BLACK)

        // Toggle complete on click
        holder.itemView.setOnClickListener {
            // Only mark completed if not already completed
            if (!note.completed) {
                note.completed = true

                // Update UI immediately
                holder.tvNote.paint.isStrikeThruText = true
                holder.tvNote.setTextColor(Color.GRAY)

                // Save notes immediately
                StorageHelper.saveNotes(holder.itemView.context, note.subjectName, noteList)
            }
            // If already completed, do nothing
        }


        // Delete on long-press
        holder.itemView.setOnLongClickListener {
            val context = holder.itemView.context
            AlertDialog.Builder(context)
                .setTitle("Delete Note")
                .setMessage("Are you sure you want to delete this note?")
                .setPositiveButton("Yes") { _, _ ->
                    noteList.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, noteList.size)

                    StorageHelper.saveNotes(context, note.subjectName, noteList)
                }
                .setNegativeButton("Cancel", null)
                .show()
            true
        }
    }

    override fun getItemCount(): Int = noteList.size
}
