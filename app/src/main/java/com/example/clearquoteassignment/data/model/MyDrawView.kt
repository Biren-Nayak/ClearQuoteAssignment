package com.example.clearquoteassignment.data.model

 import android.content.Context
 import android.graphics.*
 import android.util.AttributeSet
 import android.view.MotionEvent
 import android.view.View
 import android.view.View.OnTouchListener
 import androidx.appcompat.widget.AppCompatImageView


class MyDrawView  : AppCompatImageView, OnTouchListener {

    private lateinit var mCanvas: Canvas
    private lateinit var mMatrix: Matrix
    private lateinit var mPaint: Paint
    private var downX = 0F
    private var downY = 0F
    private var upX = 0F
    private var upY = 0F
    var doodleEnabled: Boolean = true
    var color: Int = Color.WHITE

    constructor(context: Context?) : super(context!!)
    { setOnTouchListener(this) }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs)
    { setOnTouchListener(this) }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int):
            super(context!!, attrs, defStyleAttr)
    { setOnTouchListener(this) }



    fun addText(text: String){
        mCanvas.drawText(text, 200F, 200F, mPaint)
        invalidate()
    }
    fun drawCircle(){
        mCanvas.drawCircle(450F, 450F, 250F, mPaint)
        invalidate()
    }
    fun drawSquare(){
        mCanvas.drawRect(200F, 200F, 700F, 700F, mPaint)
        invalidate()
    }


    private fun getPointerCords(e: MotionEvent): FloatArray {
        val index = e.actionIndex
        val cords = floatArrayOf(e.getX(index), e.getY(index))
        val matrix = Matrix()
        imageMatrix.invert(matrix)
        matrix.postTranslate(scrollX.toFloat(), scrollY.toFloat())
        matrix.mapPoints(cords)
        return cords
    }



    fun setNewImage(alteredBitmap: Bitmap, bmp: Bitmap) {
        mCanvas = Canvas(alteredBitmap)
        mPaint = Paint()
        mPaint.textSize = 100F
        setPaintColor(color)
        mMatrix = Matrix()
        mCanvas.drawBitmap(bmp, mMatrix, mPaint)
        setImageBitmap(alteredBitmap)
        mPaint.strokeWidth = 13F
    }

    fun setPaintColor(color: Int) {
        mPaint.color = color
    }

    override fun onTouch(p0: View?, event: MotionEvent): Boolean {
        if (doodleEnabled) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    downX = getPointerCords(event)[0]
                    downY = getPointerCords(event)[1]
                }
                MotionEvent.ACTION_MOVE -> {
                    upX = getPointerCords(event)[0]
                    upY = getPointerCords(event)[1]
                    mCanvas.drawLine(downX, downY, upX, upY, mPaint)
                    invalidate()
                    downX = upX
                    downY = upY
                }
                MotionEvent.ACTION_UP -> {
                    upX = getPointerCords(event)[0]
                    upY = getPointerCords(event)[1]
                    mCanvas.drawLine(downX, downY, upX, upY, mPaint)
                    invalidate()
                }
            }
        }
            return true

    }


}