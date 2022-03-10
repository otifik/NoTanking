package com.cse.ntv2.ui.todo

import android.graphics.Color
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.aitsuki.swipe.SwipeMenuRecyclerView
import com.cse.ntv2.ui.main.MainViewModel
import com.cse.ntv2.NoTankingApplication
import com.cse.ntv2.R
import com.cse.ntv2.base.BaseFragment
import com.cse.ntv2.databinding.FragmentTodoBinding
import com.cse.ntv2.extension.APP_AVAILABLE
import com.cse.ntv2.extension.APP_NAME
import com.cse.ntv2.extension.shortToast
import com.cse.ntv2.ui.todo.adapter.TodoAdapter
import com.cse.ntv2.ui.todo.model.Todo
import com.cse.ntv2.util.ListDataSave
import com.kongzue.dialogx.dialogs.CustomDialog
import com.kongzue.dialogx.interfaces.OnBindView

class TodoFragment : BaseFragment<FragmentTodoBinding>() {

    private lateinit var todoAdapter: TodoAdapter
    private val todoList: ArrayList<Todo> = ArrayList()
    private lateinit var todoRecycler: SwipeMenuRecyclerView
    private lateinit var listDataSave: ListDataSave

    private val viewModel: MainViewModel by activityViewModels()

    override fun initRef() {
        todoRecycler = binding.todoRecycler
        listDataSave = ListDataSave(requireContext(),"data")
    }

    override fun initView() {

        val simpleItemAnimator = todoRecycler.itemAnimator as SimpleItemAnimator
        simpleItemAnimator.supportsChangeAnimations = false

        initTodoRecycler()

        viewModel.todoLiveData.observe(this){
            todoAdapter.todoList = it
            listDataSave.saveDataList("todolist",it)
        }

        binding.add.setOnClickListener {

            CustomDialog.show(object: OnBindView<CustomDialog>(R.layout.custom_add_todo_dialog){
                override fun onBind(dialog: CustomDialog, v: View) {

                    val textView: EditText = v.findViewById(R.id.content)
                    val appPicker: NumberPicker = v.findViewById(R.id.app_picker)
                    val okButton: TextView = v.findViewById(R.id.ok_button)
                    val cancelButton: TextView = v.findViewById(R.id.cancel_button)

                    var appIndex = 0
                    appPicker.run {
                        minValue = 0
                        maxValue = APP_NAME.size-1
                        displayedValues = APP_NAME
                        wrapSelectorWheel = false
                        descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
                    }

                    appPicker.setOnValueChangedListener { numberPicker, old, new ->
                        appIndex = new
                    }
                    okButton.setOnClickListener {
                        val text = textView.text
                        if (TextUtils.isEmpty(text)){
                            shortToast("请输入TODO内容")
                            return@setOnClickListener
                        }
                        todoAdapter.todoList.add(Todo(text.toString(), APP_AVAILABLE[appIndex],false))
                        viewModel.todoLiveData.postValue(todoAdapter.todoList)
                        todoAdapter.notifyItemInserted(todoAdapter.todoList.size)
                        dialog.dismiss()
                    }
                    cancelButton.setOnClickListener {
                        dialog.dismiss()
                    }
                }

            }).setMaskColor(Color.parseColor("#4D000000"))

        }
    }

    private fun initTodoRecycler(){

        val manager = LinearLayoutManager(NoTankingApplication.context)
        val decoration = DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL)
        todoAdapter = TodoAdapter(todoList)
        //初始化缓存的todolist
        val savedTodoList = listDataSave.getDataList("todolist", Todo::class.java) as ArrayList
        for (stl in savedTodoList){
            todoAdapter.todoList.add(stl)
        }
        viewModel.todoLiveData.postValue(todoAdapter.todoList)
        //设置删除时回调接口
        todoAdapter.setDeleteTodoListener(object : TodoAdapter.DeleteTodoListener{
            override fun deleteTodo(position: Int) {
                todoAdapter.todoList.removeAt(position)
                todoAdapter.notifyItemRemoved(position)
                viewModel.todoLiveData.postValue(todoAdapter.todoList)
            }
        })
        todoAdapter.setDoneStateChangeListener(object : TodoAdapter.DoneStateChangeListener{
            override fun doneStateChange(position: Int, isDone: Boolean) {
                todoAdapter.todoList[position].isDone = !isDone
                todoAdapter.notifyItemChanged(position)
                viewModel.todoLiveData.postValue(todoAdapter.todoList)
            }
        })
        todoRecycler.apply {
            adapter = todoAdapter
            layoutManager = manager
            addItemDecoration(decoration)
        }
    }

    override fun initLiveData() {}

}