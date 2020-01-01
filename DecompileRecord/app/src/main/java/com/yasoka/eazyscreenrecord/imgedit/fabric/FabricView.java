package com.yasoka.eazyscreenrecord.imgedit.fabric;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.SystemClock;
import android.support.p000v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import com.ezscreenrecorder.imgedit.fabric.DrawableObjects.CBitmap;
import com.ezscreenrecorder.imgedit.fabric.DrawableObjects.CDrawable;
import com.ezscreenrecorder.imgedit.fabric.DrawableObjects.CPath;
import com.ezscreenrecorder.imgedit.fabric.DrawableObjects.CText;
import com.ezscreenrecorder.imgedit.fabric.DrawableObjects.CTransform;
import com.ezscreenrecorder.imgedit.fabric.DrawableObjects.CTranslation;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

public class FabricView extends View {
    public static final int BACKGROUND_STYLE_BLANK = 0;
    public static final int BACKGROUND_STYLE_GRAPH_PAPER = 2;
    public static final int BACKGROUND_STYLE_NOTEBOOK_PAPER = 1;
    public static final int DRAW_MODE = 0;
    public static final int LOCKED_MODE = 3;
    private static final int MAX_CLICK_DISTANCE = 15;
    private static final int MAX_CLICK_DURATION = 1000;
    public static final int NOTEBOOK_LEFT_LINE_COLOR = -65536;
    public static final int NOTEBOOK_LEFT_LINE_PADDING = 120;
    public static final int ROTATE_MODE = 2;
    private static final int SELECTION_LINE_WIDTH = 2;
    public static final int SELECT_MODE = 1;
    private static final float TOUCH_TOLERANCE = 4.0f;
    private Rect cropBounds = null;
    Paint currentPaint;
    CPath currentPath;
    private Bitmap deleteIcon;
    private RectF deleteIconPosition = new RectF(-1.0f, -1.0f, -1.0f, -1.0f);
    private DeletionListener deletionListener = null;
    private final RectF dirtyRect = new RectF();
    private CDrawable hovering = null;
    private float lastTouchX;
    private float lastTouchY;
    public int mAutoscrollDistance = 100;
    private int mBackgroundColor = -1;
    private int mBackgroundMode = 0;
    private int mColor = ViewCompat.MEASURED_STATE_MASK;
    private ArrayList<CDrawable> mDrawableList = new ArrayList<>();
    private float mHorizontalOffset = 1.0f;
    private int mInteractionMode = 0;
    private boolean mRedrawBackground;
    private float mSize = 5.0f;
    private Style mStyle = Style.STROKE;
    private boolean mTextExpectTouch;
    private ArrayList<CDrawable> mUndoList = new ArrayList<>();
    private float mVerticalOffset = 1.0f;
    private float mZoomLevel = 1.0f;
    private long pressStartTime;
    private float pressedX;
    private float pressedY;
    private int savePoint = 0;
    private CDrawable selected = null;
    private int selectionColor = -12303292;
    Paint selectionPaint;

    public interface DeletionListener {
        void deleted(CDrawable cDrawable);
    }

    private boolean onTouchLockedMode(MotionEvent motionEvent) {
        return false;
    }

    private boolean onTouchRotateMode(MotionEvent motionEvent) {
        return false;
    }

