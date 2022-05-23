package com.daffa.storyappcarita.view.customview

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText

class CustomEditTextPassword : AppCompatEditText, View.OnTouchListener {

    private var isFillPassword = false

    constructor(context: Context): super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        if(!isFillPassword){
            error = "Masukan password lebih dari 6 huruf"
        }
    }
    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        return false
    }

    private fun init(){
        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //set edit text harus lebih dari 6 karakter
                isFillPassword = s.length >= 6
            }

            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        })
    }
}