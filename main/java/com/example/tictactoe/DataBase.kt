package com.example.tictactoe

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(version = 1, entities = [(Joueur::class)])
abstract class DataBase : RoomDatabase(){

    abstract fun JoueurDao(): JoueurDao
}