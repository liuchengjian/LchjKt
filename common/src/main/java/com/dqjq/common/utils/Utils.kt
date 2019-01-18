package com.dqjq.common.utils

import android.text.SpannableStringBuilder
import android.text.Spanned

object Utils {
    /**
     * 实现文字颜色渐变
     * @param string
     * @param startColor
     * @param endColor
     * @return
     */
    fun getRadiusGradientSpan(string: String, startColor: Int, endColor: Int): SpannableStringBuilder {
        val spannableStringBuilder = SpannableStringBuilder(string)
        val span = LinearGradientFontSpan(startColor, endColor)
        spannableStringBuilder.setSpan(span, 0, spannableStringBuilder.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannableStringBuilder
    }
}