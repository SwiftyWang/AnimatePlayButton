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
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
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

    private OnButtonsListener mPlayListener;
    private boolean mUpdatingStatus;

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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), mButtonSize + getDimens(R.dimen.padding_parent) * 2);
    }

    private void initView() {
        setLayerType(LAYER_TYPE_HARDWARE, null);
        FrameLayout root = new FrameLayout(getContext());
        LayoutParams rootParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootParams.gravity = Gravity.CENTER;
        root.setLayoutParams(rootParams);
        addView(root);

        int size = getDimens(R.dimen.padding_parent);
        root.setPadding(size, size, size, size);
        root.setBackgroundResource(R.drawable.blue_ring);
        mPauseGroup = inflate(getContext(), R.layout.view_pause_stop, root).findViewById(R.id.pause_group);
        inflate(getContext(), R.layout.view_play, root);
        mPause = mPauseGroup.findViewById(R.id.pause);
        mStop = mPauseGroup.findViewById(R.id.stop);
        mPlay = root.findViewById(R.id.play);

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
        GradientDrawable background = (GradientDrawable) root.getBackground();
        background.setColorFilter(mBorderColor, PorterDuff.Mode.SRC_ATOP);
        background.setCornerRadius(mButtonSize);
        root.setBackgroundDrawable(background);
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
                if (mUpdatingStatus) {
                    return;
                }
                boolean changeStatus = true;
                if (mPlayListener != null) {
                    changeStatus = mPlayListener.onPlayClick(v);
                }
                if (changeStatus) {
                    updateStatus(Status.PLAYING);
                }
            }
        });
        mPause.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUpdatingStatus) {
                    return;
                }
                if (mStatus == Status.PAUSED) {
                    boolean changeStatus = true;
                    if (mPlayListener != null) {
                        changeStatus = mPlayListener.onResumeClick(v);
                    }
                    if (changeStatus) {
                        updateStatus(Status.PLAYING);
                    }
                } else {
                    boolean changeStatus = true;
                    if (mPlayListener != null) {
                        changeStatus = mPlayListener.onPauseClick(v);
                    }
                    if (changeStatus) {
                        updateStatus(Status.PAUSED);
                    }
                }
            }
        });
        mStop.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUpdatingStatus) {
                    return;
                }
                boolean changeStatus = true;
                if (mPlayListener != null) {
                    changeStatus = mPlayListener.onStopClick(v);
                }
                if (changeStatus) {
                    updateStatus(Status.STOPPED);
                }
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
            case PLAYING:
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
        if (mStatus == status) {
            return;
        }
        if (mUpdatingStatus) {
            return;
        }
        mStatus = status;
        switch (status) {
            case PLAYING:
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
                mUpdatingStatus = true;
                view.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mUpdatingStatus = false;
                view.setClickable(true);
            }
        });
        animator.start();
    }

    private void dismissAnimation(final View view, final int width, final int height) {
        ValueAnimator animator = ValueAnimator.ofFloat(1, 0).setDuration(mAnimationDuration / 3);
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
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(GONE);
            }
        });
        animator.start();
    }

    private int getDimens(@DimenRes int dimenRes) {
        return getContext().getResources().getDimensionPixelSize(dimenRes);
    }

    public void setPlayListener(OnButtonsListener PlayListener) {
        mPlayListener = PlayListener;
    }

    public Status getCurrentStatus() {
        return mStatus;
    }

    public interface OnButtonsListener {
        /**
         * when click play button will callback this
         *
         * @param playButton
         * @return true will change current status to {@link Status#PLAYING}, false will do nothing
         */
        boolean onPlayClick(View playButton);

        /**
         * when click pause button to pause will callback this
         *
         * @param pauseButton
         * @return true will change current status to {@link Status#PAUSED}, false will do nothing
         */
        boolean onPauseClick(View pauseButton);

        /**
         * when click pause button to resume will callback this
         *
         * @param pauseButton
         * @return true will change current status to {@link Status#PLAYING}, false will do nothing
         */
        boolean onResumeClick(View pauseButton);

        /**
         * when click stop button to stop will callback this
         *
         * @param stopButton
         * @return true will change current status to {@link Status#STOPPED}, false will do nothing
         */
        boolean onStopClick(View stopButton);
    }

    public enum Status {
        PLAYING,
        PAUSED,
        STOPPED
    }
}
