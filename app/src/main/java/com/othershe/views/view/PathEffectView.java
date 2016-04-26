package com.othershe.views.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposePathEffect;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.DiscretePathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.SumPathEffect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2016/4/26.
 */
public class PathEffectView extends View {

    private Paint mPaint;
    //路径对象
    private Path mPath;
    //路径效果数组
    private PathEffect[] mPathEffects;
    //偏移值
    private float mPhase;

    public PathEffectView(Context context) {
        this(context, null);
    }

    public PathEffectView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);
        mPaint.setColor(Color.DKGRAY);

        mPath = new Path();
        //设置路径起点
        mPath.moveTo(0, 0);
        //设置路径其他点
        for (int i = 0; i < 30; i++) {
            mPath.lineTo(i * 30, (float) (Math.random() * 100));
        }

        mPathEffects = new PathEffect[7];

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPathEffects[0] = null;
        //20代表转角处的圆滑程度
        mPathEffects[1] = new CornerPathEffect(20);
        //3.0f指定突出的“杂点”的密度，值越小杂点越密集，5.0f则是“杂点”突出的大小，值越大突出的距离越大反之反之
        mPathEffects[2] = new DiscretePathEffect(3.0f, 5.0f);
        //float数组中20表示了第一条实线的长度，10则表示第一条虚线的长度，如果此时数组后面不再有数据则重复第一个数以此往复循环
        //mPhase为偏移值，动态改变其值会让路径产生动画的效果
        mPathEffects[3] = new DashPathEffect(new float[]{20, 10}, mPhase);
        //PathDashPathEffect可以自己定义路径虚线的样式,如下为小圆
        Path path = new Path();
        path.addCircle(0, 0, 3, Path.Direction.CCW);
        mPathEffects[4] = new PathDashPathEffect(path, 12, mPhase, PathDashPathEffect.Style.ROTATE);
        //以下两种用来组合路径效果
        mPathEffects[5] = new ComposePathEffect(mPathEffects[2], mPathEffects[4]);
        mPathEffects[6] = new SumPathEffect(mPathEffects[2], mPathEffects[4]);

        for (int i = 0; i < mPathEffects.length; i++) {
            mPaint.setPathEffect(mPathEffects[i]);
            canvas.drawPath(mPath, mPaint);
            // 每绘制一条将画布向下平移150个像素  
            canvas.translate(0, 150);
        }

        // 刷新偏移值并重绘视图实现动画效果
        mPhase += 1;
        invalidate();
    }
}
