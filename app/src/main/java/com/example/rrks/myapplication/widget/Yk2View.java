package com.example.rrks.myapplication.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;

public class Yk2View extends View {

    private int mHeight;
    private int mWidth;
    private Context mContext;
    private Paint mPaint;
    private RectF mRectF;
    private RectF mMidRectF;
    //    private Point mMoveCenterP;
    private float mCPX;
    private float mCPY;
    private float mMoveWidth;
    private float mMoveHeight;
    private boolean couldMove;
    private float downX;
    private float startX;
    private float endX;
    private float mOldMovecpX;
    private Xfermode textXfermode = new PorterDuffXfermode(PorterDuff.Mode.XOR);


    public Yk2View(Context context) {
        this(context, null);
    }

    public Yk2View(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Yk2View(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMoveHeight = dp2px(80);
        mMoveWidth = dp2px(150);
//        startCenterX = mMoveWidth / 2;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawbg(canvas);
//        drawMove(canvas);
        drawContent(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mWidth == 0) {
            mWidth = getMeasuredWidth();
            mHeight = getMeasuredHeight();
            mRectF = new RectF(0, 0, mWidth, mHeight);
            startX = 0;
            endX = mWidth;
//            endCenterX = mWidth - mMoveWidth / 2;
//            mMoveCenterP = new Point(startCenterX, mHeight / 2);
            mCPX = startX + mMoveWidth / 2;
            mCPY = mHeight / 2;
            mMidRectF = new RectF(mCPX - mMoveWidth / 2, mCPY - mMoveHeight / 2, mCPX + mMoveWidth / 2, mCPY + mMoveHeight / 2);
        }
    }

    private void drawbg(Canvas canvas) {
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(mRectF, mHeight / 2, mHeight / 2, mPaint);
    }


    private void drawContent(Canvas canvas) {

        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setTextSize(36);
        int saved = canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG);
        mPaint.setXfermode(textXfermode);
        canvas.drawText("中间文字", mWidth / 2, mHeight / 2, mPaint);

        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(mMidRectF, mMoveHeight / 2, mMoveHeight / 2, mPaint);
        mPaint.setXfermode(null);
        canvas.restoreToCount(saved);
    }

    private void drawMove(Canvas canvas) {
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);

        canvas.drawRoundRect(mMidRectF, mMoveHeight / 2, mMoveHeight / 2, mPaint);
    }


    public int dp2px(float dipValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mOldMovecpX == 0) {
            mOldMovecpX = mCPX;
        }
//        Log.e("===", "===onTouchEvent 外层");

        switch (event.getAction()) {
            default:
                break;
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                float y = event.getY();
                couldMove = mMidRectF.contains(downX, y);
//                Log.e("===", "===onTouchEvent==down");
                break;
            case MotionEvent.ACTION_UP:
                mOldMovecpX = 0;
//                Log.e("===", "===onTouchEvent==up");
                break;
            case MotionEvent.ACTION_MOVE:
//                Log.e("===", "===onTouchEvent==move");
                if (couldMove) {
                    float x = event.getX();
                    mCPX = x - downX + mOldMovecpX;
                    if (mCPX - mMoveWidth / 2 < startX || mCPX + mMoveWidth / 2 > endX) {
//                        Log.e("===", "===阻止");
                    } else {
                        mMidRectF.set(mCPX - mMoveWidth / 2, mCPY - mMoveHeight / 2, mCPX + mMoveWidth / 2, mCPY + mMoveHeight / 2);
                        invalidate();
                    }
                }
                break;
        }


        return true;
    }
}
