package com.jelly.thor.verificationet

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText


/**
 * 类描述：自定义验证码输入框 <br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2018/11/8 15:59 <br/>
 */
class VerificationEt : ViewGroup {

    companion object {
        /**
         * 圆形
         */
        const val CIRCULAR = 0
        /**
         * 方形
         */
        const val SQUARE = 1
    }

    /**
     * 默认输入框个数
     */
    private var mCount = 6
    /**
     * 外边框形状 默认方形
     */
    private var mOutShare: Int = SQUARE
    /**
     * 外边框大小
     */
    private var mOutSize = dp2px(20)
    /**
     * 内容大小
     */
    private var mInSize = sp2px(18)
    /**
     * 是否显示输入内容
     */
    private var mIsShow = true

    private var mOutStrokeWidth = dp2px(3)
    private var mInStrokeWidth = dp2px(3)

    /**
     * 外边框颜色
     */
    private var mOutColor: Int = 0
    private var mInColor: Int = 0
    private var mSelectColor: Int = 0
    private var mSpaceSize: Int = dp2px(2)

    private var mOnFocusChangeListener: OnFocusChangeListener? = null
    private lateinit var mEt: EditText

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.VerificationEt)

        mCount = typedArray.getInt(R.styleable.VerificationEt_numCount, 6)
        mIsShow = typedArray.getBoolean(R.styleable.VerificationEt_isShowInput, true)
        mOutShare = typedArray.getInt(R.styleable.VerificationEt_outShape, SQUARE)
        mOutSize = typedArray.getDimension(R.styleable.VerificationEt_outSize, dp2px(20).toFloat()).toInt()
        mInSize = typedArray.getDimensionPixelSize(
            R.styleable.VerificationEt_inSize,
            if (mIsShow) sp2px(18) else dp2px(18)
        )
        mSpaceSize = typedArray.getDimensionPixelSize(R.styleable.VerificationEt_spaceSize, dp2px(2))
        mOutStrokeWidth = typedArray.getInt(R.styleable.VerificationEt_outStrokeWidth, dp2px(3))
        mInStrokeWidth = typedArray.getInt(R.styleable.VerificationEt_inStrokeWidth, dp2px(3))
        mOutColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            typedArray.getColor(
                R.styleable.VerificationEt_outColor,
                resources.getColor(R.color.out_color, context.theme)
            )
        } else {
            typedArray.getColor(
                R.styleable.VerificationEt_outColor,
                resources.getColor(R.color.out_color)
            )
        }

        mInColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            typedArray.getColor(
                R.styleable.VerificationEt_inColor,
                resources.getColor(R.color.in_color, context.theme)
            )
        } else {
            typedArray.getColor(
                R.styleable.VerificationEt_inColor,
                resources.getColor(R.color.in_color)
            )
        }

        mSelectColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            typedArray.getColor(
                R.styleable.VerificationEt_selectColor,
                resources.getColor(R.color.select_color, context.theme)
            )
        } else {
            typedArray.getColor(
                R.styleable.VerificationEt_selectColor,
                resources.getColor(R.color.select_color)
            )
        }

        //内部必需小于外边大小
        if (mInSize > mOutSize) {
            mInSize = mOutSize
        }

        typedArray.recycle()

        //添加子布局
        addViews()
    }

    interface VerificationEtListener {
        fun get(data: String)
    }

    private var mListener: VerificationEtListener? = null
    /**
     * 1.设置数据回调
     */
    fun setVerificationEtListener(listener: VerificationEtListener) {
        mListener = listener
    }

    /**
     * 2.设置文本显示内容
     */
    fun setText(text: String) {
        var newText = text
        if (text.length > mCount) {
            newText = text.subSequence(0, mCount) as String
        }
        mEt.setText(newText)

        invalidateChildViews()
    }

    /**
     * 3.清空文本内容
     */
    fun clearText() {
        mEt.setText("")
        invalidateChildViews()
    }

    /**
     * 4.获取输入内容
     */
    fun getText(): String {
        return mEt.text.toString()
    }

    private fun invalidateChildViews() {
        for (i in 0 until mCount) {
            getChildAt(i).invalidate()
        }
    }

    /**
     * 弹起键盘
     */
    private fun showKeyboard() {
        mEt.requestFocus()

        val inputMethodManager = context
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(mEt, 0)
    }

    /**
     * 添加子布局
     */
    private fun addViews() {
        //重写onDraw，就要调用setWillNotDraw（false）
        setWillNotDraw(false)
        //添加一个et
        mEt = EditText(context)
        mEt.setBackgroundColor(resources.getColor(android.R.color.transparent))
        mEt.setTextColor(resources.getColor(android.R.color.transparent))
        mEt.isCursorVisible = false
        mEt.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(mCount))
        mEt.inputType = InputType.TYPE_CLASS_NUMBER
        mEt.keyListener = DigitsKeyListener.getInstance("1234567890")
        //横屏时禁止输入键盘全屏显示
        mEt.imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI
        mEt.setOnFocusChangeListener { v, hasFocus ->
            val length = mEt.text.length
            updateChildViewSelectionStates(length, hasFocus)
            mEt.setSelection(length)

            mOnFocusChangeListener?.onFocusChange(v, hasFocus);
        }
        mEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                val length = s.length
                updateChildViewSelectionStates(length, mEt.hasFocus())

                if (length == mCount) {
                    mListener?.get(s.toString())
                }
            }
        })

        //添加每个view
        for (i in 0 until mCount) {
            val configuration = VerificationConfiguration(
                i,
                mOutShare,
                mOutSize,
                mInSize,
                mOutColor,
                mInColor,
                mSelectColor,
                mIsShow,
                mOutStrokeWidth,
                mInStrokeWidth
            )
            val view =
                VerificationChildView(context, configuration, mEt)
            addView(view)
        }

        addView(mEt)

        //invalidate()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            showKeyboard()
            return true
        }
        return super.onTouchEvent(event)
    }

    private fun updateChildViewSelectionStates(length: Int, hasFocus: Boolean) {
        for (i in 0 until mCount) {
            getChildAt(i).isSelected = hasFocus && i == length
        }
    }

    private fun dp2px(dp: Int): Int {
        val metrics = resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), metrics).toInt()
    }

    private fun sp2px(sp: Int): Int {
        val metrics = resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp.toFloat(), metrics).toInt()
    }

    /**
     * 子类都不可以滚动
     */
    override fun shouldDelayChildPressedState(): Boolean {
        return false
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //1.测量子布局
        for (i in 0 until childCount) {
            getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec)
        }

        //2.计算大小 外边框大小 + 空白大小
        val widthSize = (mOutSize * mCount) + ((mCount - 1) * mSpaceSize)
        val nowWidthSize = widthSize + paddingStart + paddingEnd
        val nowHeightSize = mOutSize + paddingTop + paddingBottom
        setMeasuredDimension(nowWidthSize, nowHeightSize)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        //1.确定子View位置
        for (i in 0 until mCount) {
            val childView = getChildAt(i)
            //左边开始位置
            val left = (i * mOutSize) + (i * mSpaceSize)
            val nowLeft = left + paddingStart
            val right = nowLeft + mOutSize
            childView.layout(
                nowLeft,
                paddingTop,
                right,
                paddingTop + mOutSize
            )
        }

        //2.添加一个1px的Et
        getChildAt(mCount).layout(0, 0, 1, measuredHeight)
    }

    override fun getOnFocusChangeListener(): OnFocusChangeListener? {
        return mOnFocusChangeListener
    }

    override fun setOnFocusChangeListener(l: OnFocusChangeListener?) {
        mOnFocusChangeListener = l
    }
}