package com.example.rrks.myapplication.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.TextView;

import com.blankj.utilcode.util.DeviceUtils;

public class LinearGradientFrame extends AppCompatTextView {
    private Context mContext;
    private int left, right, top, bottom;

    private RectF mRectF;
    private float textSize;
    private int mPaddignTop;
    private Paint mPaint;
    private LinearGradient mGradient;
    private int mPaddingLeft;
    private int mPaddingRight;
    private int mLineCount;
    private Path mPath;

    public LinearGradientFrame(Context context) {
        this(context, null);
    }

    public LinearGradientFrame(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LinearGradientFrame(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (heightMode == MeasureSpec.EXACTLY){
            bottom = heightSize;
        }else {

        }


        right = getMeasuredWidth();
        bottom = getMeasuredHeight();

        mPaddingLeft = getPaddingLeft();
        mPaddingRight = getPaddingRight();
        mPaddignTop = getPaddingTop();
        bottom += mPaddignTop;
        if (mGradient == null) {
            mGradient = new LinearGradient(0, 0, right, 0, stringToColor("#FA5758"), stringToColor("#FF884D"), Shader.TileMode.MIRROR);
            mPaint.setShader(mGradient);
            mPath = new Path();
            mPath.moveTo(dp2px(23),bottom);
            mPath.lineTo(dp2px(29),bottom+dp2px(5));
            mPath.lineTo(dp2px(34),bottom);
            mPath.close();
        }
        textSize = getLineHeight();
        mRectF.set(left, top, right, bottom);
        mLineCount = getLineCount();
        setMeasuredDimension(getMeasuredWidth(), (int) (mRectF.bottom+dp2px(5)));
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mRectF = new RectF(left, top, right, bottom);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);


    }

    private int stringToColor(String colorStr) {
        return Color.parseColor(colorStr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mRectF.set(left, top, right, bottom);
        canvas.drawRoundRect(mRectF, dp2px(2.5f), dp2px(2.5f), mPaint);
        canvas.drawPath(mPath,mPaint);
        super.onDraw(canvas);
    }

    private float dp2px(float dipValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
