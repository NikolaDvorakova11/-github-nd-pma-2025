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

    // Určuje, jaké XML se má "nafouknout" (zobrazit).
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Propojíme kód s XML souborem fragment_about.xml
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Vytváříme text, který se v aplikaci zobrazí.
        // Metoda .plus() prostě spojuje věty dohromady. \n znamená nový řádek.
        val aboutText = ""
            .plus("Vítejte ve Vánočním adventním kalendáři!\n\n")
            .plus("Tato aplikace byla s láskou vytvořena, aby vám zpříjemnila kouzelný čas adventu a pomohla zkrátit čekání na Štědrý den.\n\n")
            .plus("Každý den na vás čeká malé překvapení. Odkrýváním políček navíc postupně složíte skrytý vánoční obrázek.\n\n")
            .plus("Přejeme vám krásné a pohodové prožití svátků!")

        // Html.fromHtml umožní textu rozumět HTML značkám (např. kdybych chtěla něco tučně <b>).
        // Výsledek pak vložíme do textového pole (TextView), které má v XML id: aboutContentTextView
        binding.aboutContentTextView.text = Html.fromHtml(aboutText, Html.FROM_HTML_MODE_LEGACY)
    }

    // Tato funkce se spustí, když uživatel odejde z této stránky jinam.
    override fun onDestroyView() {
        super.onDestroyView()
        // Důležité: Uvolníme paměť. Tím předcházíme tomu, aby aplikace zbytečně zabírala místo, když tato stránka zrovna není vidět.
        _binding = null
    }
}
