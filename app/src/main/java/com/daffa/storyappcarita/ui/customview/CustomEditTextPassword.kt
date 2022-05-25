package com.daffa.storyappcarita.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.daffa.storyappcarita.R

class CustomEditTextPassword : AppCompatEditText, View.OnTouchListener {
    private lateinit var imageLock: Drawable

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        transformationMethod = PasswordTransformationMethod.getInstance()
        setButtonDrawables(startOfTheText = imageLock)
        setPadding(32, 48, 32, 48)
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (text.toString().length < 6 && text!!.isNotEmpty()) {
                    error = "Jumlah password kurang dari 6 karakter!"
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        return false
    }

    private fun init() {
        setOnTouchListener(this)
        imageLock = ContextCompat.getDrawable(context, R.drawable.ic_baseline_lock_24) as Drawable
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        // Sets the Drawables (if any) to appear to the left of,
        // above, to the right of, and below the text.
        compoundDrawablePadding = 42
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }

}