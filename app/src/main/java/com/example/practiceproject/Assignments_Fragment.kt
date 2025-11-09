package com.example.practiceproject

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.*
import android.widget.*
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class AssignmentFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var assignmentAdapter: AssignmentAdapter
    val assignmentList = mutableListOf<Assignment>()

    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_fragment_assignments, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Set up any listeners if needed
        (activity as MainActivity).findViewById<TextView>(R.id.toolbarTitle).text = "Assignments"

        val subjectInput = view.findViewById<EditText>(R.id.subjectInput)
        val topicInput = view.findViewById<EditText>(R.id.topicInput)
        val datePickerButton = view.findViewById<Button>(R.id.datePickerButton)
        val addAssignmentButton = view.findViewById<Button>(R.id.addAssignmentButton)
        recyclerView = view.findViewById<RecyclerView>(R.id.assignmentsRecyclerView)
        val dueDateTextView = view.findViewById<TextView>(R.id.dueDateTextView)

        // Your data list

        assignmentAdapter = AssignmentAdapter(
            assignmentList,
            onDeleteClick = { assignment -> deleteAssignment(assignment) },
            onCompleteClick = { assignment -> completeAssignment(assignment) }
        )
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = assignmentAdapter


        // Setup DatePicker
        datePickerButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                    val date = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    dueDateTextView.text = "Due Date: $date"
                },
                year, month, day
            )
            datePickerDialog.show()
        }

        addAssignmentButton.setOnClickListener {

            val subject = subjectInput.text.toString().trim()
            val topic = topicInput.text.toString().trim()
            val dueDate = dueDateTextView.text.toString().removePrefix("Due Date: ").trim()
//
            if (subject.isNotEmpty() && topic.isNotEmpty() && dueDate.isNotEmpty()) {

                val assignment = hashMapOf(
                    "subject" to subject,
                    "topic" to topic,
                    "dueDate" to dueDate,
                    "isCompleted" to false  // Always false when adding new
                )
                db.collection("Assignments")
                    .add(assignment)
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Assignment added!", Toast.LENGTH_SHORT)
                            .show()
                        subjectInput.text.clear()
                        topicInput.text.clear()
                        dueDateTextView.text = "Due Date: Not selected"
                        loadAssignments() // Refresh from Firestore
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            requireContext(),
                            "Failed to add assignment!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            } else {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        loadAssignments()

    }
        private fun loadAssignments() {
            db.collection("Assignments")
                .get()
                .addOnSuccessListener { result ->
                    assignmentList.clear()
                    for (document in result) {
                        val subject = document.getString("subject") ?: ""
                        val topic = document.getString("topic") ?: ""
                        val dueDate = document.getString("dueDate") ?: ""
                        val isCompleted = document.getBoolean("isCompleted") ?: false
                        val id = document.id
                        assignmentList.add(Assignment(subject, topic, dueDate,isCompleted,id))
                    }
                    assignmentAdapter.notifyDataSetChanged()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Failed to load assignments!", Toast.LENGTH_SHORT).show()
                }
        }
    //  Delete from Firestore & list
    private fun deleteAssignment(assignment: Assignment) {
        if (assignment.id.isNotEmpty()) {
            db.collection("Assignments").document(assignment.id)
                .delete()
                .addOnSuccessListener {
                    assignmentList.remove(assignment)
                    assignmentAdapter.notifyDataSetChanged()
                    Toast.makeText(requireContext(), "Deleted!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Delete failed!", Toast.LENGTH_SHORT).show()
                }
        }
    }
    //  Toggle complete status & update Firestore
    private fun completeAssignment(assignment: Assignment) {
        if (assignment.id.isNotEmpty()) {
            val newStatus = !assignment.isCompleted

            db.collection("Assignments").document(assignment.id)
                .update("isCompleted", newStatus)
                .addOnSuccessListener {
                    assignment.isCompleted = newStatus
                    assignmentAdapter.notifyDataSetChanged()

                    val msg = if (newStatus) "Marked as completed!" else "Marked as incomplete!"
                    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Update failed!", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
