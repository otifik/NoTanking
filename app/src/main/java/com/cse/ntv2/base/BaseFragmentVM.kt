package com.cse.ntv2.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

@Suppress("UNCHECKED_CAST")
abstract class BaseFragmentVM<VB: ViewBinding,VM: ViewModel> : BaseFragment<VB>() {

    protected lateinit var viewModel: VM

    override fun initLiveData() {
        val type = javaClass.genericSuperclass
        val mclazz = (type as ParameterizedType).actualTypeArguments[1] as Class<VM>
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[mclazz]
    }
}