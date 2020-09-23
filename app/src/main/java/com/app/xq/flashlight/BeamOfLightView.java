package com.app.xq.flashlight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * @author Android-小强
 * @email: 15075818555@163.com
 * @data: on 2020/9/23 16:56
 */
public class BeamOfLightView extends View {
    private Paint mPaint;
    private int[] colors;
    private LinearGradient linearGradient;
    private float[] position;

    public BeamOfLightView(Context context) {
        this(context, null);
    }

    public BeamOfLightView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BeamOfLightView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public BeamOfLightView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        colors = new int[]{
                Color.argb(5, 255, 255, 255),
                Color.argb(90, 255, 255, 255),
//                Color.argb(50, 255, 255, 255),
//                Color.argb(90, 255, 255, 255),
//                Color.argb(200, 255, 255, 255),
                Color.WHITE,
//                Color.argb(200, 255, 255, 255),
//                Color.argb(90, 255, 255, 255),
//                Color.argb(50, 255, 255, 255),
                Color.argb(90, 255, 255, 255),
                Color.argb(5, 255, 255, 255)
        };
        position = new float[]{
                0.7F,
                0.71F,
                0.75F,
                0.79F,
                0.8F
        };

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        SweepGradient sweepGradient = new SweepGradient(w * 0.5F, h*1.2F, colors, position);
        linearGradient = new LinearGradient(w * 0.1F, 0F, w * 0.9F, 0,
                colors,
                null,
                Shader.TileMode.CLAMP);
        mPaint.setShader(sweepGradient);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        canvas.drawArc(0, -getHeight() * 0.5F, getWidth(), getHeight() * 3, 225F, 90F, true, mPaint);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
