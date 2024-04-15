package hu.ait.grocery.ui.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.ait.grocery.data.groceryItem
import hu.ait.grocery.data.groceryCategory
import java.util.Date
import androidx.compose.foundation.lazy.items

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import hu.ait.grocery.R
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    groceryViewModel: groceryViewModel = hiltViewModel(),
) {
    val coroutineScope = rememberCoroutineScope()

    var showAddDialog by rememberSaveable {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.shopping_list))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFE784BA)
                ),
                actions = {
                    IconButton(onClick = {
                        showAddDialog = true
                    }) {
                        Icon(Icons.Filled.Add, null)
                    }
                    IconButton(onClick = {
                        groceryViewModel.clearAllgrocerys()
                    }) {
                        Icon(Icons.Filled.Delete, null)
                    }
                }
            )
        },
        content = {
            groceryListContent(
                Modifier.padding(it), groceryViewModel
            )

            if (showAddDialog) {
                AddNewgroceryDialog(groceryViewModel,
                    onDismissRequest = {
                        showAddDialog = false
                    })
            }
        }
    )
}

@Composable
fun groceryListContent(
    modifier: Modifier,
    groceryViewModel: groceryViewModel
) {
    val groceryList by groceryViewModel.getAllgroceryList()
        .collectAsState(initial = emptyList())

    var showEditgroceryDialog by rememberSaveable {
        mutableStateOf(false)
    }
    var groceryToEdit: groceryItem? by rememberSaveable {
        mutableStateOf(null)
    }

    Column(
        modifier = modifier
    ) {
        // show groceryItems from the ViewModel in a LazyColumn
        if (groceryList.isEmpty()) {
            Text(text = "No items")
        } else {
            LazyColumn(modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize(fraction = 0.5f)) {
                items(groceryList) {
                    if (!it.isDone) {
                        groceryCard(it,
                            ongroceryCheckChange = { checkValue ->
                                groceryViewModel.changegroceryState(it, checkValue)
                            },
                            onRemoveItem = { groceryViewModel.removegroceryItem(it) },
                            onEditItem = {
                                groceryToEdit = it
                                showEditgroceryDialog = true
                            }
                        )
                    }
                }
            }

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(groceryList) {
                    if (it.isDone) {
                        groceryCard(it,
                            ongroceryCheckChange = { checkValue ->
                                groceryViewModel.changegroceryState(it, checkValue)
                            },
                            onRemoveItem = { groceryViewModel.removegroceryItem(it) },
                            onEditItem = {
                                groceryToEdit = it
                                showEditgroceryDialog = true
                            }
                        )
                    }
                }
            }

            if (showEditgroceryDialog) {
                AddNewgroceryDialog(groceryViewModel, groceryToEdit) {
                    showEditgroceryDialog = false
                }
            }
        }
    }
}

@Composable
fun groceryCard(
    groceryItem: groceryItem,
    ongroceryCheckChange: (Boolean) -> Unit = {},
    onRemoveItem: () -> Unit = {},
    onEditItem: (groceryItem) -> Unit = {}
) {
    var expanded by rememberSaveable { mutableStateOf(true)

    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = Modifier.padding(5.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .animateContentSize()
        ) {


            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = groceryItem.category.getIcon()),
                    contentDescription = stringResource(R.string.category),
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 10.dp)
                )


                Text(groceryItem.title, modifier = Modifier.fillMaxWidth(0.2f))
                Spacer(modifier = Modifier.fillMaxSize(0.55f))
                Checkbox(
                    checked = groceryItem.isDone,
                    onCheckedChange = { ongroceryCheckChange(it) }
                )
                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = stringResource(R.string.edit),
                    modifier = Modifier.clickable {
                        onEditItem(groceryItem)
                    },
                    tint = Color.Blue
                )
                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = stringResource(R.string.delete),
                    modifier = Modifier.clickable {
                        onRemoveItem()
                    },
                    tint = Color.Red
                )
                Spacer(modifier = Modifier.width(10.dp))
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else
                            Icons.Filled.KeyboardArrowDown,
                        contentDescription = if (expanded) {
                            stringResource(R.string.less)
                        } else {
                            stringResource(R.string.more)
                        }
                    )
                }
            }

            if (expanded) {
                Text(text = groceryItem.price)
                Text(text = groceryItem.description)
            }
        }
    }
}

