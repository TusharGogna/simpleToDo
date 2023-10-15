package com.mdoc.simpletodo.addEditTodo

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.color.DynamicColors
import com.mdoc.simpletodo.R
import com.mdoc.simpletodo.TodoEvent
import com.mdoc.simpletodo.data.TodoEntity
import com.mdoc.simpletodo.data.TodoRepository
import com.mdoc.simpletodo.todolist.TodoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AddEditToDoActivity : AppCompatActivity() {
    private lateinit var viewModel: TodoViewModel
    private lateinit var item: TodoEntity
    private lateinit var edtTitle: EditText
    private lateinit var edtDescription: EditText
    private lateinit var btnUpdate: Button
    private lateinit var btnCancel: Button

    @Inject
    lateinit var repository: TodoRepository

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_todo)
        DynamicColors.applyToActivitiesIfAvailable(application)

        viewModel = ViewModelProvider(this).get(TodoViewModel::class.java)

        edtTitle = findViewById(R.id.edtTitle)
        edtDescription = findViewById(R.id.edtDescription)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnCancel = findViewById(R.id.btnCancel)

        btnUpdate.setOnClickListener {
            if (edtTitle.text.isEmpty()) {
                edtTitle.setError("Enter the Title before saving.")
                return@setOnClickListener
            }
            item = TodoEntity(edtTitle.text.toString(), edtDescription.text.toString(), false)
            lifecycleScope.launch {
                item.let {
                    viewModel.onEvent(TodoEvent.AddNewTodo)
                    repository.insertTodo(item)
                }
                finish()
            }
        }

        btnCancel.setOnClickListener { finish() }

        lifecycleScope.launch {
            val todoItem: String = intent.getStringExtra("event").toString()
            if (todoItem.contains("todoID")) {
                item = repository.getTodoById(todoItem.substringAfterLast("=").toInt())!!
                enableFields(false)
                edtTitle.setText(item.title)
                edtDescription.setText(item.description)
                btnUpdate.isVisible = false
            } else {
                btnUpdate.text = "Save"
                btnUpdate.isVisible = true
                enableFields(true)
            }
        }
    }

    private fun enableFields(flag: Boolean) {
        edtTitle.isEnabled = flag
        edtDescription.isEnabled = flag
    }
}