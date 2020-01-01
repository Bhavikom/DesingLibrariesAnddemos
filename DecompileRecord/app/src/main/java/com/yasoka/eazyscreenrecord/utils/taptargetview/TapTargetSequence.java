package com.yasoka.eazyscreenrecord.utils.taptargetview;

import android.app.Activity;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

public class TapTargetSequence {
    private final Activity activity;
    boolean considerOuterCircleCanceled;
    boolean continueOnCancel;
    Listener listener;
    private boolean started;
    private final TapTargetView.Listener tapTargetListener = new TapTargetView.Listener() {
        public void onTargetClick(TapTargetView tapTargetView) {
            super.onTargetClick(tapTargetView);
            if (TapTargetSequence.this.listener != null) {
                TapTargetSequence.this.listener.onSequenceStep(tapTargetView.target);
            }
            TapTargetSequence.this.showNext();
        }

        public void onOuterCircleClick(TapTargetView tapTargetView) {
            onTargetClick(tapTargetView);
        }

        public void onTargetCancel(TapTargetView tapTargetView) {
            onTargetClick(tapTargetView);
        }
    };
    private final Queue<TapTarget> targets;

    public interface Listener {
        void onOuterCircleClick();

        void onSequenceCanceled(TapTarget tapTarget);

        void onSequenceFinish();

        void onSequenceStep(TapTarget tapTarget);
    }

    public TapTargetSequence(Activity activity2) {
        if (activity2 != null) {
            this.activity = activity2;
            this.targets = new LinkedList();
            return;
        }
        throw new IllegalArgumentException("Activity is null");
    }

    public TapTargetSequence targets(List<TapTarget> list) {
        this.targets.addAll(list);
        return this;
    }

    public TapTargetSequence targets(TapTarget... tapTargetArr) {
        Collections.addAll(this.targets, tapTargetArr);
        return this;
    }

    public TapTargetSequence target(TapTarget tapTarget) {
        this.targets.add(tapTarget);
        return this;
    }

    public TapTargetSequence continueOnCancel(boolean z) {
        this.continueOnCancel = z;
        return this;
    }

    public TapTargetSequence considerOuterCircleCanceled(boolean z) {
        this.considerOuterCircleCanceled = z;
        return this;
    }

    public TapTargetSequence listener(Listener listener2) {
        this.listener = listener2;
        return this;
    }

    public void start() {
        if (!this.targets.isEmpty() && !this.started) {
            this.started = true;
            showNext();
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
