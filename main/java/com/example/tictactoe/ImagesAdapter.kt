package com.example.tictactoe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.getDrawable
import androidx.recyclerview.widget.RecyclerView

/*    Classe adapter permettant d'utiliser un recyclerview

      @param objetsAAfficher est la liste des objets que l'on veut afficher dans le recyclerview
      @param listener           est une expression lambda qui ne retourne rien                        */

class ImagesAdapter(val objetsAAfficher: Array<Int>, val listener:(Int)->Unit)
    : RecyclerView.Adapter<ImagesAdapter.ViewHolder>() {

    // Créer les éléments viewholder du recyclerview
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // On fournit l'objet view représentant le style d'une ligne
        // au constructeur de viewholder

        val uneLigneView = LayoutInflater.from(parent.context).inflate(R.layout.grid_style, parent, false)
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
           @param image est un objet de la liste
           @param listener est une expression lambda qui ne retourne rien (pour événements)  */

        fun bind(image: Int, listener: (Int) -> Unit){

            // Récupère l'imageview de la ligne à créer
            val imgView : ImageView = elementDeListe.findViewById<ImageView>(R.id.imgContainer)

            // Met l'image dans le imageView
            imgView.setImageResource(image)

            // Événement OnClick sur la ligne
            elementDeListe.setOnClickListener{listener(image)}
        }
    }
}