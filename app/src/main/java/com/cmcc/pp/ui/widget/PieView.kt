package com.cmcc.pp.ui.widget

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.text.TextUtils
import android.util.AttributeSet
import android.widget.ImageView
import com.cmcc.pp.R
import com.cmcc.pp.entity.transactiondata.RepairOrder
import com.cmcc.pp.util.UIUtils


/**
 * Created by shopping on 2017/12/27 9:08.
 * https://github.com/wheroj
 */
class PieView : ImageView {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attr: AttributeSet?) : this(context, attr, -1)
    constructor(context: Context, attr: AttributeSet?, defStyleAttr: Int) : super(context, attr, defStyleAttr) {
        initPaint()
    }

    companion object {
        const val OTHER: Int = 0x04
        const val OTT: Int = 0x03
        const val BROAD_BAND: Int = 0x01
        const val AUDIO: Int = 0x02
//        val OTHER: Int = 0x01
//        val OTT: Int = 0x02
//        val BROAD_BAND: Int = 0x03
//        val AUDIO: Int = 0x04
    }

    /**
     * 内环的半径
     */
    private val innerRadius: Float = UIUtils.dip2px(50).toFloat()

    /**
     * 圆环的宽度
     */
    private val ringWidth: Float = UIUtils.dip2px(15).toFloat()

    /**
     * 指示线的宽度
     */
    private val lineWidth: Float = 2f

    /**
     * dotBitMap到圆环的距离
     */
    private val dotDistance: Float = UIUtils.dip2px(10).toFloat()

    /**
     * 文本到水平线的距离
     */
    private val textDistance: Float = UIUtils.dip2px(5).toFloat()

    /**
     * dotBitMap到拐点的距离
     */
    private val inflectionDistance: Float = UIUtils.dip2px(10).toFloat()

    /**
     * 拐点角度
     */
    private val inflectionAngel: Float = 45f

    /**
     * 文本的字体大小
     */
    private val textSize: Float = UIUtils.dip2px(9).toFloat()

    private var mCurrentAngle: Float = 0f
    private val mTotalAngle: Float = 360f
    private var pieDatas: ArrayList<PieData> = ArrayList()

    private lateinit var mPaint: Paint
    private fun initPaint() {
        mPaint = Paint()
        mPaint.isAntiAlias = true
        val font = Typeface.createFromAsset(context.assets, "vistayahei.ttf")
        mPaint.typeface = font
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mPaint.color = resources.getColor(R.color.transparent)
        mPaint.style = Paint.Style.FILL
        canvas?.drawCircle(measuredWidth / 2.toFloat(), measuredHeight / 2.toFloat(), innerRadius, mPaint)

        mCurrentAngle = 0f
        preSpinodal= null
        for (i in pieDatas.indices) {
            drawArcToView(canvas, pieDatas[i].color, pieDatas[i].sweepAngle)
            drawLineAndText(canvas, pieDatas[i])
        }
    }

    private var preSpinodal: PointF? = null
    private fun drawLineAndText(canvas: Canvas?, pieData: PieData) {

        //draw Dot
        var dotBitMap:Bitmap? = when (pieData.type) {
            OTHER -> (resources.getDrawable(R.drawable.bitmap_red_dot) as BitmapDrawable).bitmap
            OTT -> (resources.getDrawable(R.drawable.bitmap_green_dot) as BitmapDrawable).bitmap
            BROAD_BAND -> (resources.getDrawable(R.drawable.bitmap_blue_dot) as BitmapDrawable).bitmap
            AUDIO -> (resources.getDrawable(R.drawable.bitmap_yellow_dot) as BitmapDrawable).bitmap
            else -> {null }
        }

        var angle = mCurrentAngle - pieData.sweepAngle / 2
        val dotRadius = innerRadius + ringWidth + dotDistance
        var radius = dotRadius - (dotBitMap?.width)!!.div(2)
        val dotPoint = getDotPoint(angle, radius, dotBitMap.width.div(2), dotBitMap.height.div(2))
        if (dotBitMap != null) {
            canvas?.drawBitmap(dotBitMap, dotPoint.x, dotPoint.y, mPaint)
        }

        //drawLine -----start
        var spinodal = PointF()
        var endDot = PointF()
        var startPoint = PointF()
        startPoint.x = dotPoint.x + dotBitMap.width/2
        startPoint.y = dotPoint.y + dotBitMap.height/2
        caculateSpinodal(angle, startPoint, spinodal, endDot)

        var path = Path()
        path.moveTo(startPoint.x, startPoint.y)
        path.lineTo(spinodal.x, spinodal.y)
        path.lineTo(endDot.x, endDot.y)

        mPaint.style = Paint.Style.STROKE
        mPaint.color = when(pieData.type) {
            OTHER -> resources.getColor(R.color.red_alpha20)
            OTT -> resources.getColor(R.color.green_alpha20)
            BROAD_BAND -> resources.getColor(R.color.blue_alpha20)
            AUDIO -> resources.getColor(R.color.yellow_alpha20)
            else -> resources.getColor(R.color.transparent)
//            OTHER -> resources.getColor(R.color.red_feeae7)
//            OTT -> resources.getColor(R.color.green_acedbe)
//            BROAD_BAND -> resources.getColor(R.color.blue_b6ceff)
//            AUDIO -> resources.getColor(R.color.yellow_fee5bf)
//            else -> resources.getColor(R.color.transparent)
        }
        mPaint.strokeWidth = lineWidth

        canvas?.drawPath(path, mPaint)
        path.close()
        preSpinodal = spinodal
        //drawLine -----------end



        //drawText
        if (!TextUtils.isEmpty(pieData.name)) {
            drawString(canvas, pieData.name!!, -textDistance, spinodal, endDot)
        }
        if (!TextUtils.isEmpty(pieData.value)) {
            drawString(canvas, pieData.value!!, textDistance, spinodal, endDot)
        }
        //drawText-----------end
    }


    private fun caculateSpinodal(angle: Float, startPointF: PointF, spinodal: PointF,endDot: PointF) {
        when {
            angle <= 90 -> {
                spinodal.x = startPointF.x + Math.cos(Math.toRadians(inflectionAngel.toDouble())).toFloat()*inflectionDistance
                spinodal.y = startPointF.y + Math.sin(Math.toRadians(inflectionAngel.toDouble())).toFloat()*inflectionDistance

                if (preSpinodal != null) {
                    if (preSpinodal?.x!! > measuredWidth/2
                            && preSpinodal?.y!! + getTextNeedHeight() > spinodal.y) {
                        spinodal.y = preSpinodal?.y!! + getTextNeedHeight()
                    }
                }
                endDot.y = spinodal.y
                endDot.x = (measuredWidth - paddingRight).toFloat()
            }
            angle <= 180 -> {
                spinodal.x = startPointF.x - Math.cos(Math.toRadians(inflectionAngel.toDouble())).toFloat()*inflectionDistance
                spinodal.y = startPointF.y + Math.sin(Math.toRadians(inflectionAngel.toDouble())).toFloat()*inflectionDistance

                if (preSpinodal != null) {
                    if (preSpinodal?.x!! < measuredWidth/2
                            && preSpinodal?.y!! - getTextNeedHeight() < spinodal.y) {
                        spinodal.y = startPointF.y - Math.sin(Math.toRadians(inflectionAngel.toDouble())).toFloat()*inflectionDistance

                        if(preSpinodal?.y!! - getTextNeedHeight() < spinodal.y) {
                            spinodal.y = preSpinodal?.y!! - getTextNeedHeight()
                        }
                    }
                }
                endDot.y = spinodal.y
                endDot.x = paddingLeft.toFloat()
            }
            angle <= 270 -> {
                spinodal.x = startPointF.x - Math.cos(Math.toRadians(inflectionAngel.toDouble())).toFloat()*inflectionDistance
                spinodal.y = startPointF.y - Math.sin(Math.toRadians(inflectionAngel.toDouble())).toFloat()*inflectionDistance

                if (preSpinodal != null) {
                    if (preSpinodal?.x!! < measuredWidth/2
                            && preSpinodal?.y!! - getTextNeedHeight() < spinodal.y) {
                        spinodal.y = preSpinodal?.y!! - getTextNeedHeight()
                    }
                }
                endDot.y = spinodal.y
                endDot.x = paddingLeft.toFloat()
            }
            else -> {
                spinodal.x = startPointF.x + Math.cos(Math.toRadians(inflectionAngel.toDouble())).toFloat()*inflectionDistance
                spinodal.y = startPointF.y - Math.sin(Math.toRadians(inflectionAngel.toDouble())).toFloat()*inflectionDistance

                if (preSpinodal != null) {
                    if (preSpinodal?.x!! > measuredWidth/2
                            && preSpinodal?.y!! + getTextNeedHeight() > spinodal.y) {
                        spinodal.y = startPointF.y + Math.sin(Math.toRadians(inflectionAngel.toDouble())).toFloat()*inflectionDistance
                        if (preSpinodal?.y!! + getTextNeedHeight() > spinodal.y)
                            spinodal.y = preSpinodal?.y!! + getTextNeedHeight()
                    }
                }
                endDot.y = spinodal.y
                endDot.x = (measuredWidth - paddingRight).toFloat()
            }
        }

        if (endDot.y + getTextNeedHeight()/2 > measuredHeight) {
            layoutParams.height = (endDot.y + getTextNeedHeight()/2 + paddingBottom).toInt()
            requestLayout()
        }
    }

    private fun drawString(canvas: Canvas?, text: String, distance: Float, spinodal: PointF, endDot: PointF) {
        mPaint.textSize = textSize
        mPaint.style = Paint.Style.FILL
        mPaint.color = resources.getColor(R.color.gray_deep)

        if (!TextUtils.isEmpty(text)) {
            val length: Int = text.length
            var textHeight = mPaint.fontMetrics.bottom - mPaint.fontMetrics.top

            val bounds = Rect()
            mPaint.getTextBounds(text, 0, length, bounds)
            var textWidth = bounds.right - bounds.left

            var moveDistance = if (distance > 0) distance + textHeight/2 else distance
            if (endDot.x > spinodal.x) {
                canvas?.drawText(text, endDot.x - Math.abs(distance) - textWidth, endDot.y + moveDistance, mPaint)
            } else {
                canvas?.drawText(text, endDot.x + Math.abs(distance), endDot.y + moveDistance, mPaint)
            }
        }
    }

    /**
     * 绘制文本需要的高度
     */
    private fun getTextNeedHeight(): Float {
        mPaint.textSize = textSize
        mPaint.style = Paint.Style.FILL
        var textHeight = mPaint.fontMetrics.bottom - mPaint.fontMetrics.top
        return textHeight*2 + textDistance
    }

    private fun getDotPoint(angle: Float, radius: Float, bitMapHalfWidth: Int, bitMapHalfHeight: Int): PointF {
        var point = PointF()

        when {
            angle <= 90 -> {
                point.x = measuredWidth / 2 + (Math.cos(Math.toRadians(angle.toDouble())) * radius).toFloat()
                point.y = measuredHeight / 2 + (Math.sin(Math.toRadians(angle.toDouble())) * radius).toFloat()
            }
            angle <= 180 -> {
                point.x = measuredWidth / 2 - (Math.cos(Math.toRadians(180 - angle.toDouble())) * radius).toFloat() - bitMapHalfWidth
                point.y = measuredHeight / 2 + (Math.sin(Math.toRadians(180 - angle.toDouble())) * radius).toFloat() - bitMapHalfHeight
            }
            angle <= 270 -> {
                point.x = measuredWidth / 2 - (Math.cos(Math.toRadians(angle.toDouble() - 180)) * radius).toFloat() - bitMapHalfWidth
                point.y = measuredHeight / 2 - (Math.sin(Math.toRadians(angle.toDouble() - 180)) * radius).toFloat() - bitMapHalfHeight
            }
            else -> {
                point.x = measuredWidth / 2 + (Math.cos(Math.toRadians(360 - angle.toDouble())) * radius).toFloat()
                point.y = measuredHeight / 2 - (Math.sin(Math.toRadians(360 - angle.toDouble())) * radius).toFloat()
            }
        }
        return point
    }

    private fun drawArcToView(canvas: Canvas?, arcColor: Int, sweepAngle: Float) {
        mPaint.style = Paint.Style.STROKE
        mPaint.color = arcColor
        mPaint.strokeWidth = ringWidth
        var rectF = RectF()
        rectF.left = measuredWidth / 2 - innerRadius - ringWidth / 2
        rectF.top = measuredHeight / 2 - innerRadius - ringWidth / 2
        rectF.right = measuredWidth / 2 + innerRadius + ringWidth / 2
        rectF.bottom = measuredHeight / 2 + innerRadius + ringWidth / 2
        canvas?.drawArc(rectF, mCurrentAngle, sweepAngle, false, mPaint)
        mCurrentAngle += sweepAngle
    }

    fun setData(datas: ArrayList<RepairOrder>) {
        for (i in datas.indices) {
            try {
                var pieData = PieData()
                pieData.name = datas[i].itemName
                pieData.value = datas[i].statisticsValue
                var ratio = datas[i].ratio
                var type = datas[i].type
                pieData.color = when (type) {
                    OTHER -> resources.getColor(R.color.red_ff2f2c)
                    OTT -> resources.getColor(R.color.main_green)
                    BROAD_BAND -> resources.getColor(R.color.main_blue)
                    AUDIO -> resources.getColor(R.color.main_yellow)
                    else -> resources.getColor(R.color.transparent)
                }
                pieData.sweepAngle = ratio?.times(mTotalAngle)!!
                pieData.type = type
                pieDatas.add(pieData)
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            } catch (e: NullPointerException) {
                e.printStackTrace()
            }
        }
        invalidate()
    }

    class PieData {
        var name: String? = null
        var value: String? = null
        var sweepAngle: Float = 0f
        var color: Int = -1
        var type: Int? = 0
    }
}