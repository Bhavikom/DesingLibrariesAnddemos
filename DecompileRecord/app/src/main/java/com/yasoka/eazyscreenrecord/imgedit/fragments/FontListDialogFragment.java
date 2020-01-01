package com.yasoka.eazyscreenrecord.imgedit.fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.p000v4.app.DialogFragment;
import android.support.p003v7.widget.LinearLayoutManager;
import android.support.p003v7.widget.RecyclerView;
import android.support.p003v7.widget.RecyclerView.Adapter;
import android.support.p003v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ezscreenrecorder.C0793R;
import java.util.ArrayList;
import java.util.List;

public class FontListDialogFragment extends DialogFragment {
    /* access modifiers changed from: private */
    public String label;

    class FontListAdapter extends Adapter<FontListViewHolder> {
        /* access modifiers changed from: private */
        public List<String> list = new ArrayList();

        class FontListViewHolder extends ViewHolder {
            /* access modifiers changed from: private */
            public final TextView fontView;

            FontListViewHolder(View view) {
                super(view);
                this.fontView = (TextView) view.findViewById(C0793R.C0795id.txt_font_view);
                view.setOnClickListener(new OnClickListener(FontListAdapter.this) {
                    public void onClick(View view) {
                        if (FontListDialogFragment.this.getTargetFragment() != null) {
                            Intent intent = new Intent();
                            intent.putExtra("fontName", (String) FontListAdapter.this.list.get(FontListViewHolder.this.getAdapterPosition()));
                            FontListDialogFragment.this.getTargetFragment().onActivityResult(FontListDialogFragment.this.getTargetRequestCode(), -1, intent);
                            FontListDialogFragment.this.dismiss();
                        }
                    }
                });
            }
        }

        FontListAdapter() {
            this.list.add("sans-serif-thin");
            this.list.add("sans-serif");
            this.list.add("sans-serif-smallcaps");
            this.list.add("sans-serif-light");
            this.list.add("sans-serif-condensed");
            this.list.add("sans-serif-condensed-light");
            this.list.add("serif");
            this.list.add("monospace");
            this.list.add("serif-monospace");
            this.list.add("cursive");
        }

        public FontListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new FontListViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(C0793R.layout.font_list_item, viewGroup, false));
        }

        public void onBindViewHolder(FontListViewHolder fontListViewHolder, int i) {
            fontListViewHolder.fontView.setTypeface(Typeface.create((String) this.list.get(i), 0));
            fontListViewHolder.fontView.setText(FontListDialogFragment.this.label != null ? FontListDialogFragment.this.label : (String) this.list.get(i));
        }

        public int getItemCount() {
            return this.list.size();
        }
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            this.label = getArguments().getString("label");
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(C0793R.layout.font_list_fragment, viewGroup, false);
    }

    public void onViewCreated(View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(C0793R.C0795id.rcyl_font_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new FontListAdapter());
    }
}
