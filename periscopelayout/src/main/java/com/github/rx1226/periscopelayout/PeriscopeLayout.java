/*
 * Copyright (C) 2015, 程序亦非猿
 * Modify by RX1226 in 2018
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.rx1226.periscopelayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class PeriscopeLayout extends RelativeLayout {
    private Interpolator[] interpolators = {
            new AccelerateInterpolator(), //加速
            new AccelerateDecelerateInterpolator(), //先加速后减速
//            new AnticipateInterpolator(), //先後在前
//            new AnticipateOvershootInterpolator(), //回後震盪
//            new BounceInterpolator(), //動畫結束的時候彈起
            new DecelerateInterpolator(), //先快後慢
            new LinearInterpolator(), //线性
//            new OvershootInterpolator() //反彈
    };

    private int mHeight;
    private int mWidth;
    private int fHeight;
    private int fWidth;
    private boolean isFixSize;
    private Random random = new Random();
    private int frequency = 1000;
    private int position;

    public PeriscopeLayout(Context context) {
        super(context);
        init();
    }

    public PeriscopeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        setAttr(context, attrs);
    }

    public PeriscopeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        setAttr(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PeriscopeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
        setAttr(context, attrs);
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private void setAttr(Context context, AttributeSet attrs){
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PeriscopeLayout);
        boolean enableAuto = array.getBoolean(R.styleable.PeriscopeLayout_auto, false);
        if(enableAuto) startAuto();
        int size = array.getInteger(R.styleable.PeriscopeLayout_size, 0);
        if(size > 0){
            setFixSize(size, size);
        }
        int position = array.getInteger(R.styleable.PeriscopeLayout_position, 0);
        if(size != 0) setPosition(position);
        array.recycle();
    }

    private List<FlyImage> flyImages;

    private class FlyImage{
        Drawable drawable;
        int width;
        int height;
        LayoutParams params;

        FlyImage(Drawable drawable, int width, int height, LayoutParams params) {
            this.drawable = drawable;
            this.width = width;
            this.height = height;
            this.params = params;
        }
    }

    private void init() {

        //初始化显示的图片
        Drawable[] drawables = new Drawable[]{
                getResources().getDrawable(R.drawable.pl_red),
                getResources().getDrawable(R.drawable.pl_yellow),
                getResources().getDrawable(R.drawable.pl_blue)
        };
        position = Position.CENTER;
        setDrawables(drawables);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    public void setDrawables(Drawable... drawables){
        flyImages = new ArrayList<>();
        int width;
        int height;
        for(Drawable drawable: drawables){
            if(isFixSize){
                width = fWidth;
                height = fHeight;
            }else {
                width = drawable.getIntrinsicWidth();
                height = drawable.getIntrinsicHeight();
            }
            LayoutParams params = new LayoutParams(width, height);
            params.addRule(position, TRUE);//这里的TRUE 要注意 不是true
            params.addRule(ALIGN_PARENT_BOTTOM, TRUE);
            flyImages.add(new FlyImage(drawable,
                    width,
                    height,
                    params
            ));
        }
    }
    public void setPosition(int location){
        int width;
        int height;
        position = location;
        for(int i = 0; i < flyImages.size(); i++){
            if(isFixSize){
                width = fWidth;
                height = fHeight;
            }else {
                width = flyImages.get(i).drawable.getIntrinsicWidth();
                height = flyImages.get(i).drawable.getIntrinsicHeight();
            }
            LayoutParams params = new LayoutParams(width, height);
            params.addRule(location, TRUE);//这里的TRUE 要注意 不是true
            params.addRule(ALIGN_PARENT_BOTTOM, TRUE);
            flyImages.get(i).params = params;
        }
    }
    public void setFixSize(int width, int height){
        isFixSize = true;
        fWidth = width;
        fHeight = height;
        LayoutParams params = new LayoutParams(width, height);
        params.addRule(position, TRUE);//这里的TRUE 要注意 不是true
        params.addRule(ALIGN_PARENT_BOTTOM, TRUE);
        for (int i = 0; i < flyImages.size(); i++){
            flyImages.get(i).params = params;
        }
    }

    public void setDynamicSize(){
        isFixSize = false;
        int width;
        int height;
        for(int i = 0; i < flyImages.size(); i++){
            width = flyImages.get(i).drawable.getIntrinsicWidth();
            height = flyImages.get(i).drawable.getIntrinsicHeight();
            LayoutParams params = new LayoutParams(width, height);
            params.addRule(position, TRUE);//这里的TRUE 要注意 不是true
            params.addRule(ALIGN_PARENT_BOTTOM, TRUE);
            flyImages.get(i).params = params;
        }
    }
    public void addHeart() {
        int index = random.nextInt(flyImages.size());
        ImageView imageView = new ImageView(getContext());
        //随机选一个
        imageView.setImageDrawable(flyImages.get(index).drawable);
        imageView.setLayoutParams(flyImages.get(index).params);

        addView(imageView);

        Animator set = getAnimator(imageView, index);
        set.addListener(new AnimEndListener(imageView));
        set.start();
    }

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            addHeart();
            handler.postDelayed(this, random.nextInt(frequency));
        }
    };

    public void startAuto(){
        handler.postDelayed(runnable, random.nextInt(frequency));
    }

    private AtomicInteger times;
    public void startAuto(int limits){
        times = new AtomicInteger(0);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (times.getAndAdd(1) < limits){
                    addHeart();
                    handler.postDelayed(this, random.nextInt(frequency));
                }else {
                    handler.removeCallbacks(this);
                }
            }
        }, random.nextInt(frequency));
    }
    public void stopAuto(){
        handler.removeCallbacks(runnable);
    }

    private Animator getAnimator(View target, int index) {
        AnimatorSet set = getEnterAnimtor(target);

        ValueAnimator bezierValueAnimator = getBezierValueAnimator(target, index);

        AnimatorSet finalSet = new AnimatorSet();
        finalSet.playSequentially(set);
        finalSet.playSequentially(set, bezierValueAnimator);
        finalSet.setInterpolator(interpolators[random.nextInt(interpolators.length)]);
        finalSet.setTarget(target);
        return finalSet;
    }

    private AnimatorSet getEnterAnimtor(final View target) {

        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, View.ALPHA, 0.2f, 1f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(target, View.SCALE_X, 0.2f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(target, View.SCALE_Y, 0.2f, 1f);
        AnimatorSet enter = new AnimatorSet();
        enter.setDuration(500);
        enter.setInterpolator(new LinearInterpolator());
        enter.playTogether(alpha, scaleX, scaleY);
        enter.setTarget(target);
        return enter;
    }

    private ValueAnimator getBezierValueAnimator(View target, int index) {
        BezierEvaluator evaluator = new BezierEvaluator(getPointF(2), getPointF(1));
        int width;
        int height;
        float x = 0;
        if(isFixSize){
            width = fWidth;
            height = fHeight;
        }else {
            width = flyImages.get(index).width;
            height = flyImages.get(index).height;
        }
        switch (position){
            case Position.RIGHT:
                x = mWidth - width;
                break;
            case Position.CENTER:
                x = (mWidth - width) / 2;
                break;
            case Position.LEFT:
                x = 0;
            default:
                break;
        }
        ValueAnimator animator = ValueAnimator.ofObject(evaluator, new PointF(x, mHeight - height), new PointF(random.nextInt(getWidth()), 0));
        animator.addUpdateListener(new BezierListener(target));
        animator.setTarget(target);
        animator.setDuration(3000);
        return animator;
    }

    /**
     * 获取中间的两个 点
     */
    private PointF getPointF(int scale) {

        PointF pointF = new PointF();
        pointF.x = random.nextInt((mWidth - 100));//减去100 是为了控制 x轴活动范围,看效果 随意~~
        //再Y轴上 为了确保第二个点 在第一个点之上,我把Y分成了上下两半 这样动画效果好一些  也可以用其他方法
        pointF.y = random.nextInt((mHeight - 100)) / scale;
        return pointF;
    }

    private class BezierListener implements ValueAnimator.AnimatorUpdateListener {

        private View target;

        BezierListener(View target) {
            this.target = target;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            //这里获取到贝塞尔曲线计算出来的的x y值 赋值给view 这样就能让爱心随着曲线走啦
            PointF pointF = (PointF) animation.getAnimatedValue();
            target.setX(pointF.x);
            target.setY(pointF.y);
            // 这里顺便做一个alpha动画
            target.setAlpha(1 - animation.getAnimatedFraction());
        }
    }


    private class AnimEndListener extends AnimatorListenerAdapter {
        private View target;

        AnimEndListener(View target) {
            this.target = target;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            //因为不停的add 导致子view数量只增不减,所以在view动画结束后remove掉
            removeView((target));
        }
    }
}
