package com.yasoka.eazyscreenrecord.utils.taptargetview;

import android.app.Activity;
import android.view.ViewGroup;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

public class TapTargetSequence2 {
    private final Activity activity;
    boolean considerOuterCircleCanceled;
    boolean continueOnCancel;
    Listener listener;
    public ViewGroup parentView;
    public boolean started;
    private final TapTargetView.Listener tapTargetListener = new TapTargetView.Listener() {
        public void onTargetClick(TapTargetView tapTargetView) {
            super.onTargetClick(tapTargetView);
            if (TapTargetSequence2.this.listener != null) {
                TapTargetSequence2.this.listener.onSequenceStep(tapTargetView.target);
            }
        }

        public void onOuterCircleClick(TapTargetView tapTargetView) {
            System.out.println("OUTSIDE");
            super.onOuterCircleClick(tapTargetView);
            if (TapTargetSequence2.this.considerOuterCircleCanceled) {
                onTargetCancel(tapTargetView);
            }
            if (TapTargetSequence2.this.listener != null) {
                TapTargetSequence2.this.listener.onOuterCircleClick();
            }
        }

        public void onTargetCancel(TapTargetView tapTargetView) {
            System.out.println("Cancel");
            super.onTargetCancel(tapTargetView);
            if (TapTargetSequence2.this.continueOnCancel) {
                super.onTargetClick(tapTargetView);
                TapTargetSequence2.this.showNext();
            } else if (TapTargetSequence2.this.listener != null) {
                TapTargetSequence2.this.listener.onSequenceCanceled(tapTargetView.target);
            }
        }

        public void onClickOutsideOfOuterCircle(TapTargetView tapTargetView) {
            super.onClickOutsideOfOuterCircle(tapTargetView);
            if (TapTargetSequence2.this.listener != null) {
                TapTargetSequence2.this.listener.onSequenceStep(tapTargetView.target);
            }
        }
    };
    private final Queue<TapTarget> targets;

    public interface Listener {
        void onOuterCircleClick();

        void onSequenceCanceled(TapTarget tapTarget);

        void onSequenceFinish();

        void onSequenceStep(TapTarget tapTarget);
    }

    public TapTargetSequence2(Activity activity2) {
        if (activity2 != null) {
            this.activity = activity2;
            this.targets = new LinkedList();
            return;
        }
        throw new IllegalArgumentException("Activity is null");
    }

    public TapTargetSequence2 targets(List<TapTarget> list) {
        this.targets.addAll(list);
        return this;
    }

    public TapTargetSequence2 targets(TapTarget... tapTargetArr) {
        Collections.addAll(this.targets, tapTargetArr);
        return this;
    }

    public TapTargetSequence2 target(TapTarget tapTarget) {
        this.targets.add(tapTarget);
        return this;
    }

    public TapTargetSequence2 continueOnCancel(boolean z) {
        this.continueOnCancel = z;
        return this;
    }

    public TapTargetSequence2 considerOuterCircleCanceled(boolean z) {
        this.considerOuterCircleCanceled = z;
        return this;
    }

    public TapTargetSequence2 listener(Listener listener2) {
        this.listener = listener2;
        return this;
    }

    public void start() {
        if (!this.targets.isEmpty() && !this.started) {
            this.started = true;
            showNext();
        }
    }

    public void stop() {
        try {
            this.targets.clear();
            showNext();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showNext() {
        try {
            TapTargetView.showFor(this.activity, (TapTarget) this.targets.remove(), this.tapTargetListener);
        } catch (NoSuchElementException unused) {
            Listener listener2 = this.listener;
            if (listener2 != null) {
                listener2.onSequenceFinish();
            }
        }
    }
}
