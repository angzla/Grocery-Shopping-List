package hu.ait.grocery.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface groceryDAO {


    @Query("SELECT * from grocerytable")
    fun getAllgrocerys(): Flow<List<groceryItem>>

    @Query("SELECT * from grocerytable WHERE id = :id")
    fun getgrocery(id: Int): Flow<groceryItem>

    @Query("SELECT COUNT(*) from grocerytable")
    suspend fun getgrocerysNum(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(grocery: groceryItem)

    @Update
    suspend fun update(grocery: groceryItem)

    @Delete
    suspend fun delete(grocery: groceryItem)

    @Query("DELETE from grocerytable")
    suspend fun deleteAllgrocerys()
}