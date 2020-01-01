package com.yasoka.eazyscreenrecord.imgedit.fragments;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.p000v4.app.Fragment;
import android.support.p000v4.content.ContextCompat;
import android.support.p003v7.app.AlertDialog;
import android.support.p003v7.app.AlertDialog.Builder;
import android.support.p003v7.widget.LinearLayoutManager;
import android.support.p003v7.widget.RecyclerView;
import android.support.p003v7.widget.RecyclerView.Adapter;
import android.support.p003v7.widget.RecyclerView.ViewHolder;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.bumptech.glide.Glide;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.imgedit.ImageEditActivity;
import com.ezscreenrecorder.imgedit.colorseekbar.ColorSeekBar;
import com.ezscreenrecorder.imgedit.colorseekbar.ColorSeekBar.OnColorChangeListener;
import com.ezscreenrecorder.imgedit.sticker.StickerTextView;
import com.ezscreenrecorder.imgedit.sticker.StickerView.OnTapListener;
import com.ezscreenrecorder.model.FirebaseDataDevice;
import com.ezscreenrecorder.server.ServerAPI;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import p009io.reactivex.SingleObserver;
import p009io.reactivex.observers.DisposableSingleObserver;

public class TextFragment extends Fragment {
    /* access modifiers changed from: private */
    public ColorSeekBar colorSeekBar;
    /* access modifiers changed from: private */
    public ColorSeekBar colorSeekBar1;
    /* access modifiers changed from: private */
    public FrameLayout drawLayout;
    private ImageView imgView;
    /* access modifiers changed from: private */
    public Button keyboardBtn;
    /* access modifiers changed from: private */
    public String mImagePath;
    /* access modifiers changed from: private */
    public RecyclerView recyclerViewFontList;
    /* access modifiers changed from: private */
    public StickerTextView tv_sticker;

    class FontListAdapter extends Adapter<FontListViewHolder> {
        /* access modifiers changed from: private */
        public List<String> list = new ArrayList();
        /* access modifiers changed from: private */
        public int selectedPosition = 1;

        class FontListViewHolder extends ViewHolder {
            /* access modifiers changed from: private */
            public final TextView fontView;

