package com.cse.ntv2.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.cse.ntv2.ui.todo.model.Todo

class MainViewModel : ViewModel() {

    val todoLiveData = MutableLiveData<MutableList<Todo>>()

    val count = Transformations.map(todoLiveData){
        var cnt = 0
        for (data in it){
            if (!data.isDone){
                cnt++
            }
        }
        cnt
    }



}