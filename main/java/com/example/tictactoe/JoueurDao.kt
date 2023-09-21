package com.example.tictactoe

import androidx.room.*

@Dao
interface JoueurDao {

    @Query("SELECT * FROM joueurs")
    fun get() : List<Joueur>


    @Query("SELECT * FROM joueurs WHERE nom = :nom")
    fun get(nom:String) : Joueur

    @Insert
    fun insertAll(vararg list:Joueur)

    @Update
    fun updatePlayer(item: Joueur)


    @Delete
    fun deletePlayer(item: Joueur)


}
