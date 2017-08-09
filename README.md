**先上效果图**


![device-2017-08-09-141038222.gif](http://upload-images.jianshu.io/upload_images/2704327-03d89c4acd7dd247.gif?imageMogr2/auto-orient/strip)

**为什么要有自定义控件?**
1、特定的显示风格
2、处理特有的用户交互
3、优化布局(如列表中通过自定义控件,实现复杂的自定义布局,减少messure)
4、封装等

**如何自定义控件**
1、自定义属性的声明与获取
2、测量onMeasrue
3、布局onLayout(ViewGroup)
4、绘制onDraw
5、onTouchEvent
6、onInterceptTouchEnvent(ViewGroup)
7、进度状态的恢复与保存(此处可直接继承Progress而非View,不用从0开始)


**下面开始实现**

**1.1样式的声明与获取**

1.1新建样式(目的是使用自定义View的时候可以直接在xml设置属性值)
attr.xml

```
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <attr name="HorizontalProgresUnReachColor" format="color"></attr>
    <attr name="HorizontalProgresUnReachHeight" format="dimension"></attr>
    <attr name="HorizontalProgresReachColor" format="color"></attr>
    <attr name="HorizontalProgresReachHeight" format="dimension"></attr>
    <attr name="HorizontalProgresTextColor" format="color"></attr>
    <attr name="HorizontalProgresTextSize" format="dimension"></attr>
    <attr name="HorizontalProgresTextOffset" format="dimension"></attr>


    <declare-styleable name="CustomHorizontalProgresStyle">
        <attr name="HorizontalProgresUnReachColor" ></attr>
        <attr name="HorizontalProgresUnReachHeight" ></attr>
        <attr name="HorizontalProgresReachColor" ></attr>
        <attr name="HorizontalProgresReachHeight" ></attr>
        <attr name="HorizontalProgresTextColor" ></attr>
        <attr name="HorizontalProgresTextSize" ></attr>
        <attr name="HorizontalProgresTextOffset"></attr>

    </declare-styleable>

</resources>



```


1.2样式的获取

构造方法中获取自定义属性

```
 /**
     * 获取自定义属性
     */
    private void geStyleabletAttr(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomHorizontalProgresStyle);
        HorizontalProgresUnReachColor = typedArray.getColor(R.styleable.CustomHorizontalProgresStyle_HorizontalProgresUnReachColor,DEAFUALT_PROGRESS_UNREACH_CORLOR);
        HorizontalProgresReachColor = typedArray.getColor(R.styleable.CustomHorizontalProgresStyle_HorizontalProgresUnReachColor,DEAFUALT_PROGRESS_REACH_CORLOR);
        //将sp、dp统一转换为sp
        HorizontalProgresReachHeight = typedArray.getColor(R.styleable.CustomHorizontalProgresStyle_HorizontalProgresReachHeight,dp2px(getContext(),DEAFUALT_PROGRESS_REACH_HEIGHH));
        HorizontalProgresUnReachHeight = typedArray.getColor(R.styleable.CustomHorizontalProgresStyle_HorizontalProgresUnReachHeight,dp2px(getContext(),DEAFUALT_PROGRESS_UNREACH_HEIGHH));
        HorizontalProgresTextColor = typedArray.getColor(R.styleable.CustomHorizontalProgresStyle_HorizontalProgresTextColor,DEAFUALT_PROGRESS_TEXT_CORLOR);
        HorizontalProgresTextSize = typedArray.getColor(R.styleable.CustomHorizontalProgresStyle_HorizontalProgresTextSize,DEAFUALT_PROGRESS_TEXT_SIZE);
        HorizontalProgresTextOffset = typedArray.getColor(R.styleable.CustomHorizontalProgresStyle_HorizontalProgresTextOffset,DEAFUALT_PROGRESS_TEXT_OFFSET);
        typedArray.recycle();//记得加这句
    }
```


2.2.1重写onMeasure

```
 /**
     * Determines the width of this view
     * @param measureSpec A measureSpec packed into an int
     * @return The width of the view, honoring constraints from measureSpec
     */
    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the text
            result = dp2px(getContext(),DEAFUALT_PROGRESS_VIEW_WIDTH);//
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by measureSpec
                result = Math.min(result, specSize);
            }
        }

        return result;
    }
```
2.2.2 计算宽度
```
 /**
     * Determines the width of this view
     * @param measureSpec A measureSpec packed into an int
     * @return The width of the view, honoring constraints from measureSpec
     */
    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the text
            result = dp2px(getContext(),DEAFUALT_PROGRESS_VIEW_WIDTH);//
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by measureSpec
                result = Math.min(result, specSize);
            }
        }

        return result;
    }


```

2.2.3 计算高度
```
    /**
     * Determines the height of this view
     * @param measureSpec A measureSpec packed into an int
     * @return The height of the view, honoring constraints from measureSpec
     */
    private int measureHeight(int measureSpec) {
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
            result = Math.max(textHeight,Math.max(HorizontalProgresReachHeight,HorizontalProgresUnReachHeight)) + getPaddingTop()
                    + getPaddingBottom();
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by measureSpec
                result = Math.min(result, specSize);
            }
        }
        return result;
    }
```

2.2.4 重写onDraw
```
 @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();//save、restore 图层的保存和回滚相关的方法 详见 http://blog.csdn.net/tianjian4592/article/details/45234419
        canvas.translate(0,getHeight()/2);//移动图层到垂直居中位置
        float radio = getProgress()*1.0f/getMax();
        int textWidth = (int) mPaint.measureText(getProgress()+"%");//The width of the text
        float realWidth = getWidth() - getPaddingLeft() - getPaddingRight() - textWidth - HorizontalProgresTextOffset;//实际宽度减去文字宽度
        float progressX  = radio * realWidth ;
        //绘制走完的进度线
        mPaint.setColor(HorizontalProgresReachColor);
        mPaint.setStrokeWidth(HorizontalProgresReachHeight);
        //canvas.drawLine(getPaddingLeft(),getPaddingTop(),progressX,getPaddingTop(),mPaint);//直角 垂直在同一高度 float startY, float stopY 一样
        RectF mRectF = new RectF(getPaddingLeft(),getPaddingTop()-HorizontalProgresReachHeight/2,(int)progressX,HorizontalProgresReachHeight/2);//圆角 int left, int top, int right, int bottom
        canvas.drawRoundRect(mRectF,15,15,mPaint);//圆角矩形
        //绘制进度
        mPaint.setColor(HorizontalProgresTextColor);
        mPaint.setTextSize(HorizontalProgresTextSize);
        int y = (int) -((mPaint.descent() + mPaint.ascent())/2);//文字居中
        canvas.drawText(getProgress()+"%",progressX + HorizontalProgresTextOffset/2,getPaddingTop() + y,mPaint);
        //绘制未做走完的进度
        if (progressX + HorizontalProgresTextOffset + textWidth < getWidth() - getPaddingLeft() - getPaddingRight()){//进度走完了,不再画未走完的
            mPaint.setColor(HorizontalProgresUnReachColor);
            mPaint.setStrokeWidth(HorizontalProgresUnReachHeight);
            //canvas.drawLine(getPaddingLeft()+progressX + HorizontalProgresTextOffset + textWidth,getPaddingTop(),getWidth() - getPaddingLeft() - getPaddingRight() ,getPaddingTop(),mPaint);//垂直在同一高度 float startY, float stopY 一样
            RectF mRectF2 = new RectF(getPaddingLeft()+progressX + HorizontalProgresTextOffset + textWidth,getPaddingTop()-HorizontalProgresUnReachHeight/2,getWidth() - getPaddingLeft() - getPaddingRight(),HorizontalProgresUnReachHeight/2);//圆角 int left, int top, int right, int bottom
            canvas.drawRoundRect(mRectF2,15,15,mPaint);//圆角矩形
        }
        canvas.restore();
    }

```

2.3.1布局文件中使用

```
  <LinearLayout
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:orientation="vertical" >

        <practice.csy.com.customprogress.view.CustomHorizontalProgres
            android:layout_marginTop="20dp"
            android:paddingLeft="10dp"
            android:paddingRight="10dp"
            android:id="@+id/horizontalProgress1"
            android:progress="0"
            app:HorizontalProgresReachColor="#FF0000"
            app:HorizontalProgresUnReachColor="#cab0b0"
            android:layout_width="200dp"
            android:layout_height="30dp">
        </practice.csy.com.customprogress.view.CustomHorizontalProgres>


        <practice.csy.com.customprogress.view.CustomHorizontalProgres
            android:paddingLeft="10dp"
            android:paddingRight="10dp"
            android:layout_marginTop="20dp"
            android:id="@+id/horizontalProgress2"
            android:progress="0"
            android:background="#ffffff"
            app:HorizontalProgresReachColor="#36513e"
            app:HorizontalProgresUnReachColor="#ffffff"
            android:layout_width="200dp"
            android:layout_height="30dp">

        </practice.csy.com.customprogress.view.CustomHorizontalProgres>

        <practice.csy.com.customprogress.view.CustomHorizontalProgres
            android:paddingLeft="10dp"
            android:paddingRight="10dp"
            android:layout_marginTop="20dp"
            android:id="@+id/horizontalProgress3"
            android:progress="0"
            app:HorizontalProgresReachColor="#00ffaa"
            app:HorizontalProgresUnReachColor="#0b1949"
            app:HorizontalProgresUnReachHeight="15dp"
            app:HorizontalProgresReachHeight="15dp"
            android:layout_width="200dp"
            android:layout_height="30dp">
        </practice.csy.com.customprogress.view.CustomHorizontalProgres>


    </LinearLayout>
```

2.3.2 代码中更新进度

```
  timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //实时更新进度
                if (horizontalProgress1.getProgress() >= 100) {//指定时间取消
                    timer.cancel();
                }
                horizontalProgress1.setProgress(horizontalProgress1.getProgress()+1);
            }
        }, 50, 50);

        timer2 = new Timer();
        timer2.schedule(new TimerTask() {
            @Override
            public void run() {
                //实时更新进度
                if (horizontalProgress2.getProgress() >= 100) {//指定时间取消
                    timer2.cancel();
                }
                horizontalProgress2.setProgress(horizontalProgress2.getProgress()+1);
            }
        }, 40, 40);


        timer3 = new Timer();
        timer3.schedule(new TimerTask() {
            @Override
            public void run() {
                //实时更新进度
                if (horizontalProgress3.getProgress() >= horizontalProgress3.getMax()) {//指定时间取消
                    timer3.cancel();
                }
                horizontalProgress3.setProgress(horizontalProgress3.getProgress()+1);
            }
        }, 50, 50);
    }
```