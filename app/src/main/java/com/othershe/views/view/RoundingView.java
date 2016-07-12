package com.othershe.views.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.othershe.views.R;

/**
 * Created by Administrator on 2016/7/12.
 */
public class RoundingView extends View {
    //图片资源
    private Bitmap mSourceBitmap;
    //需要左右拼接的Bitmap
    private Bitmap mLeftBitmap, mRightBitmap;

    //图片整体的偏移量
    private int offsetX;
    //图片的宽高
    private int mBitmapWidth, mBitmapHeight;

    private boolean isRounding;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                invalidate();
            }
        }
    };

    public RoundingView(Context context) {
        this(context, null);
    }

    public RoundingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mSourceBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.rounding);
        mBitmapWidth = mSourceBitmap.getWidth();
        mBitmapHeight = mSourceBitmap.getHeight();

        isRounding = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        recycleBitmap();

        //计算View宽高
        //图片的显示宽高
        int tempWidth = getWidth();//默认状态的宽度
        int height = getHeight() > mBitmapHeight ? mBitmapHeight : getHeight();

        if (offsetX >= mBitmapWidth) {
            offsetX = 0;
        }

        //在图片移动过程中计算左Bitmap的显示宽度
        //如果offsetX + tempWidth >= mBitmapWidth，则代表图片的右边界小于View的宽度，此时右Bitmap需要展示，不为空，否则其为null
        tempWidth = offsetX + tempWidth >= mBitmapWidth ? mBitmapWidth - offsetX : tempWidth;

        //初始化左Bitmap
        mLeftBitmap = Bitmap.createBitmap(mSourceBitmap, offsetX, 0, tempWidth, height);
        //绘制左Bitmap
        Rect rectL = new Rect(0, 0, tempWidth, height);
        canvas.drawBitmap(mLeftBitmap, null, rectL, null);

        //如果图片的右边界小于View的宽度，即View的右边部分开始出现空白，则需要从图片的开始截图部分来填补，以实现循环效果
        if (tempWidth < getWidth()) {
            Rect rectR = new Rect(tempWidth, 0, getWidth(), height);
            mRightBitmap = Bitmap.createBitmap(mSourceBitmap, 0, 0, getWidth() - tempWidth, height);
            canvas.drawBitmap(mRightBitmap, null, rectR, null);
        }

        offsetX += 1;

        if (isRounding) {
            mHandler.sendEmptyMessageDelayed(1, 1);
        }
    }

    private void recycleBitmap() {
        if (mLeftBitmap != null) {
            mLeftBitmap.recycle();
            mLeftBitmap = null;
        }

        if (mRightBitmap != null) {
            mRightBitmap.recycle();
            mRightBitmap = null;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isRounding = false;
        recycleBitmap();
        mSourceBitmap.recycle();
    }
}
