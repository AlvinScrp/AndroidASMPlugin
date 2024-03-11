package com.a.asm.sample;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.webuy.autotrack.AutoTrackUtil;

public class ViewListenerUtil {

    public static void setOnClickListener(@NonNull Object obj, @Nullable View.OnClickListener onClickListener) {
        if (obj instanceof View) {
            View view = (View) obj;
            if (onClickListener == null
                    || onClickListener instanceof WrapperOnClickListener) {
                view.setOnClickListener(onClickListener);
            } else {
                view.setOnClickListener(new WrapperOnClickListener(onClickListener));
            }
        }
    }

    private static class WrapperOnClickListener implements View.OnClickListener {
        @Nullable
        private final View.OnClickListener onClickListener;

        public WrapperOnClickListener(@Nullable View.OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
        }

        @Override
        public void onClick(View v) {
            if (onClickListener != null) {
                AutoTrackUtil.autoTrackClick(v);
                onClickListener.onClick(v);
            }
        }
    }
}