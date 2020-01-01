package com.yasoka.eazyscreenrecord.alertdialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
/*import android.support.p000v4.app.DialogFragment;
import android.support.p003v7.widget.AppCompatImageView;
import android.support.p003v7.widget.AppCompatTextView;
import android.support.p003v7.widget.LinearLayoutManager;
import android.support.p003v7.widget.RecyclerView;
import android.support.p003v7.widget.RecyclerView.Adapter;
import android.support.p003v7.widget.RecyclerView.ViewHolder;*/
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.crashlytics.android.Crashlytics;
import com.ezscreenrecorder.R;
import com.yasoka.eazyscreenrecord.C0793R;
import com.yasoka.eazyscreenrecord.model.ActionMoreModel;
/*import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.model.ActionMoreModel;*/
import java.util.ArrayList;
import java.util.List;

public class ActionMoreOptionDialog extends DialogFragment {
    private OnActionSelectedListener actionSelectedListener;
    private boolean isFromNotification = false;
    private ActionMoreAdapter mAdapter;
    /* access modifiers changed from: private */
    public List<ActionMoreModel> mDataList;
    private RecyclerView recyclerView;

    private class ActionMoreAdapter extends RecyclerView.Adapter<ActionMoreVH> {
        public ActionMoreAdapter() {
            ActionMoreOptionDialog.this.mDataList = new ArrayList();
        }

        public void addItem(ActionMoreModel actionMoreModel) {
            ActionMoreOptionDialog.this.mDataList.add(actionMoreModel);
        }

        @NonNull
        public ActionMoreVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new ActionMoreVH(ActionMoreOptionDialog.this.getLayoutInflater().inflate(C0793R.layout.custom_action_more_item_layout, viewGroup, false));
        }

        public void onBindViewHolder(@NonNull ActionMoreVH actionMoreVH, int i) {
            if (i != -1) {
                ActionMoreModel actionMoreModel = (ActionMoreModel) ActionMoreOptionDialog.this.mDataList.get(i);
                actionMoreVH.textView.setText(actionMoreModel.getActionTextRes());
                actionMoreVH.imageView.setImageResource(actionMoreModel.getActionImageRes());
            }
        }

        public int getItemCount() {
            return ActionMoreOptionDialog.this.mDataList.size();
        }
    }

    private class ActionMoreVH extends RecyclerView.ViewHolder implements OnClickListener {
        /* access modifiers changed from: private */
        public AppCompatImageView imageView;
        /* access modifiers changed from: private */
        public AppCompatTextView textView;

        public ActionMoreVH(View view) {
            super(view);
            this.textView = (AppCompatTextView) view.findViewById(C0793R.C0795id.id_action_more_item_text_view);
            this.imageView = (AppCompatImageView) view.findViewById(C0793R.C0795id.id_action_more_item_image_view);
            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            if (adapterPosition != -1) {
                ActionMoreOptionDialog.this.onDefaultActionSelected((ActionMoreModel) ActionMoreOptionDialog.this.mDataList.get(adapterPosition));
            }
        }
    }

    public interface OnActionSelectedListener {
        void onActionSelected(ActionMoreModel actionMoreModel);
    }

    public static ActionMoreOptionDialog getInstance(boolean z) {
        ActionMoreOptionDialog actionMoreOptionDialog = new ActionMoreOptionDialog();
        Bundle bundle = new Bundle();
        bundle.putBoolean(ActionMoreModel.KEY_IS_MORE_FROM_NOTIFICATION, z);
        actionMoreOptionDialog.setArguments(bundle);
        return actionMoreOptionDialog;
    }

    private void getArgumentValues() {
        if (getArguments() != null) {
            Bundle arguments = getArguments();
            String str = ActionMoreModel.KEY_IS_MORE_FROM_NOTIFICATION;
            if (arguments.containsKey(str)) {
                this.isFromNotification = getArguments().getBoolean(str, false);
            }
        }
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(C0793R.layout.fragment_dialog_action_more, viewGroup, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.recyclerView = (RecyclerView) view.findViewById(C0793R.C0795id.id_recycler_view);
    }

    public void onActivityCreated(@Nullable Bundle bundle) {
        super.onActivityCreated(bundle);
        getArgumentValues();
        this.mAdapter = new ActionMoreAdapter();
        this.mAdapter.addItem(new ActionMoreModel((int) C0793R.C0794drawable.ic_noti_audio_img, (int) R.string.record_audio, (int) ActionMoreModel.EXTRA_ACTION_MORE_TYPE_AUDIO));
        this.mAdapter.addItem(new ActionMoreModel((int) C0793R.C0794drawable.ic_noti_game_record_img, (int) R.string.id_record_game_txt, (int) ActionMoreModel.EXTRA_ACTION_MORE_TYPE_GAME_RECORD));
        this.mAdapter.addItem(new ActionMoreModel((int) C0793R.C0794drawable.ic_noti_interactive_video_img, (int) R.string.id_noti_record_interactive_video_txt, (int) ActionMoreModel.EXTRA_ACTION_MORE_TYPE_INTRACTIVE_VIDEO));
        if (this.isFromNotification) {
            this.mAdapter.addItem(new ActionMoreModel((int) R.drawable.ic_noti_explainer_video_img, (int) C0793R.string.id_white_board_txt, (int) ActionMoreModel.EXTRA_ACTION_MORE_TYPE_WHITE_BOARD));
        }
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), 1, false));
        this.recyclerView.setAdapter(this.mAdapter);
    }

    public void onStart() {
        super.onStart();
        final Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(-1, -1);
            dialog.setOnKeyListener(new OnKeyListener() {
                public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                    if (dialog != null && (keyEvent.getKeyCode() == 4 || keyEvent.getKeyCode() == 3)) {
                        if (dialog.isShowing()) {
                            dialogInterface.dismiss();
                        }
                        try {
                            ActionMoreOptionDialog.this.getActivity().finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    return false;
                }
            });
            dialog.getWindow().setBackgroundDrawableResource(C0793R.C0794drawable.dim_80_background_color);
        }
    }

    public void setOnActionSelectedListener(OnActionSelectedListener onActionSelectedListener) {
        this.actionSelectedListener = onActionSelectedListener;
    }

    /* access modifiers changed from: private */
    public void onDefaultActionSelected(ActionMoreModel actionMoreModel) {
        OnActionSelectedListener onActionSelectedListener = this.actionSelectedListener;
        if (onActionSelectedListener != null) {
            onActionSelectedListener.onActionSelected(actionMoreModel);
        }
        try {
            dismissAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
            Crashlytics.logException(e);
        }
    }
}
