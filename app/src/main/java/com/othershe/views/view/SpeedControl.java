package com.othershe.views.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.othershe.views.Utils;

/**
 * Created by Administrator on 2016/5/16.
 */
public class SpeedControl extends View implements Runnable {
    private Context mContext;
    //屏幕尺寸
    private int screenWidth, screenHeight;
    //屏幕密度
    private float mDensityDpi;
    //圆心
    private int pointX, pointY;
    //半径
    private int radius;
    //速度范围的2个扇形外切矩形
    private RectF speedRectF, speedRectFInner;
    //速度
    private int speed;
    //刻度值的绘制坐标
    private int baseX, baseY;
    //文字偏移量
    private float textScale;
    //计算刻度值绘制坐标的辅助值
    private float sRadius;
    //画笔
    private Paint mPaint, textPaint, speedAreaPaint;
    //开始重绘
    private boolean start = true;
    //速度控制模式  1 加速  2 减速  3 手刹
    private int type;
    //是否是刷新状态
    private boolean isFlush = false;

    public SpeedControl(Context context) {
        super(context);
    }

    public SpeedControl(Context context, AttributeSet attrs) {
        super(context, attrs);
        //关闭硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        mContext = context;
        screenWidth = Utils.getScreenSize(mContext)[0];
        screenHeight = Utils.getScreenSize(mContext)[1];
        mDensityDpi = Utils.getDensityDpi(mContext);

        radius = screenWidth / 3;
        pointX = pointY = screenWidth / 2;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(5 * mDensityDpi);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        textPaint.setColor(Color.WHITE);
        //设置字体
        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "kt.ttf");
        textPaint.setTypeface(typeface);
        textPaint.setTextSize(25 * mDensityDpi);

        speedAreaPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        speedAreaPaint.setStyle(Paint.Style.FILL);
        // 设置速度范围扇形的渐变颜色
        Shader shader = new LinearGradient(pointX - radius, pointY, pointX + radius, pointY,
                new int[]{0xFF445EED, 0xFF072AE9, 0xFF0625CE}, null, Shader.TileMode.CLAMP);
        speedAreaPaint.setShader(shader);

        // 初始化速度范围的2个扇形外切矩形
        speedRectF = new RectF(pointX - radius + 10 * mDensityDpi, pointY - radius + 10 * mDensityDpi,
                pointX + radius - 10 * mDensityDpi, pointY + radius - 10 * mDensityDpi);
        speedRectFInner = new RectF(pointX - radius / 2, pointY - radius / 2,
                pointX + radius / 2, pointY + radius / 2);

        textScale = Math.abs(textPaint.descent() + textPaint.ascent()) / 2;
        sRadius = radius - 50 * mDensityDpi;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //设置背景为黑色
        canvas.drawColor(Color.BLACK);
        drawCircle(canvas);
        drawSpeedArea(canvas);
        drawScale(canvas);

        for (int i = 0; i < 8; i++) {
            drawScaleNum(canvas, 30 * i);
        }

