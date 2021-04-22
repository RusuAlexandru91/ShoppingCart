package com.example.andoid.shoppingcart.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@TypeConverters(Converters::class)
@Database(entities = [ProductEntity::class, CartEntity::class], version = 1)
abstract class MainDatabase : RoomDatabase(){

    abstract val productDAO : ProductDAO
    abstract val cartDAO : CartDAO

    //Singletone instance
    companion object{
        @Volatile
        private var INSTANCE: MainDatabase? = null
        fun getInstance(context: Context): MainDatabase{
            synchronized(this){
                var instance: MainDatabase? = INSTANCE
                if( instance == null ){
                    instance = Room.databaseBuilder(context.applicationContext, MainDatabase::class.java, "main_database")
                        .build()
                }
                return instance
            }
        }
    }
}