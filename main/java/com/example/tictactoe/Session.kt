package com.example.tictactoe

import android.graphics.drawable.Drawable
import android.widget.TextView

class Session (val joueurs: Array<Joueur>) {  //Les deux joueurs

    var score : Array<Int> = arrayOf(0, 0)  // Pointage des joueurs pendant une session
    var premTour : Byte = 0                // Joueur qui a joué le premier tour de la partie
    var tour : Byte = 0                    // Joueur qui joue son tour actuellement

     fun updateTour(){

        if(tour.toInt() == 0 ){
            tour = 1
        }
        else{
            tour = 0
        }

    }

    fun updatePremTour(index : Int){

        if(index == -1){
            if(premTour.toInt() == 0){
                premTour = 1
            }
            else if(premTour.toInt() == 1){
                premTour = 0
            }
        }
        else {
            premTour = index.toByte()
        }

        tour = premTour
    }

    fun verifications(grille: Array<TextView>) : Array<Int> {

        val cases = Array<String>(grille.size) { i -> "" }

        for ((index, item) in grille.withIndex()) {
            // Récupérer le id de l'image background des textView
            cases[index] = item.getTag(R.id.grille).toString()
        }


        // Vérifier si une ligne est entierement remplie avec le même symbole (partie gagné)

        if(cases[0] != "blank" && cases[0] == cases[1] && cases[0] == cases[2]) // Lignes horizontales
        {
            return arrayOf(0, 1, 2)
        }
        else if(cases[3] != "blank" && cases[3] == cases[4] && cases[3] == cases[5])
        {
            return arrayOf(3, 4, 5)
        }
        else if( cases[6] != "blank" && cases[6] == cases[7] && cases[6] == cases[8])
        {
            return arrayOf(6, 7, 8)
        }
        else if(cases[0] != "blank" && cases[0] == cases[3] && cases[0] == cases[6])  // Lignes verticales
        {
            return arrayOf(0, 3, 6)
        }
        else if(cases[1] != "blank" && cases[1] == cases[4] && cases[1] == cases[7])
        {
            return arrayOf(1, 4, 7)
        }
        else if(cases[2] != "blank" && cases[2] == cases[5] && cases[2] == cases[8])
        {
            return arrayOf(2, 5, 8)
        }
        else if(cases[0] != "blank" && cases[0] == cases[4] && cases[0] == cases[8])  // Lignes diagonales
        {
            return arrayOf(0, 4, 8)
        }
        else if( cases[2] != "blank" && cases[2] == cases[4] && cases[2] == cases[6])
        {
            return arrayOf(2, 4, 6)
        }
        else if(cases.all { case -> case != "blank" })
        {
            return arrayOf(-2)   // Partie nulle
        }
        else return arrayOf(-1) // Continuer la partie

    }



}