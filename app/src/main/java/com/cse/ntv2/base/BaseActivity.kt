package com.cse.ntv2.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

@Suppress("UNCHECKED_CAST")
abstract class BaseActivity<VB: ViewBinding> : AppCompatActivity() {

    protected val TAG = javaClass.name

    protected lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initLiveData()
        initRef()
        initView()
    }

    protected fun initBinding(){
        val type = javaClass.genericSuperclass
        val bclazz = (type as ParameterizedType).actualTypeArguments[0] as Class<VB>
        val method = bclazz.getMethod("inflate", LayoutInflater::class.java)
        binding = method.invoke(null, layoutInflater) as VB
        setContentView(binding.root)
    }

    abstract fun initRef()
    abstract fun initView()
    abstract fun initLiveData()

}