package com.jelly.thor.verificationet

import androidx.annotation.IntRange

/**
 * 类描述：验证码配置<br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2018/11/9 09:50 <br/>
 */
data class VerificationConfiguration(
    val position: Int,//当前位置
    @IntRange(from = 0, to = 1) val outShape: Int,//外边框形状
    val outSize: Int,
    val inSize: Int,
    val outColor: Int,
    val inColor: Int,
    val inBackgroundColor: Int,
    val selectColor: Int,
    val isShowInput: Boolean,
    val outStrokeWidth: Int,
    val inStrokeWidth: Int
)