package com.jumincho.beatingyesterday.ui.productivity;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Originally by pheynix (7/12/2015).
 * Disable hardware acceleration if the glow effect is required.
 * See: http://developer.android.com/guide/topics/graphics/hardware-accel.html
 */
public class CircularTimerView extends View {

    private boolean isInitialized = false;
    private boolean isStarted = false;
    private boolean isInDragButton;
    private int whichDragButton;

    private Calendar timeStart;
    private Calendar timeRemain;

    private float viewWidth;
    private float viewHeight;
    private float circleRadiusHour;
    private float circleRadiusMinute;
    private float circleRadiusSecond;
    private float circleRadiusDragButton;
    private float currentDegreeHour;
    private float currentDegreeMinute;
    private float currentDegreeSecond;
    private float centerXHour;
    private float centerYHour;
    private float centerXMinute;
    private float centerYMinute;
    private float centerXSecond;
    private float centerYSecond;
    private float strokeWidth;
    private String displayNumberHour;
    private String displayNumberMinute;
    private String displayNumberSecond;

    private float[] dragButtonHourPosition;
    private float[] dragButtonMinutePosition;
    private float[] dragButtonSecondPosition;

    private float[] defaultDragButtonHourPosition;
    private float[] defaultDragButtonMinutePosition;
    private float[] defaultDragButtonSecondPosition;

    private Paint paintCircleBackground;
    private Paint paintDragButton;
    private Paint paintHour;
    private Paint paintMinute;
    private Paint paintSecond;
    private Paint paintNumber;
    private Paint paintGlowEffect;

    private static final int colorDefault = 0xFFD6D6D6;
    private static final int colorHour = 0xFF9AD13C;
    private static final int colorMinute = 0xFFA55F7C;
    private static final int colorSecond = 0xFF00BCD4;

    private static final int DEFAULT_VIEW_WIDTH = 720;
    private static final int DEFAULT_VIEW_HEIGHT = 720;

    // Handler message codes. The "1/2" pair drives countdown (Timer mode),
    // the "11/12" pair drives count-up (StopWatch mode). Named here so the
    // switch in handleMessage(...) below isn't a wall of integer literals.
    private static final int MSG_TIMER_TICK = 1;
    private static final int MSG_TIMER_DONE = 2;
    private static final int MSG_STOPWATCH_TICK = 11;
    private static final int MSG_STOPWATCH_DONE = 12;

    // Per-tick increment for both modes; matches the 100ms Timer.schedule period.
    private static final int TICK_INTERVAL_MS = 100;
    private static final long TIMER_INITIAL_DELAY_MS = 1000L;

    // Hour-clock domain is [0..5] in this view (not the standard 24h) — see
    // setStartTime() Javadoc. updateDegree() uses 6.0 because the hour ring
    // displays a 0–5 range mapped to a full 360°.
    private static final int MAX_HOUR = 5;
    private static final int MAX_MINUTE = 59;
    private static final int MAX_SECOND = 59;

    private OnTimeChangeListener timeChangeListener;
    private OnMinChangListener minChangListener;
    private OnHourChangListener hourChangListener;
    private OnSecondChangListener secondChangListener;
    private OnInitialFinishListener initialFinishListener;
    private TimerMode mode = TimerMode.Timer;

