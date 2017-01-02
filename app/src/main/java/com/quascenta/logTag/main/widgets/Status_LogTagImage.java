package com.quascenta.logTag.main.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by AKSHAY on 12/29/2016.
 */

public class Status_LogTagImage extends ImageView {

    Context context;
    int code;
    Paint paint;


    public Status_LogTagImage(Context context) {
        super(context);
        paint = new Paint();
        paint.setStrokeWidth(20);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
    }
    public Status_LogTagImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = this.getHeight()/2;
        int width_left = this.getLeft();
        int width_right = this.getRight();
        canvas.drawLine(this.getLeft(),height,this.getRight(),height,paint);

    }
}
