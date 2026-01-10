package com.example.vanocniapp.ui

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.vanocniapp.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val aboutText = ""
            .plus("Vítejte ve Vánočním adventním kalendáři!\n\n")
            .plus("Tato aplikace byla s láskou vytvořena, aby vám zpříjemnila kouzelný čas adventu a pomohla zkrátit čekání na Štědrý den.\n\n")
            .plus("Každý den na vás čeká malé překvapení. Odkrýváním políček navíc postupně složíte skrytý vánoční obrázek.\n\n")
            .plus("Přejeme vám krásné a pohodové prožití svátků!")

        // Použijeme Html.fromHtml pro hezčí formátování, pokud bychom v budoucnu chtěli něco zvýraznit
        binding.aboutContentTextView.text = Html.fromHtml(aboutText, Html.FROM_HTML_MODE_LEGACY)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
