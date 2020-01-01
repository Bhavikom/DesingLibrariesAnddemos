package com.yasoka.eazyscreenrecord.imgedit.fragments;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.p000v4.app.Fragment;
import android.support.p000v4.content.ContextCompat;
import android.support.p000v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.R;
import com.ezscreenrecorder.imgedit.ImageEditActivity;
import com.ezscreenrecorder.imgedit.colorseekbar.ColorSeekBar;
import com.ezscreenrecorder.imgedit.colorseekbar.ColorSeekBar.OnColorChangeListener;
import com.ezscreenrecorder.imgedit.fabric.DrawingView;
import com.ezscreenrecorder.imgedit.fabric.numberpicker.NumberPicker;
import com.ezscreenrecorder.imgedit.fabric.numberpicker.NumberPicker.OnValueChangeListener;
import com.ezscreenrecorder.model.FirebaseDataDevice;
import com.ezscreenrecorder.server.ServerAPI;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import p009io.reactivex.SingleObserver;
import p009io.reactivex.observers.DisposableSingleObserver;

public class DrawFragment extends Fragment {
    /* access modifiers changed from: private */
    public Button btnPencil;
    /* access modifiers changed from: private */
    public ColorSeekBar colorSeekBar;
    /* access modifiers changed from: private */
    public FrameLayout drawLayout;
    /* access modifiers changed from: private */
    public DrawingView drawView;
    private ImageView imgView;
    /* access modifiers changed from: private */
    public String mImagePath;
    /* access modifiers changed from: private */
    public NumberPicker numPickerEraser;
    /* access modifiers changed from: private */
    public NumberPicker numPickerPencil;

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            this.mImagePath = getArguments().getString(MainImageFragment.CROP_IMAGE_EXTRA_SOURCE);
        }
        setHasOptionsMenu(true);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.menu_sub_img_edit, menu);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == C0793R.C0795id.action_done) {
            saveImage();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_draw, viewGroup, false);
    }

    public void onViewCreated(View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.drawView = (DrawingView) view.findViewById(C0793R.C0795id.draw_view);
        this.imgView = (ImageView) view.findViewById(C0793R.C0795id.img_draw);
        this.drawLayout = (FrameLayout) view.findViewById(C0793R.C0795id.lay_draw);
        this.numPickerPencil = (NumberPicker) view.findViewById(C0793R.C0795id.num_picker_pencil);
        this.numPickerEraser = (NumberPicker) view.findViewById(C0793R.C0795id.num_picker_eraser);
        this.drawView.setPenColor(ContextCompat.getColor(getContext(), C0793R.color.colorPrimary));
        this.btnPencil = (Button) view.findViewById(C0793R.C0795id.btn_pencil);
        this.colorSeekBar = (ColorSeekBar) view.findViewById(C0793R.C0795id.color_seek_bar);
        this.colorSeekBar.setOnColorChangeListener(new OnColorChangeListener() {
            public void onColorChangeListener(int i, int i2, int i3) {
                DrawFragment.this.drawView.setPenColor(DrawFragment.this.colorSeekBar.getColor());
            }
        });
        final Button button = (Button) view.findViewById(C0793R.C0795id.btn_color);
        final Button button2 = (Button) view.findViewById(C0793R.C0795id.btn_eraser);
        this.btnPencil.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                DrawFragment.this.drawView.initializePen();
                DrawFragment.this.drawView.setPenColor(DrawFragment.this.colorSeekBar.getColor());
                if (DrawFragment.this.numPickerPencil.getVisibility() == 8) {
                    DrawFragment.this.btnPencil.setBackgroundResource(R.drawable.round_blue_back);
                    button.setBackgroundColor(0);
                    button2.setBackgroundColor(0);
                    DrawFragment.this.numPickerPencil.setVisibility(0);
                    DrawFragment.this.numPickerEraser.setVisibility(8);
                    DrawFragment.this.colorSeekBar.setVisibility(8);
                    return;
                }
                DrawFragment.this.btnPencil.setBackgroundColor(0);
                DrawFragment.this.numPickerPencil.setVisibility(8);
            }
        });
        button2.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                DrawFragment.this.drawView.initializeEraser();
                if (DrawFragment.this.numPickerEraser.getVisibility() == 8) {
                    button2.setBackgroundResource(C0793R.C0794drawable.round_blue_back);
                    button.setBackgroundColor(0);
                    DrawFragment.this.btnPencil.setBackgroundColor(0);
                    DrawFragment.this.numPickerEraser.setVisibility(0);
                    DrawFragment.this.numPickerPencil.setVisibility(8);
                    DrawFragment.this.colorSeekBar.setVisibility(8);
                    return;
                }
                button2.setBackgroundColor(0);
                DrawFragment.this.numPickerEraser.setVisibility(8);
            }
        });
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                DrawFragment.this.drawView.initializePen();
                if (DrawFragment.this.colorSeekBar.getVisibility() == 8) {
                    button.setBackgroundResource(C0793R.C0794drawable.round_blue_back);
                    button2.setBackgroundColor(0);
                    DrawFragment.this.btnPencil.setBackgroundColor(0);
                    DrawFragment.this.numPickerEraser.setVisibility(8);
                    DrawFragment.this.numPickerPencil.setVisibility(8);
                    DrawFragment.this.colorSeekBar.setVisibility(0);
                    DrawFragment.this.drawView.setPenColor(DrawFragment.this.colorSeekBar.getColor());
                    return;
                }
                button.setBackgroundColor(0);
                DrawFragment.this.colorSeekBar.setVisibility(8);
            }
        });
        this.numPickerPencil.setOnValueChangedListener(new OnValueChangeListener() {
            public void onValueChange(NumberPicker numberPicker, int i, int i2) {
                DrawFragment.this.drawView.setPenSize((float) i2);
            }
        });
        this.numPickerEraser.setOnValueChangedListener(new OnValueChangeListener() {
            public void onValueChange(NumberPicker numberPicker, int i, int i2) {
                DrawFragment.this.drawView.setEraserSize((float) i2);
            }
        });
        this.drawView.initializePen();
        this.numPickerPencil.setValue(5);
        this.numPickerEraser.setValue(35);
        this.drawView.setEraserSize(35.0f);
        this.drawView.setPenSize(5.0f);
        this.colorSeekBar.post(new Runnable() {
            public void run() {
                DrawFragment.this.colorSeekBar.setColor(ViewCompat.MEASURED_STATE_MASK);
                DrawFragment.this.drawView.setPenColor(DrawFragment.this.colorSeekBar.getColor());
            }
        });
    }

    public void onActivityCreated(@Nullable Bundle bundle) {
        super.onActivityCreated(bundle);
        Glide.with((Fragment) this).load(this.mImagePath).into(this.imgView);
        ((ImageEditActivity) getActivity()).setMyTitle(getString(C0793R.string.draw));
        ((ImageEditActivity) getActivity()).setTitleStartAligned(false);
        this.btnPencil.callOnClick();
        ServerAPI.getInstance().addToFireBase(getContext(), "Draw").subscribe((SingleObserver<? super T>) new DisposableSingleObserver<FirebaseDataDevice>() {
            public void onSuccess(FirebaseDataDevice firebaseDataDevice) {
            }

            public void onError(Throwable th) {
                th.printStackTrace();
            }
        });
    }

    public void saveImage() {
        this.numPickerPencil.setVisibility(8);
        this.numPickerEraser.setVisibility(8);
        this.colorSeekBar.setVisibility(8);
        ((ImageEditActivity) getActivity()).showLoading();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                DrawFragment.this.drawLayout.buildDrawingCache();
                Bitmap createBitmap = Bitmap.createBitmap(DrawFragment.this.drawLayout.getDrawingCache());
                DrawFragment.this.drawLayout.destroyDrawingCache();
                try {
                    String newOutputPath = ((ImageEditActivity) DrawFragment.this.getActivity()).getNewOutputPath(DrawFragment.this.mImagePath);
                    createBitmap.compress(CompressFormat.JPEG, 100, new FileOutputStream(newOutputPath));
                    ((ImageEditActivity) DrawFragment.this.getActivity()).addImage(newOutputPath);
                    ((ImageEditActivity) DrawFragment.this.getActivity()).hideLoading();
                    if (DrawFragment.this.getActivity().getSupportFragmentManager().getBackStackEntryCount() > 1) {
                        DrawFragment.this.getActivity().getSupportFragmentManager().popBackStack();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }, 600);
    }
}
