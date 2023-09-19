package com.mdoc.simpletodo.todolist.adapter

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.mdoc.simpletodo.R
import com.mdoc.simpletodo.TodoEvent
import com.mdoc.simpletodo.data.TodoEntity
import com.mdoc.simpletodo.todolist.TodoViewModel

class TodoListAdapter(
    private val list: List<TodoEntity>,
    val viewModel: TodoViewModel
) :
    RecyclerView.Adapter<TodoListAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTitle: TextView = itemView.findViewById(R.id.txtTitle)
        val txtDescription: CheckedTextView = itemView.findViewById(R.id.txtDescription)
        val cardView: CardView = itemView.findViewById(R.id.cardToDo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_todo, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemsViewModel = list[position]
        holder.txtTitle.text = itemsViewModel.title
        holder.txtDescription.text = itemsViewModel.description
        if (!itemsViewModel.isDone) {
            holder.txtDescription.setCheckMarkDrawable(
                android.R.drawable.checkbox_off_background
            )
        } else {
            holder.txtDescription.setCheckMarkDrawable(
                android.R.drawable.checkbox_on_background
            )
        }

        holder.cardView.setOnClickListener {
            viewModel.onEvent(TodoEvent.OnTodoClick(list[position]))
        }

        holder.txtTitle.setOnClickListener {
            viewModel.onEvent(TodoEvent.DeleteTodo(list[position]))
        }

        holder.txtDescription.setOnClickListener {
            holder.txtDescription.isChecked = !itemsViewModel.isDone

            holder.txtDescription.setCheckMarkDrawable(
                if (holder.txtDescription.isChecked) {
                    android.R.drawable.checkbox_on_background
                } else {
                    android.R.drawable.checkbox_off_background
                }
            )

            viewModel.onEvent(
                TodoEvent.OnDoneChange(
                    list[position],
                    holder.txtDescription.isChecked
                )
            )

            if (holder.txtDescription.isChecked) {
                holder.txtDescription.paintFlags =
                    holder.txtDescription.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                holder.txtTitle.paintFlags =
                    holder.txtTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                holder.txtDescription.paintFlags =
                    holder.txtDescription.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                holder.txtTitle.paintFlags =
                    holder.txtTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }
    }

}