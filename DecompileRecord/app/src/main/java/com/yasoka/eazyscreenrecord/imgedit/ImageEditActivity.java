package com.yasoka.eazyscreenrecord.imgedit;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
/*import android.support.p000v4.app.Fragment;
import android.support.p000v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.p000v4.content.FileProvider;
import android.support.p000v4.view.GravityCompat;
import android.support.p003v7.app.AlertDialog;
import android.support.p003v7.app.AppCompatActivity;
import android.support.p003v7.widget.Toolbar;
import android.support.p003v7.widget.Toolbar.LayoutParams;*/
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
/*import com.ezscreenrecorder.C0793R;
import com.ezscreenrecorder.FloatingService;
import com.ezscreenrecorder.activities.GalleryActivity;
import com.ezscreenrecorder.imgedit.fragments.CropFragment;
import com.ezscreenrecorder.imgedit.fragments.DrawFragment;
import com.ezscreenrecorder.imgedit.fragments.FiltersFragment;
import com.ezscreenrecorder.imgedit.fragments.MainImageFragment;
import com.ezscreenrecorder.imgedit.fragments.TextFragment;
import com.ezscreenrecorder.utils.AppUtils;
import com.ezscreenrecorder.utils.FilesAccessHelper;
import com.ezscreenrecorder.utils.FirebaseEventsNewHelper;
import com.ezscreenrecorder.utils.StorageHelper;*/
import com.yasoka.eazyscreenrecord.C0793R;
import com.yasoka.eazyscreenrecord.FloatingService;
import com.yasoka.eazyscreenrecord.activities.GalleryActivity;
import com.yasoka.eazyscreenrecord.imgedit.fragments.CropFragment;
import com.yasoka.eazyscreenrecord.imgedit.fragments.DrawFragment;
import com.yasoka.eazyscreenrecord.imgedit.fragments.FiltersFragment;
import com.yasoka.eazyscreenrecord.imgedit.fragments.MainImageFragment;
import com.yasoka.eazyscreenrecord.imgedit.fragments.TextFragment;
import com.yasoka.eazyscreenrecord.utils.AppUtils;
import com.yasoka.eazyscreenrecord.utils.FilesAccessHelper;
import com.yasoka.eazyscreenrecord.utils.FirebaseEventsNewHelper;
import com.yasoka.eazyscreenrecord.utils.StorageHelper;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request.Builder;
import okhttp3.Response;
/*import p009io.reactivex.Single;
import p009io.reactivex.SingleEmitter;
import p009io.reactivex.SingleObserver;
import p009io.reactivex.SingleOnSubscribe;
import p009io.reactivex.android.schedulers.AndroidSchedulers;
import p009io.reactivex.observers.DisposableSingleObserver;
import p009io.reactivex.schedulers.Schedulers;*/