    private void initialize(Canvas canvas) {
        Log.e("aa", "init");
        timeRemain = Calendar.getInstance();
        timeStart = Calendar.getInstance();
        timeStart.clear();
        timeRemain.clear();

        viewWidth = canvas.getWidth();
        viewHeight = canvas.getHeight();

        if (viewWidth > 720) {
            strokeWidth = 30;
            circleRadiusDragButton = 50;
        } else {
            strokeWidth = 15;
            circleRadiusDragButton = 25;
        }

        circleRadiusHour = viewWidth / 6;
        circleRadiusMinute = viewWidth / 3;
        circleRadiusSecond = viewWidth / 6;
        currentDegreeHour = 0;
        currentDegreeMinute = 0;
        currentDegreeSecond = 0;
        displayNumberHour = "0";
        displayNumberMinute = "0";
        displayNumberSecond = "0";
        centerXHour = viewWidth / 6 + strokeWidth / 2 + circleRadiusDragButton / 2;
        centerYHour = viewHeight / 6 + strokeWidth / 2 + circleRadiusDragButton / 2;
        centerXMinute = viewWidth / 2;
        centerYMinute = 2 * viewHeight / 3 - strokeWidth;
        centerXSecond = 5 * viewWidth / 6 - strokeWidth / 2 - circleRadiusDragButton / 2;
        centerYSecond = viewHeight / 6 + strokeWidth / 2 + circleRadiusDragButton / 2;

        defaultDragButtonHourPosition = new float[]{centerXHour, centerYHour - circleRadiusHour};
        defaultDragButtonMinutePosition = new float[]{centerXMinute, centerYMinute - circleRadiusMinute};
        defaultDragButtonSecondPosition = new float[]{centerXSecond, centerYSecond - circleRadiusSecond};
        dragButtonHourPosition = defaultDragButtonHourPosition;
        dragButtonMinutePosition = defaultDragButtonMinutePosition;
        dragButtonSecondPosition = defaultDragButtonSecondPosition;

        paintCircleBackground = new Paint();
        paintDragButton = new Paint();
        paintHour = new Paint();
        paintMinute = new Paint();
        paintSecond = new Paint();
        paintNumber = new Paint();
        paintGlowEffect = new Paint();

        paintCircleBackground.setColor(colorDefault);
        paintCircleBackground.setStrokeWidth(strokeWidth);
        paintCircleBackground.setStyle(Paint.Style.STROKE);
        paintCircleBackground.setAntiAlias(true);
        paintDragButton.setStrokeWidth(5);
        paintDragButton.setStyle(Paint.Style.FILL);
        paintDragButton.setAntiAlias(true);
        paintHour.setColor(colorHour);
        paintHour.setStrokeWidth(strokeWidth);
        paintHour.setStyle(Paint.Style.STROKE);
        paintHour.setAntiAlias(true);
        paintMinute.setColor(colorMinute);
        paintMinute.setStrokeWidth(strokeWidth);
        paintMinute.setStyle(Paint.Style.STROKE);
        paintMinute.setAntiAlias(true);
        paintSecond.setColor(colorSecond);
        paintSecond.setStrokeWidth(strokeWidth);
        paintSecond.setStyle(Paint.Style.STROKE);
        paintSecond.setAntiAlias(true);
        paintNumber.setStrokeWidth(2);
        paintNumber.setStyle(Paint.Style.FILL);
        paintNumber.setAntiAlias(true);

        paintGlowEffect.setMaskFilter(new BlurMaskFilter(2 * strokeWidth / 3, BlurMaskFilter.Blur.NORMAL));
        paintGlowEffect.setStrokeWidth(strokeWidth);
        paintGlowEffect.setAntiAlias(true);
        paintGlowEffect.setStyle(Paint.Style.FILL);

        if (initialFinishListener != null) {
            initialFinishListener.onInitialFinishListener();
        }
    }

    public CircularTimerView(Context context) {
        super(context);
    }

