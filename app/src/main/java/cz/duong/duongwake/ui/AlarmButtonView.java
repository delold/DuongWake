package cz.duong.duongwake.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import cz.duong.duongwake.R;
import cz.duong.duongwake.listeners.AlarmCloseListener;

/**
 * Vytvořeno David on 16. 3. 2015.
 */
public class AlarmButtonView extends View implements Runnable {
    private int mSize = 0;
    private int maxSize = 0;

    private AlarmCloseListener mListener;
    private Handler mHandler;

    private boolean isDown = false;

    private Paint mPaint;
    private Paint mHelperPaint;
    private ShapeDrawable mShape;

    private String mString;

    public AlarmButtonView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        TypedArray array = context.obtainStyledAttributes(attributeSet, R.styleable.AlarmButtonView);

        maxSize = array.getDimensionPixelSize(R.styleable.AlarmButtonView_circleSize, 300);

        mHandler = new Handler();

        mShape = new ShapeDrawable(new OvalShape());
        mShape.getPaint().setColor(array.getColor(R.styleable.AlarmButtonView_circleColor, 0xFFFFFF));

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(array.getColor(R.styleable.AlarmButtonView_backgroundColor, 0x00FFFFFF));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(array.getDimensionPixelSize(R.styleable.AlarmButtonView_textSize, 40));

        mHelperPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHelperPaint.setColor(array.getColor(R.styleable.AlarmButtonView_helperColor, 0x00FFFFFF));
        mHelperPaint.setStyle(Paint.Style.FILL);

        mString = array.getString(R.styleable.AlarmButtonView_textCircle);

        array.recycle();
    }

    public void setAlarmCloseListener(AlarmCloseListener mListener) {
        this.mListener = mListener;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = maxSize;
        params.width = maxSize;

        requestLayout();

        mHandler.post(this);
    }

    private void setSize(int s) {
        double size = getInterpolated(maxSize, s, maxSize);

        mShape.setBounds(
            (int)((maxSize - size) / 2),
            (int)((maxSize - size) / 2),
            (int)((maxSize + size) / 2),
            (int)((maxSize + size) / 2));

        invalidate();
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDown = true;
                break;
            case MotionEvent.ACTION_UP:
                isDown = false;
                break;
        }
        return event.getAction() == MotionEvent.ACTION_DOWN || super.onTouchEvent(event);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(maxSize / 2, maxSize / 2, maxSize / 2, mHelperPaint);

        mShape.draw(canvas);

        int x = maxSize / 2;
        int y = (int) ((maxSize / 2) - ((mPaint.descent() + mPaint.ascent()) / 2));

        canvas.drawText(mString, x, y, mPaint);
    }


    @Override
    public void run() {
        mSize += (isDown) ? 8 : -32;
        mSize = Math.min(maxSize, Math.max(0, mSize));

        setSize(mSize);
        invalidate();

        if(mSize == maxSize) {
            mListener.onAlarmClose(false);
        } else {
            mHandler.postDelayed(this, 20);
        }
    }

    //nevím, jak správně interpolovat, tak použijeme po úpravě funkci sinus
    private static double getInterpolated(int max_val, int x, int x_max) {
        return max_val*Math.pow(Math.sin((x*Math.PI)/(2*x_max)), 2);
    }
}
