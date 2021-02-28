package com.example.customviewcounter

import android.content.Context
import android.content.res.TypedArray
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View.OnClickListener
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat


class QuantityView : FrameLayout {
    //Views
    private lateinit var subtract: ImageView
    private lateinit var quantity: TextView
    private lateinit var add: ImageView

    //Attributes
    private var minQuantity = 0
    private var maxQuantity = 0
    private var startQuantity = 0
    private var currentQuantity = 0
    private var deltaQuantity = 0
    private var colorOfDecrement = 0
    private var colorOfIncrement = 0
    private var colorOfText = 0

    /**
     * Constructor.
     *
     * @param context the context.
     */
    constructor(context: Context) : super(context) {
        obtainStyledAttributes(context, null, 0)
        init()
    }

    /**
     * Constructor.
     *
     * @param context the context.
     * @param attrs   the attributes from the layout.
     */
    constructor(context: Context, @Nullable attrs: AttributeSet?) : super(context, attrs) {
        obtainStyledAttributes(context, attrs, 0)
        init()
    }

    /**
     * Constructor.
     *
     * @param context      the context.
     * @param attrs        the attributes from the layout.
     * @param defStyleAttr the attributes from the default style.
     */
    constructor(
        context: Context,
        @Nullable attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        obtainStyledAttributes(context, attrs, defStyleAttr)
        init()
    }

    private fun obtainStyledAttributes(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        if (attrs != null) {
            val typedArray: TypedArray = context.theme
                .obtainStyledAttributes(attrs, R.styleable.QuantityView, defStyleAttr, 0)
            minQuantity = typedArray.getInteger(R.styleable.QuantityView_minQuantity, 0)
            maxQuantity = typedArray.getInteger(R.styleable.QuantityView_maxQuantity, 100)
            startQuantity = typedArray.getInteger(R.styleable.QuantityView_startQuantity, 0)
            deltaQuantity = typedArray.getInteger(R.styleable.QuantityView_deltaQuantity, 1)
            colorOfDecrement = typedArray.getResourceId(
                R.styleable.QuantityView_colorOfDecrement,
                R.color.decrement
            )
            colorOfIncrement = typedArray.getResourceId(
                R.styleable.QuantityView_colorOfIncrement,
                R.color.increment
            )
            colorOfText =
                typedArray.getResourceId(R.styleable.QuantityView_colorOfText, R.color.black)
            return
        }
        minQuantity = 0
        maxQuantity = 100
        startQuantity = 0
        deltaQuantity = 1
        colorOfDecrement = R.color.decrement
        colorOfIncrement = R.color.increment
    }

    private fun init() {
        inflate(context, R.layout.quantity_view, this)
        subtract = findViewById(R.id.subtract)
        quantity = findViewById(R.id.quantity)
        add = findViewById(R.id.add)
        subtract.setOnClickListener { setQuantity(true) }
        add.setOnClickListener { setQuantity(false) }
        setupView()
    }

    private fun setupView() {

        add.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_add))
        subtract.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_remove))
        setupColors()
        when {
            startQuantity <= minQuantity -> {
                currentQuantity = minQuantity
                subtract.isEnabled = false
                add.isEnabled = true
            }
            startQuantity >= maxQuantity -> {
                currentQuantity = maxQuantity
                subtract.isEnabled = true
                add.isEnabled = false
            }
            else -> {
                currentQuantity = startQuantity
                subtract.isEnabled = true
                add.isEnabled = true
            }
        }
        quantity.text = currentQuantity.toString()
    }

    private fun setupColors() {
        val decColor = ContextCompat.getColor(context, colorOfDecrement)
        val incColor = ContextCompat.getColor(context, colorOfIncrement)
        val addDrawable = (add.background as GradientDrawable).mutate()
        (addDrawable as GradientDrawable).setColor(incColor)
        val minusDrawable = (subtract.background as GradientDrawable).mutate()
        (minusDrawable as GradientDrawable).setColor(decColor)
//        add.drawable.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
//            incColor,
//            BlendModeCompat.SRC_ATOP
//        )
//        subtract.drawable.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
//            decColor,
//            BlendModeCompat.SRC_ATOP
//        )
    }

    private fun setQuantity(isSubtract: Boolean) {
        currentQuantity =
            if (isSubtract) subtractQuantity(currentQuantity) else addQuantity(currentQuantity)
        quantity.text = currentQuantity.toString()
    }

    private fun subtractQuantity(currentQuantity: Int): Int {
        var currentQuantity = currentQuantity
        if (currentQuantity - deltaQuantity <= minQuantity) {
            currentQuantity = minQuantity
            modifyViewClickable(isEnabled = false, isSubtract = true)
        } else {
            currentQuantity -= deltaQuantity
            modifyViewClickable(isEnabled = true, isSubtract = false)
        }
        return currentQuantity
    }

    private fun addQuantity(currentQuantity: Int): Int {
        var currentQuantity = currentQuantity
        if (currentQuantity + deltaQuantity >= maxQuantity) {
            currentQuantity = maxQuantity
            modifyViewClickable(isEnabled = false, isSubtract = false)
        } else {
            currentQuantity += deltaQuantity
            modifyViewClickable(isEnabled = true, isSubtract = true)
        }
        return currentQuantity
    }

    private fun modifyViewClickable(isEnabled: Boolean, isSubtract: Boolean) {
        if (isSubtract) {
            subtract.isEnabled = isEnabled
            return
        }
        add.isEnabled = isEnabled
    }

    /**
     * Method that gets the currently set value.
     *
     * @return the current value.
     */
    fun getQuantity(): Int {
        return currentQuantity
    }
}