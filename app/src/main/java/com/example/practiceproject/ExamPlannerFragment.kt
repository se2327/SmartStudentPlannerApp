
package com.example.practiceproject

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class ExamPlannerFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var examAdapter: ExamAdapter
    private val examList = mutableListOf<Exam>()

    private val db = FirebaseFirestore.getInstance()

    private lateinit var progressBar: ProgressBar
    private lateinit var progressText: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.exam_planner_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).findViewById<TextView>(R.id.toolbarTitle).text = "Exam Planner"

        val subjectInput = view.findViewById<EditText>(R.id.subjectInput)
        val datePickerButton = view.findViewById<Button>(R.id.datePickerButton)
        val addExamButton = view.findViewById<Button>(R.id.addExamButton)
        val selectedDateTextView = view.findViewById<TextView>(R.id.selectedDateTextView)

        recyclerView = view.findViewById(R.id.examsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        progressBar = view.findViewById(R.id.preparationProgressBar)
        progressText = view.findViewById(R.id.progressText)

        examAdapter = ExamAdapter(
            examList,
            onDeleteClick = { exam -> deleteExam(exam) },
            onPreparedClick = { exam -> togglePrepared(exam) }
        )
        recyclerView.adapter = examAdapter

        datePickerButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonth, selectedDay ->
                    val date = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    selectedDateTextView.text = "Date: $date"
                },
                year, month, day
            )
            datePickerDialog.show()
        }

        addExamButton.setOnClickListener {
            val subject = subjectInput.text.toString().trim()
            val date = selectedDateTextView.text.toString().removePrefix("Date: ").trim()

            if (subject.isNotEmpty() && date.isNotEmpty()) {
                val exam = hashMapOf(
                    "subject" to subject,
                    "date" to date,
                    "isPrepared" to false
                )
                db.collection("Exams")
                    .add(exam)
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Exam added!", Toast.LENGTH_SHORT).show()
                        subjectInput.text.clear()
                        selectedDateTextView.text = "Date: Not selected"
                        loadExams()
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), "Failed to add exam!", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        loadExams()
    }

    private fun loadExams() {
        db.collection("Exams")
            .get()
            .addOnSuccessListener { result ->
                examList.clear()
                for (document in result) {
                    val subject = document.getString("subject") ?: ""
                    val date = document.getString("date") ?: ""
                    val isPrepared = document.getBoolean("isPrepared") ?: false
                    val id = document.id

                    examList.add(Exam(subject, date, isPrepared, id))
                }
                examAdapter.notifyDataSetChanged()
                updateProgress()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to load exams!", Toast.LENGTH_SHORT).show()
            }
    }

    private fun deleteExam(exam: Exam) {
        if (exam.id.isNotEmpty()) {
            db.collection("Exams").document(exam.id)
                .delete()
                .addOnSuccessListener {
                    examList.remove(exam)
                    examAdapter.notifyDataSetChanged()
                    Toast.makeText(requireContext(), "Deleted!", Toast.LENGTH_SHORT).show()
                    updateProgress()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Delete failed!", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun togglePrepared(exam: Exam) {
        if (exam.id.isNotEmpty()) {
            val newStatus = !exam.isPrepared
            db.collection("Exams").document(exam.id)
                .update("isPrepared", newStatus)
                .addOnSuccessListener {
                    exam.isPrepared = newStatus
                    examAdapter.notifyDataSetChanged()
                    updateProgress()
                    Toast.makeText(requireContext(),
                        if (newStatus) "Marked Prepared!" else "Marked Not Prepared!",
                        Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Update failed!", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun updateProgress() {
        if (examList.isNotEmpty()) {
            val preparedCount = examList.count { it.isPrepared }
            val percentage = (preparedCount * 100) / examList.size
            progressBar.progress = percentage
            progressText.text = "Prepared: $percentage%"
        } else {
            progressBar.progress = 0
            progressText.text = "Prepared: 0%"
        }
    }
}