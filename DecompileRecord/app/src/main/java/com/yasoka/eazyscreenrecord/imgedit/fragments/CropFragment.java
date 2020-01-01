package com.yasoka.eazyscreenrecord.imgedit.fragments;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.p000v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.R;
import com.ezscreenrecorder.imgedit.ImageEditActivity;
import com.ezscreenrecorder.imgedit.crop.CropImageOptions;
import com.ezscreenrecorder.imgedit.crop.CropImageView;
import com.ezscreenrecorder.imgedit.crop.CropImageView.CropResult;
import com.ezscreenrecorder.imgedit.crop.CropImageView.OnCropImageCompleteListener;
import com.ezscreenrecorder.imgedit.crop.CropImageView.OnSetImageUriCompleteListener;
import com.ezscreenrecorder.model.FirebaseDataDevice;
import com.ezscreenrecorder.server.ServerAPI;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import p009io.reactivex.SingleObserver;
import p009io.reactivex.observers.DisposableSingleObserver;

public class CropFragment extends Fragment implements OnSetImageUriCompleteListener, OnCropImageCompleteListener {
    private boolean isCrop;
    private boolean isFlip;
    private boolean isRotate;
    private Uri mCropImageUri;
    /* access modifiers changed from: private */
    public CropImageView mCropImageView;
    /* access modifiers changed from: private */
    public String mImagePath;
    /* access modifiers changed from: private */
    public CropImageOptions mOptions;

