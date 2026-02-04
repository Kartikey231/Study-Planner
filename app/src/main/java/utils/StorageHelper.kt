package utils

import android.content.Context
import data.Note
import data.Subject
import org.json.JSONArray
import org.json.JSONObject

object StorageHelper {

    private const val PREF = "study_pref"
    private const val KEY = "subjects"

    fun saveSubjects(context: Context, list: ArrayList<Subject>) {

        val jsonArray = JSONArray()

        for (sub in list) {
            val obj = JSONObject()
            obj.put("id", sub.id)
            obj.put("name", sub.name)
            jsonArray.put(obj)
        }

        context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY, jsonArray.toString())
            .apply()
    }

    fun loadSubjects(context: Context): ArrayList<Subject> {

        val list = ArrayList<Subject>()
        val str = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .getString(KEY, "[]") ?: "[]"

        val jsonArray = JSONArray(str)

        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            list.add(Subject(obj.getString("id"), obj.getString("name")))
        }

        return list
    }

    fun saveNotes(context: Context, subjectName: String, notes: ArrayList<Note>) {

        val jsonArray = JSONArray()
        for (note in notes) {
            val obj = JSONObject()
            obj.put("id", note.id)
            obj.put("subjectName", note.subjectName)
            obj.put("text", note.text)
            jsonArray.put(obj)
        }

        context.getSharedPreferences("study_pref", Context.MODE_PRIVATE)
            .edit()
            .putString("notes_$subjectName", jsonArray.toString())
            .apply()
    }

    fun loadNotes(context: Context, subjectName: String): ArrayList<Note> {

        val list = ArrayList<Note>()

        val str = context.getSharedPreferences("study_pref", Context.MODE_PRIVATE)
            .getString("notes_$subjectName", "[]") ?: "[]"

        val jsonArray = JSONArray(str)

        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            list.add(Note(obj.getString("id"), obj.getString("subjectName"), obj.getString("text")))
        }

        return list
    }

}
