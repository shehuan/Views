package com.othershe.views.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.TextView;

public class RoundTextView extends TextView {
    private Paint mPaint;

    public RoundTextView(Context context) {
        super(context);
        init();
    }

    public RoundTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void setBackgroundColor(int color) {
        mPaint.setColor(color);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        RectF rect = new RectF(0, 0, getWidth(), getHeight());
        canvas.drawRoundRect(rect, 5, 5, mPaint);

        super.onDraw(canvas);
    }
}
