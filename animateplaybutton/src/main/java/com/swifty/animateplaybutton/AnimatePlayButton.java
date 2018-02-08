package com.swifty.animateplaybutton;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by swifty on 7/2/2018.
 */

public class AnimatePlayButton extends FrameLayout {
    private final int DEFAULT_DURATION = 300;
    private @ColorInt int mBorderColor;
    private @DrawableRes int mButtonBackgroundRes;
    private int mButtonSize;
    private int mAnimationDuration;
    private @ColorInt int mButtonColor;

    private Status mStatus;

    private ViewGroup mPauseGroup;
    private ImageView mPlay;
    private ImageView mPause;
    private ImageView mStop;

    private PlayListener mPlayListener;

    public AnimatePlayButton(Context context) {
        this(context, null);
    }

    public AnimatePlayButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimatePlayButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs, defStyleAttr);
        initView();
    }

    private void initAttr(Context context, AttributeSet attrs, int defStyleAttr) {
        if (attrs == null) {
            return;
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AnimatePlayButton, defStyleAttr, 0);
        mAnimationDuration = a.getInt(R.styleable.AnimatePlayButton_animationDuration, DEFAULT_DURATION);
        mButtonSize = a.getDimensionPixelSize(R.styleable.AnimatePlayButton_buttonSize, getDimens(R.dimen.size_button));
        mBorderColor = a.getColor(R.styleable.AnimatePlayButton_borderColor, getResources().getColor(android.R.color.holo_blue_bright));
        mButtonColor = a.getColor(R.styleable.AnimatePlayButton_buttonColor, getResources().getColor(android.R.color.white));
        mButtonBackgroundRes = a.getResourceId(R.styleable.AnimatePlayButton_buttonBackground, R.drawable.circle_button_bg);
        a.recycle();
    }

    private void initView() {
        setLayerType(LAYER_TYPE_HARDWARE, null);
        int size = getDimens(R.dimen.padding_parent);
        setPadding(size, size, size, size);
        setBackgroundResource(R.drawable.blue_ring);
        mPauseGroup = inflate(getContext(), R.layout.view_pause_stop, this).findViewById(R.id.pause_group);
        inflate(getContext(), R.layout.view_play, this);
        mPause = mPauseGroup.findViewById(R.id.pause);
        mStop = mPauseGroup.findViewById(R.id.stop);
        mPlay = findViewById(R.id.play);

        //set size
        ViewGroup.LayoutParams mPauseGroupLayoutParams = mPauseGroup.getLayoutParams();
        mPauseGroupLayoutParams.width = calculatePauseGroupWidth();
        mPauseGroupLayoutParams.height = mButtonSize;
        mPauseGroup.setLayoutParams(mPauseGroupLayoutParams);
        ViewGroup.LayoutParams mPlayLayoutParams = mPlay.getLayoutParams();
        mPlayLayoutParams.width = mButtonSize;
        mPlayLayoutParams.height = mButtonSize;
        mPlay.setLayoutParams(mPlayLayoutParams);
        //update border
        GradientDrawable background = (GradientDrawable) getBackground();
        background.setColorFilter(mBorderColor, PorterDuff.Mode.SRC_ATOP);
        background.setCornerRadius(mButtonSize);
        setBackgroundDrawable(background);
        //set button background
        mPlay.setBackgroundResource(mButtonBackgroundRes);
        mPause.setBackgroundResource(mButtonBackgroundRes);
        mStop.setBackgroundResource(mButtonBackgroundRes);
        //set button image color
        mPlay.setColorFilter(mButtonColor);
        mPause.setColorFilter(mButtonColor);
        mStop.setColorFilter(mButtonColor);

        mPlay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStatus(Status.PLAYED);
                if (mPlayListener != null) mPlayListener.onPlay(v);
            }
        });
        mPause.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStatus == Status.PAUSED) {
                    updateStatus(Status.PLAYED);
                    if (mPlayListener != null) mPlayListener.onResume(v);
                } else {
                    updateStatus(Status.PAUSED);
                    if (mPlayListener != null) mPlayListener.onPause(v);
                }
            }
        });
        mStop.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStatus(Status.STOPPED);
                if (mPlayListener != null) mPlayListener.onStop(v);
            }
        });
        initStatus(Status.STOPPED);
    }

    private int calculatePauseGroupWidth() {
        return mButtonSize * 2 + getDimens(R.dimen.margin_button);
    }

    public void initStatus(Status status) {
        this.mStatus = status;
        switch (status) {
            case PLAYED:
                mPlay.setVisibility(GONE);
                mPauseGroup.setVisibility(VISIBLE);
                mPause.setImageResource(R.drawable.ic_pause_white_36dp);
                break;
            case PAUSED:
                mPlay.setVisibility(GONE);
                mPauseGroup.setVisibility(VISIBLE);
                mPause.setImageResource(R.drawable.ic_play_arrow_white_36dp);
                break;
            case STOPPED:
                mPlay.setVisibility(VISIBLE);
                mPauseGroup.setVisibility(GONE);
                break;
        }
    }

    public void updateStatus(Status status) {
        this.mStatus = status;
        switch (status) {
            case PLAYED:
                mPause.setImageResource(R.drawable.ic_pause_white_36dp);
                if (mPlay.getVisibility() == VISIBLE) {
                    dismissAnimation(mPlay, mButtonSize, mButtonSize);
                    showAnimation(mPauseGroup, calculatePauseGroupWidth(), mButtonSize);
                }
                break;
            case PAUSED:
                mPause.setImageResource(R.drawable.ic_play_arrow_white_36dp);
                break;
            case STOPPED:
                mPause.setImageResource(R.drawable.ic_pause_white_36dp);
                dismissAnimation(mPauseGroup, calculatePauseGroupWidth(), mButtonSize);
                showAnimation(mPlay, mButtonSize, mButtonSize);
                break;
        }
    }

    private void showAnimation(final View view, final int width, final int height) {
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1).setDuration(mAnimationDuration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                view.setAlpha(value);
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.width = (int) (width * value);
                layoutParams.height = (int) (height * value);
                view.setLayoutParams(layoutParams);
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                disableView(view);
                view.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                enableView(view);
                view.setClickable(true);
            }
        });
        animator.setInterpolator(new OvershootInterpolator());
        animator.start();
    }

    private void enableView(View view) {
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                enableView(((ViewGroup) view).getChildAt(i));
            }
        } else {
            view.setEnabled(true);
        }
    }

    private void disableView(View view) {
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                disableView(((ViewGroup) view).getChildAt(i));
            }
        } else {
            view.setEnabled(false);
        }
    }

    private void dismissAnimation(final View view, final int width, final int height) {
        ValueAnimator animator = ValueAnimator.ofFloat(1, 0).setDuration(mAnimationDuration / 2);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                view.setAlpha(value);
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.width = (int) (width * value);
                layoutParams.height = (int) (height * value);
                view.setLayoutParams(layoutParams);
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                disableView(view);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                enableView(view);
                view.setVisibility(GONE);
            }
        });
        animator.start();
    }

    private int getDimens(@DimenRes int dimenRes) {
        return getContext().getResources().getDimensionPixelSize(dimenRes);
    }

    public void setPlayListener(PlayListener PlayListener) {
        mPlayListener = PlayListener;
    }

    public Status getCurrentStatus() {
        return mStatus;
    }

    public interface PlayListener {
        void onPlay(View playButton);

        void onPause(View pause);

        void onResume(View pause);

        void onStop(View stop);
    }

    public enum Status {
        PLAYED,
        PAUSED,
        STOPPED
    }
}
