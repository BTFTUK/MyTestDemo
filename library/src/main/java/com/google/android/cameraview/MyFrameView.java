package com.google.android.cameraview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class MyFrameView extends View {

    private Paint mFramePaint;//框框画笔
    private RectF mRectF;

    public MyFrameView(Context context) {
        this(context, null);
    }

    public MyFrameView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyFrameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mRectF = new RectF();
        mFramePaint = new Paint();
        mFramePaint.setAntiAlias(true);
        mFramePaint.setColor(Color.parseColor("#cccccc"));
        mFramePaint.setStyle(Paint.Style.STROKE);
        mFramePaint.setStrokeWidth(5);
        DashPathEffect pathEffect = new DashPathEffect(new float[]{10, 20}, 0);
        mFramePaint.setPathEffect(pathEffect);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int frameW, frameH;
        if (width<=height){//竖屏
            frameW = 285;
            frameH = 173;
        }else {//横屏
            frameW = 469;
            frameH = 285;
        }
        int psx = (int) (width / 2 - dp2px(frameW / 2));
        int psy = (int) (height / 2 - dp2px(frameH / 2));
        int pex = (int) (width / 2 + dp2px(frameW / 2));
        int pey = (int) (height / 2 + dp2px(frameH / 2));

        mRectF.left = psx;
        mRectF.top = psy;
        mRectF.right = pex;
        mRectF.bottom = pey;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRoundRect(mRectF, dp2px(5), dp2px(5), mFramePaint);

    }

    public float dp2px(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    public RectF getRectF(){
        return mRectF;
    }
}
