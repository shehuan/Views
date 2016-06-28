package com.othershe.views.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.othershe.views.R;

/**
 * Created by Administrator on 2016/6/28.
 */
public class MoveViewPager extends ViewPager {
    private Bitmap mBg = BitmapFactory.decodeResource(getResources(), R.mipmap.bg_home);

    private int mPosition;
    private boolean isInit = true;

    public MoveViewPager(Context context) {
        super(context);
    }

    public MoveViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (isInit) {
            mPosition = getCurrentItem();
            isInit = false;
        }

        if (mBg != null) {
            int width = mBg.getWidth();
            int height = mBg.getHeight();
            int count = getAdapter().getCount();
            int scrollX = getScrollX() + mPosition * getWidth();

            //每个item需要显示的图片宽度
            float widthForItem = width * 1.0f / count;
            //控件每移动一个像素，图片应该移动的像素值
            float widthForPrePx = widthForItem / getWidth();

            Rect src = new Rect((int) (scrollX * widthForPrePx), 0, (int) (scrollX * widthForPrePx + widthForItem), height);
            Rect dest = new Rect(getScrollX(), 0, getScrollX() + getWidth(), getHeight());

            canvas.drawBitmap(mBg, src, dest, null);
        }

        super.dispatchDraw(canvas);
    }

//    moveViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
//        @Override
//        public Fragment getItem(int position) {
//            return SimpleFragment.newInstance(position + "");
//        }
//
//        @Override
//        public int getCount() {
//            return count;
//        }
//    });


//    public class SimpleFragment extends Fragment {
//
//        private String mPosition;
//
//        public static SimpleFragment newInstance(String title) {
//            SimpleFragment simpleFragment = new SimpleFragment();
//            Bundle bundle = new Bundle();
//            bundle.putString("position", title);
//            simpleFragment.setArguments(bundle);
//            return simpleFragment;
//        }
//
//        @Override
//        public void onCreate(@Nullable Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//
//            mPosition = getArguments().getString("position");
//        }
//
//        @Nullable
//        @Override
//        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//            TextView tv = new TextView(getActivity());
//            tv.setTextSize(20);
//            tv.setTextColor(Color.WHITE);
//            tv.setText(mPosition);
//            return tv;
//        }
//    }
}

