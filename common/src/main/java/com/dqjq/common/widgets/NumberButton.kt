package com.dqjq.common.widgets

import android.content.Context
import android.content.res.TypedArray
import android.text.Editable
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.dqjq.common.R

/**
 * 购物车商品数量、增加和减少控制按钮。
 */
class NumberButton @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    LinearLayout(context, attrs), View.OnClickListener, TextWatcher {
    //库存
    private var mInventory = Integer.MAX_VALUE
    //最大购买数，默认无限制
    private var mBuyMax = Integer.MAX_VALUE
    var editText: EditText? = null
        private set

    private var mOnWarnListener: OnWarnListener? = null

    val number: Int
        get() {
            try {
                return Integer.parseInt(editText!!.text.toString())
            } catch (e: NumberFormatException) {
            }

            editText!!.setText("1")
            return 1
        }

    fun setmCount(mCount: EditText) {
        this.editText = mCount
    }

    init {
        init(context, attrs)
    }
    //
    //    public NumberButton(Context context, AttributeSet attrs, int defStyleAttr) {
    //        super(context, attrs, defStyleAttr);
    //    }

    private fun init(context: Context, attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(R.layout.layout, this)

        val addButton = findViewById<View>(R.id.button_add) as TextView
        addButton.setOnClickListener(this)
        val subButton = findViewById<View>(R.id.button_sub) as TextView
        subButton.setOnClickListener(this)

        editText = findViewById<View>(R.id.text_count) as EditText
        editText!!.addTextChangedListener(this)
        editText!!.setOnClickListener(this)


        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.NumberButton)
        val editable = typedArray.getBoolean(R.styleable.NumberButton_editable, true)
        val buttonWidth = typedArray.getDimensionPixelSize(R.styleable.NumberButton_buttonWidth, -1)
        val textWidth = typedArray.getDimensionPixelSize(R.styleable.NumberButton_textWidth, -1)
        val textSize = typedArray.getDimensionPixelSize(R.styleable.NumberButton_textSize, -1)
        val textColor = typedArray.getColor(R.styleable.NumberButton_textColor, -0x1000000)
        typedArray.recycle()

        setEditable(editable)
        editText!!.setTextColor(textColor)
        subButton.setTextColor(textColor)
        addButton.setTextColor(textColor)

        if (textSize > 0)
            editText!!.textSize = textSize.toFloat()

        if (buttonWidth > 0) {
            val textParams = LinearLayout.LayoutParams(buttonWidth, LinearLayout.LayoutParams.MATCH_PARENT)
            subButton.layoutParams = textParams
            addButton.layoutParams = textParams
        }
        if (textWidth > 0) {
            val textParams = LinearLayout.LayoutParams(textWidth, LinearLayout.LayoutParams.MATCH_PARENT)
            editText!!.layoutParams = textParams
        }
    }

    override fun onClick(v: View) {
        val id = v.id
        val count = number
        if (id == R.id.button_sub) {
            if (count > 1) {
                //正常减
                editText!!.setText("" + (count - 1))
            }

        } else if (id == R.id.button_add) {
            if (count < Math.min(mBuyMax, mInventory)) {
                //正常添加
                editText!!.setText("" + (count + 1))
            } else if (mInventory < mBuyMax) {
                //库存不足
                warningForInventory()
            } else {
                //超过最大购买数
                warningForBuyMax()
            }

        } else if (id == R.id.text_count) {
            editText!!.setSelection(editText!!.text.toString().length)
        }
    }

    private fun onNumberInput() {
        //当前数量
        val count = number
        if (count <= 0) {
            //手动输入
            editText!!.setText("1")
            return
        }

        val limit = Math.min(mBuyMax, mInventory)
        if (count > limit) {
            //超过了数量
            editText!!.setText(limit.toString() + "")
            if (mInventory < mBuyMax) {
                //库存不足
                warningForInventory()
            } else {
                //超过最大购买数
                warningForBuyMax()
            }
        }

    }

    /**
     * 超过的库存限制
     * Warning for inventory.
     */
    private fun warningForInventory() {
        if (mOnWarnListener != null) mOnWarnListener!!.onWarningForInventory(mInventory)
    }

    /**
     * 超过的最大购买数限制
     * Warning for buy max.
     */
    private fun warningForBuyMax() {
        if (mOnWarnListener != null) mOnWarnListener!!.onWarningForBuyMax(mBuyMax)
    }


    private fun setEditable(editable: Boolean) {
        if (editable) {
            editText!!.isFocusable = true
            editText!!.keyListener = DigitsKeyListener()
        } else {
            editText!!.isFocusable = false
            editText!!.keyListener = null
        }
    }

    fun setCurrentNumber(currentNumber: Int): NumberButton {
        if (currentNumber < 1) editText!!.setText("1")
        editText!!.setText("" + Math.min(Math.min(mBuyMax, mInventory), currentNumber))
        return this
    }

    fun getInventory(): Int {
        return mInventory
    }

    fun setInventory(inventory: Int): NumberButton {
        mInventory = inventory
        return this
    }

    fun getBuyMax(): Int {
        return mBuyMax
    }

    fun setBuyMax(buyMax: Int): NumberButton {
        mBuyMax = buyMax
        return this
    }

    fun setOnWarnListener(onWarnListener: OnWarnListener): NumberButton {
        mOnWarnListener = onWarnListener
        return this
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        onNumberInput()
    }

    override fun afterTextChanged(s: Editable) {

    }

    interface OnWarnListener {
        fun onWarningForInventory(inventory: Int)

        fun onWarningForBuyMax(max: Int)
    }
}

