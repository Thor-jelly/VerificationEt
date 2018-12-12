package com.jelly.thor.verificationet

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.widget.EditText

/**
 * 类描述：显示框<br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2018/11/8 17:12 <br/>
 */
class VerificationChildView : View {
    private lateinit var mOutPaint: Paint
    private lateinit var mInPaint: Paint
    private lateinit var mConfiguration: VerificationConfiguration
    private lateinit var mEt: EditText

    constructor(
        context: Context,
        configuration: VerificationConfiguration,
        et: EditText
    ) : this(context) {
        mConfiguration = configuration
        mEt = et
        init()
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private fun init() {
        setWillNotDraw(false)
        //关闭硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        mOutPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mOutPaint.alpha = 255
        //抗拒齿
        mOutPaint.isAntiAlias = true
        //防抖动
        mOutPaint.isDither = true
        mOutPaint.style = Paint.Style.STROKE
        mOutPaint.strokeWidth = mConfiguration.outStrokeWidth.toFloat()
        mOutPaint.strokeCap = Paint.Cap.ROUND
        mOutPaint.strokeJoin = Paint.Join.ROUND
        mOutPaint.setShadowLayer(2F, 0F, 0F, Color.parseColor("#B4999999"))

        mInPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mInPaint.alpha = 255
        //抗拒齿
        mInPaint.isAntiAlias = true
        //防抖动
        mInPaint.isDither = true
        mInPaint.style = if (mConfiguration.isShowInput) Paint.Style.FILL else Paint.Style.FILL_AND_STROKE
        mInPaint.strokeWidth = mConfiguration.inStrokeWidth.toFloat()
        mInPaint.strokeCap = Paint.Cap.ROUND
        mInPaint.strokeJoin = Paint.Join.ROUND
        mInPaint.color = mConfiguration.inColor
        if (mConfiguration.isShowInput) {
            mInPaint.textSize =
                    if (mConfiguration.inSize.toFloat() > mConfiguration.outSize.toFloat()) mConfiguration.outSize.toFloat() else mConfiguration.inSize.toFloat()
            mInPaint.textAlign = Paint.Align.CENTER
        }

        //invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val size = mConfiguration.outSize
        setMeasuredDimension(size, size)
    }

    override fun onDraw(canvas: Canvas) {
        val center = (width / 2).toFloat()

        if (isSelected) {
            mOutPaint.color = mConfiguration.selectColor
        } else {
            mOutPaint.color = mConfiguration.outColor
        }

//        canvas.drawColor(Color.TRANSPARENT)
        canvas.drawColor(mConfiguration.inBackgroundColor)
        if (mConfiguration.outShape == VerificationEt.SQUARE) {
            val left = paddingStart
            val right = left + mConfiguration.outSize
            val top = paddingTop
            val bottom = top + mConfiguration.outSize
            canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), mOutPaint)
        } else {
            canvas.drawCircle(center, center, mConfiguration.outSize / 2 - mOutPaint.strokeWidth, mOutPaint)
        }
        if (mEt.text.length > mConfiguration.position) {
            if (mConfiguration.isShowInput) {
                val string = mEt.text.toString()
                val startPosition = mConfiguration.position
                val endPosition = startPosition + 1
                //计算基线baseLine位置 baseLine = centY +[(bottom -top)/2 - bottom]
                val fm = mInPaint.fontMetrics
                val baseLine = center - (fm.bottom + fm.top) / 2
                canvas.drawText(
                    string,
                    startPosition,
                    endPosition,
                    center,
                    baseLine,
                    mInPaint
                )
            } else {
                canvas.drawCircle(center, center, mConfiguration.inSize / 2 - mInPaint.strokeWidth, mInPaint)
            }
        }
    }
}