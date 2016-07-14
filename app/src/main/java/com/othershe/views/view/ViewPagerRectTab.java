package com.othershe.views.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewPagerRectTab extends LinearLayout {
    private int[] mTitles;
    private int mTabCount;
    private int lastPosition;
    private int[] mIndex;

    private OnTabClickListener mTabClickListener;

    public ViewPagerRectTab(Context context) {
        this(context, null);
    }

    public ViewPagerRectTab(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        RectF rect = new RectF(0, 0, getWidth(), getHeight());
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        paint.setColor(parseColor(R.color.global_orange_color));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1.5F);
        canvas.drawRoundRect(rect, 8, 8, paint);
    }

    private void generateTitleView() {
        if (getChildCount() > 0)
            this.removeAllViews();

        setWeightSum(mTabCount);
        for (int i = 0; i < mTabCount; i++) {
            final TextView tv = new TextView(getContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0,
                    LayoutParams.MATCH_PARENT);
            lp.weight = 1;

            tv.setId(i);
            tv.setGravity(Gravity.CENTER);
            if (i == 0) {
                tv.setTextColor(parseColor(R.color.global_white_color));
                tv.setBackgroundDrawable(parseDrawable(R.drawable.tab_left_selected_bg));
            } else {
                tv.setTextColor(parseColor(R.color.global_orange_color));
            }

            tv.setText(mTitles[i]);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            tv.setLayoutParams(lp);
            tv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = v.getId();
                    resetTabColor(id);
                    if (mTabClickListener != null) {
                        mTabClickListener.onClickTab(id);
                    }
                }
            });
            addView(tv);
            if (i != mTabCount - 1) {
                View view = new View(getContext());
                view.setBackgroundColor(parseColor(R.color.global_orange_color));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(1,
                        LayoutParams.MATCH_PARENT);
                view.setLayoutParams(params);
                addView(view);
            }
        }
    }

    public void setTitles(int[] titles) {
        mTitles = titles;
        mTabCount = titles.length;
        initIndex();
        generateTitleView();
    }

    public void selectTab(int position) {
        resetTabColor(position);
    }

    private void resetTabColor(int position) {
        TextView lastTab = ((TextView) getChildAt(mIndex[lastPosition]));
        lastTab.setTextColor(parseColor(R.color.global_orange_color));
        lastTab.setBackgroundDrawable(parseDrawable(R.drawable.tab_unselected_bg));

        TextView currentTab = ((TextView) getChildAt(mIndex[position]));
        currentTab.setTextColor(parseColor(R.color.global_white_color));

        if (position == 0) {
            currentTab.setBackgroundDrawable(parseDrawable(R.drawable.tab_left_selected_bg));
        } else if (position == mTabCount - 1) {
            currentTab.setBackgroundDrawable(parseDrawable(R.drawable.tab_right_selected_bg));
        } else {
            currentTab.setBackgroundDrawable(parseDrawable(R.drawable.tab_selected_bg));
        }

        lastPosition = position;
    }

    private void initIndex() {
        mIndex = new int[mTabCount];
        for (int i = 0; i < mTabCount; i++) {
            mIndex[i] = 2 * i;
        }
    }

    private int parseColor(int color) {
        return getResources().getColor(color);
    }

    private Drawable parseDrawable(int drawable) {
        return getResources().getDrawable(drawable);
    }

    public interface OnTabClickListener {
        void onClickTab(int position);
    }

    public void setOnTabClickListener(OnTabClickListener listener) {
        mTabClickListener = listener;
    }
}
