package com.kartikeydixit231.studyplanner

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kartikeydixit231.studyplanner.adapter.NoteAdapter
import data.Note
import utils.StorageHelper

class NotesActivity : AppCompatActivity() {

    private lateinit var noteList: ArrayList<Note>
    private lateinit var adapter: NoteAdapter
    private var subjectName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_notes)

        val tvTitle = findViewById<TextView>(R.id.tvTitle)
        val recycler = findViewById<RecyclerView>(R.id.recyclerNotes)
        val btnAdd = findViewById<Button>(R.id.btnAddNote)

        recycler.layoutManager = LinearLayoutManager(this)

        // Subject name from intent
        subjectName = intent.getStringExtra("subjectName") ?: ""
        tvTitle.text = subjectName

        // Load notes
        noteList = StorageHelper.loadNotes(this, subjectName)

        // Initialize adapter
        adapter = NoteAdapter(noteList)
        recycler.adapter = adapter

        // Add new note
        btnAdd.setOnClickListener {
            val edit = EditText(this)
            AlertDialog.Builder(this)
                .setTitle("Add Note")
                .setView(edit)
                .setPositiveButton("Add") { _, _ ->
                    val text = edit.text.toString()
                    if (text.isNotEmpty()) {
                        val note = Note(
                            System.currentTimeMillis().toString(),
                            subjectName,
                            text
                        )
                        noteList.add(note)
                        adapter.notifyDataSetChanged()

                        // Save immediately
                        StorageHelper.saveNotes(this, subjectName, noteList)
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    override fun onPause() {
        super.onPause()
        // Save notes on leaving activity
        StorageHelper.saveNotes(this, subjectName, noteList)
    }
}
