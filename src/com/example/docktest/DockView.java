package com.example.docktest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class DockView extends View {

    private int width;
    private int height;

    private int centerX;
    private int centerY;

    private int dockCount = 10;
    private int dockInitHeight = 60;
    private int radius = 150;
    private float dockScale = 3F;

    private int currentX;
    private int currentY;

    private int totalHeight;

    private int[] dockCurrentHeights = new int[dockCount];

    private Bitmap res;
    private Rect bound;

    private Paint paint;

    public DockView(Context context) {
        this(context, null);
    }

    public DockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DockView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        res = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
        bound = new Rect();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);

        centerX = width / 2;
        centerY = height / 2;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                currentX = (int) event.getX();
                currentY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                currentX = (int) event.getX();
                currentY = (int) event.getY();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                break;
        }
        invalidate();

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        totalHeight = 0;
        int startY = centerY - (dockCount * dockInitHeight) / 2;
        for (int i = 0; i < dockCount; i++) {
            int currentIconY = startY + (i * dockInitHeight) + (dockInitHeight / 2);

            int cDistance = Math.abs(currentIconY - currentY);
            float ratio = (float) cDistance / (float) radius;

            int gap = (int) ((dockInitHeight * dockScale) - dockInitHeight);

            dockCurrentHeights[i] = (int) (dockInitHeight + (gap * (1 - ratio)));

            dockCurrentHeights[i] =
                    dockCurrentHeights[i] < dockInitHeight ? dockInitHeight : dockCurrentHeights[i];

            totalHeight += dockCurrentHeights[i];
            Log.d("dockCurrentHeight:" + i, dockCurrentHeights[i] + "");
        }

        startY = centerY - (totalHeight / 2);
        int stepY = startY;
        for (int i = 0; i < dockCount; i++) {

            bound.top = stepY;
            bound.left = 0;
            bound.right = dockCurrentHeights[i];
            bound.bottom = bound.top + dockCurrentHeights[i];

            canvas.drawBitmap(res, null, bound, paint);
            stepY += dockCurrentHeights[i];
        }
    }
}
