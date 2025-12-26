package com.example.vanocniapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.vanocniapp.databinding.FragmentDayDetailBinding

// Datová třída pro denní překvapení s nadpisem a obsahem
data class DailySurprise(val title: String, val content: String)

class DayDetailFragment : Fragment() {

    private var _binding: FragmentDayDetailBinding? = null
    private val binding get() = _binding!!

    private val args: DayDetailFragmentArgs by navArgs()

    // Zbrusu nový seznam 24 překvapení
    private val surprises = listOf(
        DailySurprise("Vánoční zajímavost", "Věděli jste, že tradice zdobení vánočního stromečku pochází z Německa 16. století? Původně se zdobil jablky, ořechy a papírovými květinami."),
        DailySurprise("Tip na aktivitu", "Udělejte si čas a napište dopis Ježíškovi. Není to jen pro děti! Napište svá přání, sny nebo jen to, za co jste vděční."),
        DailySurprise("Recept: Horká čokoláda", "Potřebujete: 200 ml mléka, 50 g kvalitní hořké čokolády, špetku skořice a lžičku cukru. Mléko zahřejte, rozpusťte v něm čokoládu, přidejte skořici, cukr a pořádně promíchejte. Můžete ozdobit šlehačkou!"),
        DailySurprise("Tip na film", "Sám doma (1990) - Absolutní klasika, která nikdy neomrzí. Příběh Kevina, kterého rodina zapomene doma na Vánoce, zná snad každý."),
        DailySurprise("Vánoční zajímavost", "Největší vánoční dárek na světě byla Socha Svobody. Francie ji darovala USA v roce 1886 jako symbol přátelství."),
        DailySurprise("Recept: Perníčky", "Smíchejte 400g hladké mouky, 140g moučkového cukru, 100g másla, 2 vejce, 2 lžíce medu a 1 lžičku perníkového koření. Vypracujte těsto, nechte odležet, vykrajujte a pečte na 180°C asi 10 minut."),
        DailySurprise("Tip na aktivitu", "Vyrazte na procházku a obdivujte vánoční výzdobu ve vašem městě. Zkuste najít tu nejkrásnější a vyfoťte se u ní."),
        DailySurprise("Vánoční zajímavost", "V Japonsku je díky masivní marketingové kampani z roku 1974 zvykem jíst na Vánoce smažené kuře z KFC. Objednávky se dělají i měsíce dopředu!"),
        DailySurprise("Tip na film", "Láska nebeská (2003) - Deset různých příběhů o lásce, které se protnou na Štědrý den v Londýně. Film, který vás zaručeně dojme i rozesměje."),
        DailySurprise("Recept: Vaječný koňak", "Ušlehejte 4 žloutky s 200g cukru. Přilijte 250 ml smetany ke šlehání a 250 ml rumu. Vše dobře promíchejte a nechte vychladit. Na zdraví!"),
        DailySurprise("Tip na aktivitu", "Vyrobte si vlastní vánoční ozdobu. Může to být cokoliv - papírová hvězda, malovaná šiška nebo ozdoba ze slaného těsta."),
        DailySurprise("Vánoční zajímavost", "Píseň \"Jingle Bells\" byla původně napsána pro Den díkůvzdání, ne pro Vánoce. Postupem času se ale stala jedním z nejznámějších vánočních symbolů."),
        DailySurprise("Tip na film", "Grinch (2000) - Příběh o zeleném mrzoutovi, který se snaží ukrást Vánoce. Skvělý Jim Carrey v jedné ze svých nejlepších rolí."),
        DailySurprise("Recept: Svařené víno", "Do hrnce nalijte 0,7l červeného vína. Přidejte celou skořici, pár hřebíčků, 2 hvězdičky badyánu a plátky pomeranče. Pomalu zahřívejte (nevařte!) a podle chuti oslaďte medem nebo cukrem."),
        DailySurprise("Tip na aktivitu", "Vytvořte si vánoční playlist. Dejte dohromady své oblíbené vánoční písně a poslouchejte ho při pečení, uklízení nebo jen tak při relaxaci."),
        DailySurprise("Vánoční zajímavost", "Island má 13 vánočních skřítků (Yule Lads), kteří postupně přicházejí do měst 13 dní před Vánoci. Každý z nich provádí nějakou neplechu."),
        DailySurprise("Tip na film", "Polární expres (2004) - Kouzelný animovaný film o chlapci, který na Štědrý večer nastoupí do vlaku směřujícího na severní pól."),
        DailySurprise("Tip na aktivitu", "Zabalte první dárek. Pusťte si k tomu koledy, uvařte si čaj a užijte si tu chvíli klidu a těšení se."),
        DailySurprise("Vánoční zajímavost", "Proč je na špičce stromečku hvězda? Symbolizuje Betlémskou hvězdu, která podle bible dovedla Tři krále k nově narozenému Ježíškovi."),
        DailySurprise("Recept: Vosí hnízda", "Umelte 150g piškotů, přidejte 60g moučkového cukru, 60g másla a 2 lžíce rumu. Těsto vtlačte do formičky, udělejte důlek a naplňte vaječným koňakem. Přilepte na piškot."),
        DailySurprise("Tip na film", "Tři oříšky pro Popelku (1973) - Česká vánoční pohádka, bez které si svátky neumíme představit. Libuše Šafránková jako Popelka je nezapomenutelná."),
        DailySurprise("Tip na aktivitu", "Zavolejte někomu, s kým jste dlouho nemluvili. Babičce, dědovi nebo starému kamarádovi. Krátký telefonát může udělat obrovskou radost."),
        DailySurprise("Vánoční zajímavost", "V Mexiku se Vánoce slaví od 12. prosince do 6. ledna. Děti dostávají dárky až na Tři krále, a místo adventního věnce mají Poinsettii, známou jako vánoční hvězda."),
        DailySurprise("Štědrý den", "Přejeme ti nádherný Štědrý den plný klidu, pohody a radosti v kruhu tvých nejbližších. Šťastné a veselé Vánoce!")
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

        // Najdeme překvapení pro daný den
        val surprise = surprises.getOrNull(day - 1)
        if (surprise != null) {
            binding.surpriseTitleTextView.text = surprise.title
            binding.surpriseContentTextView.text = surprise.content
        } else {
            // Záložní text, pokud by se den nenašel
            binding.surpriseTitleTextView.text = "Chyba"
            binding.surpriseContentTextView.text = "Pro tento den se nám bohužel zatoulalo překvapení."
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
