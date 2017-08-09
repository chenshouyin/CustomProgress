package practice.csy.com.customprogress.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

import practice.csy.com.customprogress.R;

/**
 * Created by user on 2017-8-8.
 *
 * 直接继承ProgressBar可以实现进度额保存
 *
 * 带数字进度
 */

public class RoundlProgresWithNum extends CustomHorizontalProgresNoNum{


    private static final int DEAFUALT_PROGRESS_RADIO = 50;//圆的半径
    private static final int DEAFUALT_PROGRESS_UNREACH_PAINT_STROKEN = 2;//未走完进度的宽度
    private static final int DEAFUALT_PROGRESS_REACH_PAINT_STROKEN = 5;//走完进度的宽度
    private static final int DEAFUALT_PROGRESS_REACH_START_DGREE = 0;//走完进度 圆弧 默认从多少度开始
    private static final int DEAFUALT_PROGRESS_REACH_END_DGREE = 360;
    private static final boolean DEAFUALT_PROGRESS_HAS_NUM = false;//是否带进度数字显示

    private  int mCustomCircleRadio ;//圆的半径
    private  int mCustomCircleUnReachPaintStroken ;//走完进度的
    private  int mCustomCircleReachPaintStroken ;//走完进度的画笔宽度
    private int mCustomUnReachEnd;
    private int mCustomUnReachStart;
    private boolean mCustomHasNum;

    private RectF mRectFOval;

    public RoundlProgresWithNum(Context context) {
        this(context,null);
    }

    public RoundlProgresWithNum(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public RoundlProgresWithNum(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint.setTextSize(HorizontalProgresTextSize);//设置画笔文字大小,便于后面测量文字宽高
        mPaint.setColor(HorizontalProgresTextColor);
    }


    @Override
    protected void getStyleabletAttr(AttributeSet attrs) {
        super.getStyleabletAttr(attrs);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomCircleProgresStyle);
        mCustomCircleRadio = (int) typedArray.getDimension(R.styleable.CustomCircleProgresStyle_CustomCircleRadio,dp2px(getContext(),DEAFUALT_PROGRESS_RADIO));
        mCustomCircleUnReachPaintStroken = (int) typedArray.getDimension(R.styleable.CustomCircleProgresStyle_CustomUnReachPaintStroken,dp2px(getContext(),DEAFUALT_PROGRESS_UNREACH_PAINT_STROKEN));
        mCustomCircleReachPaintStroken = (int) typedArray.getDimension(R.styleable.CustomCircleProgresStyle_CustomReachPaintStroken,dp2px(getContext(),DEAFUALT_PROGRESS_REACH_PAINT_STROKEN));
        mCustomUnReachStart =  typedArray.getInteger(R.styleable.CustomCircleProgresStyle_CustomUnReachStart,DEAFUALT_PROGRESS_REACH_START_DGREE);
        mCustomUnReachEnd =  typedArray.getInteger(R.styleable.CustomCircleProgresStyle_CustomUnReachEnd,DEAFUALT_PROGRESS_REACH_END_DGREE);
        mCustomHasNum = typedArray.getBoolean(R.styleable.CustomCircleProgresStyle_CustomHasProgressNum,DEAFUALT_PROGRESS_HAS_NUM);
        typedArray.recycle();//记得加这句

    }

    /**
     * 获取自定义属性
     */


    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureWidth(widthMeasureSpec);//计算宽高
        int height = measureHeight(heightMeasureSpec);
        width = height = Math.min(width,height);//取宽高的最小值
        if (mCustomCircleRadio>width/2){
            mCustomCircleRadio = width/2;
        }
        mCustomCircleRadio = mCustomCircleRadio - mCustomCircleReachPaintStroken/2;//还要减去画笔的宽度
        setMeasuredDimension(width,height);//设置宽高
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        //super.onDraw(canvas);//不需要父类里面的代码
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        //移动画布,减少后续画图换算
        int centerX = getWidth()/2;
        int centerY = getHeight()/2;
        canvas.save();//save、restore 图层的保存和回滚相关的方法 详见 http://blog.csdn.net/tianjian4592/article/details/45234419
        canvas.translate(centerX,centerY);//移动图层到垂直居中位置
        //draw unreach circle
        mPaint.setColor(HorizontalProgresUnReachColor);
        mPaint.setStrokeWidth(mCustomCircleUnReachPaintStroken);
        canvas.drawCircle(0,0,mCustomCircleRadio,mPaint);
        //draw reach circle
        mPaint.setColor(HorizontalProgresReachColor);
        mPaint.setStrokeWidth(mCustomCircleReachPaintStroken);
        int progress = (int) ((getProgress()*1.0f/getMax())*360);//圆弧度数
        int mTotalProgress = Math.abs(mCustomUnReachStart) + Math.abs(mCustomUnReachEnd);
        if (progress<=mTotalProgress){
            if (mRectFOval==null){//只创建一次,减少内存的创建与回收
                mRectFOval = new RectF(-mCustomCircleRadio,-mCustomCircleRadio,mCustomCircleRadio,mCustomCircleRadio);//float left, float top, float right, float bottom
            }
            canvas.drawArc(mRectFOval,mCustomUnReachStart,progress,false,mPaint);////false绘制圆弧，不含圆心   @NonNull RectF oval, float startAngle, float sweepAngle, boolean useCenter, @NonNull Paint paint
        } else{
            if (mRectFOval==null){//只创建一次,减少内存的创建与回收
                mRectFOval = new RectF(-mCustomCircleRadio,-mCustomCircleRadio,mCustomCircleRadio,mCustomCircleRadio);//float left, float top, float right, float bottom
            }
            canvas.drawArc(mRectFOval,mCustomUnReachStart,mTotalProgress,false,mPaint);////false绘制圆弧，不含圆心   @NonNull RectF oval, float startAngle, float sweepAngle, boolean useCenter, @NonNull Paint paint
        }
        //draw Text
        if (mCustomHasNum){
            mPaint.setColor(HorizontalProgresTextColor);
            mPaint.setTextSize(HorizontalProgresTextSize);
            mPaint.setStyle(Paint.Style.FILL);
            String currentProgress = getProgress()+"%";
            Rect mRectF = new Rect();
            mPaint.getTextBounds(currentProgress,0,currentProgress.length(),mRectF);
            //canvas.drawText(currentProgress,mRectF.width(),mRectF.height(),mPaint);
            canvas.drawText(currentProgress,0 - mRectF.width()/2,0+mRectF.height()/2,mPaint);
        }
        canvas.restore();
    }




    /**
     * Determines the width of this view
     * @param measureSpec A measureSpec packed into an int
     * @return The width of the view, honoring constraints from measureSpec
     */
    protected int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = Math.min(specSize,mCustomCircleRadio*2);
        } else {
            // Measure the text
            result = dp2px(getContext(),2*mCustomCircleRadio);//
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by measureSpec
                result = Math.min(result, specSize);
            }
        }

        return result;
    }

    /**
     * Determines the height of this view
     * @param measureSpec A measureSpec packed into an int
     * @return The height of the view, honoring constraints from measureSpec
     */
    protected int measureHeight(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the text (beware: ascent is a negative number)
            //此处高度为走完的进度高度和未走完的机大以及文字的高度的最大值
            result = 2*mCustomCircleRadio + getPaddingTop()
                    + getPaddingBottom();
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by measureSpec
                result = Math.min(result, specSize);
            }
        }
        return result;
    }



}
