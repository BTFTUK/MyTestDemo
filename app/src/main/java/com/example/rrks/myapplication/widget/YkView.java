package com.example.rrks.myapplication.widget;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import com.example.rrks.myapplication.R;

public class YkView extends View implements View.OnClickListener {
    private Context mContext;
    private Paint mPaint;
    private Paint mRingPaint;
    private Paint mTextPaint;
    private int mWidth;
    private int mHeight;
    private boolean isSelected = false;
    private Bitmap mTopBitmap;
    private Bitmap mSelectedBitmap;
    private Bitmap mUnSelectedBitmap;
    private ValueAnimator mAnimator;
    private float mScale = 1f;
    private Point mCenterPoint;
    private int mBaseSize;
    private int mWidthDifference;
    private int mHeightDifference;
    private ValueAnimator mRingAnimator;
    private AnimatorSet mAnimatorSet;
    private float mRingScale = 0.5f;
    private float mWhiteScale = 0.5f;
    private int mAlpha;
    private ValueAnimator mWhiteAnimator;
    private int mWhiteRadius;
    private int mMaxNum;
    private int mMinNum;
    private ValueAnimator mTextAnimator;
    private float mTextBaseY = 0.5f;
    private int mTextAlphaTo1;
    private int mTextAlphaTo0;

    public YkView(Context context) {
        this(context, null);
    }

    public YkView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YkView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(dp2px(4));

        mRingPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRingPaint.setStrokeWidth(1);
        mRingPaint.setStyle(Paint.Style.STROKE);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(42);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(Color.BLACK);

        mTopBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_messages_like_selected_shining);
        mSelectedBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_messages_like_selected);
        mUnSelectedBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_messages_like_unselected);
        mBaseSize = mSelectedBitmap.getWidth();
        mWidthDifference = (mSelectedBitmap.getWidth() - mTopBitmap.getWidth()) / 2;
        mHeightDifference = mTopBitmap.getHeight() / 2;
        initBitmap();
        setOnClickListener(this);
        setBackgroundColor(Color.WHITE);
    }

    private void initBitmap() {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //第一层
        if (isSelected) {
            canvas.drawBitmap(mTopBitmap, mWidth / 2 - mSelectedBitmap.getWidth() / 2 + mWidthDifference,
                    mHeight / 2 - mSelectedBitmap.getHeight() / 2 - mHeightDifference, null);
        }
        //第二层
        drawWriteRing(canvas);
        if (isSelected) {
            //第三层
            drawRing(canvas);
            Bitmap resizeBmp = Bitmap.createScaledBitmap(mSelectedBitmap, (int) (mSelectedBitmap.getWidth() * mScale),
                    (int) (mSelectedBitmap.getHeight() * mScale), true);
            canvas.drawBitmap(resizeBmp, mWidth / 2 - resizeBmp.getWidth() / 2, mHeight / 2 - resizeBmp.getHeight() / 2, null);
        } else {
            Bitmap resizeBmp = Bitmap.createScaledBitmap(mUnSelectedBitmap, (int) (mUnSelectedBitmap.getWidth() * mScale),
                    (int) (mUnSelectedBitmap.getHeight() * mScale), true);
            canvas.drawBitmap(resizeBmp, mWidth / 2 - resizeBmp.getWidth() / 2, mHeight / 2 - resizeBmp.getHeight() / 2, mPaint);
        }

        drawText(canvas);
    }

    private void drawText(Canvas canvas) {
        mTextPaint.setAlpha(255);
        canvas.drawText("32", mCenterPoint.x + 60, (float) (mCenterPoint.y + mPaint.getTextSize() * 0.5), mTextPaint);
        float textWidth = mTextPaint.measureText("32");

        if (isSelected) {//up
            mTextPaint.setAlpha(mTextAlphaTo0);//中间变到上面
            canvas.drawText("0", mCenterPoint.x + 60 + textWidth,
                    (float) (mCenterPoint.y + mPaint.getTextSize() * 0.5) - mTextPaint.getFontSpacing() * mTextBaseY, mTextPaint);

            mTextPaint.setAlpha(mTextAlphaTo1);//下面变到中间
            canvas.drawText("1", mCenterPoint.x + 60 + textWidth,
                    (float) (mCenterPoint.y + mPaint.getTextSize() * 0.5) + mTextPaint.getFontSpacing() - mTextPaint.getFontSpacing() * mTextBaseY, mTextPaint);
        } else {//down
            mTextPaint.setAlpha(mTextAlphaTo1);//上面变中间
            canvas.drawText("0", mCenterPoint.x + 60 + textWidth,
                    (float) (mCenterPoint.y + mPaint.getTextSize() * 0.5) - mTextPaint.getFontSpacing() + mTextPaint.getFontSpacing() * mTextBaseY, mTextPaint);
            mTextPaint.setAlpha(mTextAlphaTo0);//中间变下面
            canvas.drawText("1", mCenterPoint.x + 60 + textWidth,
                    (float) (mCenterPoint.y + mPaint.getTextSize() * 0.5) + mTextPaint.getFontSpacing() * mTextBaseY, mTextPaint);
        }

    }

    private void drawRing(Canvas canvas) {
        mRingPaint.setColor(Color.parseColor("#ef5c2e"));
        mRingPaint.setStrokeWidth(2);
        mRingPaint.setAlpha(mAlpha);
        canvas.drawArc(mCenterPoint.x - mBaseSize * mRingScale, mCenterPoint.y - mBaseSize * mRingScale,
                mCenterPoint.x + mBaseSize * mRingScale, mCenterPoint.y + mBaseSize * mRingScale,
                360, 360, false, mRingPaint);
    }

    private void drawWriteRing(Canvas canvas) {
        mPaint.setColor(Color.WHITE);
//        mWhiteRadius = (int) ((mCenterPoint.y - (mHeight / 2 - mSelectedBitmap.getHeight() / 2 - mHeightDifference))*mAlpha);
        mPaint.setStrokeWidth(mWhiteRadius * mWhiteScale);

        canvas.drawArc(mCenterPoint.x - mWhiteRadius, mCenterPoint.y - mWhiteRadius,
                mCenterPoint.x + mWhiteRadius, mCenterPoint.y + mWhiteRadius,
                360, 360, false, mPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mWidth == 0) {
            mWidth = getMeasuredWidth();
            mHeight = getMeasuredHeight();
            mCenterPoint = new Point(mWidth / 2, mHeight / 2);

            mWhiteRadius = (mCenterPoint.y - (mHeight / 2 - mSelectedBitmap.getHeight() / 2 - mHeightDifference));
        }
    }

    public int dp2px(float dipValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    @Override
    public void onClick(View view) {
        isSelected = !isSelected;
        refreshView();
    }

    private void refreshView() {
        if (mAnimator == null) {
            mAnimator = ValueAnimator.ofFloat(0.8f, 1.0f);
            mAnimator.setDuration(250);
            mAnimator.setRepeatCount(0);
            mAnimator.setInterpolator(new OvershootInterpolator());
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    mScale = (float) valueAnimator.getAnimatedValue();
                    invalidate();
                }
            });
        }
        if (mRingAnimator == null) {
            mRingAnimator = ValueAnimator.ofFloat(0.5f, 1.2f);
            mRingAnimator.setDuration(250);
            mRingAnimator.setRepeatCount(0);
            mRingAnimator.setInterpolator(new LinearInterpolator());
            mRingAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float f = (float) valueAnimator.getAnimatedValue();
                    mRingScale = f;
                    mAlpha = (int) (255 * (1.2f - f) / 0.7f);
                    invalidate();
                }
            });
        }
        if (mWhiteAnimator == null) {
            mWhiteAnimator = ValueAnimator.ofFloat(1.2f, 0f);
            mWhiteAnimator.setDuration(250);
            mWhiteAnimator.setRepeatCount(0);
            mWhiteAnimator.setInterpolator(new LinearInterpolator());
            mWhiteAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    mWhiteScale = (float) valueAnimator.getAnimatedValue();
                    invalidate();
                }
            });
        }
        if (mTextAnimator == null) {
            mTextAnimator = ValueAnimator.ofFloat(0.0f, 1f);
            mTextAnimator.setDuration(250);
            mTextAnimator.setRepeatCount(0);
            mTextAnimator.setInterpolator(new LinearInterpolator());
            mTextAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    mTextBaseY = (float) valueAnimator.getAnimatedValue();
                    mTextAlphaTo1 = (int) (255 * mTextBaseY);
                    mTextAlphaTo0 = (int) (255 * (1f - mTextBaseY));
                    invalidate();
                }
            });
        }


        if (mAnimatorSet == null) {
            mAnimatorSet = new AnimatorSet();
            mAnimatorSet.playTogether(mAnimator, mRingAnimator, mWhiteAnimator, mTextAnimator);
        }
        mAnimatorSet.start();
    }
}
