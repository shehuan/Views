package com.othershe.views.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.othershe.views.Utils;

/**
 * Created by Administrator on 2016/4/22.
 */
public class CircleView extends View implements Runnable {

    private Paint mPaint;
    private Context mContext;

    private int radius;//半径值

    /**
     * new 对象需要此构造方法即可
     *
     * @param context
     */
    public CircleView(Context context) {
        this(context, null);
    }

    /**
     * 在xml文件引用需要有AttributeSet参数的构造方法
     *
     * @param context
     * @param attrs
     */
    public CircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);//抗锯齿，一种让图像边缘显得更圆滑光泽动感的算法
        /**
         * 画笔的三种样式
         * 1、Paint.Style.STROKE:描边
         * 2、Paint.Style.FILL_AND_STROKE:描边并填充
         * 3、Paint.Style.FILL:填充
         */
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.GRAY);
        /**
         * 设置描边的粗细，单位：像素px 
         * 当setStrokeWidth(0)的时候描边宽度并不为0而是只占一个像素 
         */
        mPaint.setStrokeWidth(6);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(Utils.getScreenSize(mContext)[0] / 2, Utils.getScreenSize(mContext)[1] / 2, radius, mPaint);
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (radius <= 200) {
                    radius += 10;
                    /**
                     * 在子线程请求刷新view，invalidate()在主线程刷新view
                     */
                    postInvalidate();
                } else {
                    radius = 0;
                }
                Thread.sleep(60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
