package com.example.practiceproject

import android.graphics.Color
import android.widget.ImageButton
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AssignmentAdapter (
    private val assignmentList: List<Assignment>,
    private val onDeleteClick: (Assignment) -> Unit,
    private val onCompleteClick: (Assignment) -> Unit
) : RecyclerView.Adapter<AssignmentAdapter.AssignmentViewHolder>() {

    class AssignmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val subjectTextView: TextView = itemView.findViewById(R.id.subjectTextView)
        val topicTextView: TextView = itemView.findViewById(R.id.topicTextView)
        val dueDateTextView: TextView = itemView.findViewById(R.id.dueDateTextView)
        val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)
        val completeButton: ImageButton = itemView.findViewById(R.id.completeButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssignmentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_assignment, parent, false)
        return AssignmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: AssignmentViewHolder, position: Int) {
        val assignment = assignmentList[position]
        holder.subjectTextView.text = assignment.subject
        holder.topicTextView.text = assignment.topic
        holder.dueDateTextView.text = "Due: ${assignment.dueDate}"

        //  Change background color if completed
        if (assignment.isCompleted) {
            holder.itemView.setBackgroundColor(0xFFE0FFE0.toInt()) // Light green
        } else {
            holder.itemView.setBackgroundColor(0xFFFFFFFF.toInt())  //white
        }

        //  Handle Delete click
        holder.deleteButton.setOnClickListener {
            onDeleteClick(assignment)
        }

        //  Handle Complete click
        holder.completeButton.setOnClickListener {
            onCompleteClick(assignment)
        }

    }


    override fun getItemCount(): Int {
        return assignmentList.size
    }
}
