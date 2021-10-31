package com.tejas.stackoverflow.database

import androidx.room.*
import com.tejas.stackoverflow.model.Owner
import com.tejas.stackoverflow.model.Question

@Dao
interface MainDao {

    @Transaction
    suspend fun insertAllQuestions(list: MutableList<Question?>) {
        deleteQuestions()
        insertQuestions(list)
        list.forEach {
            it?.owner?.let { owner ->
                insertOwner(owner)
            }
        }
    }

    @Query("DELETE FROM Question")
    fun deleteQuestions()

    @Query("DELETE FROM Owner")
    fun deleteOwners()

    @Transaction
    suspend fun getAllQuestions(): MutableList<Question?> {
        val list = getQuestions()
        list.forEach {
            it?.owner = getOwnerById(it?.owner_id)
        }
        return list
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(list: MutableList<Question?>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOwner(owner: Owner)

    @Query("SELECT * FROM Question")
    suspend fun getQuestions(): MutableList<Question?>

    @Query("SELECT * FROM Owner WHERE user_id=:id limit 1")
    suspend fun getOwnerById(id: Int?): Owner
}