package io.github.eh.eh.asutils

import io.github.eh.eh.R

class ScreenSizeClassifier {

    companion object {
        @JvmStatic
        fun getScreenSizeType(densityDpi:Int) : ScreenSize{
            when(densityDpi){
                in 1..120 -> {
                    return ScreenSize.LDPI
                }
                in 121..160 -> {
                    return ScreenSize.MDPI
                }
                in 161..240 -> {
                    return ScreenSize.HDPI
                }
                in 241..320 -> {
                    return ScreenSize.XHDPI
                }
                in 321..480 -> {
                    return ScreenSize.XXHDPI
                }
                in 481..640 -> {
                    return ScreenSize.XXXHDPI
                }
            }
            return ScreenSize.UNKNOWN
        }
    }


    enum class ScreenSize(val id:Int){
        UNKNOWN(-1),
        LDPI(R.drawable.intro_ldpi),
        MDPI(R.drawable.intro_mdpi),
        HDPI(R.drawable.intro_hdpi),
        XHDPI(R.drawable.intro_xdpi),
        XXHDPI(R.drawable.intro_xxdpi),
        XXXHDPI(R.drawable.intro_xxxdpi)
    }
}