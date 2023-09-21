package com.example.tictactoe

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.example.tictactoe.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    var joueur1Fragment : PlayersFragment = PlayersFragment.newInstance(1,"", R.drawable.img_x ) // Fragment d'identification du 1e joueur
    var joueur2Fragment : PlayersFragment = PlayersFragment.newInstance(2,"", R.drawable.img_o ) // Fragment d'identification du 2e joueur
    var idCourant: Int = 0  // Quel joueur est en train de s'identifier


    // Objet de liaison
    private lateinit var binding: ActivityLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Mettre en place la liaison vue/controleur
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mettre l'activité dans son état initial
        reset()
    }

    private fun reset() {
        changeFragment(1)

        joueur1Fragment  = PlayersFragment.newInstance(1,"", R.drawable.img_x )
        joueur2Fragment  = PlayersFragment.newInstance(2, "", R.drawable.img_o )

        idCourant = 0
    }

    override fun onStop() {
        super.onStop()

        reset()
    }

    //Click sur bouton Jouer du home Fragment
    fun btnJouer_OnClick(view: View) {
        // Affiche le fragment d'identification
        changeFragment(2)
    }


    fun changeFragment(num :Int){ // Remplacer le fragment du FragmentContainerView

       var fragment : Fragment? = null

        when(num){ // Définie le fragment a afficher selon le numero
            1-> { // Accueil
                fragment = HomeFragment.newInstance()
            }
            2-> { // Joueur1 identification
                fragment = joueur1Fragment
                idCourant = 1
            }
            3-> { // Joueur2 identification
                fragment = joueur2Fragment
                idCourant = 2
            }
            4-> {  // Selectionner joueur existant
                fragment = SelectFragment.newInstance("player")

            }
            5-> { // Selectionner image
                fragment = SelectFragment.newInstance( "img")
            }
        }

        // Remplacer le fragment
        if(fragment != null) {
            supportFragmentManager.beginTransaction().replace(binding.fragmentContainerView.id, fragment).commitAllowingStateLoss()
        }
    }

    override fun onBackPressed() { // Redéfinir onBackPressed pour revenir au fragment précédent

        val fragment =  supportFragmentManager.findFragmentById(R.id.fragmentContainerView)

        when(fragment){
            is HomeFragment ->  {  // Fragment Home
                try {
                    finish() // Quitter
                }
                catch (ex : Exception){
                    println(ex.message)
                }
            }

            is PlayersFragment -> { // Fragment d'identification

                if(fragment == joueur1Fragment) { // Revenir au fragment home
                    changeFragment(1)
                }
                else if(fragment == joueur2Fragment) { // Revenir a l'identification du joueur1
                    changeFragment(2)
                }

            }

            is SelectFragment -> {  // Fragment de selection (RecyclerView)

                if(idCourant == 1) {
                    changeFragment(2) // Revenir a l'identification du joueur1
                }
                else if(idCourant == 2){
                    changeFragment(3) // Revenir a l'identification du joueur2
                }
            }
        }
    }


    // Bouton confirmer du fragment Players
    fun Confirmer_OnClick(view: View){

        if(idCourant == 1){ // Identification 1e joueur

            val nom1 = joueur1Fragment.binding.txtJoueurNom.text.trim().toString()

            // Si le nom est vide : Quitter
            if(nom1.isEmpty())
            {
                Toast.makeText(this,"Veuillez entrer votre nom", Toast.LENGTH_SHORT).show()
                return
            }

            // Changer de fragment
            changeFragment(3)
        }
        else if(idCourant == 2){ // Identification 2e joueur

            // Verifier les information de connexion
            // Récupérer les infos
            val nom1 = joueur1Fragment.binding.txtJoueurNom.text.trim().toString()
            val nom2 = joueur2Fragment.binding.txtJoueurNom.text.trim().toString()

            val image1 = joueur1Fragment.binding.imgJoueur.getTag(R.id.imgJoueur) as Int
            val image2 = joueur2Fragment.binding.imgJoueur.getTag(R.id.imgJoueur) as Int


            if(nom2.isEmpty()) // Si le nom est vide : Quitter
            {
                Toast.makeText(this,"Veuillez entrer votre nom", Toast.LENGTH_SHORT).show()
                return
            }


            if(nom1.lowercase() == nom2.lowercase()) // Si les deux nom sont égaux
            {
                Toast.makeText(this,"Les joueurs ne peuvent pas avoir le même nom", Toast.LENGTH_SHORT).show()
                return
            }

            if(image1 == image2)  // Si les deux images sont égaux
            {
                Toast.makeText(this,"Les joueurs ne peuvent pas avoir le même avatar", Toast.LENGTH_SHORT).show()
                return
            }

            var joueur1 : Joueur? = verifyPlayer(nom1)
            var joueur2 : Joueur? = verifyPlayer(nom2)

            if(joueur1 == null)  // nouveau joueur
            {
                // Enregistrer le joueur
                joueur1 = Joueur(nom1, image1)
                addPlayer(joueur1)

            }
            else { // joueur existant

                if(joueur1.image != image1){ // Verifier si l'image du joueur existant a été modifié

                    // Modifier le joueur
                    joueur1.image = image1
                    updatePlayer(joueur1)

                }
            }

            if(joueur2 == null)  // nouveau joueur
            {
                // Enregistrer le joueur
                joueur2 = Joueur(nom2, image2)
                addPlayer(joueur2)
            }
            else { // joueur existant

                if(joueur2.image != image2){ // Verifier si l'image du joueur existant a été modifié

                    // Modifier le joueur
                    joueur2.image = image2
                    updatePlayer(joueur2)

                }
            }

            // Créer une intention avec nos deux joueurs en parametre pour afficher MainActivity
            val intention = Intent(this, MainActivity::class.java)
            intention.putExtra("joueur1", joueur1 )
            intention.putExtra("joueur2", joueur2 )
            // Lancer l'activité Main
            startActivity(intention)


        }
    }


    // Bouton selectionner un joueur existant
   fun select_OnClick(view: View){

        val liste = getJoueurs() // Vérifier si il y a des joueurs a afficher

        if(liste.isNullOrEmpty()) { // Liste vide
            Toast.makeText(this, "Aucun joueur n'a été sauvegardé pour le moment", Toast.LENGTH_SHORT).show()
        }
        else {
            // Fragment affichant un recycler view avec nos joueurs
            changeFragment(4)
        }
   }

    // Selectionner une autre image
    fun img_OnClick(view: View){
        // Fragment affichant un recycler view avec nos images
        changeFragment(5)
    }

    // Le joueur a choisi une nouvelle image
    fun updateImage(img: Int){

        if(idCourant == 1){

            // Recréer le fragment avec la photo d'identification choisie
            val text = joueur1Fragment.binding.txtJoueurNom.text.toString()
            joueur1Fragment = PlayersFragment.newInstance(1, text, img )
            // Revenir au fragment
            changeFragment(2)

        }
        else  if(idCourant == 2){

            // Recréer le fragment d'identification avec la photo choisie
            val text = joueur2Fragment.binding.txtJoueurNom.text.toString()
            joueur2Fragment = PlayersFragment.newInstance(2, text, img )
            // Revenir au fragment
            changeFragment(3)
        }
    }

    // Le joueur a choisi un joueur existant
    fun updateProfil(joueur : Joueur){

        val text = joueur.nom
        val img = joueur.image

        if(idCourant == 1){
            // Recréer le fragment
            joueur1Fragment = PlayersFragment.newInstance(1, text, img )
            // Revenir au fragment
            changeFragment(2)

        }
        else  if(idCourant == 2){
            // Recréer le fragment
            joueur2Fragment = PlayersFragment.newInstance(2, text, img )
            // Revenir au fragment
            changeFragment(3)
        }

    }

    // Fonction qui retourne tout nos joueurs
    fun getJoueurs() : Array<Joueur>?{

        var allPlayers : Array<Joueur>? = null

      val thread =  Thread {
            val db = Room.databaseBuilder(
                this,
                DataBase::class.java,
                "bddJoueurs"
            ).build()

            val dao = db.JoueurDao()

            allPlayers = dao.get().toTypedArray()  // Récupère tout nos joueurs

            db.close()
        }

        thread.start()
        thread.join()

        if(allPlayers.isNullOrEmpty()) {
            return null   // Si la liste est vide
        }
        else {
            return allPlayers
        }


    }


    // Fonction qui verifie si le joueur existe déjà
    fun verifyPlayer(nom :String) : Joueur? {

        var player: Joueur? = null

        val thread = Thread {
            val db = Room.databaseBuilder(
                this,
                DataBase::class.java,
                "bddJoueurs"
            ).build()

            val dao = db.JoueurDao()

            player = dao.get(nom)  // Récupère le joueur qui porte ce nom

            db.close()
        }

        thread.start()
        thread.join()

        return player
    }

    // Fonction qui insert des nouveaux joueurs
    fun addPlayer(joueur : Joueur) {

        val thread = Thread {
            val db = Room.databaseBuilder(
                this,
                DataBase::class.java,
                "bddJoueurs"
            ).build()

            val dao = db.JoueurDao()

             dao.insertAll(joueur)  // Insert nos joueurs

            db.close()
        }

        thread.start()
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