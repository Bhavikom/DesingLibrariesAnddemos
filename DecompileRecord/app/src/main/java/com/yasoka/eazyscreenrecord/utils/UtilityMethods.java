package com.yasoka.eazyscreenrecord.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaExtractor;
import android.support.p000v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import com.ezscreenrecorder.model.MetadataInfoModel;
import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.TimeUnit;
import p009io.reactivex.Single;
import p009io.reactivex.functions.Function;

public class UtilityMethods {
    public static final int MEDIA_TYPE_AUDIO = 1640;
    public static final int MEDIA_TYPE_VIDEO = 1641;
    private static final UtilityMethods ourInstance = new UtilityMethods();

    @Retention(RetentionPolicy.SOURCE)
    @interface MediaType {
    }

    public static UtilityMethods getInstance() {
        return ourInstance;
    }

    private UtilityMethods() {
    }

    public String getFileSizeFromPath(String str) {
        String str2 = "";
        try {
            if (TextUtils.isEmpty(str)) {
                return str2;
            }
            File file = new File(str);
            if (!file.exists()) {
                return str2;
            }
            return String.format("%sMB", new Object[]{Double.valueOf(((double) Math.round((float) (((file.length() * 100) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID))) / 100.0d)});
        } catch (Exception unused) {
            return str2;
        }
    }

    public String getImageResolutionFromPath(String str) {
        String str2 = "";
        try {
            if (TextUtils.isEmpty(str)) {
                return str2;
            }
            Bitmap decodeFile = BitmapFactory.decodeFile(str);
            String valueOf = String.valueOf(decodeFile.getHeight());
            String valueOf2 = String.valueOf(decodeFile.getWidth());
            StringBuilder sb = new StringBuilder();
            sb.append(valueOf);
            sb.append("x");
            sb.append(valueOf2);
            return sb.toString();
        } catch (Exception unused) {
            return str2;
        }
    }

    public Single<MetadataInfoModel> getMediaMetadataFromPath(final String str, final int i) {
        return Single.just(Boolean.valueOf(true)).delay(5, TimeUnit.SECONDS).map(new Function<Boolean, MediaExtractor>() {
            public MediaExtractor apply(Boolean bool) throws Exception {
                File file = new File(str);
                if (!file.exists() || !file.canRead()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("File Not exist ");
                    sb.append(str);
                    throw new RuntimeException(sb.toString());
                }
                MediaExtractor mediaExtractor = new MediaExtractor();
                mediaExtractor.setDataSource(file.toString());
                return mediaExtractor;
            }
        }).delay(1000, TimeUnit.MILLISECONDS).map(new Function<MediaExtractor, MetadataInfoModel>() {
            public MetadataInfoModel apply(MediaExtractor mediaExtractor) throws Exception {
                int i = 0;
                while (true) {
                    if (i >= mediaExtractor.getTrackCount()) {
                        i = -1;
                        break;
                    }
                    String string = mediaExtractor.getTrackFormat(i).getString("mime");
                    int i2 = i;
                    if (i2 != 1641) {
                        if (i2 == 1640 && string.startsWith("audio/")) {
                            break;
                        }
                    } else if (string.startsWith("video/")) {
                        break;
                    }
                    i++;
                }
                if (i != -1) {
                    mediaExtractor.selectTrack(i);
                    long j = mediaExtractor.getTrackFormat(i).getLong("durationUs");
                    MetadataInfoModel metadataInfoModel = new MetadataInfoModel();
                    metadataInfoModel.setDurationInSec(String.valueOf(TimeUnit.MICROSECONDS.toSeconds(j)));
                    metadataInfoModel.setSizeInMb(UtilityMethods.getInstance().getFileSizeFromPath(str));
                    return metadataInfoModel;
                }
                StringBuilder sb = new StringBuilder();
                sb.append("No video track found in ");
                sb.append(str);
                throw new RuntimeException(sb.toString());
            }
        });
    }
}