    public void onSetImageUriComplete(CropImageView cropImageView, Uri uri, Exception exc) {
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            this.mImagePath = getArguments().getString(MainImageFragment.CROP_IMAGE_EXTRA_SOURCE);
            if (this.mImagePath.contains("file://")) {
                this.mCropImageUri = Uri.parse(this.mImagePath);
            } else {
                this.mCropImageUri = Uri.fromFile(new File(this.mImagePath));
            }
            this.isCrop = getArguments().getBoolean("isCrop");
            this.isFlip = getArguments().getBoolean("isFlip");
            this.isRotate = getArguments().getBoolean("isRotate");
        }
        this.mOptions = new CropImageOptions();
        setHasOptionsMenu(true);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(C0793R.C0797menu.menu_sub_img_edit, menu);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == C0793R.C0795id.action_done) {
            if (this.isCrop) {
                cropImage();
            } else if (this.isFlip) {
                saveImage();
            } else if (this.isRotate) {
                saveImage();
            }
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_crop, viewGroup, false);
    }

    public void onViewCreated(View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.mCropImageView = (CropImageView) view.findViewById(C0793R.C0795id.img_crop);
        this.mCropImageView.setImageUriAsync(this.mCropImageUri);
        view.findViewById(C0793R.C0795id.txt_rotate_left).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                CropFragment cropFragment = CropFragment.this;
                cropFragment.rotateImage(-cropFragment.mOptions.rotationDegrees);
            }
        });
        view.findViewById(C0793R.C0795id.txt_rotate_right).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                CropFragment cropFragment = CropFragment.this;
                cropFragment.rotateImage(cropFragment.mOptions.rotationDegrees);
            }
        });
        view.findViewById(C0793R.C0795id.txt_flip_horizontal).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                CropFragment.this.mCropImageView.flipImageHorizontally();
            }
        });
        view.findViewById(C0793R.C0795id.txt_flip_vertical).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                CropFragment.this.mCropImageView.flipImageVertically();
            }
        });
        if (this.isCrop) {
            view.findViewById(C0793R.C0795id.lay_flip).setVisibility(8);
            view.findViewById(C0793R.C0795id.lay_rotate).setVisibility(8);
            this.mCropImageView.setShowCropOverlay(true);
        } else if (this.isFlip) {
            view.findViewById(C0793R.C0795id.lay_rotate).setVisibility(8);
            view.findViewById(C0793R.C0795id.lay_flip).setVisibility(0);
            this.mCropImageView.setShowCropOverlay(false);
        } else if (this.isRotate) {
            view.findViewById(C0793R.C0795id.lay_rotate).setVisibility(0);
            view.findViewById(C0793R.C0795id.lay_flip).setVisibility(8);
            this.mCropImageView.setShowCropOverlay(false);
        }
    }

    public void onActivityCreated(@Nullable Bundle bundle) {
        super.onActivityCreated(bundle);
        if (this.isCrop) {
            ((ImageEditActivity) getActivity()).setMyTitle(getString(C0793R.string.crop));
            ((ImageEditActivity) getActivity()).setTitleStartAligned(false);
            ServerAPI.getInstance().addToFireBase(getContext(), "Crop").subscribe((SingleObserver<? super T>) new DisposableSingleObserver<FirebaseDataDevice>() {
                public void onSuccess(FirebaseDataDevice firebaseDataDevice) {
                }

                public void onError(Throwable th) {
                    th.printStackTrace();
                }
            });
        } else if (this.isFlip) {
            ((ImageEditActivity) getActivity()).setMyTitle(getString(C0793R.string.flip));
            ((ImageEditActivity) getActivity()).setTitleStartAligned(false);
            ServerAPI.getInstance().addToFireBase(getContext(), "Flip").subscribe((SingleObserver<? super T>) new DisposableSingleObserver<FirebaseDataDevice>() {
                public void onSuccess(FirebaseDataDevice firebaseDataDevice) {
                }

                public void onError(Throwable th) {
                    th.printStackTrace();
                }
            });
        } else if (this.isRotate) {
            ((ImageEditActivity) getActivity()).setMyTitle(getString(C0793R.string.rotate));
            ((ImageEditActivity) getActivity()).setTitleStartAligned(false);
            ServerAPI.getInstance().addToFireBase(getContext(), "Rotate").subscribe((SingleObserver<? super T>) new DisposableSingleObserver<FirebaseDataDevice>() {
                public void onSuccess(FirebaseDataDevice firebaseDataDevice) {
                }

                public void onError(Throwable th) {
                    th.printStackTrace();
                }
            });
        }
    }

    private void saveImage() {
        ((ImageEditActivity) getActivity()).showLoading();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (CropFragment.this.isAdded()) {
                    CropFragment.this.mCropImageView.buildDrawingCache();
                    Bitmap createBitmap = Bitmap.createBitmap(CropFragment.this.mCropImageView.getDrawingCache());
                    CropFragment.this.mCropImageView.destroyDrawingCache();
                    try {
                        String newOutputPath = ((ImageEditActivity) CropFragment.this.getActivity()).getNewOutputPath(CropFragment.this.mImagePath);
                        createBitmap.compress(CompressFormat.JPEG, 100, new FileOutputStream(newOutputPath));
                        ((ImageEditActivity) CropFragment.this.getActivity()).addImage(newOutputPath);
                        ((ImageEditActivity) CropFragment.this.getActivity()).hideLoading();
                        if (CropFragment.this.getActivity().getSupportFragmentManager().getBackStackEntryCount() > 1) {
                            CropFragment.this.getActivity().getSupportFragmentManager().popBackStack();
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 600);
    }

    public void onStart() {
        super.onStart();
        this.mCropImageView.setOnSetImageUriCompleteListener(this);
        this.mCropImageView.setOnCropImageCompleteListener(this);
    }

    public void onStop() {
        super.onStop();
        this.mCropImageView.setOnSetImageUriCompleteListener(null);
        this.mCropImageView.setOnCropImageCompleteListener(null);
    }

    public void onCropImageComplete(CropImageView cropImageView, CropResult cropResult) {
        ((ImageEditActivity) getActivity()).addImage(cropResult.getUri().getPath());
        ((ImageEditActivity) getActivity()).hideLoading();
        if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    /* access modifiers changed from: protected */
    public void cropImage() {
        ((ImageEditActivity) getActivity()).showLoading();
        this.mCropImageView.saveCroppedImageAsync(Uri.fromFile(new File(((ImageEditActivity) getActivity()).getNewOutputPath(this.mCropImageUri.toString()))), this.mOptions.outputCompressFormat, this.mOptions.outputCompressQuality, this.mOptions.outputRequestWidth, this.mOptions.outputRequestHeight, this.mOptions.outputRequestSizeOptions);
    }

    /* access modifiers changed from: protected */
    public void rotateImage(int i) {
        this.mCropImageView.rotateImage(i);
    }
}