            FontListViewHolder(View view) {
                super(view);
                this.fontView = (TextView) view.findViewById(C0793R.C0795id.txt_font_view);
                view.setOnClickListener(new OnClickListener(FontListAdapter.this) {
                    public void onClick(View view) {
                        int adapterPosition = FontListViewHolder.this.getAdapterPosition();
                        if (adapterPosition == FontListAdapter.this.selectedPosition) {
                            FontListAdapter.this.selectedPosition = -1;
                            TextFragment.this.tv_sticker.setDefaultFont();
                        } else {
                            FontListAdapter.this.selectedPosition = adapterPosition;
                            TextFragment.this.tv_sticker.setFont((String) FontListAdapter.this.list.get(adapterPosition));
                        }
                        TextFragment.this.tv_sticker.setText(TextFragment.this.tv_sticker.getText());
                        FontListAdapter.this.notifyDataSetChanged();
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
            if (i == this.selectedPosition) {
                fontListViewHolder.fontView.setTypeface(Typeface.create((String) this.list.get(i), 1));
                fontListViewHolder.fontView.setTextSize(TextFragment.this.getResources().getDimension(C0793R.dimen.txt_size_selected));
                fontListViewHolder.fontView.setTextColor(ContextCompat.getColor(TextFragment.this.getContext(), C0793R.color.colorPrimary));
                return;
            }
            fontListViewHolder.fontView.setTypeface(Typeface.create((String) this.list.get(i), 0));
            fontListViewHolder.fontView.setTextSize(TextFragment.this.getResources().getDimension(C0793R.dimen.txt_size_unselected));
            fontListViewHolder.fontView.setTextColor(-1);
        }

        public int getItemCount() {
            return this.list.size();
        }
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            this.mImagePath = getArguments().getString(MainImageFragment.CROP_IMAGE_EXTRA_SOURCE);
        }
        setHasOptionsMenu(true);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(C0793R.C0797menu.menu_sub_img_edit, menu);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == C0793R.C0795id.action_done) {
            saveImage();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(C0793R.layout.fragment_text, viewGroup, false);
    }

    public void onViewCreated(View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        final InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService("input_method");
        FrameLayout frameLayout = (FrameLayout) view.findViewById(C0793R.C0795id.canvasView);
        this.colorSeekBar = (ColorSeekBar) view.findViewById(C0793R.C0795id.color_seek_bar);
        this.colorSeekBar1 = (ColorSeekBar) view.findViewById(C0793R.C0795id.color_seek_bar1);
        this.colorSeekBar.setOnColorChangeListener(new OnColorChangeListener() {
            public void onColorChangeListener(int i, int i2, int i3) {
                TextFragment.this.tv_sticker.setTextColor(TextFragment.this.colorSeekBar.getColor());
            }
        });
        this.colorSeekBar1.setOnColorChangeListener(new OnColorChangeListener() {
            public void onColorChangeListener(int i, int i2, int i3) {
                TextFragment.this.tv_sticker.setTextBackgroundColor(TextFragment.this.colorSeekBar1.getColor());
            }
        });
        this.drawLayout = (FrameLayout) view.findViewById(C0793R.C0795id.draw_layout);
        final EditText editText = (EditText) view.findViewById(C0793R.C0795id.ed_hide);
        editText.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                TextFragment.this.tv_sticker.setText(editable.toString());
            }
        });
        this.tv_sticker = new StickerTextView(getContext());
        this.tv_sticker.setTextColor(ContextCompat.getColor(getContext(), C0793R.color.colorPrimary));
        frameLayout.addView(this.tv_sticker);
        this.tv_sticker.setOnTapListener(new OnTapListener() {
            public void doubleTap() {
                TextFragment.this.keyboardBtn.callOnClick();
            }

            public void deleteClick() {
                TextFragment.this.getActivity().onBackPressed();
            }
        });
        frameLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                TextFragment.this.tv_sticker.setControlItemsHidden(true);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });
        this.imgView = (ImageView) view.findViewById(C0793R.C0795id.img_view);
        final Button button = (Button) view.findViewById(C0793R.C0795id.btn_color);
        Button button2 = (Button) view.findViewById(C0793R.C0795id.btn_font);
        Button button3 = (Button) view.findViewById(C0793R.C0795id.btn_background);
        this.keyboardBtn = (Button) view.findViewById(C0793R.C0795id.btn_keyboard);
        this.keyboardBtn.setVisibility(8);
        final InputMethodManager inputMethodManager2 = inputMethodManager;
        final Button button4 = button;
        final Button button5 = button3;
        final Button button6 = button2;
        C12966 r0 = new OnClickListener() {
            public void onClick(View view) {
                inputMethodManager2.hideSoftInputFromWindow(view.getWindowToken(), 0);
                if (TextFragment.this.colorSeekBar.getVisibility() == 8) {
                    button4.setBackgroundResource(C0793R.C0794drawable.round_blue_back);
                    button5.setBackgroundColor(0);
                    button6.setBackgroundColor(0);
                    TextFragment.this.keyboardBtn.setBackgroundColor(0);
                    TextFragment.this.colorSeekBar.setVisibility(0);
                    TextFragment.this.colorSeekBar1.setVisibility(8);
                    TextFragment.this.recyclerViewFontList.setVisibility(8);
                    return;
                }
                button4.setBackgroundColor(0);
                TextFragment.this.colorSeekBar.setVisibility(8);
            }
        };
        button.setOnClickListener(r0);
        final Button button7 = button2;
        final Button button8 = button3;
        final Button button9 = button;
        final InputMethodManager inputMethodManager3 = inputMethodManager;
        C12977 r02 = new OnClickListener() {
            public void onClick(View view) {
                if (TextFragment.this.recyclerViewFontList.getVisibility() == 8) {
                    TextFragment.this.recyclerViewFontList.setVisibility(0);
                    button7.setBackgroundResource(C0793R.C0794drawable.round_blue_back);
                    button8.setBackgroundColor(0);
                    TextFragment.this.keyboardBtn.setBackgroundColor(0);
                    button9.setBackgroundColor(0);
                    inputMethodManager3.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    TextFragment.this.colorSeekBar.setVisibility(8);
                    TextFragment.this.colorSeekBar1.setVisibility(8);
                    return;
                }
                button7.setBackgroundColor(0);
                TextFragment.this.recyclerViewFontList.setVisibility(8);
            }
        };
        button2.setOnClickListener(r02);
        final InputMethodManager inputMethodManager4 = inputMethodManager;
        final Button button10 = button2;
        final Button button11 = button;
        C12988 r03 = new OnClickListener() {
            public void onClick(View view) {
                inputMethodManager4.hideSoftInputFromWindow(view.getWindowToken(), 0);
                if (TextFragment.this.colorSeekBar1.getVisibility() == 8) {
                    button8.setBackgroundResource(C0793R.C0794drawable.round_blue_back);
                    TextFragment.this.keyboardBtn.setBackgroundColor(0);
                    button10.setBackgroundColor(0);
                    button11.setBackgroundColor(0);
                    TextFragment.this.colorSeekBar1.setVisibility(0);
                    TextFragment.this.colorSeekBar.setVisibility(8);
                    TextFragment.this.recyclerViewFontList.setVisibility(8);
                    return;
                }
                button8.setBackgroundColor(0);
                TextFragment.this.colorSeekBar1.setVisibility(8);
            }
        };
        button3.setOnClickListener(r03);
        Button button12 = this.keyboardBtn;
        final Button button13 = button3;
        final Button button14 = button2;
        C12999 r2 = new OnClickListener() {
            public void onClick(View view) {
                TextFragment.this.keyboardBtn.setBackgroundResource(C0793R.C0794drawable.round_blue_back);
                button13.setBackgroundColor(0);
                button14.setBackgroundColor(0);
                TextFragment.this.recyclerViewFontList.setVisibility(8);
                button.setBackgroundColor(0);
                TextFragment.this.colorSeekBar.setVisibility(8);
                TextFragment.this.colorSeekBar1.setVisibility(8);
                editText.requestFocus();
                TextFragment.this.showTextDialog();
            }
        };
        button12.setOnClickListener(r2);
        this.recyclerViewFontList = (RecyclerView) view.findViewById(C0793R.C0795id.rcyl_font_list);
        this.recyclerViewFontList.setLayoutManager(new LinearLayoutManager(getContext(), 0, false));
        final FontListAdapter fontListAdapter = new FontListAdapter();
        this.recyclerViewFontList.setAdapter(fontListAdapter);
        this.colorSeekBar.post(new Runnable() {
            public void run() {
                TextFragment.this.colorSeekBar.setColorBarPosition(30);
                TextFragment.this.tv_sticker.setTextColor(ContextCompat.getColor(TextFragment.this.getContext(), C0793R.color.colorPrimary));
                TextFragment.this.colorSeekBar1.setAlphaBarPosition(129);
                TextFragment.this.tv_sticker.setTextBackgroundColor(Color.parseColor("#75FFFFFF"));
                TextFragment.this.tv_sticker.setFont((String) fontListAdapter.list.get(fontListAdapter.selectedPosition));
            }
        });
    }

    /* access modifiers changed from: private */
    public void showTextDialog() {
        final InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService("input_method");
        Builder builder = new Builder(getContext());
        View inflate = LayoutInflater.from(getContext()).inflate(C0793R.layout.lay_txt_dialog, null);
        final EditText editText = (EditText) inflate.findViewById(C0793R.C0795id.ed_txt_dialog);
        builder.setView(inflate);
        editText.setText(this.tv_sticker.getText());
        inflate.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                editText.requestFocus();
                inputMethodManager.toggleSoftInput(2, 0);
            }
        });
        final AlertDialog create = builder.create();
        editText.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i != 6 && keyEvent.getAction() != 0) {
                    return false;
                }
                inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                create.dismiss();
                return true;
            }
        });
        create.setOnShowListener(new OnShowListener() {
            public void onShow(DialogInterface dialogInterface) {
                create.getButton(-1).setTextColor(-1);
                editText.requestFocus();
                inputMethodManager.toggleSoftInput(2, 0);
            }
        });
        create.setOnDismissListener(new OnDismissListener() {
            public void onDismiss(DialogInterface dialogInterface) {
                inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                TextFragment.this.tv_sticker.setText(editText.getText().toString());
            }
        });
        LayoutParams layoutParams = new LayoutParams();
        layoutParams.copyFrom(create.getWindow().getAttributes());
        layoutParams.width = -1;
        layoutParams.height = -1;
        layoutParams.gravity = 17;
        layoutParams.dimAmount = 0.85f;
        create.show();
        create.getWindow().setAttributes(layoutParams);
        create.getWindow().setLayout(-1, -1);
        create.getWindow().addFlags(2);
        create.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        create.show();
        editText.requestFocus();
    }

    public void onActivityCreated(@Nullable Bundle bundle) {
        super.onActivityCreated(bundle);
        Glide.with((Fragment) this).load(this.mImagePath).into(this.imgView);
        ((ImageEditActivity) getActivity()).setMyTitle(getString(C0793R.string.edit_text));
        ((ImageEditActivity) getActivity()).setTitleStartAligned(false);
        this.keyboardBtn.callOnClick();
        ServerAPI.getInstance().addToFireBase(getContext(), "Text").subscribe((SingleObserver<? super T>) new DisposableSingleObserver<FirebaseDataDevice>() {
            public void onSuccess(FirebaseDataDevice firebaseDataDevice) {
            }

            public void onError(Throwable th) {
                th.printStackTrace();
            }
        });
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 34) {
            this.tv_sticker.setFont(intent.getStringExtra("fontName"));
        }
    }

    public void saveImage() {
        ((ImageEditActivity) getActivity()).showLoading();
        this.tv_sticker.setControlItemsHidden(true);
        this.colorSeekBar.setVisibility(8);
        this.colorSeekBar1.setVisibility(8);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                TextFragment.this.drawLayout.buildDrawingCache();
                Bitmap createBitmap = Bitmap.createBitmap(TextFragment.this.drawLayout.getDrawingCache());
                TextFragment.this.drawLayout.destroyDrawingCache();
                try {
                    String newOutputPath = ((ImageEditActivity) TextFragment.this.getActivity()).getNewOutputPath(TextFragment.this.mImagePath);
                    createBitmap.compress(CompressFormat.JPEG, 100, new FileOutputStream(newOutputPath));
                    ((ImageEditActivity) TextFragment.this.getActivity()).addImage(newOutputPath);
                    ((ImageEditActivity) TextFragment.this.getActivity()).hideLoading();
                    if (TextFragment.this.getActivity().getSupportFragmentManager().getBackStackEntryCount() > 1) {
                        TextFragment.this.getActivity().getSupportFragmentManager().popBackStack();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }, 500);
    }
}
