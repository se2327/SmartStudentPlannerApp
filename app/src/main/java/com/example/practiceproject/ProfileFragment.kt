package com.example.practiceproject

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).findViewById<TextView>(R.id.toolbarTitle).text = "Profile"

        val nameInput = view.findViewById<EditText>(R.id.nameInput)
        val phoneInput = view.findViewById<EditText>(R.id.phoneInput)
        val emailInput = view.findViewById<EditText>(R.id.emailInput)
        val collegeInput = view.findViewById<EditText>(R.id.collegeInput)
        val bioInput = view.findViewById<EditText>(R.id.bioInput)

        // New inputs for links:
        val linkedInInput = view.findViewById<EditText>(R.id.linkedinInput)
        val githubInput = view.findViewById<EditText>(R.id.githubInput)

        val linkedInIcon = view.findViewById<ImageView>(R.id.linkedinIcon)
        val githubIcon = view.findViewById<ImageView>(R.id.githubIcon)
        val saveButton = view.findViewById<Button>(R.id.saveProfileButton)

        // 🔹 Load profile data when fragment opens
        db.collection("Profile").document("UserProfile")
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    nameInput.setText(document.getString("name") ?: "")
                    phoneInput.setText(document.getString("phone") ?: "")
                    emailInput.setText(document.getString("email") ?: "")
                    collegeInput.setText(document.getString("college") ?: "")
                    bioInput.setText(document.getString("bio") ?: "")
                    linkedInInput.setText(document.getString("linkedin") ?: "")
                    githubInput.setText(document.getString("github") ?: "")
                }
            }

        // 🔹 Open LinkedIn
        linkedInIcon.setOnClickListener {
            val url = linkedInInput.text.toString().trim()

            if (url.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            } else {
            Toast.makeText(requireContext(), "No LinkedIn link!", Toast.LENGTH_SHORT).show()
            }
        }

        // 🔹 Open GitHub
        githubIcon.setOnClickListener {
            val url = githubInput.text.toString().trim()
            if (url.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            } else {
                Toast.makeText(requireContext(), "No GitHub link!", Toast.LENGTH_SHORT).show()
            }
        }

        // 🔹 Save profile
        saveButton.setOnClickListener {
            val profileData = hashMapOf(
                "name" to nameInput.text.toString(),
                "phone" to phoneInput.text.toString(),
                "email" to emailInput.text.toString(),
                "college" to collegeInput.text.toString(),
                "bio" to bioInput.text.toString(),
                "linkedin" to linkedInInput.text.toString(),
                "github" to githubInput.text.toString()
            )

            db.collection("Profile").document("UserProfile")
                .set(profileData)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Profile Saved!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Failed to Save!", Toast.LENGTH_SHORT).show()
                }
        }
    }
}