public class ImageEditActivity extends AppCompatActivity {
    public static final String EXTRA_IMAGE_PATH = "image";
    public static final String EXTRA_IS_FROM_OTHER_APP = "ImageFromOtherApp";
    public static final String EXTRA_IS_FROM_SERVER = "fromServer";
    public String data;
    /* access modifiers changed from: private */
    public File dir;
    private DisposableSingleObserver<File> disposableSingleObserver;
    /* access modifiers changed from: private */
    public final DateFormat fileFormat2 = new SimpleDateFormat("yyyyMMdd-HHmmss'.jpg'", Locale.US);
    /* access modifiers changed from: private */
    public boolean fromServer;
    public List<String> imageHistoryList = new ArrayList();
    /* access modifiers changed from: private */
    public boolean isImgFromOtherApp = false;
    private ProgressDialog progressDialog;
    private TextView toolbarText;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        String str = ".EZ-ImageEdit";
        super.onCreate(bundle);
        setContentView((int) C0793R.layout.activity_image_edit);
        this.data = getIntent().getStringExtra("image");
        this.fromServer = getIntent().getBooleanExtra(EXTRA_IS_FROM_SERVER, false);
        Intent intent = getIntent();
        String str2 = EXTRA_IS_FROM_OTHER_APP;
        if (intent.hasExtra(str2)) {
            this.isImgFromOtherApp = getIntent().getBooleanExtra(str2, false);
        }
        if (this.data == null) {
            Toast.makeText(this, C0793R.string.id_error_file_path_msg, 1).show();
            finish();
            return;
        }
        try {
            this.dir = new File(AppUtils.getImageDir(this), str);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            this.dir = new File(StorageHelper.getInstance().getFileBasePath(), str);
        }
        if (!this.dir.exists()) {
            this.dir.mkdirs();
        }
        final Toolbar toolbar = (Toolbar) findViewById(C0793R.C0795id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle((CharSequence) "");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon((int) C0793R.C0794drawable.ic_cross);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (ImageEditActivity.this.getSupportFragmentManager().getBackStackEntryCount() > 1) {
                    ImageEditActivity.this.getSupportFragmentManager().popBackStackImmediate();
                } else {
                    ImageEditActivity.this.finish();
                }
            }
        });
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                if (ImageEditActivity.this.getCurrentFragment() instanceof MainImageFragment) {
                    toolbar.setNavigationIcon((Drawable) null);
                } else {
                    toolbar.setNavigationIcon((int) C0793R.C0794drawable.ic_cross);
                }
            }
        });
        this.toolbarText = (TextView) toolbar.findViewById(C0793R.C0795id.txt_toolbar_title);
        final LinearLayout linearLayout = (LinearLayout) findViewById(C0793R.C0795id.progress_view);
        linearLayout.setVisibility(0);
        this.disposableSingleObserver = new DisposableSingleObserver<File>() {
            public void onSuccess(File file) {
                FirebaseEventsNewHelper.getInstance().sendScreenshotEditSuccessEvent();
                Uri fromFile = Uri.fromFile(file);
                Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                intent.setData(fromFile);
                ImageEditActivity.this.sendBroadcast(intent);
                linearLayout.setVisibility(View.GONE);
                ImageEditActivity.this.imageHistoryList.add(file.getAbsolutePath());
                ImageEditActivity.this.replaceFragment(MainImageFragment.class, "", null);
            }

            public void onError(Throwable th) {
                if (!isDisposed()) {
                    try {
                        linearLayout.setVisibility(View.GONE);
                        Toast.makeText(ImageEditActivity.this.getApplicationContext(), C0793R.string.id_error_view_image_message, 1).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Single.create(new SingleOnSubscribe<File>() {
            public void subscribe(final SingleEmitter<File> singleEmitter) throws Exception {
                if (ImageEditActivity.this.fromServer) {
                    new OkHttpClient().newCall(new Builder().url(ImageEditActivity.this.data).build()).enqueue(new Callback() {
                        public void onFailure(Call call, IOException iOException) {
                            PrintStream printStream = System.out;
                            StringBuilder sb = new StringBuilder();
                            sb.append("request failed: ");
                            sb.append(iOException.getMessage());
                            printStream.println(sb.toString());
                            if (!singleEmitter.isDisposed()) {
                                singleEmitter.onError(iOException);
                            }
                        }

                        public void onResponse(Call call, Response response) throws IOException {
                            if (!singleEmitter.isDisposed()) {
                                BufferedInputStream bufferedInputStream = new BufferedInputStream(response.body().byteStream(), 8192);
                                File access$100 = ImageEditActivity.this.dir;
                                StringBuilder sb = new StringBuilder();
                                sb.append("edit_image_");
                                sb.append(System.currentTimeMillis());
                                sb.append(".jpg");
                                File file = new File(access$100, sb.toString());
                                String absolutePath = file.getAbsolutePath();
                                FileOutputStream fileOutputStream = new FileOutputStream(file);
                                byte[] bArr = new byte[4096];
                                while (true) {
                                    int read = bufferedInputStream.read(bArr);
                                    if (read == -1) {
                                        break;
                                    }
                                    fileOutputStream.write(bArr, 0, read);
                                }
                                File file2 = new File(absolutePath);
                                fileOutputStream.flush();
                                fileOutputStream.close();
                                bufferedInputStream.close();
                                if (!singleEmitter.isDisposed()) {
                                    singleEmitter.onSuccess(file2);
                                }
                            }
                        }
                    });
                    return;
                }
                String substring = ImageEditActivity.this.data.substring(ImageEditActivity.this.data.lastIndexOf("."));
                File access$100 = ImageEditActivity.this.dir;
                StringBuilder sb = new StringBuilder();
                sb.append("edit_image_");
                sb.append(System.currentTimeMillis());
                sb.append(substring);
                File file = new File(access$100, sb.toString());
                ImageEditActivity imageEditActivity = ImageEditActivity.this;
                imageEditActivity.copyFile(new File(imageEditActivity.data), file);
                if (!singleEmitter.isDisposed()) {
                    singleEmitter.onSuccess(file);
                }
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe((SingleObserver<? super T>) this.disposableSingleObserver);
        this.progressDialog = new ProgressDialog(this, 2131755398);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0793R.C0797menu.menu_main_img_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        if (getSupportFragmentManager() == null || this.imageHistoryList == null || getSupportFragmentManager().getBackStackEntryCount() != 1 || this.imageHistoryList.size() <= 1) {
            menu.findItem(C0793R.C0795id.action_share).setVisible(false);
            return false;
        }
        menu.findItem(C0793R.C0795id.action_share).setVisible(true);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == C0793R.C0795id.action_share) {
            List<String> list = this.imageHistoryList;
            if (list != null && list.size() > 1) {
                List<String> list2 = this.imageHistoryList;
                String str = (String) list2.get(list2.size() - 1);
                if (!TextUtils.isEmpty(str)) {
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.setType("image/jpeg");
                    intent.putExtra("android.intent.extra.SUBJECT", getString(C0793R.string.share_image));
                    intent.putExtra("android.intent.extra.TEXT", getString(C0793R.string.share_image_txt));
                    intent.addFlags(1);
                    Context applicationContext = getApplicationContext();
                    StringBuilder sb = new StringBuilder();
                    sb.append(getPackageName());
                    sb.append(".my.package.name.provider");
                    intent.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(applicationContext, sb.toString(), new File(str)));
                    startActivity(Intent.createChooser(intent, getString(C0793R.string.share_image)));
                    FirebaseEventsNewHelper.getInstance().sendShareEvent("Image");
                }
            }
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void copyFile(File file, File file2) throws IOException {
        if (file.exists()) {
            FileChannel channel = new FileInputStream(file).getChannel();
            FileChannel channel2 = new FileOutputStream(file2).getChannel();
            if (channel != null) {
                channel2.transferFrom(channel, 0, channel.size());
            }
            if (channel != null) {
                channel.close();
            }
            channel2.close();
            FilesAccessHelper.getInstance().refreshGallery(getApplicationContext(), file2.getAbsolutePath());
        }
    }

    private void deleteAll() {
        for (String file : this.imageHistoryList) {
            File file2 = new File(file);
            String absolutePath = file2.getAbsolutePath();
            file2.delete();
            FilesAccessHelper.getInstance().refreshGallery(getApplicationContext(), absolutePath);
        }
        String absolutePath2 = this.dir.getAbsolutePath();
        this.dir.delete();
        FilesAccessHelper.getInstance().refreshGallery(getApplicationContext(), absolutePath2);
    }

    public void setMyTitle(String str) {
        this.toolbarText.setText(str);
    }

    public void setTitleStartAligned(boolean z) {
        this.toolbarText.setLayoutParams(getTitleLayoutParams(z));
    }

    private WindowManager.LayoutParams getTitleLayoutParams(boolean z) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-2, -2);
        if (z) {
            layoutParams.gravity = GravityCompat.START;
        } else {
            layoutParams.gravity = 17;
        }
        return layoutParams;
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        DisposableSingleObserver<File> disposableSingleObserver2 = this.disposableSingleObserver;
        if (disposableSingleObserver2 != null) {
            disposableSingleObserver2.dispose();
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        DisposableSingleObserver<File> disposableSingleObserver2 = this.disposableSingleObserver;
        if (disposableSingleObserver2 != null) {
            disposableSingleObserver2.dispose();
        }
        deleteAll();
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x003a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void replaceFragment(Class r3, String r4, Bundle r5) {
        /*
            r2 = this;
            java.lang.String r0 = r3.getSimpleName()
            if (r4 == 0) goto L_0x0015
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r0)
            r1.append(r4)
            java.lang.String r0 = r1.toString()
        L_0x0015:
            android.support.v4.app.FragmentManager r4 = r2.getSupportFragmentManager()
            r1 = 0
            boolean r4 = r4.popBackStackImmediate(r0, r1)
            if (r4 != 0) goto L_0x005a
            android.support.v4.app.FragmentManager r4 = r2.getSupportFragmentManager()
            android.support.v4.app.Fragment r4 = r4.findFragmentByTag(r0)
            if (r4 != 0) goto L_0x0037
            java.lang.Object r3 = r3.newInstance()     // Catch:{ InstantiationException -> 0x0033, IllegalAccessException -> 0x0031 }
            android.support.v4.app.Fragment r3 = (android.support.p000v4.app.Fragment) r3     // Catch:{ InstantiationException -> 0x0033, IllegalAccessException -> 0x0031 }
            goto L_0x0038
        L_0x0031:
            r3 = move-exception
            goto L_0x0034
        L_0x0033:
            r3 = move-exception
        L_0x0034:
            r3.printStackTrace()
        L_0x0037:
            r3 = r4
        L_0x0038:
            if (r3 == 0) goto L_0x005a
            if (r5 == 0) goto L_0x003f
            r3.setArguments(r5)
        L_0x003f:
            android.support.v4.app.FragmentManager r4 = r2.getSupportFragmentManager()
            android.support.v4.app.FragmentTransaction r4 = r4.beginTransaction()
            android.support.v4.app.Fragment r5 = r2.getCurrentFragment()
            if (r5 != r3) goto L_0x004e
            return
        L_0x004e:
            r5 = 2131296409(0x7f090099, float:1.8210734E38)
            r4.replace(r5, r3, r0)
            r4.addToBackStack(r0)
            r4.commitAllowingStateLoss()
        L_0x005a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ezscreenrecorder.imgedit.ImageEditActivity.replaceFragment(java.lang.Class, java.lang.String, android.os.Bundle):void");
    }

    public Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(C0793R.C0795id.container_edit);
    }

    public void onBackPressed() {
        if (getCurrentFragment() == null) {
            finish();
        } else if ((getCurrentFragment() instanceof MainImageFragment) && this.imageHistoryList.size() > 1) {
            new AlertDialog.Builder(this).setTitle((int) C0793R.string.id_quit_from_main_img_filters_msg).setPositiveButton((int) C0793R.string.yes, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    ImageEditActivity.this.finish();
                }
            }).setNegativeButton((int) C0793R.string.f82no, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).create().show();
        } else if ((getCurrentFragment() instanceof DrawFragment) || (getCurrentFragment() instanceof TextFragment) || (getCurrentFragment() instanceof CropFragment)) {
            final AlertDialog create = new AlertDialog.Builder(this).setTitle((int) C0793R.string.id_quit_from_sub_img_filters_msg).setPositiveButton((int) C0793R.string.yes, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (ImageEditActivity.this.getSupportFragmentManager().getBackStackEntryCount() > 1) {
                        ImageEditActivity.this.getSupportFragmentManager().popBackStack();
                    }
                }
            }).setNegativeButton((int) C0793R.string.f82no, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).create();
            create.setOnShowListener(new OnShowListener() {
                public void onShow(DialogInterface dialogInterface) {
                    create.getButton(-1).setAllCaps(false);
                    create.getButton(-2).setAllCaps(false);
                }
            });
            create.show();
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStackImmediate();
        } else {
            finish();
        }
    }

    public void save() {
        Toast.makeText(this, C0793R.string.id_saving_txt, 1).show();
        showLoading();
        Single.create(new SingleOnSubscribe<String>() {
            public void subscribe(SingleEmitter<String> singleEmitter) throws Exception {
                File file = new File(AppUtils.getImageDir(ImageEditActivity.this.getApplicationContext()), ImageEditActivity.this.fileFormat2.format(new Date()));
                ImageEditActivity.this.copyFile(new File((String) ImageEditActivity.this.imageHistoryList.get(ImageEditActivity.this.imageHistoryList.size() - 1)), file);
                for (String file2 : ImageEditActivity.this.imageHistoryList) {
                    File file3 = new File(file2);
                    String absolutePath = file3.getAbsolutePath();
                    file3.delete();
                    FilesAccessHelper.getInstance().refreshGallery(ImageEditActivity.this.getApplicationContext(), absolutePath);
                }
                singleEmitter.onSuccess(file.getAbsolutePath());
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe((SingleObserver<? super T>) new DisposableSingleObserver<String>() {
            public void onSuccess(String str) {
                ImageEditActivity.this.hideLoading();
                if (ImageEditActivity.this.isImgFromOtherApp) {
                    Intent intent = new Intent(ImageEditActivity.this, GalleryActivity.class);
                    intent.putExtra(FloatingService.GALLERY_TYPE_IMAGE, true);
                    ImageEditActivity.this.startActivity(intent);
                } else {
                    if (ImageEditActivity.this.fromServer) {
                        Toast.makeText(ImageEditActivity.this.getApplicationContext(), C0793R.string.edit_img_from_server, 1).show();
                    }
                    Intent intent2 = new Intent();
                    intent2.putExtra("imageEdit", str);
                    intent2.putExtra(ImageEditActivity.EXTRA_IS_FROM_SERVER, ImageEditActivity.this.fromServer);
                    ImageEditActivity.this.setResult(-1, intent2);
                }
                ImageEditActivity.this.finish();
            }

            public void onError(Throwable th) {
                ImageEditActivity.this.hideLoading();
            }
        });
    }

    public String getCurrentImage() {
        if (this.imageHistoryList.size() == 0) {
            return null;
        }
        List<String> list = this.imageHistoryList;
        return (String) list.get(list.size() - 1);
    }

    public String getNewOutputPath(String str) {
        String substring = str.substring(str.lastIndexOf("."));
        if (getCurrentFragment() instanceof CropFragment) {
            File file = this.dir;
            StringBuilder sb = new StringBuilder();
            sb.append("crop_image_");
            sb.append(System.currentTimeMillis());
            sb.append(substring);
            return new File(file, sb.toString()).getAbsolutePath();
        } else if (getCurrentFragment() instanceof DrawFragment) {
            File file2 = this.dir;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("draw_image_");
            sb2.append(System.currentTimeMillis());
            sb2.append(substring);
            return new File(file2, sb2.toString()).getAbsolutePath();
        } else if (getCurrentFragment() instanceof TextFragment) {
            File file3 = this.dir;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("text_image_");
            sb3.append(System.currentTimeMillis());
            sb3.append(substring);
            return new File(file3, sb3.toString()).getAbsolutePath();
        } else if (!(getCurrentFragment() instanceof FiltersFragment)) {
            return null;
        } else {
            File file4 = this.dir;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("filter_image_");
            sb4.append(System.currentTimeMillis());
            sb4.append(substring);
            return new File(file4, sb4.toString()).getAbsolutePath();
        }
    }

    public void showLoading() {
        this.progressDialog.show();
    }

    public void hideLoading() {
        this.progressDialog.dismiss();
    }

    public void addImage(String str) {
        this.imageHistoryList.add(str);
    }
}