    public FabricView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setBackgroundColor(this.mBackgroundColor);
        this.mTextExpectTouch = false;
        this.selectionPaint = new Paint();
        this.selectionPaint.setAntiAlias(true);
        this.selectionPaint.setColor(this.selectionColor);
        this.selectionPaint.setStyle(Style.STROKE);
        this.selectionPaint.setStrokeJoin(Join.ROUND);
        this.selectionPaint.setStrokeWidth(2.0f);
        this.selectionPaint.setPathEffect(new DashPathEffect(new float[]{10.0f, 20.0f}, 0.0f));
        this.deleteIcon = BitmapFactory.decodeResource(context.getResources(), 17301564);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        drawBackground(canvas, this.mBackgroundMode);
        Rect rect = new Rect(canvas.getWidth(), canvas.getHeight(), 0, 0);
        for (int i = 0; i < this.mDrawableList.size(); i++) {
            try {
                CDrawable cDrawable = (CDrawable) this.mDrawableList.get(i);
                if (!(cDrawable instanceof CTransform)) {
                    Rect computeBounds = cDrawable.computeBounds();
                    rect.union(computeBounds);
                    cDrawable.draw(canvas);
                    if (this.mInteractionMode == 1 && cDrawable.equals(this.selected)) {
                        growRect(computeBounds, 2);
                        canvas.drawRect(new RectF(computeBounds), this.selectionPaint);
                        this.deleteIconPosition = new RectF();
                        this.deleteIconPosition.left = (float) (computeBounds.right - (this.deleteIcon.getWidth() / 2));
                        this.deleteIconPosition.top = (float) (computeBounds.top - (this.deleteIcon.getHeight() / 2));
                        this.deleteIconPosition.right = this.deleteIconPosition.left + ((float) this.deleteIcon.getWidth());
                        this.deleteIconPosition.bottom = this.deleteIconPosition.top + ((float) this.deleteIcon.getHeight());
                        canvas.drawBitmap(this.deleteIcon, this.deleteIconPosition.left, this.deleteIconPosition.top, cDrawable.getPaint());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (rect.width() <= 0) {
            this.cropBounds = null;
        } else {
            this.cropBounds = rect;
        }
    }

    private void growRect(Rect rect, int i) {
        rect.left -= i;
        rect.top -= i;
        rect.bottom += i;
        rect.right += i;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (getInteractionMode() == 0) {
            return onTouchDrawMode(motionEvent);
        }
        if (getInteractionMode() == 1) {
            return onTouchSelectMode(motionEvent);
        }
        if (getInteractionMode() == 2) {
            return onTouchRotateMode(motionEvent);
        }
        return onTouchLockedMode(motionEvent);
    }

    public boolean onTouchDrawMode(MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        if (x < 0.0f) {
            x = 0.0f;
        }
        if (y < 0.0f) {
            y = 0.0f;
        }
        if (x > ((float) getWidth())) {
            x = (float) getWidth();
        }
        if (y > ((float) getHeight())) {
            y = (float) getHeight();
        }
        int action = motionEvent.getAction();
        if (action != 0) {
            if (action == 1) {
                this.currentPath.lineTo(x, y);
            } else if (action == 2) {
                float abs = Math.abs(x - this.lastTouchX);
                float abs2 = Math.abs(y - this.lastTouchY);
                if (abs >= TOUCH_TOLERANCE || abs2 >= TOUCH_TOLERANCE) {
                    CPath cPath = this.currentPath;
                    float f = this.lastTouchX;
                    float f2 = this.lastTouchY;
                    cPath.quadTo(f, f2, (x + f) / 2.0f, (y + f2) / 2.0f);
                    this.lastTouchX = x;
                    this.lastTouchY = y;
                }
                this.dirtyRect.left = Math.min((float) this.currentPath.getXcoords(), this.dirtyRect.left);
                this.dirtyRect.right = Math.max((float) (this.currentPath.getXcoords() + this.currentPath.getWidth()), this.dirtyRect.right);
                this.dirtyRect.top = Math.min((float) this.currentPath.getYcoords(), this.dirtyRect.top);
                this.dirtyRect.bottom = Math.max((float) (this.currentPath.getYcoords() + this.currentPath.getHeight()), this.dirtyRect.bottom);
                cleanDirtyRegion(x, y);
                invalidate();
                this.lastTouchX = x;
                this.lastTouchY = y;
                return true;
            }
            return false;
        }
        this.currentPath = new CPath();
        this.currentPaint = new Paint();
        this.currentPaint.setAntiAlias(true);
        this.currentPaint.setColor(this.mColor);
        this.currentPaint.setStyle(this.mStyle);
        this.currentPaint.setStrokeJoin(Join.ROUND);
        this.currentPaint.setStrokeWidth(this.mSize);
        this.currentPath.setPaint(this.currentPaint);
        this.currentPath.moveTo(x, y);
        this.lastTouchX = x;
        this.lastTouchY = y;
        this.mDrawableList.add(this.currentPath);
        this.mUndoList.clear();
        return true;
    }

    private boolean onTouchSelectMode(MotionEvent motionEvent) {
        ArrayList<CDrawable> arrayList = this.mDrawableList;
        ListIterator listIterator = arrayList.listIterator(arrayList.size());
        int action = motionEvent.getAction();
        if (action == 0) {
            this.pressStartTime = SystemClock.uptimeMillis();
            this.pressedX = motionEvent.getX();
            this.pressedY = motionEvent.getY();
            while (true) {
                if (!listIterator.hasPrevious()) {
                    break;
                }
                CDrawable cDrawable = (CDrawable) listIterator.previous();
                if (!(cDrawable instanceof CTransform) && cDrawable.computeBounds().contains((int) this.pressedX, (int) this.pressedY)) {
                    this.hovering = cDrawable;
                    break;
                }
            }
            return true;
        } else if (action != 1) {
            return false;
        } else {
            long uptimeMillis = SystemClock.uptimeMillis() - this.pressStartTime;
            double sqrt = Math.sqrt(Math.pow((double) (motionEvent.getX() - this.pressedX), 2.0d) + Math.pow((double) (motionEvent.getY() - this.pressedY), 2.0d));
            if (uptimeMillis >= 1000 || sqrt >= 15.0d) {
                if (sqrt > 15.0d) {
                    CDrawable cDrawable2 = this.hovering;
                    if (cDrawable2 != null) {
                        CTranslation cTranslation = new CTranslation(cDrawable2);
                        Vector vector = new Vector(2);
                        vector.add(Integer.valueOf((int) (motionEvent.getX() - this.pressedX)));
                        vector.add(Integer.valueOf((int) (motionEvent.getY() - this.pressedY)));
                        cTranslation.setDirection(vector);
                        this.hovering.addTransform(cTranslation);
                        this.mDrawableList.add(cTranslation);
                        this.mUndoList.clear();
                    }
                }
            } else if (this.hovering != null || !this.deleteIconPosition.contains(motionEvent.getX(), motionEvent.getY())) {
                this.selected = this.hovering;
            } else {
                deleteSelection();
                return true;
            }
            invalidate();
            this.hovering = null;
            return true;
        }
    }

    public void drawBackground(Canvas canvas, int i) {
        if (i != 0) {
            Paint paint = new Paint();
            paint.setColor(Color.argb(50, 0, 0, 0));
            paint.setStyle(this.mStyle);
            paint.setStrokeJoin(Join.ROUND);
            paint.setStrokeWidth(this.mSize - 2.0f);
            if (i == 1) {
                drawNotebookPaperBackground(canvas, paint);
            } else if (i == 2) {
                drawGraphPaperBackground(canvas, paint);
            }
        }
        this.mRedrawBackground = false;
    }

    private void drawGraphPaperBackground(Canvas canvas, Paint paint) {
        boolean z = false;
        boolean z2 = false;
        int i = 0;
        while (true) {
            if (!z || !z2) {
                if (i < canvas.getHeight()) {
                    float f = (float) i;
                    canvas.drawLine(0.0f, f, (float) canvas.getWidth(), f, paint);
                } else {
                    z = true;
                }
                if (i < canvas.getWidth()) {
                    float f2 = (float) i;
                    canvas.drawLine(f2, 0.0f, f2, (float) canvas.getHeight(), paint);
                } else {
                    z2 = true;
                }
                i += 75;
            } else {
                return;
            }
        }
    }

    private void drawNotebookPaperBackground(Canvas canvas, Paint paint) {
        boolean z = false;
        int i = 0;
        while (!z) {
            if (i < canvas.getHeight()) {
                float f = (float) i;
                canvas.drawLine(0.0f, f, (float) canvas.getWidth(), f, paint);
            } else {
                z = true;
            }
            i += 75;
        }
        paint.setColor(-65536);
        canvas.drawLine(120.0f, 0.0f, 120.0f, (float) canvas.getHeight(), paint);
    }

    public void drawText(String str, int i, int i2, Paint paint) {
        this.mDrawableList.add(new CText(str, i, i2, paint));
        this.mUndoList.clear();
        invalidate();
    }

    private void drawTextFromKeyboard() {
        Toast.makeText(getContext(), "Touch where you want the text to be", 1).show();
        this.mTextExpectTouch = true;
    }

    private void cleanDirtyRegion(float f, float f2) {
        this.dirtyRect.left = Math.min(this.lastTouchX, f);
        this.dirtyRect.right = Math.max(this.lastTouchX, f);
        this.dirtyRect.top = Math.min(this.lastTouchY, f2);
        this.dirtyRect.bottom = Math.max(this.lastTouchY, f2);
    }

    public void undo() {
        if (this.mDrawableList.size() > 0) {
            ArrayList<CDrawable> arrayList = this.mDrawableList;
            CDrawable cDrawable = (CDrawable) arrayList.get(arrayList.size() - 1);
            this.mUndoList.add(cDrawable);
            ArrayList<CDrawable> arrayList2 = this.mDrawableList;
            arrayList2.remove(arrayList2.size() - 1);
            if (cDrawable instanceof CTransform) {
                CTransform cTransform = (CTransform) cDrawable;
                cTransform.getDrawable().removeTransform(cTransform);
            }
            invalidate();
        }
    }

    public void redo() {
        if (this.mUndoList.size() > 0) {
            ArrayList<CDrawable> arrayList = this.mUndoList;
            CDrawable cDrawable = (CDrawable) arrayList.get(arrayList.size() - 1);
            this.mDrawableList.add(cDrawable);
            this.mDrawableList.addAll(cDrawable.getTransforms());
            this.mUndoList.remove(cDrawable);
            if (cDrawable instanceof CTransform) {
                CTransform cTransform = (CTransform) cDrawable;
                cTransform.getDrawable().addTransform(cTransform);
            }
            invalidate();
        }
    }

    public void cleanPage() {
        this.mDrawableList.clear();
        this.currentPath = null;
        this.mUndoList.clear();
        this.savePoint = 0;
        invalidate();
    }

    public void drawImage(int i, int i2, int i3, int i4, Bitmap bitmap) {
        CBitmap cBitmap = new CBitmap(bitmap, i, i2);
        cBitmap.setWidth(i3);
        cBitmap.setHeight(i4);
        this.mDrawableList.add(cBitmap);
        this.mUndoList.clear();
        invalidate();
    }

    public Bitmap getCanvasBitmap() {
        buildDrawingCache();
        Bitmap createBitmap = Bitmap.createBitmap(getDrawingCache());
        destroyDrawingCache();
        return createBitmap;
    }

    public Bitmap getCroppedCanvasBitmap() {
        if (this.cropBounds == null) {
            return null;
        }
        return Bitmap.createBitmap(getCanvasBitmap(), this.cropBounds.left, this.cropBounds.top, this.cropBounds.width(), this.cropBounds.height());
    }

    public int getColor() {
        return this.mColor;
    }

    public void setColor(int i) {
        this.mColor = i;
    }

    public int getBackgroundColor() {
        return this.mBackgroundColor;
    }

    public void setBackgroundColor(int i) {
        this.mBackgroundColor = i;
    }

    public int getBackgroundMode() {
        return this.mBackgroundMode;
    }

    public void setBackgroundMode(int i) {
        this.mBackgroundMode = i;
        invalidate();
    }

    public Style getStyle() {
        return this.mStyle;
    }

    public void setStyle(Style style) {
        this.mStyle = style;
    }

    public float getSize() {
        return this.mSize;
    }

    public void setSize(float f) {
        this.mSize = f;
    }

    public int getInteractionMode() {
        return this.mInteractionMode;
    }

    public void setInteractionMode(int i) {
        int i2 = 3;
        if (i <= 3 && i >= 0) {
            i2 = i;
        }
        this.mInteractionMode = i2;
        invalidate();
    }

    public List<CDrawable> getDrawablesList() {
        return this.mDrawableList;
    }

    public void markSaved() {
        this.savePoint = this.mDrawableList.size();
    }

    public boolean isSaved() {
        return this.savePoint == this.mDrawableList.size();
    }

    public List<CDrawable> getUnsavedDrawablesList() {
        if (this.savePoint > this.mDrawableList.size()) {
            return new ArrayList();
        }
        ArrayList<CDrawable> arrayList = this.mDrawableList;
        return arrayList.subList(this.savePoint, arrayList.size());
    }

    public void revertUnsaved() {
        for (CDrawable deleteDrawable : getUnsavedDrawablesList()) {
            deleteDrawable(deleteDrawable);
        }
    }

    public void selectLastDrawn() {
        if (!this.mDrawableList.isEmpty()) {
            ArrayList<CDrawable> arrayList = this.mDrawableList;
            ListIterator listIterator = arrayList.listIterator(arrayList.size());
            while (true) {
                if (!listIterator.hasPrevious()) {
                    break;
                }
                CDrawable cDrawable = (CDrawable) listIterator.previous();
                if (!(cDrawable instanceof CTransform)) {
                    this.selected = cDrawable;
                    break;
                }
            }
            invalidate();
        }
    }

    public CDrawable getSelection() {
        return this.selected;
    }

    public void deSelect() {
        this.selected = null;
        invalidate();
    }

    public void deleteSelection() {
        CDrawable cDrawable = this.selected;
        if (cDrawable != null) {
            deleteDrawable(cDrawable);
            this.selected = null;
        }
    }

    public void deleteDrawable(CDrawable cDrawable) {
        if (cDrawable != null) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(cDrawable);
            arrayList.addAll(cDrawable.getTransforms());
            this.mDrawableList.removeAll(arrayList);
            DeletionListener deletionListener2 = this.deletionListener;
            if (deletionListener2 != null) {
                deletionListener2.deleted(cDrawable);
            }
            this.mUndoList.add(cDrawable);
            invalidate();
        }
    }

    public void setDeleteIcon(Bitmap bitmap) {
        this.deleteIcon = bitmap;
    }

    public void setDeletionListener(DeletionListener deletionListener2) {
        this.deletionListener = deletionListener2;
    }

    public int getSelectionColor() {
        return this.selectionColor;
    }

    public void setSelectionColor(int i) {
        this.selectionColor = i;
    }
}
