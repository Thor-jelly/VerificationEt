# 自定义验证码输入框

[![GitHub release](https://img.shields.io/badge/release-v1.0.0-green.svg)]()

# 属性

```
        <!--输入框数量-->
        <attr name="numCount" format="integer"/>
        <!--外边框形状-->
        <attr name="outShape" format="enum">
            <enum name="circular" value="0"/>
            <enum name="square" value="1"/>
        </attr>
        <!--外边框大小-->
        <attr name="outSize" format="dimension"/>
        <!--内边框大小-也就是不显示数字时候有效-->
        <attr name="inSize" format="dimension"/>
        <!--外边框颜色-->
        <attr name="outColor" format="color|reference"/>
        <!--内边框颜色-->
        <attr name="inColor" format="color|reference"/>
        <!--选中颜色-->
        <attr name="selectColor" format="color|reference"/>
        <!--是否显示输入内容-->
        <attr name="isShowInput" format="boolean"/>
        <!--间隔大小-->
        <attr name="spaceSize" format="dimension"/>
        <!--外边框宽度-->
        <attr name="outStrokeWidth" format="integer"/>
        <attr name="inStrokeWidth" format="integer"/>
```

# 方法

```
    /**
     * 1.设置数据回调
     */
    fun setVerificationEtListener(listener: VerificationEtListener)

    /**
     * 2.设置文本显示内容
     */
    fun setText(text: String)

    /**
     * 3.清空文本内容
     */
    fun clearText()
    
    /**
     * 4.获取文本内容
     */
    fun getText(): String
```