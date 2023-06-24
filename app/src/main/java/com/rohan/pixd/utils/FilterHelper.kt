package com.rohan.pixd.utils

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter

class FilterHelper {

    fun getFilter(value: Int): ColorMatrixColorFilter? {
        when(value){
            0->{
                return null;
            }
            1->{
                val sepiaMatrix = ColorMatrix(floatArrayOf(
                    0.393f, 0.769f, 0.189f, 0f, 0f,
                    0.349f, 0.686f, 0.168f, 0f, 0f,
                    0.272f, 0.534f, 0.131f, 0f, 0f,
                    0f, 0f, 0f, 1f, 0f
                ))
                val sepiaFilter = ColorMatrixColorFilter(sepiaMatrix)
                return sepiaFilter
            }
            2->{
                val vintageMatrix = ColorMatrix(floatArrayOf(
                    0.8f, 0f, 0f, 0f, 0f,
                    0f, 0.9f, 0f, 0f, 0f,
                    0f, 0f, 0.6f, 0f, 0f,
                    0f, 0f, 0f, 1f, 0f
                ))
                val vintageFilter = ColorMatrixColorFilter(vintageMatrix)
                return vintageFilter
            }
            3->{
                val invertMatrix = ColorMatrix(floatArrayOf(
                    -1f, 0f, 0f, 0f, 255f,
                    0f, -1f, 0f, 0f, 255f,
                    0f, 0f, -1f, 0f, 255f,
                    0f, 0f, 0f, 1f, 0f
                ))
                val invertFilter = ColorMatrixColorFilter(invertMatrix)
                return invertFilter
            }
            4->{
                val brightnessMatrix = ColorMatrix(floatArrayOf(
                    1.2f, 0f, 0f, 0f, 0f,
                    0f, 1.2f, 0f, 0f, 0f,
                    0f, 0f, 1.2f, 0f, 0f,
                    0f, 0f, 0f, 1f, 0f
                ))
                val brightnessFilter = ColorMatrixColorFilter(brightnessMatrix)
                return brightnessFilter
            }
            5->{
                val contrastMatrix = ColorMatrix(floatArrayOf(
                    1.8f, 0f, 0f, 0f, -128f,
                    0f, 1.8f, 0f, 0f, -128f,
                    0f, 0f, 1.8f, 0f, -128f,
                    0f, 0f, 0f, 1f, 0f
                ))
                val contrastFilter = ColorMatrixColorFilter(contrastMatrix)
                return contrastFilter
            }
            6->{
                val saturationMatrix = ColorMatrix(floatArrayOf(
                    0.8f, 0f, 0f, 0f, 0f,
                    0f, 0.8f, 0f, 0f, 0f,
                    0f, 0f, 0.8f, 0f, 0f,
                    0f, 0f, 0f, 1f, 0f
                ))
                val saturationFilter = ColorMatrixColorFilter(saturationMatrix)
                return saturationFilter
            }
            7->{
                val inversionMatrix = ColorMatrix(floatArrayOf(
                    -1f, 0f, 0f, 0f, 255f,
                    0f, -1f, 0f, 0f, 255f,
                    0f, 0f, -1f, 0f, 255f,
                    0f, 0f, 0f, 1f, 0f
                ))
                val inversionFilter = ColorMatrixColorFilter(inversionMatrix)
                return inversionFilter
            }
            8->{
                val inversionMatrix = ColorMatrix(floatArrayOf(
                    -1f, 0f, 0f, 0f, 255f,
                    0f, -1f, 0f, 0f, 255f,
                    0f, 0f, -1f, 0f, 255f,
                    0f, 0f, 0f, 1f, 0f
                ))
                val inversionFilter = ColorMatrixColorFilter(inversionMatrix)
                return inversionFilter
            }
            else->{
                return null;
            }
        }
    }

}