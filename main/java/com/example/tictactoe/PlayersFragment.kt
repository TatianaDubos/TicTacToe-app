package com.example.tictactoe

import android.content.Context
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tictactoe.databinding.FragmentPlayersBinding


class PlayersFragment(val num: Int, var nom : String, var img : Int) : Fragment() {

    // Objet de liaison
    lateinit var binding: FragmentPlayersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Liaison Vue-Controller
        binding = FragmentPlayersBinding.inflate(inflater, container, false)

        // Remplacer le titre, mettre la bonne photo et le bon nom
        val title: String


        if(num == 1) {
            title = getString(R.string.premier_joueur)

        }
        else {
            title = getString(R.string.deuxi_joueur)
        }

        binding.titre.text = title
        binding.imgJoueur.setImageResource(img)
        binding.imgJoueur.setTag(R.id.imgJoueur, img)
        binding.txtJoueurNom.text = Editable.Factory.getInstance().newEditable(nom)

        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(num:Int, nom: String, img : Int) = PlayersFragment(num, nom, img)
    }

}