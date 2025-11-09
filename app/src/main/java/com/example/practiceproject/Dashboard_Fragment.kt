package com.example.practiceproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.card.MaterialCardView


class DashboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Set up any listeners if needed
        (activity as MainActivity).findViewById<TextView>(R.id.toolbarTitle).text = "Dashboard"

        //  Find your grid cards by their IDs
        val cardProfile = view.findViewById<MaterialCardView>(R.id.card_profile)
        val cardAssignment = view.findViewById<MaterialCardView>(R.id.cardAssignment)
        val cardStudyResources = view.findViewById<MaterialCardView>(R.id.card_studyresources)
        val cardExamPlanner = view.findViewById<MaterialCardView>(R.id.cardExamPlanner)

        //  Card click for Profile
        cardProfile.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ProfileFragment())
                .addToBackStack(null)
                .commit()
        }

        //  Card click for Assignment
        cardAssignment.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AssignmentFragment())
                .addToBackStack(null)
                .commit()
        }

        //  Card click for Study Resources
        cardStudyResources.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, StudyResourcesFragment())
                .addToBackStack(null)
                .commit()
        }

        //  Card click for Exam Planner
        cardExamPlanner.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ExamPlannerFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}
