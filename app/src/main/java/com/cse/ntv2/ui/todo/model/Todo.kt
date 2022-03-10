package com.cse.ntv2.ui.todo.model

import java.io.Serializable

class Todo(val content: String, val appStr: String, var isDone: Boolean = false): Serializable