@Composable
fun AddNewgroceryDialog(
    groceryViewModel: groceryViewModel,
    groceryToEdit: groceryItem? = null,
    onDismissRequest: () -> Unit
) {
    var groceryTitle by rememberSaveable {
        mutableStateOf(groceryToEdit?.title ?: "")
    }
    var groceryPrice by rememberSaveable {
        mutableStateOf(groceryToEdit?.price ?: "")
    }
    var groceryDescription by rememberSaveable {
        mutableStateOf(groceryToEdit?.description ?: "")
    }

    var selectedCategory by rememberSaveable {
        mutableStateOf(groceryToEdit?.category ?: groceryCategory.FRUIT)
    }

    var itemErrorState by rememberSaveable { mutableStateOf(false) }

    fun validate() {
        itemErrorState = groceryTitle.isEmpty()
    }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                Modifier.padding(16.dp)
            ) {
                Text(
                    text = if (groceryToEdit == null) stringResource(R.string.add_item) else stringResource(
                        R.string.edit_item
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center),
                    textAlign = TextAlign.Center,
                )

                OutlinedTextField(
                    value = groceryTitle,
                    onValueChange = { groceryTitle = it
                        validate()},
                    label = { Text(text = stringResource(R.string.enter_item_here)) },
                    isError = itemErrorState
                )
                if (itemErrorState) {
                    Text(
                        text = when {
                            itemErrorState -> stringResource(R.string.item_title_cannot_be_empty)
                            else -> ""
                        },
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }
                OutlinedTextField(
                    value = groceryPrice,
                    onValueChange = { groceryPrice = it },
                    label = { Text(text = stringResource(R.string.enter_item_price)) }
                )
                OutlinedTextField(
                    value = groceryDescription,
                    onValueChange = { groceryDescription = it },
                    label = { Text(text = stringResource(R.string.enter_item_description)) }
                )

                SpinnerSample(
                    list = groceryCategory.values().toList(),
                    preselected = selectedCategory,
                    onSelectionChanged = { selectedCategory = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = {
                            if (groceryTitle.isNotEmpty()) {
                                if (groceryToEdit == null) {
                                    groceryViewModel.addgroceryList(
                                        groceryItem(
                                            title = groceryTitle,
                                            price = groceryPrice,
                                            description = groceryDescription,
                                            createDate = Date(System.currentTimeMillis()).toString(),
                                            category = selectedCategory,
                                            isDone = false
                                        )
                                    )
                                } else {
                                    val editedgrocery = groceryToEdit.copy(
                                        title = groceryTitle,
                                        price = groceryPrice,
                                        description = groceryDescription,
                                        category = selectedCategory
                                    )
                                    groceryViewModel.editgroceryItem(editedgrocery)
                                }
                                onDismissRequest()
                            } else {
                                itemErrorState = true
                            }
                        },
                        enabled = groceryTitle.isNotEmpty()
                    ) {
                        Text(text = stringResource(R.string.save))
                    }
                    TextButton(onClick = { onDismissRequest() }) {
                        Text(text = stringResource(R.string.cancel))
                    }
                }
                }

            }
        }
    }

@Composable
fun SpinnerSample(
    list: List<groceryCategory>,
    preselected: groceryCategory,
    onSelectionChanged: (myData: groceryCategory) -> Unit,
    modifier: Modifier = Modifier
){
    var selected by remember { mutableStateOf(preselected) }
    var expanded by remember { mutableStateOf(false) } // initial value
    OutlinedCard(
        modifier = modifier.clickable {
            expanded = !expanded
        } ){
        Row(
            horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top,
        ){ Text(
            text = selected.name,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
            Icon(Icons.Outlined.ArrowDropDown, null, modifier = Modifier.padding(8.dp))
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }, modifier = Modifier.fillMaxWidth()
            ){
                list.forEach { listEntry ->
                    DropdownMenuItem( onClick = {
                        selected = listEntry
                        expanded = false
                        onSelectionChanged(selected)
                    },
                        text = {
                            Text(
                                text = listEntry.name,
                                modifier = Modifier
                            ) },
                    )
                } }}}}




