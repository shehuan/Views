package com.othershe.views.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.othershe.views.R;
import com.othershe.views.Utils;

/**
 * 光照颜色过滤
 * LightingColorFilter (int mul, int add)  
 * mul全称是colorMultiply意为色彩倍增，而add全称是colorAdd意为色彩添加，这两个值都是16进制的色彩值0xAARRGGBB
 */
public class StarView extends View {
    private Context mContext;
    private Paint mPaint;
    private Bitmap mBitmap;

    // 位图绘制时左上角的起点坐标 
    private int x;
    private int y;
    //点击标志
    private boolean isClick;

    public StarView(Context context) {
        this(context, null);
    }

    public StarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        initPaint();
        initRes();

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClick) {
                    //取消颜色过滤
                    mPaint.setColorFilter(null);
                    isClick = false;
                } else {
                    mPaint.setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0x000000FF));
                    isClick = true;
                }
                invalidate();
            }
        });
    }

    private void initRes() {
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.gray_star);
        x = (Utils.getScreenSize(mContext)[0] - mBitmap.getWidth()) / 2;
        y = (Utils.getScreenSize(mContext)[1] - mBitmap.getHeight()) / 2;
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, x, y, mPaint);
    }
}