        drawCenter(canvas);
    }

    /**
     * 绘制圆
     *
     * @param canvas
     */
    private void drawCircle(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(0xFF343434);
        canvas.drawCircle(pointX, pointY, radius, mPaint);

        //外圈2个圆
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(0xBF3F6AB5);
        mPaint.setStrokeWidth(4 * mDensityDpi);
        canvas.drawCircle(pointX, pointY, radius, mPaint);
        mPaint.setStrokeWidth(3 * mDensityDpi);
        canvas.drawCircle(pointX, pointY, radius - 10 * mDensityDpi, mPaint);

        //内圈2个圆
        mPaint.setStrokeWidth(5 * mDensityDpi);
        mPaint.setColor(0xE73F51B5);
        canvas.drawCircle(pointX, pointY, radius / 2, mPaint);
        mPaint.setColor(0x7E3F51B5);
        canvas.drawCircle(pointX, pointY, radius / 2 + 5 * mDensityDpi, mPaint);
        mPaint.setStrokeWidth(3 * mDensityDpi);
    }

    /**
     * 绘制速度区域扇形
     *
     * @param canvas
     */
    private void drawSpeedArea(Canvas canvas) {
        int degree;
        if (speed < 210) {
            degree = speed * 36 / 30;
        } else {
            degree = 210 * 36 / 30;
        }

        canvas.drawArc(speedRectF, 144, degree, true, speedAreaPaint);

        //不显示中间的内圈的扇形区域
        mPaint.setColor(0xFF343434);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawArc(speedRectFInner, 144, degree, true, mPaint);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    /**
     * 绘制刻度线
     *
     * @param canvas
     */
    private void drawScale(Canvas canvas) {
        mPaint.setColor(0xBF3F6AB5);
        for (int i = 0; i < 60; i++) {
            if (i % 6 == 0) {
                canvas.drawLine(pointX - radius + 10 * mDensityDpi, pointY,
                        pointX - radius + 50 * mDensityDpi, pointY, mPaint);
            } else {
                canvas.drawLine(pointX - radius + 10 * mDensityDpi, pointY,
                        pointX - radius + 30 * mDensityDpi, pointY, mPaint);
            }

            canvas.rotate(6, pointX, pointY);
        }
    }

    /**
     * 绘制刻度值
     *
     * @param canvas
     * @param value
     */
    private void drawScaleNum(Canvas canvas, int value) {
        String TEXT = String.valueOf(value);
        switch (value) {
            case 0:
                // 计算Baseline绘制的起点X轴坐标
                baseX = (int) (pointX - sRadius * Math.cos(Math.PI / 5) + textPaint.measureText(TEXT) / 2 + textScale / 2);
                // 计算Baseline绘制的Y坐标
                baseY = (int) (pointY + sRadius * Math.sin(Math.PI / 5) + textScale / 2);
                break;
            case 30:
                baseX = (int) (pointX - radius + 50 * mDensityDpi + textPaint.measureText(TEXT) / 2);
                baseY = (int) (pointY + textScale);
                break;
            case 60:
                baseX = (int) (pointX - sRadius * Math.cos(Math.PI / 5) + textScale);
                baseY = (int) (pointY - sRadius * Math.sin(Math.PI / 5) + textScale * 2);
                break;
            case 90:
                baseX = (int) (pointX - sRadius * Math.cos(2 * Math.PI / 5) - textScale / 2);
                baseY = (int) (pointY - sRadius * Math.sin(2 * Math.PI / 5) + 2 * textScale);
                break;
            case 120:
                baseX = (int) (pointX + sRadius * Math.sin(Math.PI / 10) - textPaint.measureText(TEXT) / 2);
                baseY = (int) (pointY - sRadius * Math.cos(Math.PI / 10) + 2 * textScale);
                break;
            case 150:
                baseX = (int) (pointX + sRadius * Math.cos(Math.PI / 5) - textPaint.measureText(TEXT) - textScale / 2);
                baseY = (int) (pointY - sRadius * Math.sin(Math.PI / 5) + textScale * 2);
                break;
            case 180:
                baseX = (int) (pointX + sRadius - textPaint.measureText(TEXT) - textScale / 2);
                baseY = (int) (pointY + textScale);
                break;
            case 210:
                baseX = (int) (pointX + sRadius * Math.cos(Math.PI / 5) - textPaint.measureText(TEXT) - textScale / 2);
                baseY = (int) (pointY + sRadius * Math.sin(Math.PI / 5) - textScale / 2);
                break;
        }

        canvas.drawText(TEXT, baseX, baseY, textPaint);
    }

    /**
     * 绘制中心的速度和单位
     *
     * @param canvas
     */
    private void drawCenter(Canvas canvas) {
        textPaint.setTextSize(60 * mDensityDpi);
        float width = textPaint.measureText(String.valueOf(speed));
        baseX = (int) (pointX - width / 2);
        baseY = (int) (pointY + Math.abs(textPaint.descent() + textPaint.ascent()) / 4);
        canvas.drawText(String.valueOf(speed), baseX, baseY, textPaint);

        textPaint.setTextSize(20 * mDensityDpi);
        width = textPaint.measureText("km/h");
        baseX = (int) (pointX - width / 2);
        baseY = (int) (pointY + radius / 4 + Math.abs(textPaint.descent() + textPaint.ascent()) / 4);
        canvas.drawText("km/h", baseX, baseY, textPaint);
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    // 设置速度 并重绘视图
    public void setSpeed(int speed) {
        this.speed = speed;
        postInvalidate();
    }

    //设置速度控制模式
    public void setType(int type) {
        this.type = type;
    }

    @Override
    public void run() {
        while (start) {
            switch (type) {
                case 1://油门
                    speed += 3;
                    break;
                case 2://刹车
                    speed -= 5;
                    break;
                case 3://手刹
                    speed -= 1;
                    break;
            }

            if (speed < 0) {
                speed = 0;
            }

            if (speed > 360) {
                speed = 360;
            }

            try {
                Thread.sleep(50);
                setSpeed(speed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
