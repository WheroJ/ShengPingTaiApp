package com.cmcc.pp.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.cmcc.pp.R;
import com.cmcc.pp.util.UIUtils;

/**
 * Created by shopping on 2017/12/26 15:58.
 * https://github.com/wheroj
 */

public class BarGraphView extends AppCompatTextView {

    public static final int BLUE = 1;
    public static final int YELLOW = 2;

    private Paint mPaint;
    private int resId;
    private float radio;
    private final int barHeight = UIUtils.dip2px(10);

    /**
     * bar的radius
     */
    private final int radius = UIUtils.dip2px(5);
    /**
     * 预留绘制文本数据的位置宽度
     */
    private final int textDrawWidth = UIUtils.dip2px(50);
    private String mData;

    public BarGraphView(Context context) {
        this(context, null);
    }

    public BarGraphView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public BarGraphView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BarGraphView);
        resId = typedArray.getResourceId(R.styleable.BarGraphView_backDrawable, R.color.transparent);
        radio = typedArray.getFraction(R.styleable.BarGraphView_ratio, 1, 1, 0);
        typedArray.recycle();
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int measuredHeight = getMeasuredHeight();
        int measuredWidth = getMeasuredWidth();
        int width = measuredWidth - getPaddingLeft() - getPaddingRight();
        int height = measuredHeight - getPaddingBottom() - getPaddingTop();

        canvas.drawColor(getResources().getColor(R.color.transparent));

        if (radio > 0 && resId != -1) {
            RectF rect = new RectF();
            rect.left = getPaddingLeft();
            rect.top = (height - barHeight)/2 + getPaddingTop();
            rect.bottom = (height + barHeight)/2 + getPaddingTop();
            rect.right = getPaddingLeft() + (width -textDrawWidth) * radio;
            mPaint.setColor(resId);
            canvas.drawRoundRect(rect, radius, radius, mPaint);

            if (!TextUtils.isEmpty(mData)) {
                mPaint.setColor(getResources().getColor(R.color.main_black));
                mPaint.setTextSize(UIUtils.dip2px(12));
                Rect bounds = new Rect();
                mPaint.getTextBounds(mData, 0, mData.length(), bounds);
                Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
                float textHeight = fontMetrics.bottom - fontMetrics.top;
                int textWidth = bounds.right - bounds.left;
                canvas.drawText(mData, rect.right + (textDrawWidth - textWidth)/2, height / 2 + getPaddingTop() + textHeight / 2 - UIUtils.dip2px(5), mPaint);
            }
        }
    }

    public void setData(float radio, int type, String data) {
        if (type == YELLOW) {
            resId = getResources().getColor(R.color.main_yellow);
        } else resId = getResources().getColor(R.color.main_blue);
        this.radio = radio;
        mData = data;
        invalidate();
    }
}
