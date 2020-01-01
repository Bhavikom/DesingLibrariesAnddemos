package com.yasoka.eazyscreenrecord.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.p000v4.view.ViewCompat;
import android.support.p003v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnSystemUiVisibilityChangeListener;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.R;
import com.ezscreenrecorder.imgedit.colorseekbar.ColorSeekBar;
import com.ezscreenrecorder.imgedit.colorseekbar.ColorSeekBar.OnColorChangeListener;
import com.ezscreenrecorder.imgedit.fabric.DrawingView;

public class ExplainerVideoActivity extends AppCompatActivity implements OnClickListener {
    /* access modifiers changed from: private */
    public int backgroundColor = -1;
    /* access modifiers changed from: private */
    public ConstraintLayout backgroundView;
    private ImageButton bgColorButton;
    private ImageButton brushColorButton;
    private ImageButton clearButton;
    private ImageButton colorPickerCloseButton;
    private ColorSeekBar colorSeekBar;
    /* access modifiers changed from: private */
    public DrawingView drawingView;
    /* access modifiers changed from: private */
    public ImageButton eraserButton;
    /* access modifiers changed from: private */
    public int lastUsedColorBar = 0;
    /* access modifiers changed from: private */
    public int penColor = ViewCompat.MEASURED_STATE_MASK;
    private ImageButton resizeButton;
    private ImageButton sizePickerButton;
    private SeekBar sizeSeekbar;

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle bundle) {
        hideStatusBarAndNavigationBar();
        fixScreenOrientation();
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_explainer_video);
        initViews();
    }

    private void fixScreenOrientation() {
        int i = getResources().getConfiguration().orientation;
        if (i == 2) {
            setRequestedOrientation(0);
        } else if (i == 1) {
            setRequestedOrientation(1);
        }
    }

    public void onClick(View view) {
        this.lastUsedColorBar = view.getId();
        switch (view.getId()) {
            case C0793R.C0795id.id_color_bar_close_img_button /*2131296501*/:
                findViewById(C0793R.C0795id.id_explainer_color_bar_main_button).setVisibility(0);
                findViewById(C0793R.C0795id.id_explainer_color_picker_panel).setVisibility(8);
                this.lastUsedColorBar = 0;
                break;
            case C0793R.C0795id.id_explainer_color_bar_background_button /*2131296527*/:
                this.colorSeekBar.setColor(this.backgroundColor);
                findViewById(C0793R.C0795id.id_color_bar_preview_img_view).setBackgroundColor(this.backgroundColor);
                findViewById(C0793R.C0795id.id_explainer_color_bar_main_button).setVisibility(8);
                findViewById(C0793R.C0795id.id_explainer_color_picker_panel).setVisibility(0);
                break;
            case C0793R.C0795id.id_explainer_color_bar_brush_button /*2131296528*/:
                this.drawingView.initializePen();
                this.drawingView.setPenColor(this.penColor);
                this.colorSeekBar.setColor(this.penColor);
                if (this.eraserButton.isSelected()) {
                    this.eraserButton.setSelected(false);
                }
                if (this.brushColorButton.isSelected()) {
                    findViewById(C0793R.C0795id.id_color_bar_preview_img_view).setBackgroundColor(this.penColor);
                    findViewById(C0793R.C0795id.id_explainer_color_bar_main_button).setVisibility(8);
                    findViewById(C0793R.C0795id.id_explainer_color_picker_panel).setVisibility(0);
                    break;
                } else {
                    this.brushColorButton.setSelected(true);
                    return;
                }
            case C0793R.C0795id.id_explainer_color_bar_clear_button /*2131296529*/:
                this.drawingView.clearBackground();
                break;
            case C0793R.C0795id.id_explainer_color_bar_eraser_button /*2131296531*/:
                this.drawingView.initializeEraser();
                if (this.brushColorButton.isSelected()) {
                    this.brushColorButton.setSelected(false);
                }
                if (!this.eraserButton.isSelected()) {
                    this.eraserButton.setSelected(true);
                    break;
                }
                break;
            case C0793R.C0795id.id_explainer_color_bar_resizer_button /*2131296533*/:
                findViewById(C0793R.C0795id.id_explainer_color_bar_main_button).setVisibility(8);
                findViewById(C0793R.C0795id.id_explainer_size_bar_panel).setVisibility(0);
                break;
            case C0793R.C0795id.id_explainer_size_bar_close_img_button /*2131296536*/:
                findViewById(C0793R.C0795id.id_explainer_color_bar_main_button).setVisibility(0);
                findViewById(C0793R.C0795id.id_explainer_size_bar_panel).setVisibility(8);
                break;
        }
    }

    private void initViews() {
        this.clearButton = (ImageButton) findViewById(C0793R.C0795id.id_explainer_color_bar_clear_button);
        this.bgColorButton = (ImageButton) findViewById(C0793R.C0795id.id_explainer_color_bar_background_button);
        this.eraserButton = (ImageButton) findViewById(C0793R.C0795id.id_explainer_color_bar_eraser_button);
        this.brushColorButton = (ImageButton) findViewById(C0793R.C0795id.id_explainer_color_bar_brush_button);
        this.colorPickerCloseButton = (ImageButton) findViewById(C0793R.C0795id.id_color_bar_close_img_button);
        this.colorSeekBar = (ColorSeekBar) findViewById(C0793R.C0795id.id_color_bar_seek_bar_color);
        this.drawingView = (DrawingView) findViewById(C0793R.C0795id.id_explainer_color_bar_drawing_view);
        this.backgroundView = (ConstraintLayout) findViewById(C0793R.C0795id.id_explainer_video_act_background);
        this.sizeSeekbar = (SeekBar) findViewById(C0793R.C0795id.id_explainer_size_seek_bar);
        this.sizePickerButton = (ImageButton) findViewById(C0793R.C0795id.id_explainer_size_bar_close_img_button);
        this.resizeButton = (ImageButton) findViewById(C0793R.C0795id.id_explainer_color_bar_resizer_button);
        this.backgroundView.setBackgroundColor(this.backgroundColor);
        int penSize = (int) this.drawingView.getPenSize();
        this.drawingView.setEraserSize((float) penSize);
        this.sizeSeekbar.setProgress(penSize);
        this.brushColorButton.setSelected(true);
        this.clearButton.setOnClickListener(this);
        this.resizeButton.setOnClickListener(this);
        this.sizePickerButton.setOnClickListener(this);
        this.bgColorButton.setOnClickListener(this);
        this.eraserButton.setOnClickListener(this);
        this.brushColorButton.setOnClickListener(this);
        this.colorPickerCloseButton.setOnClickListener(this);
        this.sizeSeekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (i == 0) {
                    seekBar.setProgress(1);
                    return;
                }
                float f = (float) i;
                ExplainerVideoActivity.this.drawingView.setEraserSize(f);
                ExplainerVideoActivity.this.drawingView.setPenSize(f);
                if (ExplainerVideoActivity.this.eraserButton.isSelected()) {
                    ExplainerVideoActivity.this.drawingView.initializeEraser();
                } else {
                    ExplainerVideoActivity.this.drawingView.initializePen();
                    ExplainerVideoActivity.this.drawingView.setPenColor(ExplainerVideoActivity.this.penColor);
                }
            }
        });
        this.colorSeekBar.setOnColorChangeListener(new OnColorChangeListener() {
            public void onColorChangeListener(int i, int i2, int i3) {
                switch (ExplainerVideoActivity.this.lastUsedColorBar) {
                    case C0793R.C0795id.id_explainer_color_bar_background_button /*2131296527*/:
                        ExplainerVideoActivity.this.findViewById(C0793R.C0795id.id_color_bar_preview_img_view).setBackgroundColor(i3);
                        ExplainerVideoActivity.this.backgroundView.setBackgroundColor(ExplainerVideoActivity.this.backgroundColor);
                        ExplainerVideoActivity.this.backgroundColor = i3;
                        return;
                    case C0793R.C0795id.id_explainer_color_bar_brush_button /*2131296528*/:
                        ExplainerVideoActivity.this.findViewById(C0793R.C0795id.id_color_bar_preview_img_view).setBackgroundColor(i3);
                        ExplainerVideoActivity.this.drawingView.setPenColor(i3);
                        ExplainerVideoActivity.this.penColor = i3;
                        return;
                    default:
                        return;
                }
            }
        });
        this.drawingView.initializePen();
        this.drawingView.setPenColor(ViewCompat.MEASURED_STATE_MASK);
    }

    private void hideStatusBarAndNavigationBar() {
        getWindow().getDecorView().setSystemUiVisibility(5894);
        final View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new OnSystemUiVisibilityChangeListener() {
            public void onSystemUiVisibilityChange(int i) {
                if ((i & 4) == 0) {
                    decorView.setSystemUiVisibility(5894);
                }
            }
        });
    }
}
