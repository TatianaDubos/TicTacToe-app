package com.example.tictactoe

import android.content.res.ColorStateList
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "joueurs")
data class Joueur(@ColumnInfo(name = "nom") @PrimaryKey(autoGenerate = false)val nom : String,
                  @ColumnInfo(name = "image") var image : Int,  @ColumnInfo(name = "score") var score: Int = 0 ) : Parcelable {
    // Le nom, symbole, couleur du symbole du joueur, pointage


}