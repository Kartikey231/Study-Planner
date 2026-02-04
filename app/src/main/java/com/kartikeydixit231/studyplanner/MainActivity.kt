package com.kartikeydixit231.studyplanner

import adapter.SubjectAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import data.Subject
import utils.StorageHelper

class MainActivity : AppCompatActivity() {

    private lateinit var subjectList: ArrayList<Subject>
    private lateinit var adapter: SubjectAdapter
    private lateinit var recycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        recycler = findViewById(R.id.recycler)
        val btnAdd = findViewById<Button>(R.id.btnAdd)
        recycler.layoutManager = LinearLayoutManager(this)

        // Load subjects from storage
        subjectList = StorageHelper.loadSubjects(this)

        // Initialize adapter
        adapter = SubjectAdapter(
            this,
            subjectList,
            onSubjectClick = { subject ->
                val intent = Intent(this, NotesActivity::class.java)
                intent.putExtra("subjectName", subject.name)
                startActivity(intent)
            },
            onSubjectLongClick = { subject, position ->
                AlertDialog.Builder(this)
                    .setTitle("Delete Subject")
                    .setMessage("Are you sure you want to delete this subject? All its notes will be deleted too.")
                    .setPositiveButton("Yes") { _, _ ->
                        subjectList.removeAt(position)
                        adapter.notifyDataSetChanged()
                        StorageHelper.saveSubjects(this, subjectList)

                        // Delete notes for this subject
                        getSharedPreferences("study_pref", Context.MODE_PRIVATE)
                            .edit()
                            .remove("notes_${subject.name}")
                            .apply()
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
        )
        recycler.adapter = adapter

        // Add new subject
        btnAdd.setOnClickListener {
            val editText = EditText(this)
            AlertDialog.Builder(this)
                .setTitle("Add Subject")
                .setView(editText)
                .setPositiveButton("Add") { _, _ ->
                    val name = editText.text.toString()
                    if (name.isNotEmpty()) {
                        val subject = Subject(System.currentTimeMillis().toString(), name)
                        subjectList.add(subject)
                        adapter.notifyDataSetChanged()
                        StorageHelper.saveSubjects(this, subjectList)
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    override fun onResume() {
        super.onResume()
        // Reload subjects and refresh adapter to get latest note completions
        adapter.notifyDataSetChanged()
    }
}
