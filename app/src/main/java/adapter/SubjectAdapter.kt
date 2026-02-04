package adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.kartikeydixit231.studyplanner.NotesActivity
import com.kartikeydixit231.studyplanner.R
import data.Subject
import utils.StorageHelper

class SubjectAdapter(
    private val context: Context,
    private val subjectList: ArrayList<Subject>,
    private val onSubjectClick: (Subject) -> Unit,
    private val onSubjectLongClick: (Subject, Int) -> Unit
) : RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder>() {

    inner class SubjectViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView: CardView = view.findViewById(R.id.cardView)
        val progressOverlay: View = view.findViewById(R.id.progressOverlay)
        val tvName: TextView = view.findViewById(R.id.tvName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_subject, parent, false)
        return SubjectViewHolder(view)
    }

    override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {
        val subject = subjectList[position]
        holder.tvName.text = subject.name

        //  Load notes dynamically from storage every time this card is displayed
        val notes = StorageHelper.loadNotes(holder.itemView.context, subject.name)

        val total = notes.size
        val completed = notes.count { it.completed }

        val progressRatio = if (total == 0) 0f else completed.toFloat() / total.toFloat()

        // Update overlay width to show progress
        holder.cardView.post {
            val progressWidth = (holder.cardView.width * progressRatio).toInt()
            holder.progressOverlay.layoutParams.width = progressWidth
            holder.progressOverlay.requestLayout()
        }

        // Click and long-click listeners
        holder.cardView.setOnClickListener { onSubjectClick(subject) }
        holder.cardView.setOnLongClickListener {
            onSubjectLongClick(subject, position)
            true
        }
    }


    override fun getItemCount(): Int = subjectList.size
}
