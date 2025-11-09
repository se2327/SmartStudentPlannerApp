package com.example.practiceproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExamAdapter(
    private val examList: List<Exam>,
    private val onDeleteClick: (Exam) -> Unit,
    private val onPreparedClick: (Exam) -> Unit
) : RecyclerView.Adapter<ExamAdapter.ExamViewHolder>() {

    class ExamViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val subjectTextView: TextView = itemView.findViewById(R.id.subjectTextView)
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)
        val preparedButton: ImageButton = itemView.findViewById(R.id.preparedButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExamViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_exam, parent, false)
        return ExamViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExamViewHolder, position: Int) {
        val exam = examList[position]
        holder.subjectTextView.text = exam.subject
        holder.dateTextView.text = "Date: ${exam.date}"

        // ✅ Set background if prepared
        if (exam.isPrepared) {
            holder.itemView.setBackgroundColor(0xFFE0FFE0.toInt()) // Light green
        } else {
            holder.itemView.setBackgroundColor(0xFFFFFFFF.toInt()) // White
        }

        // ✅ Delete action
        holder.deleteButton.setOnClickListener {
            onDeleteClick(exam)
        }

        // ✅ Mark prepared action
        holder.preparedButton.setOnClickListener {
            onPreparedClick(exam)
        }
    }

    override fun getItemCount(): Int {
        return examList.size
    }
}