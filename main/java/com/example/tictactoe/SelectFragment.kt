package com.example.tictactoe

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.tictactoe.databinding.FragmentSelectBinding


class SelectFragment(val action: String) : Fragment() {

    // Activité mère
    private lateinit var activiteMere : LoginActivity
    // Objet de liaison
    lateinit var binding: FragmentSelectBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Liaison Vue-Controller
        binding = FragmentSelectBinding.inflate(inflater, container, false)

        if(action == "img"){
            selectImg()
        }
        else if(action == "player") {
            selectPlayer()
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        activiteMere = context as LoginActivity
    }

    companion object {

        @JvmStatic
        fun newInstance(action: String) = SelectFragment(action)
    }


    // Afficher les images dans le recyclerview
    fun selectImg(){

        val images = arrayOf(R.drawable.img_x, R.drawable.img_o, R.drawable.img_1, R.drawable.img_2, R.drawable.img_3, R.drawable.img_4, R.drawable.img_5, R.drawable.img_6, R.drawable.img_7, R.drawable.img_8, R.drawable.img_9, R.drawable.img_10, R.drawable.img_11,R.drawable.img_12,R.drawable.img_13,R.drawable.img_14)

        val recycleview =  binding.recyclerView                        // Récupère le RecyclerView

        recycleview.layoutManager = GridLayoutManager(activiteMere, 3)     // Utilisation obligatoire d'un LayoutManager


        val adapterRecycle = ImagesAdapter(images){         // adapter(Array<>){ it -> unit}
            // ONCLICK
            // Appeler la methode qui va mettre a jour le fragment d'identification
            activiteMere.updateImage(it)
        }
        recycleview.adapter = adapterRecycle                                                        // On lie l'adapter au recycleview
    }

    fun selectPlayer(){

            val allPlayers = activiteMere.getJoueurs()!! // Récupère tout nos joueurs


            val recycleview =  binding.recyclerView                        // Récupère le RecyclerView

            recycleview.layoutManager = LinearLayoutManager(activiteMere)     // Utilisation obligatoire d'un LayoutManager


            val adapterRecycle = JoueurAdapter(allPlayers){         // adapter(Array<>){ it -> unit}
                // ONCLICK
                activiteMere.updateProfil(it)
            }
            // Appeler la methode qui va mettre a jour le fragment d'identification
            recycleview.adapter = adapterRecycle

    }
}