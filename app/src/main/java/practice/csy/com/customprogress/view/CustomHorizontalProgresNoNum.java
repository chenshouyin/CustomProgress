package practice.csy.com.customprogress.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

import practice.csy.com.customprogress.R;

/**
 * Created by user on 2017-8-8.
 * <p>
 * 直接继承ProgressBar可以实现进度额保存
 * <p>
 * 不带数字进度
 */

public class CustomHorizontalProgresNoNum extends ProgressBar {

    //默认值
    private static final int DEAFUALT_PROGRESS_UNREACH_HEIGHH = 10;//dp
    protected static final int DEAFUALT_PROGRESS_UNREACH_CORLOR = 0xFFD3D6DA;
    private static final int DEAFUALT_PROGRESS_REACH_HEIGHH = 10;//dp
    protected static final int DEAFUALT_PROGRESS_REACH_CORLOR = 0xFFFC00D1;
    protected static final int DEAFUALT_PROGRESS_TEXT_SIZE = 10;//sp
    protected static final int DEAFUALT_PROGRESS_TEXT_CORLOR = 0xFFD3D6DA;
    protected static final int DEAFUALT_PROGRESS_TEXT_OFFSET = 10;//dp
    private static final int DEAFUALT_PROGRESS_VIEW_WIDTH = 200;//进度条默认宽度

    protected int HorizontalProgresUnReachColor;//不能用static修饰,不然多个View会共用此属性
    private int HorizontalProgresUnReachHeight;
    protected int HorizontalProgresReachColor;
    private int HorizontalProgresReachHeight;
    protected int HorizontalProgresTextColor;
    protected int HorizontalProgresTextSize;
    protected int HorizontalProgresTextOffset;

    protected Paint mPaint = new Paint();
    //protected float[] rids = {15.0f, 15.0f, 15.0f, 15.0f, 0.0f, 0.0f, 0.0f, 0.0f,};  上面圆角

    protected Path path;
    protected float[] rids = {15.0f, 15.0f, 15.0f, 15.0f, 15.0f, 15.0f, 15.0f, 15.0f,};//四个角都圆角
    public CustomHorizontalProgresNoNum(Context context) {
        this(context, null);
    }

