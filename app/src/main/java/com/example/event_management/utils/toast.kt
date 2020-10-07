package com.example.event_management.utils

import android.content.Context
import android.widget.Toast.*

fun toast(context: Context, text: CharSequence) {
    makeText(context, text, LENGTH_SHORT).show()
}