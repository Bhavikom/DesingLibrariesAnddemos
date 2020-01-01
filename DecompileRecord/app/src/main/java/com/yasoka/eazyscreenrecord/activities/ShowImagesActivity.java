package com.yasoka.eazyscreenrecord.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.p000v4.app.FragmentActivity;
import android.support.p003v7.app.ActionBar;
import android.support.p003v7.app.AppCompatActivity;
import android.support.p003v7.widget.LinearLayoutManager;
import android.support.p003v7.widget.RecyclerView.Adapter;
import android.support.p003v7.widget.RecyclerView.ViewHolder;
import android.support.p003v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.FloatingService;
import com.ezscreenrecorder.model.ImageVideoFile;
import com.ezscreenrecorder.server.model.ServerDatum;
import com.ezscreenrecorder.utils.AppUtils;
import com.ezscreenrecorder.utils.FilesAccessHelper;
import com.ezscreenrecorder.utils.TouchImageView;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager.OnPageChangedListener;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import p009io.reactivex.BackpressureStrategy;
import p009io.reactivex.Flowable;
import p009io.reactivex.FlowableEmitter;
import p009io.reactivex.FlowableOnSubscribe;
import p009io.reactivex.SingleObserver;
import p009io.reactivex.android.schedulers.AndroidSchedulers;
import p009io.reactivex.functions.Function;
import p009io.reactivex.observers.DisposableSingleObserver;
import p009io.reactivex.schedulers.Schedulers;

public class ShowImagesActivity extends AppCompatActivity {
    /* access modifiers changed from: private */
    public AppBarLayout appBarLayout;
    private int currentPosition;
    private ArrayList<ServerDatum> data;
    private ArrayList<String> data2;
    private boolean isPathFromFile = false;
    private boolean isServer;
    /* access modifiers changed from: private */
    public Comparator lastModifySort = new Comparator<ShowImage>() {
        public int compare(ShowImage showImage, ShowImage showImage2) {
            return Long.compare(showImage2.dateCreated, showImage.dateCreated);
        }
    };
    /* access modifiers changed from: private */
    public ImageListAdapter mImageListAdapter;
    private Toolbar mToolbar;
    /* access modifiers changed from: private */
    public RecyclerViewPager mViewPager;
    /* access modifiers changed from: private */
    public String path;
    private String sharePath;

    class ImageListAdapter extends Adapter<ImageListViewHolder> {
        /* access modifiers changed from: private */
        public final View mDecorView;
        private List<ShowImage> mList;

        class ImageListViewHolder extends ViewHolder {
            /* access modifiers changed from: private */
            public final ImageView mImgPlaceHolder;
            /* access modifiers changed from: private */
            public final LinearLayout mProgressWheel;
            /* access modifiers changed from: private */
            public final TouchImageView mTouchImageView;

            ImageListViewHolder(View view) {
                super(view);
                this.mTouchImageView = (TouchImageView) view.findViewById(C0793R.C0795id.img_touch_image);
                this.mProgressWheel = (LinearLayout) view.findViewById(C0793R.C0795id.progress_view);
                this.mImgPlaceHolder = (ImageView) view.findViewById(C0793R.C0795id.img_place_holder);
            }
        }

        ImageListAdapter(List<ShowImage> list) {
            this.mList = new ArrayList(list);
            this.mDecorView = ShowImagesActivity.this.getWindow().getDecorView();
        }

