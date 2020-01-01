package com.yasoka.eazyscreenrecord.utils.taptargetview;

import android.app.Activity;
import android.view.ViewGroup;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

public class TapTargetSequence1 {
    private final Activity activity;
    boolean considerOuterCircleCanceled;
    public ViewGroup contentView;
    boolean continueOnCancel;
    private TapTargetView lastView;
    Listener listener;
    private boolean started;
    private final TapTargetView.Listener tapTargetListener = new TapTargetView.Listener() {
        public void onTargetClick(TapTargetView tapTargetView) {
            super.onTargetClick(tapTargetView);
            if (TapTargetSequence1.this.listener != null) {
                TapTargetSequence1.this.listener.onSequenceStep(tapTargetView.target);
            }
        }

        public void onOuterCircleClick(TapTargetView tapTargetView) {
            super.onOuterCircleClick(tapTargetView);
            if (TapTargetSequence1.this.considerOuterCircleCanceled) {
                onTargetCancel(tapTargetView);
            }
            if (TapTargetSequence1.this.listener != null) {
                TapTargetSequence1.this.listener.onOuterCircleClick();
            }
        }

        public void onTargetCancel(TapTargetView tapTargetView) {
            System.out.println("Cancel");
            super.onTargetCancel(tapTargetView);
        }
    };
    private final Queue<TapTarget> targets;

    public interface Listener {
        void onOuterCircleClick();

        void onSequenceCanceled(TapTarget tapTarget);

        void onSequenceFinish();

        void onSequenceStep(TapTarget tapTarget);
    }

    public TapTargetSequence1(Activity activity2) {
        if (activity2 != null) {
            this.activity = activity2;
            this.targets = new LinkedList();
            return;
        }
        throw new IllegalArgumentException("Activity is null");
    }

    public TapTargetSequence1 targets(List<TapTarget> list) {
        this.targets.addAll(list);
        return this;
    }

    public TapTargetSequence1 targets(TapTarget... tapTargetArr) {
        Collections.addAll(this.targets, tapTargetArr);
        return this;
    }

    public TapTargetSequence1 target(TapTarget tapTarget) {
        this.targets.add(tapTarget);
        return this;
    }

    public TapTargetSequence1 continueOnCancel(boolean z) {
        this.continueOnCancel = z;
        return this;
    }

    public TapTargetSequence1 considerOuterCircleCanceled(boolean z) {
        this.considerOuterCircleCanceled = z;
        return this;
    }

    public TapTargetSequence1 listener(Listener listener2) {
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
            if (!(this.lastView == null || this.tapTargetListener == null)) {
                this.tapTargetListener.onTargetCancel(this.lastView);
            }
            this.lastView = TapTargetView.showFor(this.activity, this.contentView, (TapTarget) this.targets.remove(), this.tapTargetListener);
        } catch (NoSuchElementException unused) {
            Listener listener2 = this.listener;
            if (listener2 != null) {
                listener2.onSequenceFinish();
            }
        }
    }
}
