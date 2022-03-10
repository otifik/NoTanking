package com.cse.ntv2.ui.todo.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cse.ntv2.NoTankingApplication
import com.cse.ntv2.R
import com.cse.ntv2.extension.addStrikeThrough
import com.cse.ntv2.extension.jumpToAPP
import com.cse.ntv2.extension.removeStrikeThrough
import com.cse.ntv2.ui.settings.AUTO_DONE
import com.cse.ntv2.ui.settings.SETTINGS_NAME
import com.cse.ntv2.ui.todo.model.Todo
import com.kongzue.dialogx.dialogs.MessageDialog

const val VIEW_TYPE_EMPTY = 0
const val VIEW_TYPE_TODO_APP = 1
const val VIEW_TYPE_TODO_NORMAL = 2
const val EMPTY_APP_STR = "NULL"

class TodoAdapter(var todoList: MutableList<Todo>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private lateinit var deleteTodoListener: DeleteTodoListener
    private lateinit var doneStateChangeListener: DoneStateChangeListener

    inner class TodoAppViewHolder(view: View): RecyclerView.ViewHolder(view){
        val content: TextView = view.findViewById(R.id.todo_content)
        val todoDone: TextView = view.findViewById(R.id.todo_done)
        val delete: TextView = view.findViewById(R.id.todo_delete)
        val jump: TextView = view.findViewById(R.id.todo_jump)
    }

    inner class TodoNormalViewHolder(view: View): RecyclerView.ViewHolder(view){
        val content: TextView = view.findViewById(R.id.todo_content)
        val todoDone: TextView = view.findViewById(R.id.todo_done)
        val delete: TextView = view.findViewById(R.id.todo_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_EMPTY -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.todolist_empty_view,parent,false)
                object : RecyclerView.ViewHolder(view){}
            }
            VIEW_TYPE_TODO_APP -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todo_app,parent,false)
                TodoAppViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todo_normal,parent,false)
                TodoNormalViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TodoAppViewHolder){
            val todo = todoList[position]
            holder.content.text = todo.content
            holder.delete.setOnClickListener {
                MessageDialog.show("提示","是否删除此TODO？","确认","取消")
                    .setOkButtonClickListener { _, _ ->
                    deleteTodoListener.deleteTodo(holder.adapterPosition)
                    false
                    }
                    .setCancelButton { _, _ ->
                        false
                    }
            }

            if(todo.isDone){
                holder.content.addStrikeThrough()
                holder.todoDone.setText(R.string.s_mark_undone)
            }else {
                holder.content.removeStrikeThrough()
                holder.todoDone.setText(R.string.s_mark_done)
            }

            val sharedPreferences = NoTankingApplication.context.getSharedPreferences(
                SETTINGS_NAME,
                Context.MODE_PRIVATE
            )

            if (sharedPreferences.getBoolean(AUTO_DONE,false)){
                holder.jump.setOnClickListener {
                    jumpToAPP(todo.appStr)
                    doneStateChangeListener.doneStateChange(holder.adapterPosition,todo.isDone)
                }
            }else{
                holder.jump.setOnClickListener {
                    jumpToAPP(todo.appStr)
                }
            }

            holder.todoDone.setOnClickListener {
                doneStateChangeListener.doneStateChange(holder.adapterPosition,todo.isDone)
            }

        }else if (holder is TodoNormalViewHolder){
            val todo = todoList[position]
            holder.content.text = todo.content
            holder.delete.setOnClickListener {
                MessageDialog.show("提示","是否删除此TODO？","确认","取消")
                    .setOkButtonClickListener { _, _ ->
                        deleteTodoListener.deleteTodo(holder.adapterPosition)
                        false
                    }
                    .setCancelButton { _, _ ->
                        false
                    }
            }

            if(todo.isDone){
                holder.content.addStrikeThrough()
                holder.todoDone.setText(R.string.s_mark_undone)
            }else {
                holder.content.removeStrikeThrough()
                holder.todoDone.setText(R.string.s_mark_done)
            }

            holder.todoDone.setOnClickListener {
                doneStateChangeListener.doneStateChange(holder.adapterPosition,todo.isDone)
            }
        }
    }

    override fun getItemCount() = when(todoList.size){
        0 -> 1
        else -> todoList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            todoList.size == 0 -> {
                VIEW_TYPE_EMPTY
            }
            todoList[position].appStr != EMPTY_APP_STR -> {
                VIEW_TYPE_TODO_APP
            }
            else -> {
                VIEW_TYPE_TODO_NORMAL
            }
        }
    }

    interface DeleteTodoListener{
        fun deleteTodo(position: Int)
    }

    interface DoneStateChangeListener{
        fun doneStateChange(position: Int,isDone: Boolean)
    }

    fun setDeleteTodoListener(deleteTodoListener: DeleteTodoListener){
        this.deleteTodoListener = deleteTodoListener
    }

    fun setDoneStateChangeListener(doneStateChangeListener: DoneStateChangeListener){
        this.doneStateChangeListener = doneStateChangeListener
    }

}