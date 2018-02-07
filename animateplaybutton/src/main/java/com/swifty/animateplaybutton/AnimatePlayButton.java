package com.swifty.animateplaybutton;

import android.content.Context;
import android.support.annotation.DimenRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by swifty on 7/2/2018.
 */

public class AnimatePlayButton extends FrameLayout {
    public AnimatePlayButton(Context context) {
        super(context);
        initView();
    }

    public AnimatePlayButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public AnimatePlayButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        int size = getSize(R.dimen.padding_parent);
        setPadding(size, size, size, size);
        setBackgroundResource(R.drawable.blue_ring);
        inflate(getContext(), R.layout.view_pause_stop, this);
    }

    private int getSize(@DimenRes int dimenRes) {
        return getContext().getResources().getDimensionPixelSize(dimenRes);
    }
}