    public CustomHorizontalProgresNoNum(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomHorizontalProgresNoNum(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getStyleabletAttr(attrs);
        mPaint.setTextSize(HorizontalProgresTextSize);//设置画笔文字大小,便于后面测量文字宽高
        mPaint.setColor(HorizontalProgresTextColor);
        path = new Path();

    }


    /**
     * 获取自定义属性
     */
    protected void getStyleabletAttr(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomHorizontalProgresStyle);
        HorizontalProgresUnReachColor = typedArray.getColor(R.styleable.CustomHorizontalProgresStyle_HorizontalProgresUnReachColor, DEAFUALT_PROGRESS_UNREACH_CORLOR);
        HorizontalProgresReachColor = typedArray.getColor(R.styleable.CustomHorizontalProgresStyle_HorizontalProgresReachColor, DEAFUALT_PROGRESS_REACH_CORLOR);
        //将sp、dp统一转换为sp
        HorizontalProgresReachHeight = (int) typedArray.getDimension(R.styleable.CustomHorizontalProgresStyle_HorizontalProgresReachHeight, dp2px(getContext(), DEAFUALT_PROGRESS_REACH_HEIGHH));
        HorizontalProgresUnReachHeight = (int) typedArray.getDimension(R.styleable.CustomHorizontalProgresStyle_HorizontalProgresUnReachHeight, dp2px(getContext(), DEAFUALT_PROGRESS_UNREACH_HEIGHH));
        HorizontalProgresTextColor = typedArray.getColor(R.styleable.CustomHorizontalProgresStyle_HorizontalProgresTextColor, DEAFUALT_PROGRESS_TEXT_CORLOR);
        HorizontalProgresTextSize = (int) typedArray.getDimension(R.styleable.CustomHorizontalProgresStyle_HorizontalProgresTextSize, sp2px(getContext(), DEAFUALT_PROGRESS_TEXT_SIZE));
        HorizontalProgresTextOffset = (int) typedArray.getDimension(R.styleable.CustomHorizontalProgresStyle_HorizontalProgresTextOffset, DEAFUALT_PROGRESS_TEXT_OFFSET);
        typedArray.recycle();//记得加这句
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureWidth(widthMeasureSpec);//计算宽高
        int height = measureHeight(heightMeasureSpec);
        setMeasuredDimension(width, height);//设置宽高
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();//save、restore 图层的保存和回滚相关的方法 详见 http://blog.csdn.net/tianjian4592/article/details/45234419
        canvas.translate(0, getHeight() / 2);//移动图层到垂直居中位置


        float radio = getProgress() * 1.0f / getMax();
        float realWidth = getWidth() - getPaddingLeft() - getPaddingRight();//实际宽度减去文字宽度
        float progressX = radio * realWidth;

         /*向路径中添加圆角矩形。radii数组定义圆角矩形的四个圆角的x,y半径。radii长度必须为8*/
        path.addRoundRect(new RectF(getPaddingLeft(),getPaddingTop()-HorizontalProgresReachHeight/2,realWidth,HorizontalProgresReachHeight/2), rids, Path.Direction.CW);
        canvas.clipPath(path);

        //绘制走完的进度线
        mPaint.setColor(HorizontalProgresReachColor);
        mPaint.setStrokeWidth(HorizontalProgresReachHeight);
        //canvas.drawLine(getPaddingLeft(), getPaddingTop(), progressX, getPaddingTop(), mPaint);//直角 垂直在同一高度 float startY, float stopY 一样
        RectF mRectF = new RectF(getPaddingLeft(),getPaddingTop()-HorizontalProgresReachHeight/2,(int)progressX,HorizontalProgresReachHeight/2);//圆角 int left, int top, int right, int bottom
        canvas.drawRoundRect(mRectF,0,0,mPaint);//圆角矩形

        //绘制未做走完的进度
        if (progressX < getWidth() - getPaddingLeft() - getPaddingRight()) {//进度走完了,不再画未走完的
            mPaint.setColor(HorizontalProgresUnReachColor);
            mPaint.setStrokeWidth(HorizontalProgresUnReachHeight);
            //canvas.drawLine(progressX, getPaddingTop(), getWidth() - getPaddingLeft() - getPaddingRight(), getPaddingTop(), mPaint);//垂直在同一高度 float startY, float stopY 一样
            RectF mRectF2 = new RectF(progressX - 15 ,getPaddingTop()-HorizontalProgresUnReachHeight/2,realWidth,HorizontalProgresUnReachHeight/2);//圆角 int left, int top, int right, int bottom
            canvas.drawRoundRect(mRectF2,0,0,mPaint);//圆角矩形
        }
        canvas.restore();


    }

    /**
     * dp转px
     *
     * @param context
     * @param dpVal
     * @return
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     *
     * @param context
     * @param spVal
     * @return
     */
    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }


    /**
     * Determines the width of this view
     *
     * @param measureSpec A measureSpec packed into an int
     * @return The width of the view, honoring constraints from measureSpec
     */
    protected int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the text
            result = dp2px(getContext(), DEAFUALT_PROGRESS_VIEW_WIDTH);//
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by measureSpec
                result = Math.min(result, specSize);
            }
        }

        return result;
    }

    /**
     * Determines the height of this view
     *
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
            int textHeight = (int) (mPaint.descent() - mPaint.ascent());//得到字的高度有二种方式,第一种是React,第二种这个
            result = Math.max(textHeight, Math.max(HorizontalProgresReachHeight, HorizontalProgresUnReachHeight)) + getPaddingTop()
                    + getPaddingBottom();
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by measureSpec
                result = Math.min(result, specSize);
            }
        }
        return result;
    }


}
