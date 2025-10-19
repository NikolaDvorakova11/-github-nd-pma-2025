package com.example.myapp007bfragmentsexample1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

class DetailFragment : Fragment() {

    private lateinit var imageViewSign: ImageView
    private lateinit var textViewName: TextView
    private lateinit var textViewDescription: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail, container, false)
        imageViewSign = view.findViewById(R.id.imageViewSign)
        textViewName = view.findViewById(R.id.textViewName)
        textViewDescription = view.findViewById(R.id.textViewDescription)

        arguments?.let {
            val name = it.getString("name")
            val description = it.getString("description")
            val imageResId = it.getInt("imageResId")
            updateDetails(name ?: "Neznámá značka", description ?: "", imageResId)
        }

        return view
    }

    fun updateDetails(name: String, description: String, imageResId: Int) {
        textViewName.text = name
        textViewDescription.text = description
        imageViewSign.setImageResource(imageResId)
    }
}