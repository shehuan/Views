package com.othershe.views.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2016/5/20.
 */
public class GapBorderView extends LinearLayout {
    private Paint mPaint;

    //半圆间距
    private float GAP_SIZE = 8;
    //圆半径
    private float RADIUS = 10;
    //半圆数量(上下两边的半圆数量)
    private int circleNum;
    //剩下的(当半圆数量不是刚好整除得到的时候需要考虑)
    private float remain;

    public GapBorderView(Context context) {
        this(context, null);
    }

    public GapBorderView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        remain = (w - GAP_SIZE) % (2 * RADIUS + GAP_SIZE);
        circleNum = (int) ((w - GAP_SIZE) / (2 * RADIUS + GAP_SIZE));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < circleNum; i++) {
            //计算圆心的x坐标(如果remain大于0，表示circleNum不是整除得到的，加上remain/2，可保证左右两边的gap宽度相同)
            float x = GAP_SIZE + RADIUS + remain / 2 + (GAP_SIZE + 2 * RADIUS) * i;
            //绘制上border
            canvas.drawCircle(x, 0, RADIUS, mPaint);
            //绘制下border
            canvas.drawCircle(x, getHeight(), RADIUS, mPaint);
        }
    }
}