    public CircularTimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircularTimerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!isInitialized) {
            initialize(canvas);
            isInitialized = true;
        }

        if (isStarted) {
            updateDegree();
        }

        canvas.drawCircle(centerXHour, centerYHour, circleRadiusHour, paintCircleBackground);
        canvas.drawCircle(centerXMinute, centerYMinute, circleRadiusMinute, paintCircleBackground);
        canvas.drawCircle(centerXSecond, centerYSecond, circleRadiusSecond, paintCircleBackground);

        RectF rectFHour = new RectF(centerXHour - circleRadiusHour, centerYHour - circleRadiusHour,
                centerXHour + circleRadiusHour, centerYHour + circleRadiusHour);
        RectF rectFMinute = new RectF(centerXMinute - circleRadiusMinute, centerYMinute - circleRadiusMinute,
                centerXMinute + circleRadiusMinute, centerYMinute + circleRadiusMinute);
        RectF rectFSecond = new RectF(centerXSecond - circleRadiusSecond, centerYSecond - circleRadiusSecond,
                centerXSecond + circleRadiusSecond, centerYSecond + circleRadiusSecond);

        canvas.drawArc(rectFHour, -90, currentDegreeHour, false, paintHour);
        canvas.drawArc(rectFMinute, -90, currentDegreeMinute, false, paintMinute);
        canvas.drawArc(rectFSecond, -90, currentDegreeSecond, false, paintSecond);

        paintDragButton.setColor(colorHour);
        canvas.drawCircle(dragButtonHourPosition[0], dragButtonHourPosition[1], strokeWidth / 2, paintDragButton);
        paintGlowEffect.setColor(colorHour);
        canvas.drawCircle(dragButtonHourPosition[0], dragButtonHourPosition[1], strokeWidth, paintGlowEffect);

        paintDragButton.setColor(colorMinute);
        canvas.drawCircle(dragButtonMinutePosition[0], dragButtonMinutePosition[1], strokeWidth / 2, paintDragButton);
        paintGlowEffect.setColor(colorMinute);
        canvas.drawCircle(dragButtonMinutePosition[0], dragButtonMinutePosition[1], strokeWidth, paintGlowEffect);

        paintDragButton.setColor(colorSecond);
        canvas.drawCircle(dragButtonSecondPosition[0], dragButtonSecondPosition[1], strokeWidth / 2, paintDragButton);
        paintGlowEffect.setColor(colorSecond);
        canvas.drawCircle(dragButtonSecondPosition[0], dragButtonSecondPosition[1], strokeWidth, paintGlowEffect);

        getDisplayNumber();
        Rect rect = new Rect();

        paintNumber.setTextSize(70);
        paintNumber.setColor(colorHour);
        paintNumber.getTextBounds(displayNumberHour, 0, displayNumberHour.length(), rect);
        canvas.drawText(displayNumberHour, centerXHour - rect.width() / 2, centerYHour + rect.height() / 2, paintNumber);
        paintNumber.setTextSize(25);
        canvas.drawText("H", centerXHour + 30, centerYHour + 25, paintNumber);

        paintNumber.setTextSize(70);
        paintNumber.setColor(colorMinute);
        paintNumber.getTextBounds(displayNumberMinute, 0, displayNumberMinute.length(), rect);
        canvas.drawText(displayNumberMinute, centerXMinute - rect.width() / 2, centerYMinute + rect.height() / 2, paintNumber);
        paintNumber.setTextSize(25);
        canvas.drawText("M", centerXMinute + 50, centerYMinute + 25, paintNumber);

        paintNumber.setTextSize(70);
        paintNumber.setColor(colorSecond);
        paintNumber.getTextBounds(displayNumberSecond, 0, displayNumberSecond.length(), rect);
        canvas.drawText(displayNumberSecond, centerXSecond - rect.width() / 2, centerYSecond + rect.height() / 2, paintNumber);
        paintNumber.setTextSize(25);
        canvas.drawText("S", centerXSecond + 50, centerYSecond + 25, paintNumber);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isInDragButton(event.getX(), event.getY());
                break;

            case MotionEvent.ACTION_MOVE:
                if (!isStarted) {
                    if (isInDragButton) {
                        switch (whichDragButton) {
                            case 1:
                                currentDegreeHour = getDegree(event.getX(), event.getY(), centerXHour, centerYHour);
                                updateTime(1);
                                updateDragButtonPosition(1);
                                invalidate();
                                break;
                            case 2:
                                currentDegreeMinute = getDegree(event.getX(), event.getY(), centerXMinute, centerYMinute);
                                updateTime(2);
                                updateDragButtonPosition(2);
                                invalidate();
                                break;
                            case 3:
                                currentDegreeSecond = getDegree(event.getX(), event.getY(), centerXSecond, centerYSecond);
                                updateTime(3);
                                updateDragButtonPosition(3);
                                invalidate();
                                break;
                        }
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                isInDragButton = false;
                break;
        }

        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getDimension(DEFAULT_VIEW_WIDTH, widthMeasureSpec);
        int height = getDimension(width, heightMeasureSpec);

        viewWidth = width;
        viewHeight = height;

        setMeasuredDimension(width, height);
    }

    private int getDimension(int defaultDimension, int measureSpec) {
        int result;
        switch (MeasureSpec.getMode(measureSpec)) {
            case MeasureSpec.EXACTLY:
                result = MeasureSpec.getSize(measureSpec);
                break;
            case MeasureSpec.AT_MOST:
                result = Math.min(defaultDimension, MeasureSpec.getSize(measureSpec));
                break;
            default:
                result = defaultDimension;
                break;
        }
        return result;
    }

    private float getDegree(float eventX, float eventY, float centerX, float centerY) {
        double tx = eventX - centerX;
        double ty = eventY - centerY;
        double tLength = Math.sqrt(tx * tx + ty * ty);
        double a = Math.acos(ty / tLength);
        float degree = 180 - (float) Math.toDegrees(a);

        if (centerX > eventX) {
            degree = 180 + (float) Math.toDegrees(a);
        }

        return degree;
    }

    private void getDisplayNumber() {
        if (Integer.valueOf(displayNumberHour) != timeRemain.get(Calendar.HOUR_OF_DAY)) {
            displayNumberHour = timeRemain.get(Calendar.HOUR_OF_DAY) + "";
            if (hourChangListener != null) {
                hourChangListener.onHourChange(timeRemain.get(Calendar.HOUR_OF_DAY));
            }
        }
        if (Integer.valueOf(displayNumberMinute) != timeRemain.get(Calendar.MINUTE)) {
            displayNumberMinute = timeRemain.get(Calendar.MINUTE) + "";
            if (minChangListener != null) {
                minChangListener.onMinChange(timeRemain.get(Calendar.MINUTE));
            }
        }
        if (Integer.valueOf(displayNumberSecond) != timeRemain.get(Calendar.SECOND)) {
            displayNumberSecond = timeRemain.get(Calendar.SECOND) + "";
            if (secondChangListener != null) {
                secondChangListener.onSecondChange(timeRemain.get(Calendar.SECOND));
            }
        }
    }

    private void isInDragButton(float eventX, float eventY) {
        if (circleRadiusDragButton > Math.sqrt(Math.pow(eventX - dragButtonHourPosition[0], 2)
                + Math.pow(eventY - dragButtonHourPosition[1], 2))) {
            isInDragButton = true;
            whichDragButton = 1;
        } else if (circleRadiusDragButton > Math.sqrt(Math.pow(eventX - dragButtonMinutePosition[0], 2)
                + Math.pow(eventY - dragButtonMinutePosition[1], 2))) {
            isInDragButton = true;
            whichDragButton = 2;
        } else if (circleRadiusDragButton > Math.sqrt(Math.pow(eventX - dragButtonSecondPosition[0], 2)
                + Math.pow(eventY - dragButtonSecondPosition[1], 2))) {
            isInDragButton = true;
            whichDragButton = 3;
        } else {
            isInDragButton = false;
            whichDragButton = 0;
        }
    }

    private void updateDegree() {
        currentDegreeHour = (float) ((timeRemain.get(Calendar.HOUR_OF_DAY) * 60 + timeRemain.get(Calendar.MINUTE)) / (6.0 * 60)) * 360;
        currentDegreeMinute = (float) ((timeRemain.get(Calendar.MINUTE) * 60 + timeRemain.get(Calendar.SECOND)) / (60.0 * 60)) * 360;
        currentDegreeSecond = (float) ((timeRemain.get(Calendar.SECOND) * 1000 + timeRemain.get(Calendar.MILLISECOND)) / (60.0 * 1000)) * 360;

        updateDragButtonPosition(0);
    }

    private void updateDragButtonPosition(int flag) {
        switch (flag) {
            case 0:
                dragButtonHourPosition[0] = (float) (centerXHour + circleRadiusHour * (Math.sin(Math.toRadians(currentDegreeHour))));
                dragButtonHourPosition[1] = (float) (centerYHour - circleRadiusHour * (Math.cos(Math.toRadians(currentDegreeHour))));

                dragButtonMinutePosition[0] = (float) (centerXMinute + circleRadiusMinute * Math.sin(Math.toRadians(currentDegreeMinute)));
                dragButtonMinutePosition[1] = (float) (centerYMinute - circleRadiusMinute * Math.cos(Math.toRadians(currentDegreeMinute)));

                dragButtonSecondPosition[0] = (float) (centerXSecond + circleRadiusSecond * Math.sin(Math.toRadians(currentDegreeSecond)));
                dragButtonSecondPosition[1] = (float) (centerYSecond - circleRadiusSecond * Math.cos(Math.toRadians(currentDegreeSecond)));
                break;
            case 1:
                dragButtonHourPosition[0] = (float) (centerXHour + circleRadiusHour * (Math.sin(Math.toRadians(currentDegreeHour))));
                dragButtonHourPosition[1] = (float) (centerYHour - circleRadiusHour * (Math.cos(Math.toRadians(currentDegreeHour))));
                break;
            case 2:
                dragButtonMinutePosition[0] = (float) (centerXMinute + circleRadiusMinute * Math.sin(Math.toRadians(currentDegreeMinute)));
                dragButtonMinutePosition[1] = (float) (centerYMinute - circleRadiusMinute * Math.cos(Math.toRadians(currentDegreeMinute)));
                break;
            case 3:
                dragButtonSecondPosition[0] = (float) (centerXSecond + circleRadiusSecond * Math.sin(Math.toRadians(currentDegreeSecond)));
                dragButtonSecondPosition[1] = (float) (centerYSecond - circleRadiusSecond * Math.cos(Math.toRadians(currentDegreeSecond)));
                break;
        }
    }

    private void updateTime(int flag) {
        switch (flag) {
            case 0:
                timeStart.set(Calendar.HOUR_OF_DAY, (int) Math.floor(6 * currentDegreeHour / 360));
                timeRemain.set(Calendar.HOUR_OF_DAY, (int) Math.floor(6 * currentDegreeHour / 360));

                timeStart.set(Calendar.MINUTE, (int) Math.floor(60 * currentDegreeMinute / 360));
                timeRemain.set(Calendar.MINUTE, (int) Math.floor(60 * currentDegreeMinute / 360));

                timeStart.set(Calendar.SECOND, (int) Math.floor(60 * currentDegreeSecond / 360));
                timeRemain.set(Calendar.SECOND, (int) Math.floor(60 * currentDegreeSecond / 360));
                break;
            case 1:
                timeStart.set(Calendar.HOUR_OF_DAY, (int) Math.floor(6 * currentDegreeHour / 360));
                timeRemain.set(Calendar.HOUR_OF_DAY, (int) Math.floor(6 * currentDegreeHour / 360));
                break;
            case 2:
                timeStart.set(Calendar.MINUTE, (int) Math.floor(60 * currentDegreeMinute / 360));
                timeRemain.set(Calendar.MINUTE, (int) Math.floor(60 * currentDegreeMinute / 360));
                break;
            case 3:
                timeStart.set(Calendar.SECOND, (int) Math.floor(60 * currentDegreeSecond / 360));
                timeRemain.set(Calendar.SECOND, (int) Math.floor(60 * currentDegreeSecond / 360));
                break;
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case MSG_TIMER_TICK:
                    timeRemain.add(Calendar.MILLISECOND, -TICK_INTERVAL_MS);
                    if (timeChangeListener != null) {
                        timeChangeListener.onTimeChange(timeStart.getTimeInMillis(), timeRemain.getTimeInMillis());
                    }
                    invalidate();
                    break;
                case MSG_TIMER_DONE:
                    isStarted = false;
                    timerTask.cancel();
                    break;
                case MSG_STOPWATCH_TICK:
                    timeRemain.add(Calendar.MILLISECOND, TICK_INTERVAL_MS);
                    if (timeChangeListener != null) {
                        timeChangeListener.onTimeChange(timeStart.getTimeInMillis(), timeRemain.getTimeInMillis());
                    }
                    invalidate();
                    break;
                case MSG_STOPWATCH_DONE:
                    isStarted = false;
                    timerTask.cancel();
                    break;
            }
        }
    };

    Timer timer = new Timer(true);
    TimerTask timerTask;

    public boolean start() {
        if (mode == TimerMode.Timer) {
            if (!isTimeEmpty() && !isStarted) {
                timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        if (!isTimeEmpty()) {
                            Message message = new Message();
                            message.what = MSG_TIMER_TICK;
                            mHandler.sendMessage(message);
                        } else {
                            Message message = new Message();
                            message.what = MSG_TIMER_DONE;
                            mHandler.sendMessage(message);
                        }
                    }
                };

                timer.schedule(timerTask, TIMER_INITIAL_DELAY_MS, TICK_INTERVAL_MS);
                isStarted = true;

                if (timeChangeListener != null) {
                    timeChangeListener.onTimerStart(timeStart.getTimeInMillis());
                }
            }
        } else if (mode == TimerMode.StopWatch) {
            if (!isMaxTime() && !isStarted) {
                timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        if (!isMaxTime()) {
                            Message message = new Message();
                            message.what = MSG_STOPWATCH_TICK;
                            mHandler.sendMessage(message);
                        } else {
                            Message message = new Message();
                            message.what = MSG_STOPWATCH_DONE;
                            mHandler.sendMessage(message);
                        }
                    }
                };

                timer.schedule(timerTask, TIMER_INITIAL_DELAY_MS, TICK_INTERVAL_MS);
                isStarted = true;

                if (timeChangeListener != null) {
                    timeChangeListener.onTimerStart(timeStart.getTimeInMillis());
                }
            }
        }
        return isStarted;
    }

    private boolean isTimeEmpty() {
        return timeRemain.get(Calendar.HOUR_OF_DAY) == 0
                && timeRemain.get(Calendar.MINUTE) == 0
                && timeRemain.get(Calendar.SECOND) == 0
                && timeRemain.get(Calendar.MILLISECOND) == 0;
    }

    public long stop() {
        if (timerTask != null) {
            timerTask.cancel();   // reset() calls stop() before start(); timerTask is null until start()
        }
        isStarted = false;

        if (timeChangeListener != null) {
            timeChangeListener.onTimeStop(timeStart.getTimeInMillis(), timeRemain.getTimeInMillis());
        }

        return timeStart.getTimeInMillis() - timeRemain.getTimeInMillis();
    }

    public Calendar getTimeStart() {
        return timeStart;
    }

    public Calendar getTimeRemain() {
        return timeRemain;
    }

    public long getTimePass() {
        return timeStart.getTimeInMillis() - timeRemain.getTimeInMillis();
    }

    public void setOnTimeChangeListener(OnTimeChangeListener listener) {
        if (listener != null) {
            timeChangeListener = listener;
        }
    }

    public void setMinChangListener(OnMinChangListener minChangListener) {
        this.minChangListener = minChangListener;
    }

    public void setSecondChangListener(OnSecondChangListener secondChangListener) {
        this.secondChangListener = secondChangListener;
    }

    public void setHourChangListener(OnHourChangListener hourChangListener) {
        this.hourChangListener = hourChangListener;
    }

    public void reset() {
        stop();
        isInitialized = false;
        invalidate();
    }

    public boolean isMaxTime() {
        return timeRemain.get(Calendar.HOUR_OF_DAY) == MAX_HOUR
                && timeRemain.get(Calendar.MINUTE) == MAX_MINUTE
                && timeRemain.get(Calendar.SECOND) == MAX_SECOND;
    }

    public interface OnTimeChangeListener {
        void onTimerStart(long timeStart);

        void onTimeChange(long timeStart, long timeRemain);

        void onTimeStop(long timeStart, long timeRemain);
    }

    public interface OnMinChangListener {
        void onMinChange(int minute);
    }

    public interface OnHourChangListener {
        void onHourChange(int hour);
    }

    public interface OnInitialFinishListener {
        void onInitialFinishListener();
    }

    public interface OnSecondChangListener {
        void onSecondChange(int second);
    }

    public void setMode(TimerMode mode) {
        this.mode = mode;
    }

    /**
     * @param h max 5
     * @param m max 59
     * @param s max 59
     */
    public void setStartTime(final int h, final int m, final int s) throws NumberFormatException {
        initialFinishListener = new OnInitialFinishListener() {
            @Override
            public void onInitialFinishListener() {
                if (h > MAX_HOUR || m > MAX_MINUTE || s > MAX_SECOND || h < 0 || m < 0 || s < 0) {
                    throw new NumberFormatException("hour must in [0-5], minute and second must in [0-59]");
                }
                timeRemain.set(Calendar.HOUR_OF_DAY, h);
                timeRemain.set(Calendar.MINUTE, m);
                timeRemain.set(Calendar.SECOND, s);
                timeStart.set(Calendar.HOUR_OF_DAY, h);
                timeStart.set(Calendar.MINUTE, m);
                timeStart.set(Calendar.SECOND, s);
                updateDegree();
                invalidate();
            }
        };
    }
}
