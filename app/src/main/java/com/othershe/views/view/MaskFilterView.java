package com.othershe.views.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.othershe.views.R;
import com.othershe.views.Utils;

/**
 * 模糊遮罩滤镜
 */
public class MaskFilterView extends View {
    private static final int RECT_SIZE = 400;
    private Paint mPaint;
    private int left, top, right, bottom;

    //原bitmap和阴影位图
    private Bitmap mBitmap, shadowBitmap;

    private Context mContext;

    public MaskFilterView(Context context) {
        this(context, null);
    }

    public MaskFilterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        //关闭当前view的硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        initPaint();
        initRes();
    }

    private void initRes() {
        left = Utils.getScreenSize(mContext)[0] / 2 - RECT_SIZE / 2;
        top = Utils.getScreenSize(mContext)[1] / 2 - RECT_SIZE / 2;
        right = Utils.getScreenSize(mContext)[0] / 2 + RECT_SIZE / 2;
        bottom = Utils.getScreenSize(mContext)[1] / 2 + RECT_SIZE / 2;

        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl);
        //获取bitmap的Alpha通道
        shadowBitmap = mBitmap.extractAlpha();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
//        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(0xFF603811);

        /**
         * 给画笔设置遮罩滤镜
         * SOLID 在图像的Alpha边界外产生一层与Paint颜色一致的阴影效果而不影响图像本身
         * NORMAL 会将整个图像模糊掉
         * OUTER 会在Alpha边界外产生一层阴影且会将原本的图像变透明
         * INNER 会在图像内部产生模糊
         *
         * 直接作用于图片则无效果
         */
        mPaint.setMaskFilter(new BlurMaskFilter(10, BlurMaskFilter.Blur.NORMAL));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.GRAY);

        //绘制矩形
//        canvas.drawRect(left, top, right, bottom, mPaint);


        //测试绘制有阴影的图片
        //先绘制阴影
        canvas.drawBitmap(shadowBitmap, left, top, mPaint);
        //再绘制位图
        canvas.drawBitmap(mBitmap, left, top, null);
    }
}
