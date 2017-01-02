package com.quascenta.petersroad.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.PathInterpolator;
import android.widget.EditText;

/**
 * Created by AKSHAY on 12/22/2016.
 */

public class LogTagEditText extends EditText {

    CharSequence mAnimHintString = "";

        Paint mHintPaint;

        int mDefaultAlpha;
        float mDefaultTextSize;
        float mTextLength;


public LogTagEditText(Context context) {
        super(context);
        init();
        }

public LogTagEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        }

public LogTagEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        }

private void init() {
        mHintPaint = new Paint();
        mHintPaint.setAntiAlias(true);
        mHintPaint.setColor(getHintTextColors().getDefaultColor());
        mHintPaint.setAlpha((int) getAlpha());
        mHintPaint.setColor(Color.GRAY);
        mHintPaint.setTextSize(getTextSize());
        mHintPaint.setTextAlign(Paint.Align.CENTER);
        mDefaultTextSize = getTextSize();
        mDefaultAlpha = mHintPaint.getAlpha();
        }


@Override
protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (TextUtils.isEmpty(getText().toString())) {
        if (Float.floatToRawIntBits(mTextLength) == 0) {
        mTextLength = getPaint().measureText(mAnimHintString.toString());
        }
        float startX = getCompoundPaddingLeft();
        canvas.drawText(mAnimHintString.toString(), startX + mTextLength / 2, getLineBounds(0, null), mHintPaint);
        }

        }

/**
 * Hint
 *
 * @param hint
 */
public void setHintString(final CharSequence hint) {
        mAnimHintString = hint;
        if (mAnimHintString != null) {
        mTextLength = getPaint().measureText(mAnimHintString.toString());
        }else{
        mTextLength = 0;
        }
        invalidate();
        }

/**
 *
 *
 * @param hint
 */
public void changeHintWithAnim(final CharSequence hint) {


        ValueAnimator rollPlayAnim = ValueAnimator.ofFloat(100, 90);
        rollPlayAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
@Override
public void onAnimationUpdate(ValueAnimator animation) {

        float value = (float) animation.getAnimatedValue();

        float textsize = mDefaultTextSize * (value / 100);
        float alpha = (value - 90) / 10 * mDefaultAlpha;

        mHintPaint.setTextSize(textsize);
        mHintPaint.setAlpha((int) alpha);

        invalidate();
        }
        });
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        rollPlayAnim.setInterpolator(new PathInterpolator(0.3f, 0, 0.7f, 1));
    }
    rollPlayAnim.setDuration(250);
        rollPlayAnim.setRepeatMode(ValueAnimator.REVERSE);
        rollPlayAnim.setRepeatCount(1);
        rollPlayAnim.setStartDelay(30);


        rollPlayAnim.addListener(new Animator.AnimatorListener() {
@Override
public void onAnimationStart(Animator animation) {
        mTextLength = getPaint().measureText(mAnimHintString.toString());
        }

@Override
public void onAnimationEnd(Animator animation) {
        mAnimHintString = hint;
        }

@Override
public void onAnimationCancel(Animator animation) {

        }

@Override
public void onAnimationRepeat(Animator animation) {
        mAnimHintString = hint;
        mTextLength = getPaint().measureText(mAnimHintString.toString());
        }
        });

        rollPlayAnim.start();

        }




        public CharSequence getAnimHintString() {
        return mAnimHintString;
        }


        }

