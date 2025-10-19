package com.example.myapp007bfragmentsexample1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import com.example.myapp007bfragmentsexample1.MainActivity


// Datová třída pro značku
data class TrafficSign(
    val name: String,
    val description: String,
    val imageResId: Int
)
class ListFragment : Fragment() {

    private lateinit var listView: ListView

    // Seznam dopravních značek
    private val trafficSigns = listOf(
        TrafficSign(
            "Stop",
            "Značka přikazuje řidiči zastavit vozidlo na hranici křižovatky nebo přechodu.",
            R.drawable.sign_stop
        ),
        TrafficSign(
            "Hlavní silnice",
            "Označuje začátek hlavní silnice, na které máš přednost v jízdě.",
            R.drawable.sign_main_road
        ),
        TrafficSign(
            "Přechod pro chodce",
            "Upozorňuje na místo, kde mohou chodci bezpečně přecházet vozovku.",
            R.drawable.sign_pedestrian
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        listView = view.findViewById(R.id.listViewSigns)

        // Adapter pro názvy značek
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            trafficSigns.map { it.name }
        )
        listView.adapter = adapter

        // Po kliknutí na položku zavoláme metodu v MainActivity
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedSign = trafficSigns[position]
            (activity as? MainActivity)?.onSignSelected(
                selectedSign.name,
                selectedSign.description,
                selectedSign.imageResId
            )
        }
        return view
    }

}