package com.cmcc.pp.ui.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.ImageView
import com.cmcc.pp.R
import com.cmcc.pp.util.UIUtils

/**
 * Created by shopping on 2017/12/28 11:25.
 * https://github.com/wheroj
 */
class FillLineChart : ImageView {

    private var chartHeight: Int = UIUtils.dip2px(170)
    private var textHorizontalLine: Float = 0f
    private var textSize: Float = UIUtils.dip2px(12).toFloat()
    private var strokeWidth: Float = UIUtils.dip2px(2).toFloat()
    private var valueDistance: Float = UIUtils.dip2px(5).toFloat()
    private var xValueSpace: Float = UIUtils.dip2px(10).toFloat()
    private val xValueDistance: Float = UIUtils.dip2px(5).toFloat()

    private var hadMeasureHeight = false

    /**
     * 0.2 透明度
     */
    private var backGroundAlpha: Int = (0.3 * 255).toInt()

    /**
     * 填充背景颜色
     */
    private var backGroundColor = resources.getColor(R.color.blue_ebf1ff)

    private var mPaint: Paint? = null
    private var chartDatas: ArrayList<DataPoint> = ArrayList()

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attr: AttributeSet?) : this(context, attr, -1)
    constructor(context: Context, attr: AttributeSet?, defStyleAttr: Int) : super(context, attr, defStyleAttr) {
        initPaint()
    }

    private fun initPaint() {
        mPaint = Paint()
        mPaint?.isAntiAlias = true
        val font = Typeface.createFromAsset(context.assets, "vistayahei.ttf")
        mPaint?.typeface = font
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (!chartDatas.isEmpty()) {
            drawDataToView(canvas)
        }
    }

    private fun drawDataToView(canvas: Canvas?) {
        val maxValue = getMaxYValue()
        val (unitY, unitX) = getXYUnit(maxValue)
        val textWidth = getLastTextWidth()

        caculateHorizontalLine(canvas)

        val path = Path()
        path.fillType = Path.FillType.WINDING
        val startPoint = PointF()
        val endPoint = PointF()

        for (i in chartDatas.indices) {
            val dataPoint = chartDatas[i]
            var yFloatValue = try {
                dataPoint.trendValue.toFloat()
            } catch (e: Exception) {
                0f
            }
            dataPoint.pointY = paddingTop + chartHeight - yFloatValue * unitY
            //目的 图表居中
            dataPoint.pointX = paddingLeft + i * unitX.toFloat() + (unitX - textWidth) / 2

            drawXValue(canvas, i - 1, dataPoint)
            drawYValue(canvas, i - 1, dataPoint)

            //-----------填充颜色  画折线------------------
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

//            drawBack(startPoint, endPoint, canvas)
            fillBack(canvas, startPoint, endPoint)
            startPoint.x = endPoint.x
            startPoint.y = endPoint.y

            if (i == chartDatas.size - 1) {
                endPoint.x = startPoint.x + textWidth / 2
                endPoint.y = startPoint.y
//                drawBack(startPoint, endPoint, canvas)
                fillBack(canvas, startPoint, endPoint)

                path.lineTo(endPoint.x, endPoint.y)
            }
        }
        mPaint?.color = resources.getColor(R.color.main_blue)
        mPaint?.style = Paint.Style.STROKE
        mPaint?.strokeWidth = strokeWidth
        canvas?.drawPath(path, mPaint)
        path.close()
    }

    /**
     * 获取最后一个 x轴文本 的宽度
     */
    private fun getLastTextWidth(): Int {
        val bounds = Rect()
        mPaint?.getTextBounds(chartDatas[chartDatas.size - 1].dayTime, 0, chartDatas[chartDatas.size - 1].dayTime?.length!!, bounds)

        return bounds.right - bounds.left
    }

    /**
     * 绘制水平线
     */
    private fun caculateHorizontalLine(canvas: Canvas?) {
        mPaint?.textSize = textSize
        var textHeight = mPaint?.fontMetrics?.bottom!! - mPaint?.fontMetrics?.top!!
        textHorizontalLine = paddingTop + chartHeight + xValueDistance + textHeight

        if(!hadMeasureHeight) {
            layoutParams.height = (textHorizontalLine + paddingBottom).toInt()
            requestLayout()
            hadMeasureHeight = true
        }

//        mPaint?.style = Paint.Style.STROKE
//        mPaint?.color = resources.getColor(R.color.light_line)
//        mPaint?.strokeWidth = strokeWidth
//        canvas?.drawLine(paddingLeft.toFloat(), (paddingTop + chartHeight).toFloat()
//                , (measuredWidth - paddingRight).toFloat(), (paddingTop + chartHeight).toFloat(), mPaint)
    }

    /**
     * 绘制背景
     */
    private fun drawBack(startPoint: PointF, endPoint: PointF, canvas: Canvas?) {
        mPaint?.color = backGroundColor
        mPaint?.alpha = backGroundAlpha
        mPaint?.style = Paint.Style.FILL

        var floats = getFillPoint(startPoint, endPoint)
        canvas?.drawPoints(floats, mPaint)
    }

    /**
     * 画 y轴坐标
     */
    private fun drawYValue(canvas: Canvas?, i: Int, dataPoint: DataPoint) {
        mPaint?.textSize = textSize
        mPaint?.style = Paint.Style.FILL
        mPaint?.color = resources.getColor(R.color.main_black)

        val textHeight = mPaint?.fontMetrics?.bottom!! - mPaint?.fontMetrics?.top!!

        //避免重叠
        if (i >= 0) {
            //这里的i实际为 i-1
            if (i + 2 < chartDatas.size
                    && chartDatas[i].trendValue > dataPoint.trendValue
                    && chartDatas[i + 2].trendValue > dataPoint.trendValue) {
                // 类似 V 型折线，y值显示在线下
                var y = Math.min((dataPoint.pointY!! + textHeight).toDouble()
                        , (chartHeight + paddingTop).toDouble())
                canvas?.drawText(dataPoint.trendValue, dataPoint.pointX!!
                        , y.toFloat(), mPaint)
            } else {
                var lastDrawIndex = i
                while (lastDrawIndex >= 0) {
                    if (chartDatas[lastDrawIndex].isDrawY) {
                        var bounds = Rect()
                        val preYValue = chartDatas[lastDrawIndex].trendValue
                        mPaint?.getTextBounds(preYValue, 0, preYValue.length!!, bounds)
                        var preTextWidth = bounds.right - bounds.left

                        if (chartDatas[lastDrawIndex].pointX!! + preTextWidth < dataPoint.pointX!!) {
                            canvas?.drawText(dataPoint.trendValue, dataPoint.pointX!!
                                    , dataPoint.pointY!! - valueDistance, mPaint)
                            dataPoint.isDrawY = true
                        } else {
                            if ((chartDatas[lastDrawIndex].pointY!! - textHeight > dataPoint.pointY!!)
                                    || (chartDatas[lastDrawIndex].pointY!! < dataPoint.pointY!! - textHeight)) {
                                canvas?.drawText(dataPoint.trendValue, dataPoint.pointX!!
                                        , dataPoint.pointY!! - valueDistance, mPaint)
                                dataPoint.isDrawY = true
                            } else {
                                dataPoint.isDrawY = false
                            }
                        }
                        break
                    }
                    lastDrawIndex--
                }
            }
        } else {
            canvas?.drawText(dataPoint.trendValue, dataPoint.pointX!!
                    , dataPoint.pointY!! - valueDistance, mPaint)
        }
    }

    /**
     * 画x轴坐标
     */
    private fun drawXValue(canvas: Canvas?, i: Int, dataPoint: DataPoint) {
        mPaint?.textSize = textSize
        mPaint?.style = Paint.Style.FILL
        mPaint?.color = resources.getColor(R.color.main_black)

        var bounds = Rect()
        if (i >= 0) {

            //最近一条
            var lastDrawIndex = i
            while (lastDrawIndex >= 0) {
                if (chartDatas[lastDrawIndex].isDrawX) {
                    mPaint?.getTextBounds(dataPoint.dayTime, 0, dataPoint.dayTime?.length!!, bounds)
                    var textWidth = bounds.right - bounds.left

                    // x轴距离能画得下 才画
                    if (chartDatas[lastDrawIndex].pointX?.plus(textWidth)!!.plus(xValueSpace) < dataPoint.pointX!!) {
                        canvas?.drawText(dataPoint.dayTime, dataPoint.pointX!!, textHorizontalLine, mPaint)
                        dataPoint.isDrawX = true
                    } else {
                        dataPoint.isDrawX = false
                    }
                    break
                }
                lastDrawIndex--
            }
        } else {
            canvas?.drawText(dataPoint.dayTime, dataPoint.pointX!!, textHorizontalLine, mPaint)
        }
    }

    /**
     * 获取x和y轴每一个单元格的宽度
     */
    private fun getXYUnit(maxValue: Float): Pair<Float, Int> {
        var unitY = chartHeight / calculateMax(maxValue)
        var unitX = (measuredWidth - paddingLeft - paddingRight) / chartDatas.size
        return Pair(unitY, unitX)
    }

    private fun calculateMax(nowMax: Float): Float {
        var nowMax = nowMax
        var lastMax = 1f

        var rem = 0
        while (nowMax >= 10) {
            rem = (nowMax % 10).toInt()
            nowMax /= 10
            lastMax *= 10f
        }
        lastMax = (rem + 5) * (lastMax / 10) + nowMax.toInt() * lastMax
        return lastMax
    }

    /**
     * 获取y轴最大值
     */
    private fun getMaxYValue(): Float {
        var maxValue = 0f
        try {
            chartDatas.indices
                    .asSequence()
                    .filter { maxValue < chartDatas[it].trendValue.toFloat() }
                    .forEach { maxValue = chartDatas[it].trendValue.toFloat() }
            return maxValue
        } catch (e: Exception) {
            return Float.MAX_VALUE
        }
    }

    /**
     * 获取需要填充的点
     */
    private fun getFillPoint(startPoint: PointF, endPoint: PointF): FloatArray {
        var pointArray = ArrayList<Float>()
        run {
            var x = startPoint.x
            while (x < endPoint.x) {
                run {
                    val min = Math.min(startPoint.y, endPoint.y)
                    var y = min
                    while (y < chartHeight + paddingTop) {
                        val pointF = PointF(x, y)
                        if (isUnderLine(startPoint, endPoint, pointF)) {
                            pointArray.add(pointF.x)
                            pointArray.add(pointF.y)
                        }
                        y++
                    }
                }
                x++
            }
        }
        return pointArray.toFloatArray()
    }

    /**
     * 获取需要填充的点
     */
    private fun fillBack(canvas: Canvas?, startPoint: PointF, endPoint: PointF): FloatArray {
        mPaint?.color = backGroundColor
        mPaint?.alpha = backGroundAlpha
        mPaint?.style = Paint.Style.FILL

        var pointArray = ArrayList<Float>()
        run {
            var x = startPoint.x
            while (x < endPoint.x) {
                run {
                    val min = Math.min(startPoint.y, endPoint.y)
                    var y = min
                    while (y < chartHeight + paddingTop) {
                        val pointF = PointF(x, y)
                        if (isUnderLine(startPoint, endPoint, pointF)) {
                            canvas?.drawLine(x, y, x, (paddingTop + chartHeight).toFloat(), mPaint)
                            break
                        }
                        y++
                    }
                }
                x++
            }
        }
        return pointArray.toFloatArray()
    }

    private fun isUnderLine(startPoint: PointF, endPoint: PointF, pointF: PointF): Boolean {
        var k = (endPoint.y - startPoint.y) / (endPoint.x - startPoint.x)
        var b = startPoint.y - startPoint.x * k
        if (pointF.y > k * pointF.x + b) {
            return true
        }
        return false
    }


    fun setData(datas: ArrayList<DataPoint>) {
        chartDatas = ArrayList()
        chartDatas.addAll(datas)
        invalidate()
    }

    class DataPoint {
        var dayTime: String? = null
        var trendValue: String = "0"

        var pointX: Float? = null
        var pointY: Float? = null
        var isDrawX = true
        var isDrawY = true
    }
}