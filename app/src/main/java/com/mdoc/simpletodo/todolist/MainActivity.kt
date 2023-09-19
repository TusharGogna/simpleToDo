package com.mdoc.simpletodo.todolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Visibility
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.color.DynamicColors
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.mdoc.simpletodo.R
import com.mdoc.simpletodo.TodoEvent
import com.mdoc.simpletodo.addEditTodo.AddEditToDoActivity
import com.mdoc.simpletodo.data.TodoEntity
import com.mdoc.simpletodo.todolist.adapter.TodoListAdapter
import com.mdoc.simpletodo.utils.UiEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TodoListAdapter
    private lateinit var btnAdd: FloatingActionButton
    private lateinit var viewModel: TodoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        DynamicColors.applyToActivitiesIfAvailable(application)
        viewModel = ViewModelProvider(this).get(TodoViewModel::class.java)

        recyclerView = findViewById(R.id.rvTodo)
        btnAdd = findViewById(R.id.btnAdd)
        val txtEmptyList: TextView = findViewById(R.id.txtEmptyList)
        val lottieToDo: LottieAnimationView = findViewById(R.id.lottieToDo)

        btnAdd.setOnClickListener {
            viewModel.onEvent(TodoEvent.AddNewTodo)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            viewModel.allTodo.collect {
                if (it.isEmpty()) {
                    txtEmptyList.visibility = View.VISIBLE
                    lottieToDo.visibility = View.VISIBLE
                } else {
                    txtEmptyList.visibility = View.INVISIBLE
                    lottieToDo.visibility = View.INVISIBLE
                }
                adapter = TodoListAdapter(it, viewModel)
                recyclerView.adapter = adapter
            }
        }

        lifecycleScope.launch {
            viewModel.uiEvent.collect { uiEvent ->
                when (uiEvent) {
                    is UiEvent.Navigate -> {
                        val intent =
                            Intent(applicationContext, AddEditToDoActivity::class.java).apply {
                                putExtra("event", uiEvent.route)
                            }
                        startActivity(intent)
                    }

                    is UiEvent.ShowSnackbar -> {
                        Snackbar.make(recyclerView, uiEvent.msg, Snackbar.LENGTH_LONG)
                            .setAction(uiEvent.action) {
                                viewModel.onEvent(TodoEvent.OnUndoDeleteClick)
                            }.show()
                    }

                    else -> {
                        Unit
                    }
                }
            }
        }

    }
}