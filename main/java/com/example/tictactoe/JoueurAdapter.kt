package com.example.tictactoe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getDrawable
import androidx.recyclerview.widget.RecyclerView

/*    Classe adapter permettant d'utiliser un recyclerview

      @param objetsAAfficher est la liste des objets que l'on veut afficher dans le recyclerview
      @param listener           est une expression lambda qui ne retourne rien                        */

class JoueurAdapter(val objetsAAfficher: Array<Joueur>, val listener:(Joueur)->Unit)
    : RecyclerView.Adapter<JoueurAdapter.ViewHolder>() {

    // Créer les éléments viewholder du recyclerview
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // On fournit l'objet view représentant le style d'une ligne
        // au constructeur de viewholder

        val uneLigneView = LayoutInflater.from(parent.context).inflate(R.layout.row_style, parent, false)
        return ViewHolder(uneLigneView)
    }

    // Charge les données d'un objet dans un élément du recyclerview
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // On fournit l'objet à charger et l'expression lambda
        // à la méthode bind de la classe viewholder

        holder.bind(objetsAAfficher[position], listener)
    }

    // Retourne le nombre d'objets fournis
    override fun getItemCount(): Int {
        return objetsAAfficher.size
    }



    /*  Classe interne permettant de créer les éléments que contient le recyclerview
      @param elementDeListe  est un objet view représentant une ligne de la liste */

    class ViewHolder(val elementDeListe: View): RecyclerView.ViewHolder(elementDeListe){

        /* Cette fonction permet de charger les données dans les éléments du recyclerview
           @param joueur est un objet de la liste
           @param listener est une expression lambda qui ne retourne rien (pour événements)  */

        fun bind(joueur: Joueur, listener: (Joueur) -> Unit){

            // Récupère les textview de la ligne à créer
            val tvNom = elementDeListe.findViewById<TextView>(R.id.txtNom)
            val tvScore = elementDeListe.findViewById<TextView>(R.id.txtScore)

            // Met les données de l'objet dans les textview
            tvNom.text = joueur.nom
            tvScore.text = joueur.score.toString()

            // Met l'image dans le imageView
            val imgView : ImageView = elementDeListe.findViewById<ImageView>(R.id.imgJoueur)
            val drawable = getDrawable(elementDeListe.context, joueur.image )
            imgView.setImageDrawable(drawable)

            // Événement OnClick sur la ligne
            elementDeListe.setOnClickListener{listener(joueur)}
        }
    }
}