package com.example.tictactoe

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.tictactoe.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {

    // Objet de liaison
    private lateinit var binding: ActivityMainBinding
    // Grille du tictactoe
    private lateinit var grille : Array<TextView>
    // Session courante
    private lateinit var session: Session

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Mettre en place la liaison vue/controleur
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialiser la variable grille
        grille = arrayOf(binding.case1, binding.case2, binding.case3, binding.case4, binding.case5, binding.case6, binding.case7, binding.case8, binding.case9 )

        // Récupérer les joueurs
        val joueur1 : Joueur
        val joueur2 : Joueur

            // Pour les API niveau 33 ou plus récentes
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
             joueur1 =  intent.getParcelableExtra("joueur1", Joueur::class.java)!!
             joueur2 =  intent.getParcelableExtra("joueur2", Joueur::class.java)!!
         }
        else // Pour les API plus anciennes que 33
         {
             joueur1 =  intent.getParcelableExtra<Joueur>("joueur1")!!
             joueur2 =  intent.getParcelableExtra<Joueur>("joueur2")!!
         }

        // Initialiser l'objet session
        session = Session(arrayOf(joueur1, joueur2))

        // Afficher le nom des joueurs
        binding.txtNom1.text = session.joueurs[0].nom
        binding.txtNom2.text = session.joueurs[1].nom
        // Afficher l'image des joueurs
        binding.imgJoueur1.setImageResource(session.joueurs[0].image)
        binding.imgJoueur2.setImageResource(session.joueurs[1].image)

        // Mettre l'activité dans son état initial
        reset()
    }

    private fun reset() {

        //Vider les case, activer les cases et désactiver l'animation
        grille.forEach { case ->
            case.setBackgroundColor(resources.getColor(R.color.white, null))
            case.setTag(R.id.grille, "blank")
            case.isEnabled = true
            case.clearAnimation()
        }

        // Afficher les score
        afficheScore()
        // Mettre en évidence à quel joueur est le tour
        afficheTour()
    }

    fun cases_OnClick(view: View) {  // Clic sur une case de la grille
        // Récupérer la case
        val case = view as TextView

        if(case.getTag(R.id.grille) != "blank")
        {// Quitter la fonction si la case est déjà occupé
            return
        }

        // Récupérer le joueur qui vien de jouer
        val joueur = session.joueurs[session.tour.toInt()]
        // Afficher l'avatar
         case.setBackgroundResource(joueur.image)
        case.setTag(R.id.grille, joueur.image.toString())
        // Vérifier si il y a un gagnant ou si la partie est nulle

        val result =  session.verifications(grille)


         if(result[0] == -1){
              session.updateTour() ; afficheTour()   // Tour à l'autre joueur
          }
          else if(result[0] == -2){                  // Partie nulle

              partieNulle()
          }
          else if(result.size == 3){                 // Le joueur a gagné la partie

              partieGagne(arrayOf(grille[result[0]], grille[result[1]], grille[result[2]]))
          }

    }

    fun afficheTour(){   // Indicateur visuel pour montrer à qui est le tour de jouer

        if(session.tour.toInt() == 0){
            binding.groupJoueur1.backgroundTintList = resources.getColorStateList(R.color.pale_blue, null)
            binding.groupJoueur2.backgroundTintList = resources.getColorStateList(R.color.light_gray, null)
        }
        else if(session.tour.toInt() == 1) {
            binding.groupJoueur2.backgroundTintList = resources.getColorStateList(R.color.pale_blue, null)
            binding.groupJoueur1.backgroundTintList = resources.getColorStateList(R.color.light_gray, null)
        }
    }

    fun afficheScore(){  // Affiche le score des joueurs

        binding.txtScore1.text = session.score[0].toString()
        binding.txtScore2.text = session.score[1].toString()
    }

    fun nouvPartie_OnClick(view: View) {

        reset()
    }

    fun partieGagne(cases : Array<TextView>){

        // Empecher la modification des cases
        grille.forEach { case -> case.isEnabled = false }

        val index = session.tour.toInt()

        // Récupérer le joueur gagnant
        val gagnant = session.joueurs[index]

        // Augmenter son score
        gagnant.score++
        session.score[index]++
        afficheScore()

        // Mise à jour du score du joueur dans la base de données
        updatePlayer(gagnant)


        // Le gagnant va commencer la prochaine partie
        session.updatePremTour(index)

        // Créer une animation pour faire clignoter les cases gagnantes
        val blinkAnimation = AlphaAnimation(0.5f, 1.0f).apply {
            duration = 800
            startOffset = 20
            repeatMode = Animation.REVERSE
            repeatCount = Animation.INFINITE
        }

        // Créer une liste d'animations à exécuter pour chaque case gagnante
        val animationSet = AnimationSet(true).apply {
            addAnimation(blinkAnimation)
        }

        // Appliquer l'animation à chaque case gagnante
            cases[0].startAnimation(animationSet)
            cases[1].startAnimation(animationSet)
            cases[2].startAnimation(animationSet)


        // Message
        Snackbar.make(binding.constraintlayout, "\uD83D\uDC51 " + gagnant.nom.uppercase() + " a gagné la partie", Snackbar.LENGTH_SHORT)
            .setBackgroundTint(resources.getColor(R.color.green, null))
            .setAction("Gagnant", null).show()

    }

    fun partieNulle(){

        // Empecher la modification des cases
        grille.forEach { case -> case.isEnabled = false }

        // Le joueur qui n'avais pas commencé cette partie va commencer la prochaine
        session.updatePremTour(-1)

        //Message
        Snackbar.make(binding.constraintlayout, "Partie nulle \uD83D\uDE41", Snackbar.LENGTH_SHORT)
            .setBackgroundTint(resources.getColor(R.color.red, null))
            .setAction("Nulle", null).show()
    }


    // Fonction qui met à jour un joueur dans la base de donnees
    fun updatePlayer(joueur : Joueur) {

        val thread = Thread {
            val db = Room.databaseBuilder(
                this,
                DataBase::class.java,
                "bddJoueurs"
            ).build()

            val dao = db.JoueurDao()

            dao.updatePlayer(joueur) // Met à jour le joueur

            db.close()
        }

        thread.start()
    }

}