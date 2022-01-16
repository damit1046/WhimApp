package com.android.app.core.extension

import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.core.text.HtmlCompat

fun TextView.makeHyperLink(url: String) {
    text = HtmlCompat.fromHtml("<a href='${url}'>${text}</a>", HtmlCompat.FROM_HTML_MODE_COMPACT)
    movementMethod = LinkMovementMethod.getInstance()
    isClickable = true
}