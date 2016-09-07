package com.othershe.views.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.othershe.views.R;

/**
 * Author: Othershe
 * Time: 2016/9/7 14:18
 */
public class PolyToPoly extends View {
    private Bitmap mBitmap;
    private Matrix mMatrix;

    private int mWidth, mHeight;

    private int bw, bh;

    /**
     * 0	相当于reset
     * 1	相当于translate
     * 2	可以进行 缩放、旋转、平移 变换
     * 3	可以进行 缩放、旋转、平移、错切 变换
     * 4	可以进行 缩放、旋转、平移、错切以及任何形变
     */
    private int pointCount = 4;//控制点（[0, 4]）

    private float[] src = new float[8];
    private float[] dst = new float[8];

    private Paint mPointPaint;
    private Paint mBitmapPaint;

    public PolyToPoly(Context context) {
        this(context, null);
    }

    public PolyToPoly(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl);

        bw = mBitmap.getWidth();
        bh = mBitmap.getHeight();

        //图片的四个顶点(默认取数组中的前四个点，所以多余点无效)
        float[] temp = {0, 0,
                bw, 0,
                bw, bh,
                0, bh};


        src = temp.clone();
        dst = temp.clone();

        mPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPointPaint.setStrokeCap(Paint.Cap.ROUND);
        mPointPaint.setStrokeWidth(50);
        mPointPaint.setColor(Color.GRAY);

        mBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);

        mMatrix = new Matrix();
        mMatrix.setPolyToPoly(src, 0, dst, 0, 4);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();
                for (int i = 0; i < pointCount * 2; i += 2) {
                    if (Math.abs(x - dst[i]) <= 180 && Math.abs(y - dst[i + 1]) <= 180) {
                        dst[i] = x - 100;
                        dst[i + 1] = y - 100;
                        break;
                    }
                }

                mMatrix.reset();
                // 核心要点
                mMatrix.setPolyToPoly(src, 0, dst, 0, pointCount);
                invalidate();
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(100, 100);

        canvas.drawBitmap(mBitmap, mMatrix, mBitmapPaint);

//        float[] newPoints = new float[8];
//        mMatrix.mapPoints(newPoints, src);

        for (int i = 0; i < pointCount * 2; i += 2) {
            canvas.drawPoint(dst[i], dst[i + 1], mPointPaint);
        }
    }
}
