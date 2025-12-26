package com.example.vanocniapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.vanocniapp.databinding.FragmentDayDetailBinding

class DayDetailFragment : Fragment() {

    private var _binding: FragmentDayDetailBinding? = null
    private val binding get() = _binding!!

    private val args: DayDetailFragmentArgs by navArgs()

    // Seznam vánočních překvapení pro každý den
    private val surprises = listOf(
        "Dnes je první den adventu! Usměj se a dej si horkou čokoládu.",
        "Zabal první dárek.",
        "Poslechni si vánoční koledu.",
        "Sněz něco dobrého, co ti připomíná Vánoce.",
        "Podívej se na vánoční pohádku.",
        "Napiš dopis Ježíškovi.",
        "Vytvoř si vánoční playlist.",
        "Udělej si procházku a pozoruj vánoční výzdobu.",
        "Zapál si vonnou svíčku.",
        "Kup malý dárek pro někoho, koho máš rád.",
        "Upeč si perníčky.",
        "Zavolej někomu, s kým jsi dlouho nemluvil.",
        "Ozdob si okno.",
        "Vyrob si papírovou vločku.",
        "Přečti si vánoční příběh.",
        "Udělej dobrý skutek.",
        "Navštiv vánoční trhy.",
        "Dej si svařák nebo horký mošt.",
        "Vyrob si vlastní vánoční ozdobu.",
        "Nauč se říct 'Veselé Vánoce' v novém jazyce.",
        "Podívej se na fotky z minulých Vánoc.",
        "Vytvoř si seznam filmů na vánoční prázdniny.",
        "Zkus si vzpomenout na svůj nejoblíbenější dárek z dětství.",
        "Šťastné a veselé Vánoce! Užij si dnešní den s rodinou."
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDayDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val day = args.day
        binding.dayTitleTextView.text = "$day. prosince"
        
        // Zobrazíme překvapení pro daný den. Index je o 1 menší než den.
        if (day in 1..surprises.size) {
            binding.surpriseTextView.text = surprises[day - 1]
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
