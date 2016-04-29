package com.othershe.views.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.othershe.views.R;

/**
 * BitmapShader
 * <p>
 * Shader.TileMode.CLAMP 拉伸
 * Shader.TileMode.MIRROR 镜像
 * Shader.TileMode.REPEAT
 */
public class MoveView extends View {
    //填充图片的画笔
    private Paint mFillPaint;
    //描边的画笔
    private Paint mStrokePaint;
    //bitmap着色器
    private BitmapShader mBitmapShader;

    private float posX, posY;// 触摸点的XY坐标  
    private static final int CIRCLE_RADIUS = 100;

    public MoveView(Context context) {
        this(context, null);
    }

    public MoveView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        mStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mStrokePaint.setColor(0xFF000000);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setStrokeWidth(5);

        mFillPaint = new Paint();

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.brick);
        mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        mFillPaint.setShader(mBitmapShader);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            posX = event.getX();
            posY = event.getY();
            invalidate();
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.DKGRAY);

        canvas.drawCircle(posX, posY, CIRCLE_RADIUS, mFillPaint);
        canvas.drawCircle(posX, posY, CIRCLE_RADIUS, mStrokePaint);
    }
}
