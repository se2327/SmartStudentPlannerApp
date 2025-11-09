package com.example.practiceproject

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class StudyResourcesFragment : Fragment() {

    private lateinit var titleInput: EditText
    private lateinit var linkInput: EditText
    private lateinit var saveButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: StudyMaterialAdapter

    private val studyList = mutableListOf<StudyMaterial>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_study_resources, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).findViewById<TextView>(R.id.toolbarTitle).text = "My Study Resources"

        titleInput = view.findViewById(R.id.titleInput)
        linkInput = view.findViewById(R.id.linkInput)
        saveButton = view.findViewById(R.id.saveButton)
        recyclerView = view.findViewById(R.id.studyRecyclerView)

        adapter = StudyMaterialAdapter(
            studyList,
            onDeleteClick = { studyMaterial -> deleteStudyMaterial(studyMaterial) },
            onOpenClick = { studyMaterial -> openStudyMaterial(studyMaterial) }
        )

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        saveButton.setOnClickListener {
            addStudyMaterial()
        }

        loadStudyMaterials()
    }

    private fun addStudyMaterial() {
        val title = titleInput.text.toString().trim()
        val link = linkInput.text.toString().trim()

        if (title.isEmpty() || link.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill both Title and Link", Toast.LENGTH_SHORT).show()
            return
        }

        val studyMaterial = hashMapOf(
            "title" to title,
            "link" to link
        )

        db.collection("StudyMaterials")
            .add(studyMaterial)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Study Material added!", Toast.LENGTH_SHORT).show()
                titleInput.text.clear()
                linkInput.text.clear()
                loadStudyMaterials() // refresh list
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to add Study Material!", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadStudyMaterials() {
        db.collection("StudyMaterials")
            .get()
            .addOnSuccessListener { result ->
                studyList.clear()
                for (document in result) {
                    val id = document.id
                    val title = document.getString("title") ?: ""
                    val link = document.getString("link") ?: ""
                    studyList.add(StudyMaterial(id, title, link))
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to load Study Materials!", Toast.LENGTH_SHORT).show()
            }
    }

    private fun deleteStudyMaterial(studyMaterial: StudyMaterial) {
        if (studyMaterial.id.isNotEmpty()) {
            db.collection("StudyMaterials").document(studyMaterial.id)
                .delete()
                .addOnSuccessListener {
                    studyList.remove(studyMaterial)
                    adapter.notifyDataSetChanged()
                    Toast.makeText(requireContext(), "Deleted!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Delete failed!", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun openStudyMaterial(studyMaterial: StudyMaterial) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(studyMaterial.link)
        startActivity(intent)
    }
}