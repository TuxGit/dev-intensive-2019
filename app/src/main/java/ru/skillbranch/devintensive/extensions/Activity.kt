package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.graphics.Rect


fun Activity.hideKeyboard(): Unit {
    val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    //Find the currently focused view, so we can grab the correct window token from it.
    var view = this.currentFocus
    //If no view currently has focus, create a new one, just so we can grab a window token from it
    if (view == null) {
        view = View(this)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Activity.isKeyboardOpen(): Boolean {
    val r = Rect()
    val rootView = window.decorView
    rootView.getWindowVisibleDisplayFrame(r)
    val screenHeight = rootView.height

    // r.bottom is the position above soft keypad or device button.
    // if keypad is shown, the r.bottom is smaller than that before.
    val keypadHeight = screenHeight - r.bottom

    return keypadHeight > screenHeight * 0.15
}

fun Activity.isKeyboardClosed(): Boolean {
    return !this.isKeyboardOpen()
}
