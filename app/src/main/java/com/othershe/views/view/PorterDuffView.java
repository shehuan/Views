package com.othershe.views.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

import com.othershe.views.Utils;
import com.othershe.views.bo.PorterDuffBO;

/**
 * Src为源图像，意为将要绘制的图像；Dest为目标图像，要把Src绘制到Dest上
 *
 * PorterDuffXfermode
 *
 * PorterDuff.Mode.ADD 饱和度相加
 * PorterDuff.Mode.CLEAR 清除图像
 * PorterDuff.Mode.DARKEN 变暗，两个图像混合，较深的颜色总是会覆盖较浅的颜色，如果两者深浅相同则混合
 * PorterDuff.Mode.DST 只绘制目标图像
 * PorterDuff.Mode.DST_ATOP 在源图像和目标图像相交的地方绘制目标图像而在不相交的地方绘制源图像
 * PorterDuff.Mode.DST_IN 只在源图像和目标图像相交的地方绘制目标图像
 * PorterDuff.Mode.DST_OVER 在源图像的上方绘制目标图像
 * PorterDuff.Mode.DST_OUT 只在源图像和目标图像不相交的地方绘制目标图像
 * PorterDuff.Mode.LIGHTEN 变亮，与DARKEN相反
 * PorterDuff.Mode.MULTIPLY 正片叠底，源图像素颜色值乘以目标图像素颜色值除以255即得混合后图像像素的颜色值
 * PorterDuff.Mode.OVERLAY 叠加，它会将源色与目标色混合产生一种中间色，这种中间色生成的规律也很简单，如果源色比目标色暗，那么让目标色的颜色倍增否则颜色递减
 * PorterDuff.Mode.SCREEN 滤色，让图像焦媃幻化，有一种色调均和的感觉
 * PorterDuff.Mode.SRC 只绘制源图像
 * PorterDuff.Mode.SRC_ATOP 在源图像和目标图像相交的地方绘制源图像，在不相交的地方绘制目标图像
 * PorterDuff.Mode.SRC_IN 只在源图像和目标图像相交的地方绘制源图像
 * PorterDuff.Mode.SRC_OUT 只在源图像和目标图像不相交的地方绘制源图像
 * PorterDuff.Mode.SRC_OVER 在目标图像的顶部绘制源图像
 * PorterDuff.Mode.XOR 在源图像和目标图像重叠之外的任何地方绘制他们，而在不重叠的地方不绘制任何内容
 */
public class PorterDuffView extends View {

    //混合模式常量
    private static final PorterDuff.Mode MODE = PorterDuff.Mode.DST_OVER;
    //图像混合模式
    private PorterDuffXfermode porterDuffXfermode;

    //左右上方src\dest正方形大小
    private static final int RECT_SIZE_SMALL = 300;
    //中间合成后正方形大小
    private static final int RECT_SIZE_BIG = 500;
    //屏幕尺寸
    private int screenW, screenH;
    //右上bitmap原点坐标(src)
    private int src_x, src_y;
    //左上bitmap原点坐标(dest)
    private int dest_x, dest_y;
    //合成后的bitmap原点坐标
    private int result_x, result_y;

    private Paint mPaint;

    //生成src\dest bitmap的业务类
    private PorterDuffBO porterDuffBO;

    private Context mContext;

    public PorterDuffView(Context context) {
        this(context, null);
    }

    public PorterDuffView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PorterDuffView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        porterDuffBO = new PorterDuffBO();
        porterDuffXfermode = new PorterDuffXfermode(MODE);

        calculatePosition();
    }

    private void calculatePosition() {
        screenW = Utils.getScreenSize(mContext)[0];
        screenH = Utils.getScreenSize(mContext)[1];

        src_x = src_y = 0;

        dest_x = screenW - RECT_SIZE_SMALL;
        dest_y = 0;

        result_x = (screenW - RECT_SIZE_BIG) / 2;
        result_y = RECT_SIZE_SMALL + ((screenH - RECT_SIZE_SMALL) - RECT_SIZE_BIG) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //设置画布黑色
        canvas.drawColor(Color.BLACK);

        porterDuffBO.setSize(RECT_SIZE_SMALL);
        //绘制src bitmap
        canvas.drawBitmap(porterDuffBO.initSrcBitmap(), src_x, src_y, mPaint);
        //绘制dest bitmap
        canvas.drawBitmap(porterDuffBO.initDestBitmap(), dest_x, dest_y, mPaint);

        //将绘制操作保存到新的图层（离屏缓存）
        int src = canvas.saveLayer(0, 0, screenW, screenH, null, Canvas.ALL_SAVE_FLAG);

        porterDuffBO.setSize(RECT_SIZE_BIG);
        //绘制dest
        canvas.drawBitmap(porterDuffBO.initDestBitmap(), result_x, result_y, mPaint);
        //设置混合模式
        mPaint.setXfermode(porterDuffXfermode);
        //绘制src
        canvas.drawBitmap(porterDuffBO.initSrcBitmap(), result_x, result_y, mPaint);
        //还原混合模式
        mPaint.setXfermode(null);
        //还原画布
        canvas.restoreToCount(src);
    }
}