        public ImageListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ImageListViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(C0793R.layout.fragment_show_images, viewGroup, false));
        }

        public void onBindViewHolder(final ImageListViewHolder imageListViewHolder, int i) {
            ShowImage showImage = (ShowImage) this.mList.get(i);
            PrintStream printStream = System.out;
            StringBuilder sb = new StringBuilder();
            sb.append("sdds-->");
            sb.append(showImage.path);
            sb.append("<>");
            sb.append(showImage.thumbnail);
            printStream.println(sb.toString());
            File file = new File(showImage.path);
            if (file.exists()) {
                Glide.with((FragmentActivity) ShowImagesActivity.this).load(file).asBitmap().into(new SimpleTarget<Bitmap>() {
                    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                        imageListViewHolder.mTouchImageView.setImageBitmap(bitmap);
                        imageListViewHolder.mTouchImageView.post(new Runnable() {
                            public void run() {
                                imageListViewHolder.mTouchImageView.setZoom(1.0f);
                            }
                        });
                    }
                });
                ShowImagesActivity.this.getSupportActionBar().setTitle((CharSequence) file.getName());
            } else {
                imageListViewHolder.mTouchImageView.setImageDrawable(null);
                imageListViewHolder.mProgressWheel.setVisibility(0);
                if (showImage.thumbnail != null) {
                    Glide.with((FragmentActivity) ShowImagesActivity.this).load(showImage.thumbnail).asBitmap().into(new SimpleTarget<Bitmap>() {
                        public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                            imageListViewHolder.mTouchImageView.setImageBitmap(bitmap);
                            imageListViewHolder.mTouchImageView.post(new Runnable() {
                                public void run() {
                                    imageListViewHolder.mTouchImageView.setZoom(1.0f);
                                }
                            });
                        }
                    });
                }
                Glide.with((FragmentActivity) ShowImagesActivity.this).load(showImage.path).asBitmap().listener((RequestListener<? super ModelType, TranscodeType>) new RequestListener<String, Bitmap>() {
                    public boolean onException(Exception exc, String str, Target<Bitmap> target, boolean z) {
                        imageListViewHolder.mProgressWheel.setVisibility(8);
                        Toast.makeText(ShowImagesActivity.this.getApplicationContext(), C0793R.string.id_error_view_image_message, 1).show();
                        return false;
                    }

                    public boolean onResourceReady(Bitmap bitmap, String str, Target<Bitmap> target, boolean z, boolean z2) {
                        imageListViewHolder.mProgressWheel.setVisibility(8);
                        return false;
                    }
                }).into(new SimpleTarget<Bitmap>() {
                    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                        imageListViewHolder.mImgPlaceHolder.setVisibility(8);
                        imageListViewHolder.mTouchImageView.setImageBitmap(bitmap);
                        imageListViewHolder.mTouchImageView.post(new Runnable() {
                            public void run() {
                                imageListViewHolder.mTouchImageView.setZoom(1.0f);
                            }
                        });
                    }
                });
                ShowImagesActivity.this.getSupportActionBar().setTitle((CharSequence) "Title Pending");
            }
            imageListViewHolder.mTouchImageView.post(new Runnable() {
                public void run() {
                    imageListViewHolder.mTouchImageView.setZoom(1.0f);
                }
            });
            ShowImagesActivity.this.getWindow().getDecorView();
            imageListViewHolder.mTouchImageView.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    if (ShowImagesActivity.this.getSupportActionBar().isShowing()) {
                        ShowImagesActivity.this.hideActionBar();
                        ImageListAdapter.this.mDecorView.setSystemUiVisibility(2054);
                        return;
                    }
                    ShowImagesActivity.this.showActionBar();
                    ImageListAdapter.this.mDecorView.setSystemUiVisibility(3584);
                }
            });
        }

        public int getItemCount() {
            return this.mList.size();
        }

        /* access modifiers changed from: 0000 */
        public ShowImage getItem(int i) {
            if (i < 0 || this.mList.size() == 0) {
                return null;
            }
            return (ShowImage) this.mList.get(i);
        }

        /* access modifiers changed from: 0000 */
        public void removeItem(int i) {
            this.mList.remove(i);
            notifyDataSetChanged();
        }

        /* access modifiers changed from: 0000 */
        public int indexOf(String str) {
            for (int i = 0; i < this.mList.size(); i++) {
                if (str.equals(((ShowImage) this.mList.get(i)).path)) {
                    return i;
                }
            }
            return 0;
        }
    }

    class ShowImage {
        long dateCreated;
        String name;
        String path;
        String thumbnail;

        ShowImage(String str, String str2, String str3, long j) {
            this.path = str;
            this.name = str2;
            this.thumbnail = str3;
            this.dateCreated = j;
        }

        ShowImage(String str, String str2, String str3) {
            this.path = str;
            this.name = str2;
            this.thumbnail = str3;
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0793R.layout.activity_show_images);
        if (getIntent() == null) {
            Toast.makeText(this, C0793R.string.id_error_view_image_message, 1).show();
            finish();
            return;
        }
        onNewIntent(getIntent());
        getWindow().setFlags(512, 512);
        this.mToolbar = (Toolbar) findViewById(C0793R.C0795id.toolbar);
        setSupportActionBar(this.mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.mViewPager = (RecyclerViewPager) findViewById(C0793R.C0795id.container);
        this.mViewPager.addOnPageChangedListener(new OnPageChangedListener() {
            public void OnPageChanged(int i, int i2) {
                try {
                    if (ShowImagesActivity.this.mImageListAdapter != null) {
                        ShowImagesActivity.this.getSupportActionBar().setTitle((CharSequence) ShowImagesActivity.this.mImageListAdapter.getItem(i2).name);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.mViewPager.setLayoutManager(new LinearLayoutManager(this, 0, false));
        this.appBarLayout = (AppBarLayout) findViewById(C0793R.C0795id.appbar);
        ArrayList<ServerDatum> arrayList = this.data;
        if (arrayList == null || !this.isServer) {
            ArrayList<String> arrayList2 = this.data2;
            if (arrayList2 != null) {
                Flowable.fromIterable(arrayList2).map(new Function<String, ShowImage>() {
                    public ShowImage apply(String str) throws Exception {
                        return new ShowImage(str, str, str);
                    }
                }).toList().subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe((SingleObserver<? super T>) new DisposableSingleObserver<List<ShowImage>>() {
                    public void onSuccess(List<ShowImage> list) {
                        ShowImagesActivity showImagesActivity = ShowImagesActivity.this;
                        showImagesActivity.mImageListAdapter = new ImageListAdapter(list);
                        ShowImagesActivity.this.mViewPager.setAdapter(ShowImagesActivity.this.mImageListAdapter);
                        ShowImagesActivity.this.mViewPager.post(new Runnable() {
                            public void run() {
                                int indexOf = ShowImagesActivity.this.mImageListAdapter.indexOf(ShowImagesActivity.this.path);
                                if (indexOf >= 0) {
                                    ShowImagesActivity.this.mViewPager.scrollToPosition(indexOf);
                                }
                            }
                        });
                    }

                    public void onError(Throwable th) {
                        th.printStackTrace();
                        Toast.makeText(ShowImagesActivity.this.getApplicationContext(), C0793R.string.id_server_error_msg, 1).show();
                        ShowImagesActivity.this.finish();
                    }
                });
            } else {
                Flowable.create(new FlowableOnSubscribe<ImageVideoFile>() {
                    public void subscribe(FlowableEmitter<ImageVideoFile> flowableEmitter) throws Exception {
                        String imageDir = AppUtils.getImageDir(ShowImagesActivity.this.getApplicationContext(), false);
                        File file = null;
                        File file2 = !TextUtils.isEmpty(imageDir) ? new File(imageDir) : null;
                        if (file2 != null && file2.isDirectory()) {
                            File[] listFiles = file2.listFiles();
                            Arrays.sort(listFiles, new Comparator<File>() {
                                public int compare(File file, File file2) {
                                    return Long.compare(file2.lastModified(), file.lastModified());
                                }
                            });
                            for (File file3 : listFiles) {
                                if (!file3.isDirectory()) {
                                    ImageVideoFile imageVideoFile = new ImageVideoFile();
                                    imageVideoFile.setPath(file3.getAbsolutePath());
                                    imageVideoFile.setName(file3.getName());
                                    imageVideoFile.setVideo(false);
                                    imageVideoFile.setCreated(file3.lastModified());
                                    imageVideoFile.setFileSize(file3.length());
                                    flowableEmitter.onNext(imageVideoFile);
                                }
                            }
                        }
                        String imageDir2 = AppUtils.getImageDir(ShowImagesActivity.this.getApplicationContext(), true);
                        if (!TextUtils.isEmpty(imageDir2)) {
                            file = new File(imageDir2);
                        }
                        if (file != null && file.isDirectory()) {
                            File[] listFiles2 = file.listFiles();
                            Arrays.sort(listFiles2, new Comparator<File>() {
                                public int compare(File file, File file2) {
                                    return Long.compare(file2.lastModified(), file.lastModified());
                                }
                            });
                            for (File file4 : listFiles2) {
                                if (!file4.isDirectory()) {
                                    ImageVideoFile imageVideoFile2 = new ImageVideoFile();
                                    imageVideoFile2.setPath(file4.getAbsolutePath());
                                    imageVideoFile2.setName(file4.getName());
                                    imageVideoFile2.setVideo(false);
                                    imageVideoFile2.setCreated(file4.lastModified());
                                    imageVideoFile2.setFileSize(file4.length());
                                    flowableEmitter.onNext(imageVideoFile2);
                                }
                            }
                        }
                        flowableEmitter.onComplete();
                    }
                }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).map(new Function<ImageVideoFile, ShowImage>() {
                    public ShowImage apply(ImageVideoFile imageVideoFile) throws Exception {
                        ShowImage showImage = new ShowImage(imageVideoFile.getPath(), imageVideoFile.getName(), null, imageVideoFile.getCreated());
                        return showImage;
                    }
                }).toList().subscribe((SingleObserver<? super T>) new DisposableSingleObserver<List<ShowImage>>() {
                    public void onSuccess(List<ShowImage> list) {
                        Collections.sort(list, ShowImagesActivity.this.lastModifySort);
                        ShowImagesActivity showImagesActivity = ShowImagesActivity.this;
                        showImagesActivity.mImageListAdapter = new ImageListAdapter(list);
                        ShowImagesActivity.this.mViewPager.setAdapter(ShowImagesActivity.this.mImageListAdapter);
                        ShowImagesActivity.this.mViewPager.post(new Runnable() {
                            public void run() {
                                int indexOf = ShowImagesActivity.this.mImageListAdapter.indexOf(ShowImagesActivity.this.path);
                                if (indexOf >= 0) {
                                    ShowImagesActivity.this.mViewPager.scrollToPosition(indexOf);
                                }
                            }
                        });
                    }

                    public void onError(Throwable th) {
                        th.printStackTrace();
                        Toast.makeText(ShowImagesActivity.this.getApplicationContext(), C0793R.string.id_server_error_msg, 1).show();
                        ShowImagesActivity.this.finish();
                    }
                });
            }
        } else {
            Flowable.fromIterable(arrayList).map(new Function<ServerDatum, ShowImage>() {
                public ShowImage apply(ServerDatum serverDatum) throws Exception {
                    return new ShowImage(serverDatum.getImageUrl(), serverDatum.getImageName(), serverDatum.getImageUrl300());
                }
            }).toList().subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe((SingleObserver<? super T>) new DisposableSingleObserver<List<ShowImage>>() {
                public void onSuccess(List<ShowImage> list) {
                    ShowImagesActivity showImagesActivity = ShowImagesActivity.this;
                    showImagesActivity.mImageListAdapter = new ImageListAdapter(list);
                    ShowImagesActivity.this.mViewPager.setAdapter(ShowImagesActivity.this.mImageListAdapter);
                    ShowImagesActivity.this.mViewPager.post(new Runnable() {
                        public void run() {
                            int indexOf = ShowImagesActivity.this.mImageListAdapter.indexOf(ShowImagesActivity.this.path);
                            if (indexOf >= 0) {
                                ShowImagesActivity.this.mViewPager.scrollToPosition(indexOf);
                            }
                        }
                    });
                }

                public void onError(Throwable th) {
                    th.printStackTrace();
                    Toast.makeText(ShowImagesActivity.this.getApplicationContext(), C0793R.string.id_server_error_msg, 1).show();
                    ShowImagesActivity.this.finish();
                }
            });
        }
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            if (!(intent.getAction() == null || !intent.getAction().equals("android.intent.action.VIEW") || intent.getData() == null)) {
                this.isPathFromFile = true;
                this.path = FilesAccessHelper.getInstance().getPath(getApplicationContext(), intent.getData(), true);
            }
            if (!this.isPathFromFile) {
                this.path = intent.getStringExtra("ImgPath");
                this.sharePath = intent.getStringExtra("sharePath");
                this.isServer = intent.getBooleanExtra("isFromServer", false);
                this.currentPosition = intent.getIntExtra("currentPosition", 0);
                this.data = intent.getParcelableArrayListExtra("files");
                this.data2 = intent.getStringArrayListExtra("files2");
            } else if (TextUtils.isEmpty(this.path)) {
                Toast.makeText(getApplicationContext(), C0793R.string.id_error_view_image_message, 1).show();
                finish();
            } else {
                if (this.data2 == null) {
                    this.data2 = new ArrayList<>();
                }
                this.data2.add(this.path);
                Intent intent2 = new Intent(getApplicationContext(), SplashActivity.class);
                intent2.putExtra(FloatingService.EXTRA_STARTED_FROM_OTHER_APPS, true);
                startActivity(intent2);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void hideActionBar() {
        final ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null && supportActionBar.isShowing()) {
            Toolbar toolbar = this.mToolbar;
            if (toolbar != null) {
                toolbar.animate().translationY(-112.0f).setDuration(80).withEndAction(new Runnable() {
                    public void run() {
                        supportActionBar.hide();
                        ShowImagesActivity.this.appBarLayout.setVisibility(8);
                    }
                }).start();
            } else {
                supportActionBar.hide();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void showActionBar() {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null && !supportActionBar.isShowing()) {
            this.appBarLayout.setVisibility(0);
            supportActionBar.show();
            Toolbar toolbar = this.mToolbar;
            if (toolbar != null) {
                toolbar.animate().translationY(0.0f).setDuration(80).start();
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0793R.C0797menu.menu_show_images, menu);
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem findItem = menu.findItem(C0793R.C0795id.action_delete);
        if (findItem != null) {
            if (this.isServer || this.isPathFromFile) {
                findItem.setVisible(false);
            } else {
                findItem.setVisible(true);
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x00ac  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onOptionsItemSelected(MenuItem r9) {
        /*
            r8 = this;
            int r0 = r9.getItemId()
            r1 = 0
            r2 = 1
            switch(r0) {
                case 16908332: goto L_0x0105;
                case 2131296281: goto L_0x00ed;
                case 2131296289: goto L_0x008c;
                case 2131296303: goto L_0x000b;
                default: goto L_0x0009;
            }
        L_0x0009:
            goto L_0x0109
        L_0x000b:
            com.ezscreenrecorder.activities.ShowImagesActivity$ImageListAdapter r9 = r8.mImageListAdapter
            com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager r0 = r8.mViewPager
            int r0 = r0.getCurrentPosition()
            com.ezscreenrecorder.activities.ShowImagesActivity$ShowImage r9 = r9.getItem(r0)
            if (r9 != 0) goto L_0x001a
            return r1
        L_0x001a:
            android.content.Intent r0 = new android.content.Intent
            java.lang.String r3 = "android.intent.action.SEND"
            r0.<init>(r3)
            r3 = 2131689945(0x7f0f01d9, float:1.900892E38)
            java.lang.String r4 = "android.intent.extra.SUBJECT"
            r0.putExtra(r4, r3)
            boolean r4 = r8.isServer
            if (r4 == 0) goto L_0x006f
            java.lang.String r9 = "text/plain"
            r0.setType(r9)
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            r1 = 2131689941(0x7f0f01d5, float:1.9008912E38)
            java.lang.String r1 = r8.getString(r1)
            r9.append(r1)
            java.lang.String r1 = " "
            r9.append(r1)
            java.lang.String r1 = r8.sharePath
            r9.append(r1)
            java.lang.String r1 = " .\n"
            r9.append(r1)
            r1 = 2131689514(0x7f0f002a, float:1.9008046E38)
            java.lang.String r1 = r8.getString(r1)
            r9.append(r1)
            java.lang.String r9 = r9.toString()
            java.lang.String r1 = "android.intent.extra.TEXT"
            r0.putExtra(r1, r9)
            java.lang.String r9 = r8.getString(r3)
            android.content.Intent r9 = android.content.Intent.createChooser(r0, r9)
            r8.startActivity(r9)
            goto L_0x0082
        L_0x006f:
            android.content.Context r3 = r8.getApplicationContext()
            java.lang.String[] r4 = new java.lang.String[r2]
            java.lang.String r9 = r9.path
            r4[r1] = r9
            r9 = 0
            com.ezscreenrecorder.activities.ShowImagesActivity$12 r1 = new com.ezscreenrecorder.activities.ShowImagesActivity$12
            r1.<init>(r0)
            android.media.MediaScannerConnection.scanFile(r3, r4, r9, r1)
        L_0x0082:
            com.ezscreenrecorder.utils.FirebaseEventsNewHelper r9 = com.ezscreenrecorder.utils.FirebaseEventsNewHelper.getInstance()
            java.lang.String r0 = "Image"
            r9.sendShareEvent(r0)
            return r2
        L_0x008c:
            com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager r0 = r8.mViewPager
            r3 = -1
            if (r0 == 0) goto L_0x0096
            int r0 = r0.getCurrentPosition()
            goto L_0x0097
        L_0x0096:
            r0 = -1
        L_0x0097:
            if (r0 == r3) goto L_0x00a4
            com.ezscreenrecorder.activities.ShowImagesActivity$ImageListAdapter r3 = r8.mImageListAdapter
            if (r3 == 0) goto L_0x00a4
            com.ezscreenrecorder.activities.ShowImagesActivity$ShowImage r0 = r3.getItem(r0)
            java.lang.String r0 = r0.path
            goto L_0x00a6
        L_0x00a4:
            java.lang.String r0 = ""
        L_0x00a6:
            boolean r3 = android.text.TextUtils.isEmpty(r0)
            if (r3 != 0) goto L_0x0109
            boolean r3 = r8.isServer
            java.lang.String r4 = "ImageFromOtherApp"
            java.lang.String r5 = "image"
            if (r3 == 0) goto L_0x00ce
            android.content.Intent r3 = new android.content.Intent
            android.content.Context r6 = r8.getApplicationContext()
            java.lang.Class<com.ezscreenrecorder.imgedit.ImageEditActivity> r7 = com.ezscreenrecorder.imgedit.ImageEditActivity.class
            r3.<init>(r6, r7)
            r3.putExtra(r5, r0)
            java.lang.String r0 = "fromServer"
            r3.putExtra(r0, r2)
            r3.putExtra(r4, r1)
            r8.startActivity(r3)
            goto L_0x00e9
        L_0x00ce:
            android.content.Intent r1 = new android.content.Intent
            android.content.Context r2 = r8.getApplicationContext()
            java.lang.Class<com.ezscreenrecorder.imgedit.ImageEditActivity> r3 = com.ezscreenrecorder.imgedit.ImageEditActivity.class
            r1.<init>(r2, r3)
            r1.putExtra(r5, r0)
            boolean r0 = r8.isPathFromFile
            r1.putExtra(r4, r0)
            r0 = 33554432(0x2000000, float:9.403955E-38)
            r1.addFlags(r0)
            r8.startActivity(r1)
        L_0x00e9:
            android.support.p000v4.app.ActivityCompat.finishAfterTransition(r8)
            goto L_0x0109
        L_0x00ed:
            r9 = 1511(0x5e7, float:2.117E-42)
            com.ezscreenrecorder.alertdialogs.DeleteConfirmationDialog r9 = com.ezscreenrecorder.alertdialogs.DeleteConfirmationDialog.getInstance(r9)
            com.ezscreenrecorder.activities.ShowImagesActivity$11 r0 = new com.ezscreenrecorder.activities.ShowImagesActivity$11
            r0.<init>()
            r9.setDialogResultListener(r0)
            android.support.v4.app.FragmentManager r0 = r8.getSupportFragmentManager()
            java.lang.String r1 = "delete_image_img_preview"
            r9.show(r0, r1)
            return r2
        L_0x0105:
            r8.onBackPressed()
            return r2
        L_0x0109:
            boolean r9 = super.onOptionsItemSelected(r9)
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ezscreenrecorder.activities.ShowImagesActivity.onOptionsItemSelected(android.view.MenuItem):boolean");
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        if (getSupportActionBar().isShowing()) {
            hideActionBar();
            getWindow().getDecorView().setSystemUiVisibility(2054);
        }
    }
}
