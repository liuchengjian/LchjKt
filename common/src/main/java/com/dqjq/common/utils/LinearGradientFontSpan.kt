package com.dqjq.common.utils

import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.text.style.ReplacementSpan

/**
 * 实现文本渐变
 * @author lchj
 */
class LinearGradientFontSpan(private val startColor: Int, private val endColor: Int) : ReplacementSpan() {
    private var mSize: Int = 0
    override fun getSize(paint: Paint, text: CharSequence, start: Int, end: Int, fm: Paint.FontMetricsInt?): Int {
        mSize = paint.measureText(text, start, end).toInt()

        //这段不可以去掉，字体高度没设置，可能出现draw（）方法没有被调用，如果你调用SpannableStringBuilder后append一个字符串，效果也是会出来，下面这段就不需要了
        // 原因详见https://stackoverflow.com/questions/20069537/replacementspans-draw-method-isnt-called
        val metrics = paint.fontMetricsInt
        if (fm != null) {
            fm.top = metrics.top
            fm.ascent = metrics.ascent
            fm.descent = metrics.descent
            fm.bottom = metrics.bottom
        }

        return mSize
    }

    override fun draw(canvas: Canvas, text: CharSequence, start: Int, end: Int, x: Float, top: Int, y: Int, bottom: Int, paint: Paint) {
        val lg = LinearGradient(0f, 0f, 0f, paint.descent() - paint.ascent(),
                startColor,
                endColor,
                Shader.TileMode.REPEAT) //从上到下渐变
        paint.shader = lg
        canvas.drawText(text, start, end, x, y.toFloat(), paint)//绘制文字
    }


}
