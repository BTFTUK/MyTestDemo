package com.example.rrks.myapplication.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

public class SpringWaveView extends View {

    private static final float DEFAULT_AMPLITUDE_RATIO = 0.05f;
    private static final float DEFAULT_WATER_LEVEL_RATIO = 0.5f;
    private static final float DEFAULT_WAVE_LENGTH_RATIO = 1.0f;
    private static final float DEFAULT_WAVE_SHIFT_RATIO = 0.0f;

    //y = Asin(wx+b)+h

    private float A;
    private float W;
    private float B;
    private float H;
    private Paint mPaint;
    private int width;
    private int height;
    private float[] mYs;
    private Path mPath;
    private float mCircle;//周期
    private BitmapShader mWaveShader;
    private Matrix mShaderMatrix;

    private float mAmplitudeRatio = DEFAULT_AMPLITUDE_RATIO;
    private float mWaveLengthRatio = DEFAULT_WAVE_LENGTH_RATIO;
    private float mWaterLevelRatio = DEFAULT_WATER_LEVEL_RATIO;
    private float mWaveShiftRatio = DEFAULT_WAVE_SHIFT_RATIO;

    private float mDefaultAmplitude;
    private float mDefaultWaterLevel;
    private float mDefaultWaveLength;
    private double mDefaultAngularFrequency;
    public static final int DEFAULT_BEHIND_WAVE_COLOR = Color.parseColor("#28000000");
    public static final int DEFAULT_FRONT_WAVE_COLOR = Color.parseColor("#3C000000");
    private int mBehindWaveColor = DEFAULT_BEHIND_WAVE_COLOR;
    private int mFrontWaveColor = DEFAULT_FRONT_WAVE_COLOR;


    public SpringWaveView(Context context) {
        this(context, null);
    }

    public SpringWaveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpringWaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mShaderMatrix = new Matrix();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (width == 0) {
            width = w;
            height = h;

//            calculate();
            createShader();
            initAnimator();
        }
    }

    private void initAnimator() {

        List<Animator> animators = new ArrayList<>();


        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setDuration(2000);
        animator.setInterpolator(new LinearInterpolator());

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if (mWaveShiftRatio != valueAnimator.getAnimatedFraction()) {
                    mWaveShiftRatio = valueAnimator.getAnimatedFraction();
                    invalidate();
                }
            }
        });
        animators.add(animator);



        ValueAnimator waterLevelAnim = ValueAnimator.ofFloat(0f, 0.5f);
        waterLevelAnim.setDuration(10000);
        waterLevelAnim.setInterpolator(new DecelerateInterpolator());
        waterLevelAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if (mWaterLevelRatio != valueAnimator.getAnimatedFraction()) {
                    mWaterLevelRatio = valueAnimator.getAnimatedFraction();
                    invalidate();
                }
            }
        });
//        animators.add(waterLevelAnim);

        // amplitude animation.
        // wave grows big then grows small, repeatedly
        ValueAnimator amplitudeAnim = ValueAnimator.ofFloat(0.0001f, 0.05f);
        amplitudeAnim.setRepeatCount(ValueAnimator.INFINITE);
        amplitudeAnim.setRepeatMode(ValueAnimator.REVERSE);
        amplitudeAnim.setDuration(5000);
        amplitudeAnim.setInterpolator(new LinearInterpolator());
        amplitudeAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if (mAmplitudeRatio != valueAnimator.getAnimatedFraction()) {
                    mAmplitudeRatio = valueAnimator.getAnimatedFraction();
                    invalidate();
                }
            }
        });
//        animators.add(amplitudeAnim);




        AnimatorSet mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(animators);

        mAnimatorSet.start();

    }
    private void createShader() {

//        A = height / 4;
//        mCircle = width;
//        W = (float) (2 * Math.PI / mCircle);//周期等于w

        mDefaultAngularFrequency = 2.0f * Math.PI / DEFAULT_WAVE_LENGTH_RATIO / getWidth();
        mDefaultAmplitude = getHeight() * DEFAULT_AMPLITUDE_RATIO;
        mDefaultWaterLevel = getHeight() * DEFAULT_WATER_LEVEL_RATIO;
        mDefaultWaveLength = getWidth();

        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint wavePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        wavePaint.setStrokeWidth(2);
        wavePaint.setStyle(Paint.Style.FILL);

        final int endX = getWidth() + 1;
        final int endY = getHeight() +1;

        float[] waveY = new float[endX];
        wavePaint.setColor(mBehindWaveColor);
        for (int beginX = 0; beginX < endX; beginX++) {
            double wx = beginX * mDefaultAngularFrequency;
            float beginY = (float) (mDefaultWaterLevel + mDefaultAmplitude * Math.sin(wx));
            canvas.drawLine(beginX, beginY, beginX, endY, wavePaint);

            waveY[beginX] = beginY;
        }

        wavePaint.setColor(mFrontWaveColor);
        final int wave2Shift = width / 4;
        for (int beginX = 0; beginX < endX; beginX++) {
            canvas.drawLine(beginX, waveY[(beginX + wave2Shift) % endX], beginX, endY, wavePaint);
        }

        // use the bitamp to create the shader
        mWaveShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
        mPaint.setShader(mWaveShader);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mWaveShader != null) {
            if (mPaint.getShader() == null) {
                mPaint.setShader(mWaveShader);
            }

            mShaderMatrix.setScale(
                    mWaveLengthRatio / DEFAULT_WAVE_LENGTH_RATIO,
                    mAmplitudeRatio / DEFAULT_AMPLITUDE_RATIO,
                    0,
                    mDefaultWaterLevel);

            mShaderMatrix.postTranslate(
                    mWaveShiftRatio * getWidth(),
                    (DEFAULT_WATER_LEVEL_RATIO - mWaterLevelRatio) * getHeight());

            mWaveShader.setLocalMatrix(mShaderMatrix);
        }

        canvas.drawRect(0, 0, width,
                height, mPaint);


    }
}
