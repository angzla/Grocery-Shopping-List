package hu.ait.grocery.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.ait.grocery.data.groceryDAO
import hu.ait.grocery.data.groceryItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class groceryViewModel @Inject constructor(
    val groceryDAO: groceryDAO
): ViewModel() {

    fun getAllgroceryList(): Flow<List<groceryItem>> {
        return groceryDAO.getAllgrocerys()
    }

    suspend fun getAllgroceryNum(): Int {
        return groceryDAO.getgrocerysNum()
    }

    fun addgroceryList(groceryItem: groceryItem) {
        viewModelScope.launch {
            groceryDAO.insert(groceryItem)
        }
    }


    fun removegroceryItem(groceryItem: groceryItem) {
        viewModelScope.launch {
            groceryDAO.delete(groceryItem)
        }
    }

    fun editgroceryItem(editedgrocery: groceryItem) {
        viewModelScope.launch {
            groceryDAO.update(editedgrocery)
        }
    }

    fun changegroceryState(groceryItem: groceryItem, value: Boolean) {
        val changedgrocery = groceryItem.copy()
        changedgrocery.isDone = value
        viewModelScope.launch {
            groceryDAO.update(changedgrocery)
        }
    }

    fun clearAllgrocerys() {
        viewModelScope.launch {
            groceryDAO.deleteAllgrocerys()
        }
    }

}