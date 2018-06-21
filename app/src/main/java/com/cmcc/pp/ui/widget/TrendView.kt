package com.cmcc.pp.ui.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import com.cmcc.pp.R
import com.cmcc.pp.util.UIUtils

/**
 * Created by shopping on 2018/1/4 14:56.
 * https://github.com/wheroj
 */
class TrendView : AppCompatTextView {

    private var mPaint: Paint = Paint()

    /**
     * 线宽
     */
    private var strokeWidth: Float = UIUtils.dip2px(2).toFloat()

    /**
     * 圆点半径
     */
    var dotRadius = UIUtils.dip2px(2).toFloat()

    /**
     *
     */
    private var lineColor: Int = resources.getColor(R.color.main_blue)

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, -1)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initPaint()
    }

    private fun initPaint() {
        mPaint.isAntiAlias = true
        mPaint.style = Paint.Style.FILL
        mPaint.strokeWidth = strokeWidth
        val font = Typeface.createFromAsset(context.assets, "vistayahei.ttf")
        mPaint.typeface = font
    }

    private var chartHeight: Int = 0
    private var chartMinHeight: Int = UIUtils.dip2px(135)
    private var valueDistance: Float = UIUtils.dip2px(5).toFloat()


    private var chartDatas: java.util.ArrayList<DataPoint> = java.util.ArrayList()

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (measuredHeight < chartMinHeight) {
            chartHeight = chartMinHeight
            layoutParams.height = chartMinHeight + paddingTop + paddingBottom
            requestLayout()
        }

        if (!chartDatas.isEmpty()) {
            drawDataToView(canvas)
        }
    }

    private fun drawDataToView(canvas: Canvas?) {
        val (maxValue, minValue) = getMaxYValue()
        val (unitY, unitX) = getXYUnit(maxValue, minValue)
        val textWidth = getLastTextWidth()

        val path = Path()
        path.fillType = Path.FillType.WINDING
        val startPoint = PointF()
        val endPoint = PointF()

        for (i in chartDatas.indices) {
            val dataPoint = chartDatas[i]
            dataPoint.pointY = paddingTop + chartHeight - dataPoint.yValue * unitY
            //目的 图表居中
            dataPoint.pointX = paddingLeft + i * unitX.toFloat() + (unitX - textWidth) / 2

            drawYValue(canvas, i - 1, dataPoint)

            endPoint.x = dataPoint.pointX!! + textWidth / 2
            endPoint.y = dataPoint.pointY!!
            if (i == 0) {
                startPoint.x = dataPoint.pointX!!
                startPoint.y = dataPoint.pointY!!

                path.moveTo(startPoint.x, startPoint.y)
                path.lineTo(endPoint.x, endPoint.y)

            } else {
                path.lineTo(endPoint.x, endPoint.y)
            }
            mPaint.color = lineColor
//            canvas?.drawCircle(endPoint.x, endPoint.y, dotRadius, mPaint)

            startPoint.x = endPoint.x
            startPoint.y = endPoint.y

            if (i == chartDatas.size - 1) {
                endPoint.x = startPoint.x + textWidth / 2
                endPoint.y = startPoint.y

                path.lineTo(endPoint.x, endPoint.y)
            }
        }
        mPaint.color = lineColor
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = strokeWidth
        canvas?.drawPath(path, mPaint)
        path.close()
    }

    /**
     * 获取最后一个 x轴文本 的宽度
     */
    private fun getLastTextWidth(): Int {
        val bounds = Rect()
        mPaint.getTextBounds(chartDatas[chartDatas.size - 1].yValue.toString()
                , 0, chartDatas[chartDatas.size - 1].yValue.toString().length, bounds)

        return bounds.right - bounds.left
    }


    /**
     * 画 y轴坐标
     */
    private fun drawYValue(canvas: Canvas?, i: Int, dataPoint: DataPoint) {
        mPaint.textSize = textSize
        mPaint.style = Paint.Style.FILL
        mPaint.color = resources.getColor(R.color.main_black)

        val textHeight = mPaint.fontMetrics?.bottom!! - mPaint.fontMetrics?.top!!

        //避免重叠
        if (i >= 0) {
            if (i + 2 < chartDatas.size
                    && chartDatas[i].yValue > dataPoint.yValue
                    && chartDatas[i + 2].yValue > dataPoint.yValue) {
                // 类似 V 型折线，y值显示在线下
                val y = Math.min((dataPoint.pointY!! + textHeight).toDouble()
                        , (chartHeight + paddingTop).toDouble())
                canvas?.drawText(dataPoint.yValue.toString(), dataPoint.pointX!!
                        , y.toFloat(), mPaint)
            } else {
                if (chartDatas[i].isDrawY) {
                    val bounds = Rect()
                    val preYValue = chartDatas[i].yValue.toString()
                    mPaint.getTextBounds(preYValue, 0, preYValue.length, bounds)
                    val preTextWidth = bounds.right - bounds.left

                    if (chartDatas[i].pointX!! + preTextWidth < dataPoint.pointX!!) {
                        canvas?.drawText(dataPoint.yValue.toString(), dataPoint.pointX!!
                                , dataPoint.pointY!! - valueDistance, mPaint)
                    } else {
                        if ((chartDatas[i].pointY!! - textHeight > dataPoint.pointY!!)
                                || (chartDatas[i].pointY!! < dataPoint.pointY!! - textHeight)) {
                            canvas?.drawText(dataPoint.yValue.toString(), dataPoint.pointX!!
                                    , dataPoint.pointY!! - valueDistance, mPaint)
                        } else {
                            dataPoint.isDrawY = false
                        }
                    }
                } else {
                    canvas?.drawText(dataPoint.yValue.toString(), dataPoint.pointX!!
                            , dataPoint.pointY!! - valueDistance, mPaint)
                }
            }
        } else {
            canvas?.drawText(dataPoint.yValue.toString(), dataPoint.pointX!!
                    , dataPoint.pointY!! - valueDistance, mPaint)
        }
    }

    /**
     * 获取x和y轴每一个单元格的宽度
     */
    private fun getXYUnit(maxValue: Float, minValue: Float): Pair<Float, Int> {
        var unitY = 0f
        if(maxValue + minValue != 0f) unitY = chartHeight / (maxValue + minValue)
        val unitX = (measuredWidth - paddingLeft - paddingRight) / chartDatas.size
        return Pair(unitY, unitX)
    }

    /**
     * 获取y轴最大值
     */
    private fun getMaxYValue(): Pair<Float, Float> {
        var maxValue = 0f
        var minValue = Int.MAX_VALUE.toFloat()
        chartDatas.indices
                .asSequence()
                .filter { maxValue < chartDatas[it].yValue }
                .forEach { maxValue = chartDatas[it].yValue }
        chartDatas.indices
                .asSequence()
                .filter { minValue > chartDatas[it].yValue }
                .forEach { minValue = chartDatas[it].yValue }
        return Pair(maxValue, minValue)
    }

    fun setData(data: ArrayList<String>) {
        chartDatas = ArrayList()
        for (i in data.indices) {
            val dataPoint = DataPoint()
            try {
                dataPoint.yValue = data[i].toFloat()
            } catch (e: NumberFormatException) {
                dataPoint.yValue = 0f
            }
            chartDatas.add(dataPoint)
        }
        invalidate()
    }

    fun setLineColor(lineColorResId: Int) {
        lineColor = lineColorResId
    }

    class DataPoint {
        var pointX: Float? = null
        var pointY: Float? = null
        var yValue: Float = 0f
        var isDrawY = true
    }
}