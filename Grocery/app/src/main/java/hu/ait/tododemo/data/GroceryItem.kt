package hu.ait.grocery.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import hu.ait.grocery.R
import java.io.Serializable

@Entity(tableName = "grocerytable")
data class groceryItem(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "title") val title:String,
    @ColumnInfo(name = "price") val price:String,
    @ColumnInfo(name = "description") val description:String,
    @ColumnInfo(name = "createDate") val createDate:String,
    @ColumnInfo(name = "category") var category:groceryCategory,
    @ColumnInfo(name = "isDone") var isDone: Boolean
) : Serializable

enum class groceryCategory {
    FRUIT, MEAT, BREAD;

    fun getIcon(): Int {
        return if (this == FRUIT) R.drawable.fruit
        else if (this==MEAT) R.drawable.meat
        else R.drawable.bread
    }
}



