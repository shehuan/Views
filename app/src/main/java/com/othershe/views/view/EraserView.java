package com.othershe.views.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.othershe.views.R;
import com.othershe.views.Utils;

/**
 * Created by Administrator on 2016/4/25.
 */
public class EraserView extends View {

    Context mContext;

    //屏幕尺寸
    private int screenW, screenH;

    //橡皮擦路径画笔
    private Paint mPaint;
    //橡皮擦绘制路径
    private Path mPath;
    //绘制橡皮擦路径的画布
    private Canvas mCanvas;

    // 前景橡皮擦的Bitmap和背景底图的Bitmap
    private Bitmap fgBitmap, bgBitmap;

    // 记录上一个触摸事件的位置坐标  
    private float preX, preY;
    private int mTouchSlop;

    public EraserView(Context context) {
        this(context, null);
    }

    public EraserView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EraserView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        calculate();
        init();
    }

    private void init() {
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        // 设置画笔透明度为0是关键！我们要让绘制的路径是透明的，然后让该路径与前景的底色混合“抠”出绘制路径
        mPaint.setARGB(0, 256, 256, 256);

        //设置混合模式
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        mPaint.setStyle(Paint.Style.STROKE);
        //设置路径连接处样式
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        //设置笔触类型
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(30);

        fgBitmap = Bitmap.createBitmap(screenW, screenH, Bitmap.Config.ARGB_8888);
        //将前景bitmap注入画布
        mCanvas = new Canvas(fgBitmap);
        //设置画布为灰色
        mCanvas.drawColor(0xFF808080);

        bgBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl);
        //缩放背景Bitmap至屏幕大小
        bgBitmap = Bitmap.createScaledBitmap(bgBitmap, screenW, screenH, true);
    }

    private void calculate() {
        screenW = Utils.getScreenSize(mContext)[0];
        screenH = Utils.getScreenSize(mContext)[1];

        mTouchSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(bgBitmap, 0, 0, null);
        canvas.drawBitmap(fgBitmap, 0, 0, null);

        /**
         * 这里要注意canvas和mCanvas是两个不同的画布对象
         * 当我们在屏幕上移动手指绘制路径时会把路径通过mCanvas绘制到fgBitmap上
         * 每当我们手指移动一次均会将路径mPath作为目标图像绘制到mCanvas上，而在上面我们先在mCanvas上绘制了中性灰色
         * 两者会因为SRC_IN模式的计算只显示透明path，计算生成的混合图像也会是透明的，所以我们会得到“橡皮擦”的效果
         */
        mCanvas.drawPath(mPath, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /*
         * 获取当前事件位置坐标
         */
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:// 手指接触屏幕重置路径
                mPath.reset();
                mPath.moveTo(x, y);
                preX = x;
                preY = y;
                break;
            case MotionEvent.ACTION_MOVE:// 手指移动时连接路径
                float dx = Math.abs(x - preX);
                float dy = Math.abs(y - preY);
                if (dx >= mTouchSlop || dy >= mTouchSlop) {
                    mPath.quadTo(preX, preY, (x + preX) / 2, (y + preY) / 2);
                    preX = x;
                    preY = y;
                }
                break;
        }
        // 重绘视图
        invalidate();
        return true;
    }
}
