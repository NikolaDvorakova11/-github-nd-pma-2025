package com.example.vanocniapp.ui

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.vanocniapp.R
import com.example.vanocniapp.databinding.FragmentDayDetailBinding

// Rozšířená datová třída, která obsahuje i odkaz na obrázek
data class DailySurprise(val title: String, val content: String, @DrawableRes val imageResId: Int)

class DayDetailFragment : Fragment() {

    private var _binding: FragmentDayDetailBinding? = null
    private val binding get() = _binding!!

    private val args: DayDetailFragmentArgs by navArgs()

    // Rozšířený seznam překvapení s texty a obrázky
    // Předpokládá se, že obrázky day_1, day_2, atd. existují v res/drawable
    private val surprises by lazy {
        listOf(
            DailySurprise("Vánoční zajímavost", "Tradice zdobení vánočního stromečku pochází z Německa 16. století.", R.drawable.day_1),
            DailySurprise("Tip na aktivitu", "Napište dopis Ježíškovi. Není to jen pro děti!", R.drawable.day_2),
            DailySurprise("Recept: Horká čokoláda", "Zahřejte mléko a rozpusťte v něm kvalitní hořkou čokoládu.", R.drawable.day_3),
            DailySurprise("Tip na film", "Sám doma (1990) - Absolutní klasika, která nikdy neomrzí.", R.drawable.day_4),
            DailySurprise("Vánoční zajímavost", "Největší vánoční dárek na světě byla Socha Svobody.", R.drawable.day_5),
            DailySurprise("Recept: Perníčky", "Vypracujte těsto, nechte odležet, vykrajujte a pečte.", R.drawable.day_6),
            DailySurprise("Tip na aktivitu", "Vyrazte na procházku a obdivujte vánoční výzdobu ve vašem městě.", R.drawable.day_7),
            DailySurprise("Vánoční zajímavost", "V Japonsku je zvykem jíst na Vánoce smažené kuře z KFC.", R.drawable.day_8),
            DailySurprise("Tip na film", "Láska nebeská (2003) - Deset různých příběhů o lásce, které se protnou na Štědrý den.", R.drawable.day_9),
            DailySurprise("Recept: Vaječný koňak", "Ušlehejte žloutky s cukrem, přilijte smetanu a rum.", R.drawable.day_10),
            DailySurprise("Tip na aktivitu", "Vyrobte si vlastní vánoční ozdobu z papíru nebo slaného těsta.", R.drawable.day_11),
            DailySurprise("Vánoční zajímavost", "Píseň \"Jingle Bells\" byla původně napsána pro Den díkůvzdání.", R.drawable.day_12),
            DailySurprise("Tip na film", "Grinch (2000) - Příběh o zeleném mrzoutovi, který se snaží ukrást Vánoce.", R.drawable.day_13),
            DailySurprise("Recept: Svařené víno", "Zahřejte červené víno s kořením a plátky pomeranče.", R.drawable.day_14),
            DailySurprise("Tip na aktivitu", "Vytvořte si vánoční playlist svých oblíbených písní.", R.drawable.day_15),
            DailySurprise("Vánoční zajímavost", "Island má 13 vánočních skřítků (Yule Lads).", R.drawable.day_16),
            DailySurprise("Tip na film", "Polární expres (2004) - Kouzelný animovaný film o cestě na severní pól.", R.drawable.day_17),
            DailySurprise("Tip na aktivitu", "Zabalte první dárek a pusťte si k tomu koledy.", R.drawable.day_18),
            DailySurprise("Vánoční zajímavost", "Hvězda na špičce stromečku symbolizuje Betlémskou hvězdu.", R.drawable.day_19),
            DailySurprise("Recept: Vosí hnízda", "Umelte piškoty, smíchejte s máslem, cukrem a rumem.", R.drawable.day_20),
            DailySurprise("Tip na film", "Tři oříšky pro Popelku (1973) - Česká vánoční pohádka, bez které si svátky neumíme představit.", R.drawable.day_21),
            DailySurprise("Tip na aktivitu", "Zavolejte někomu, s kým jste dlouho nemluvili.", R.drawable.day_22),
            DailySurprise("Vánoční zajímavost", "V Mexiku je hlavním symbolem Vánoc květina Poinsettia (vánoční hvězda).", R.drawable.day_23),
            DailySurprise("Štědrý den", "Přejeme ti nádherný Štědrý den plný klidu, pohody a radosti.", R.drawable.day_24)
        )
    }

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

        val surprise = surprises.getOrNull(day - 1)
        if (surprise != null) {
            binding.surpriseTitleTextView.text = surprise.title
            binding.surpriseContentTextView.text = surprise.content
            binding.surpriseImageView.setImageResource(surprise.imageResId)
        } else {
            binding.surpriseTitleTextView.text = "Chyba"
            binding.surpriseContentTextView.text = "Pro tento den se nám bohužel zatoulalo překvapení."
            binding.surpriseImageView.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
