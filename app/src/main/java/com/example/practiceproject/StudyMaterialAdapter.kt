package com.example.practiceproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StudyMaterialAdapter (
    private val materialList: List<StudyMaterial>,
    private val onOpenClick: (StudyMaterial) -> Unit,
    private val onDeleteClick: (StudyMaterial) -> Unit
) : RecyclerView.Adapter<StudyMaterialAdapter.StudyMaterialViewHolder>(){

    class StudyMaterialViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val linkTextView: TextView = itemView.findViewById(R.id.linkTextView)
        val openButton: ImageButton = itemView.findViewById(R.id.openButton)
        val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudyMaterialViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_study_material, parent, false)
        return StudyMaterialViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudyMaterialViewHolder, position: Int) {
        val material = materialList[position]
        holder.titleTextView.text = material.title
        holder.linkTextView.text = material.link

        holder.openButton.setOnClickListener {
            onOpenClick(material)
        }

        holder.deleteButton.setOnClickListener {
            onDeleteClick(material)
        }
    }

    override fun getItemCount(): Int {
        return materialList.size
    }
}