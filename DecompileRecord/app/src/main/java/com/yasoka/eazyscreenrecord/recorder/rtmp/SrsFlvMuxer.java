package com.yasoka.eazyscreenrecord.recorder.rtmp;

import android.media.MediaCodec.BufferInfo;
import android.os.Process;
import android.util.Log;
import com.github.faucamp.simplertmp.DefaultRtmpPublisher;
import com.google.common.base.Ascii;
import com.google.common.primitives.SignedBytes;
import com.google.common.primitives.UnsignedBytes;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import net.ossrs.rtmp.SrsAllocator.Allocation;

public class SrsFlvMuxer {
    private static final int AUDIO_ALLOC_SIZE = 4096;
    private static final String TAG = "SrsFlvMuxer";
    private static final int VIDEO_ALLOC_SIZE = 131072;
    /* access modifiers changed from: private */
    public ConnectCheckerRtmp connectCheckerRtmp;
    private volatile boolean connected = false;
    private SrsFlv flv = new SrsFlv();
    /* access modifiers changed from: private */
    public boolean isPpsSpsSend = false;
    /* access modifiers changed from: private */
    public SrsAllocator mAudioAllocator = new SrsAllocator(4096);
    /* access modifiers changed from: private */
    public SrsFlvFrame mAudioSequenceHeader;
    /* access modifiers changed from: private */
    public BlockingQueue<SrsFlvFrame> mFlvTagCache = new LinkedBlockingQueue(30);
    /* access modifiers changed from: private */
    public SrsAllocator mVideoAllocator = new SrsAllocator(131072);
    /* access modifiers changed from: private */
    public SrsFlvFrame mVideoSequenceHeader;
    /* access modifiers changed from: private */
    public boolean needToFindKeyFrame = true;
    /* access modifiers changed from: private */
    public byte profileIop = 0;
    private DefaultRtmpPublisher publisher;
    /* access modifiers changed from: private */
    public int sampleRate = 0;
    /* access modifiers changed from: private */
    public Thread worker;

    private class SrsAacObjectType {
        public static final int AacLC = 2;

        private SrsAacObjectType() {
        }
    }

    private class SrsAnnexbSearch {
        public boolean match;
        public int nb_start_code;

        private SrsAnnexbSearch() {
            this.nb_start_code = 0;
            this.match = false;
        }
    }

    private class SrsAvcNaluType {
        public static final int AccessUnitDelimiter = 9;
        public static final int CodedSliceExt = 20;
        public static final int DataPartitionA = 2;
        public static final int DataPartitionB = 3;
        public static final int DataPartitionC = 4;
        public static final int EOSequence = 10;
        public static final int EOStream = 11;
        public static final int FilterData = 12;
        public static final int IDR = 5;
        public static final int LayerWithoutPartition = 19;
        public static final int NonIDR = 1;
        public static final int PPS = 8;
        public static final int PrefixNALU = 14;
        public static final int Reserved = 0;
        public static final int SEI = 6;
        public static final int SPS = 7;
        public static final int SPSExt = 13;
        public static final int SubsetSPS = 15;

        private SrsAvcNaluType() {
        }
    }

    private class SrsCodecAudioSampleRate {
        public static final int R11025 = 11025;
        public static final int R16000 = 16000;
        public static final int R22050 = 22050;
        public static final int R32000 = 32000;
        public static final int R44100 = 44100;
        public static final int R5512 = 5512;

        private SrsCodecAudioSampleRate() {
        }
    }

    private class SrsCodecFlvTag {
        public static final int Audio = 8;
        public static final int Video = 9;

        private SrsCodecFlvTag() {
        }
    }

    private class SrsCodecVideo {
        public static final int AVC = 7;

        private SrsCodecVideo() {
        }
    }

    private class SrsCodecVideoAVCFrame {
        public static final int InterFrame = 2;
        public static final int KeyFrame = 1;

        private SrsCodecVideoAVCFrame() {
        }
    }

    private class SrsCodecVideoAVCType {
        public static final int NALU = 1;
        public static final int SequenceHeader = 0;

        private SrsCodecVideoAVCType() {
        }
    }

    private class SrsFlv {
        private ByteBuffer Pps;
        private ByteBuffer Sps;
        private boolean aac_specific_config_got;
        private int achannel;
        private Allocation audio_tag;
        private SrsRawH264Stream avc = new SrsRawH264Stream();
        private ArrayList<SrsFlvFrameBytes> ipbs = new ArrayList<>();
        private Allocation video_tag;

        public SrsFlv() {
            reset();
        }

        public void setAchannel(int i) {
            this.achannel = i;
        }

        public void reset() {
            this.Sps = null;
            this.Pps = null;
            SrsFlvMuxer.this.isPpsSpsSend = false;
            this.aac_specific_config_got = false;
        }

        public void writeAudioSample(ByteBuffer byteBuffer, BufferInfo bufferInfo) {
            byte b;
            int i;
            int i2 = (int) (bufferInfo.presentationTimeUs / 1000);
            this.audio_tag = SrsFlvMuxer.this.mAudioAllocator.allocate(bufferInfo.size + 2);
            int i3 = 3;
            if (!this.aac_specific_config_got) {
                if (bufferInfo.flags == 2) {
                    i = byteBuffer.get(0) & 248;
                } else {
                    i = (byteBuffer.get(0) & 248) / 2;
                }
                byte b2 = (byte) i;
                int i4 = SrsFlvMuxer.this.sampleRate == 22050 ? 7 : SrsFlvMuxer.this.sampleRate == 11025 ? 10 : SrsFlvMuxer.this.sampleRate == 32000 ? 5 : SrsFlvMuxer.this.sampleRate == 16000 ? 8 : 4;
                this.audio_tag.put((byte) (b2 | ((i4 >> 1) & 7)), 2);
                this.audio_tag.put((byte) (((byte) ((i4 << 7) & 128)) | (((this.achannel == 2 ? 2 : 1) << 3) & 120)), 3);
                this.aac_specific_config_got = true;
                writeAdtsHeader(this.audio_tag.array(), 4);
                this.audio_tag.appendOffset(7);
                b = 0;
            } else {
                byteBuffer.get(this.audio_tag.array(), 2, bufferInfo.size);
                this.audio_tag.appendOffset(bufferInfo.size + 2);
                b = 1;
            }
            int i5 = this.achannel == 2 ? 1 : 0;
            if (SrsFlvMuxer.this.sampleRate == 22050) {
                i3 = 2;
            } else if (SrsFlvMuxer.this.sampleRate == 11025) {
                i3 = 1;
            }
            this.audio_tag.put((byte) (((byte) (((byte) (((byte) (i5 & 1)) | 2)) | ((i3 << 2) & Ascii.f350FF))) | 160), 0);
            this.audio_tag.put(b, 1);
            writeRtmpPacket(8, i2, 0, b, this.audio_tag);
        }

        private void writeAdtsHeader(byte[] bArr, int i) {
            bArr[i] = -1;
            int i2 = i + 1;
            bArr[i2] = -16;
            bArr[i2] = (byte) (bArr[i2] | 0);
            bArr[i2] = (byte) (bArr[i2] | 0);
            bArr[i2] = (byte) (bArr[i2] | 1);
            int i3 = i + 2;
            bArr[i3] = SignedBytes.MAX_POWER_OF_TWO;
            bArr[i3] = (byte) (bArr[i3] | Ascii.DLE);
            bArr[i3] = (byte) (bArr[i3] | 0);
            int i4 = i + 3;
            bArr[i4] = UnsignedBytes.MAX_POWER_OF_TWO;
            bArr[i4] = (byte) (bArr[i4] | 0);
            bArr[i4] = (byte) (bArr[i4] | 0);
            bArr[i4] = (byte) (bArr[i4] | 0);
            bArr[i4] = (byte) (bArr[i4] | 0);
            bArr[i4] = (byte) (bArr[i4] | (((bArr.length - 2) & 6144) >> 11));
            bArr[i + 4] = (byte) (((bArr.length - 2) & 2040) >> 3);
            int i5 = i + 5;
            bArr[i5] = (byte) (((bArr.length - 2) & 7) << 5);
            bArr[i5] = (byte) (bArr[i5] | Ascii.f360US);
            int i6 = i + 6;
            bArr[i6] = -4;
            bArr[i6] = (byte) (bArr[i6] | 0);
        }

        public void writeVideoSample(ByteBuffer byteBuffer, BufferInfo bufferInfo) {
            if (bufferInfo.size >= 4) {
                int i = (int) (bufferInfo.presentationTimeUs / 1000);
                int i2 = 2;
                SrsFlvFrameBytes demuxAnnexb = this.avc.demuxAnnexb(byteBuffer, bufferInfo.size, true);
                byte b = demuxAnnexb.data.get(0) & Ascii.f360US;
                if (b == 5) {
                    i2 = 1;
                } else if (b == 7 || b == 8) {
                    SrsFlvFrameBytes demuxAnnexb2 = this.avc.demuxAnnexb(byteBuffer, bufferInfo.size, false);
                    demuxAnnexb.size = (demuxAnnexb.size - demuxAnnexb2.size) - 4;
                    if (!demuxAnnexb.data.equals(this.Sps)) {
                        byte[] bArr = new byte[demuxAnnexb.size];
                        demuxAnnexb.data.get(bArr);
                        SrsFlvMuxer.this.isPpsSpsSend = false;
                        this.Sps = ByteBuffer.wrap(bArr);
                    }
                    SrsFlvFrameBytes demuxAnnexb3 = this.avc.demuxAnnexb(byteBuffer, bufferInfo.size, false);
                    if (demuxAnnexb3.size > 0 && 6 == (demuxAnnexb3.data.get(0) & Ascii.f360US)) {
                        demuxAnnexb2.size = (demuxAnnexb2.size - demuxAnnexb3.size) - 3;
                    }
                    if (demuxAnnexb2.size > 0 && !demuxAnnexb2.data.equals(this.Pps)) {
                        byte[] bArr2 = new byte[demuxAnnexb2.size];
                        demuxAnnexb2.data.get(bArr2);
                        SrsFlvMuxer.this.isPpsSpsSend = false;
                        this.Pps = ByteBuffer.wrap(bArr2);
                        writeH264SpsPps(i);
                    }
                    return;
                } else if (b != 1) {
                    return;
                }
                this.ipbs.add(this.avc.muxNaluHeader(demuxAnnexb));
                this.ipbs.add(demuxAnnexb);
                writeH264IpbFrame(this.ipbs, i2, i);
                this.ipbs.clear();
            }
        }

        public void setSpsPPs(ByteBuffer byteBuffer, ByteBuffer byteBuffer2) {
            this.Sps = byteBuffer;
            this.Pps = byteBuffer2;
        }

        private void writeH264SpsPps(int i) {
            if (this.Pps != null && this.Sps != null && !SrsFlvMuxer.this.isPpsSpsSend) {
                ArrayList arrayList = new ArrayList();
                this.avc.muxSequenceHeader(this.Sps, this.Pps, arrayList);
                this.video_tag = this.avc.muxFlvTag(arrayList, 1, 0);
                SrsFlvMuxer.this.isPpsSpsSend = true;
                writeRtmpPacket(9, i, 1, 0, this.video_tag);
                Log.i(SrsFlvMuxer.TAG, String.format("flv: h264 sps/pps sent, sps=%dB, pps=%dB", new Object[]{Integer.valueOf(this.Sps.array().length), Integer.valueOf(this.Pps.array().length)}));
            }
        }

        private void writeH264IpbFrame(ArrayList<SrsFlvFrameBytes> arrayList, int i, int i2) {
            if (this.Pps != null && this.Sps != null) {
                this.video_tag = this.avc.muxFlvTag(arrayList, i, 1);
                writeRtmpPacket(9, i2, i, 1, this.video_tag);
            }
        }

        private void writeRtmpPacket(int i, int i2, int i3, int i4, Allocation allocation) {
            SrsFlvFrame srsFlvFrame = new SrsFlvFrame();
            srsFlvFrame.flvTag = allocation;
            srsFlvFrame.type = i;
            srsFlvFrame.dts = i2;
            srsFlvFrame.frame_type = i3;
            srsFlvFrame.avc_aac_type = i4;
            if (srsFlvFrame.is_video()) {
                if (!SrsFlvMuxer.this.needToFindKeyFrame) {
                    flvFrameCacheAdd(srsFlvFrame);
                } else if (srsFlvFrame.is_keyframe()) {
                    SrsFlvMuxer.this.needToFindKeyFrame = false;
                    flvFrameCacheAdd(srsFlvFrame);
                }
            } else if (srsFlvFrame.is_audio()) {
                flvFrameCacheAdd(srsFlvFrame);
            }
        }

        private void flvFrameCacheAdd(SrsFlvFrame srsFlvFrame) {
            try {
                SrsFlvMuxer.this.mFlvTagCache.add(srsFlvFrame);
            } catch (IllegalStateException unused) {
                Log.i(SrsFlvMuxer.TAG, "frame discarded");
            }
        }
    }

    private class SrsFlvFrame {
        public int avc_aac_type;
        public int dts;
        public Allocation flvTag;
        public int frame_type;
        public int type;

        private SrsFlvFrame() {
        }

        public boolean is_keyframe() {
            return is_video() && this.frame_type == 1;
        }

        public boolean is_sequenceHeader() {
            return this.avc_aac_type == 0;
        }

        public boolean is_video() {
            return this.type == 9;
        }

        public boolean is_audio() {
            return this.type == 8;
        }
    }

    private class SrsFlvFrameBytes {
        public ByteBuffer data;
        public int size;

        private SrsFlvFrameBytes() {
        }
    }

    private class SrsRawH264Stream {
        private static final String TAG = "SrsFlvMuxer";
        private SrsAnnexbSearch annexb;
        private SrsFlvFrameBytes nalu_header;
        private SrsFlvFrameBytes pps_bb;
        private SrsFlvFrameBytes pps_hdr;
        private SrsFlvFrameBytes seq_hdr;
        private SrsFlvFrameBytes sps_bb;
        private SrsFlvFrameBytes sps_hdr;

        private SrsRawH264Stream() {
            this.annexb = new SrsAnnexbSearch();
            this.nalu_header = new SrsFlvFrameBytes();
            this.seq_hdr = new SrsFlvFrameBytes();
            this.sps_hdr = new SrsFlvFrameBytes();
            this.sps_bb = new SrsFlvFrameBytes();
            this.pps_hdr = new SrsFlvFrameBytes();
            this.pps_bb = new SrsFlvFrameBytes();
        }

        public boolean isSps(SrsFlvFrameBytes srsFlvFrameBytes) {
            return srsFlvFrameBytes.size >= 1 && (srsFlvFrameBytes.data.get(0) & Ascii.f360US) == 7;
        }

        public boolean isPps(SrsFlvFrameBytes srsFlvFrameBytes) {
            return srsFlvFrameBytes.size >= 1 && (srsFlvFrameBytes.data.get(0) & Ascii.f360US) == 8;
        }

        public SrsFlvFrameBytes muxNaluHeader(SrsFlvFrameBytes srsFlvFrameBytes) {
            if (this.nalu_header.data == null) {
                this.nalu_header.data = ByteBuffer.allocate(4);
                this.nalu_header.size = 4;
            }
            this.nalu_header.data.rewind();
            this.nalu_header.data.putInt(srsFlvFrameBytes.size);
            this.nalu_header.data.rewind();
            return this.nalu_header;
        }

        public void muxSequenceHeader(ByteBuffer byteBuffer, ByteBuffer byteBuffer2, ArrayList<SrsFlvFrameBytes> arrayList) {
            if (this.seq_hdr.data == null) {
                this.seq_hdr.data = ByteBuffer.allocate(5);
                this.seq_hdr.size = 5;
            }
            this.seq_hdr.data.rewind();
            byte b = byteBuffer.get(1);
            byte b2 = byteBuffer.get(3);
            this.seq_hdr.data.put(1);
            this.seq_hdr.data.put(b);
            this.seq_hdr.data.put(SrsFlvMuxer.this.profileIop);
            this.seq_hdr.data.put(b2);
            this.seq_hdr.data.put(3);
            this.seq_hdr.data.rewind();
            arrayList.add(this.seq_hdr);
            if (this.sps_hdr.data == null) {
                this.sps_hdr.data = ByteBuffer.allocate(3);
                this.sps_hdr.size = 3;
            }
            this.sps_hdr.data.rewind();
            this.sps_hdr.data.put(1);
            this.sps_hdr.data.putShort((short) byteBuffer.array().length);
            this.sps_hdr.data.rewind();
            arrayList.add(this.sps_hdr);
            this.sps_bb.size = byteBuffer.array().length;
            this.sps_bb.data = byteBuffer.duplicate();
            arrayList.add(this.sps_bb);
            if (this.pps_hdr.data == null) {
                this.pps_hdr.data = ByteBuffer.allocate(3);
                this.pps_hdr.size = 3;
            }
            this.pps_hdr.data.rewind();
            this.pps_hdr.data.put(1);
            this.pps_hdr.data.putShort((short) byteBuffer2.array().length);
            this.pps_hdr.data.rewind();
            arrayList.add(this.pps_hdr);
            this.pps_bb.size = byteBuffer2.array().length;
            this.pps_bb.data = byteBuffer2.duplicate();
            arrayList.add(this.pps_bb);
        }

        public Allocation muxFlvTag(ArrayList<SrsFlvFrameBytes> arrayList, int i, int i2) {
            int i3 = 5;
            for (int i4 = 0; i4 < arrayList.size(); i4++) {
                i3 += ((SrsFlvFrameBytes) arrayList.get(i4)).size;
            }
            Allocation allocate = SrsFlvMuxer.this.mVideoAllocator.allocate(i3);
            allocate.put((byte) ((i << 4) | 7));
            allocate.put((byte) i2);
            allocate.put((byte) 0);
            allocate.put((byte) 0);
            allocate.put((byte) 0);
            for (int i5 = 0; i5 < arrayList.size(); i5++) {
                SrsFlvFrameBytes srsFlvFrameBytes = (SrsFlvFrameBytes) arrayList.get(i5);
                srsFlvFrameBytes.data.rewind();
                srsFlvFrameBytes.data.get(allocate.array(), allocate.size(), srsFlvFrameBytes.size);
                allocate.appendOffset(srsFlvFrameBytes.size);
            }
            return allocate;
        }

        private SrsAnnexbSearch searchStartcode(ByteBuffer byteBuffer, int i) {
            SrsAnnexbSearch srsAnnexbSearch = this.annexb;
            srsAnnexbSearch.match = false;
            srsAnnexbSearch.nb_start_code = 0;
            if (i - 4 > 0) {
                if (byteBuffer.get(0) == 0 && byteBuffer.get(1) == 0 && byteBuffer.get(2) == 0 && byteBuffer.get(3) == 1) {
                    SrsAnnexbSearch srsAnnexbSearch2 = this.annexb;
                    srsAnnexbSearch2.match = true;
                    srsAnnexbSearch2.nb_start_code = 4;
                } else if (byteBuffer.get(0) == 0 && byteBuffer.get(1) == 0 && byteBuffer.get(2) == 1) {
                    SrsAnnexbSearch srsAnnexbSearch3 = this.annexb;
                    srsAnnexbSearch3.match = true;
                    srsAnnexbSearch3.nb_start_code = 3;
                }
            }
            return this.annexb;
        }

        private SrsAnnexbSearch searchAnnexb(ByteBuffer byteBuffer, int i) {
            SrsAnnexbSearch srsAnnexbSearch = this.annexb;
            srsAnnexbSearch.match = false;
            srsAnnexbSearch.nb_start_code = 0;
            int position = byteBuffer.position();
            while (true) {
                if (position >= i - 4) {
                    break;
                }
                if (byteBuffer.get(position) == 0 && byteBuffer.get(position + 1) == 0) {
                    int i2 = position + 2;
                    if (byteBuffer.get(i2) != 1) {
                        if (byteBuffer.get(i2) == 0 && byteBuffer.get(position + 3) == 1) {
                            SrsAnnexbSearch srsAnnexbSearch2 = this.annexb;
                            srsAnnexbSearch2.match = true;
                            srsAnnexbSearch2.nb_start_code = (position + 4) - byteBuffer.position();
                            break;
                        }
                    } else {
                        SrsAnnexbSearch srsAnnexbSearch3 = this.annexb;
                        srsAnnexbSearch3.match = true;
                        srsAnnexbSearch3.nb_start_code = (position + 3) - byteBuffer.position();
                        break;
                    }
                }
                position++;
            }
            return this.annexb;
        }

        public SrsFlvFrameBytes demuxAnnexb(ByteBuffer byteBuffer, int i, boolean z) {
            SrsFlvFrameBytes srsFlvFrameBytes = new SrsFlvFrameBytes();
            if (byteBuffer.position() < i - 4) {
                SrsAnnexbSearch searchStartcode = z ? searchStartcode(byteBuffer, i) : searchAnnexb(byteBuffer, i);
                if (!searchStartcode.match || searchStartcode.nb_start_code < 3) {
                    Log.e(TAG, "annexb not match.");
                } else {
                    for (int i2 = 0; i2 < searchStartcode.nb_start_code; i2++) {
                        byteBuffer.get();
                    }
                    srsFlvFrameBytes.data = byteBuffer.slice();
                    srsFlvFrameBytes.size = i - byteBuffer.position();
                }
            }
            return srsFlvFrameBytes;
        }
    }

    public SrsFlvMuxer(ConnectCheckerRtmp connectCheckerRtmp2) {
        this.connectCheckerRtmp = connectCheckerRtmp2;
        this.publisher = new DefaultRtmpPublisher(connectCheckerRtmp2);
    }

    public void setProfileIop(byte b) {
        this.profileIop = b;
    }

    public void setSpsPPs(ByteBuffer byteBuffer, ByteBuffer byteBuffer2) {
        this.flv.setSpsPPs(byteBuffer, byteBuffer2);
    }

    public void setSampleRate(int i) {
        this.sampleRate = i;
    }

    public void setIsStereo(boolean z) {
        this.flv.setAchannel(z ? 2 : 1);
    }

    public void setAuthorization(String str, String str2) {
        this.publisher.setAuthorization(str, str2);
    }

    public boolean isConnected() {
        return this.connected;
    }

    public void setVideoResolution(int i, int i2) {
        this.publisher.setVideoResolution(i, i2);
    }

    /* access modifiers changed from: private */
    public void disconnect(ConnectCheckerRtmp connectCheckerRtmp2) {
        try {
            this.publisher.close();
        } catch (IllegalStateException unused) {
        }
        this.connected = false;
        this.mVideoSequenceHeader = null;
        this.mAudioSequenceHeader = null;
        connectCheckerRtmp2.onDisconnectRtmp();
        Log.i(TAG, "worker: disconnect ok.");
    }

    /* access modifiers changed from: private */
    public boolean connect(String str) {
        if (!this.connected) {
            Log.i(TAG, String.format("worker: connecting to RTMP server by url=%s\n", new Object[]{str}));
            if (this.publisher.connect(str)) {
                this.connected = this.publisher.publish("live");
            }
            this.mVideoSequenceHeader = null;
            this.mAudioSequenceHeader = null;
        }
        return this.connected;
    }

    /* access modifiers changed from: private */
    public void sendFlvTag(SrsFlvFrame srsFlvFrame) {
        if (this.connected && srsFlvFrame != null) {
            if (srsFlvFrame.is_video()) {
                if (srsFlvFrame.is_keyframe()) {
                    Log.i(TAG, String.format("worker: send frame type=%d, dts=%d, size=%dB", new Object[]{Integer.valueOf(srsFlvFrame.type), Integer.valueOf(srsFlvFrame.dts), Integer.valueOf(srsFlvFrame.flvTag.array().length)}));
                }
                this.publisher.publishVideoData(srsFlvFrame.flvTag.array(), srsFlvFrame.flvTag.size(), srsFlvFrame.dts);
                this.mVideoAllocator.release(srsFlvFrame.flvTag);
            } else if (srsFlvFrame.is_audio()) {
                this.publisher.publishAudioData(srsFlvFrame.flvTag.array(), srsFlvFrame.flvTag.size(), srsFlvFrame.dts);
                this.mAudioAllocator.release(srsFlvFrame.flvTag);
            }
        }
    }

    public void start(final String str) {
        this.worker = new Thread(new Runnable() {
            public void run() {
                Process.setThreadPriority(-1);
                if (SrsFlvMuxer.this.connect(str)) {
                    SrsFlvMuxer.this.connectCheckerRtmp.onConnectionSuccessRtmp();
                    while (!Thread.interrupted()) {
                        try {
                            SrsFlvFrame srsFlvFrame = (SrsFlvFrame) SrsFlvMuxer.this.mFlvTagCache.take();
                            if (srsFlvFrame.is_sequenceHeader()) {
                                if (srsFlvFrame.is_video()) {
                                    SrsFlvMuxer.this.mVideoSequenceHeader = srsFlvFrame;
                                    SrsFlvMuxer.this.sendFlvTag(SrsFlvMuxer.this.mVideoSequenceHeader);
                                } else if (srsFlvFrame.is_audio()) {
                                    SrsFlvMuxer.this.mAudioSequenceHeader = srsFlvFrame;
                                    SrsFlvMuxer.this.sendFlvTag(SrsFlvMuxer.this.mAudioSequenceHeader);
                                }
                            } else if (srsFlvFrame.is_video() && SrsFlvMuxer.this.mVideoSequenceHeader != null) {
                                SrsFlvMuxer.this.sendFlvTag(srsFlvFrame);
                            } else if (srsFlvFrame.is_audio() && SrsFlvMuxer.this.mAudioSequenceHeader != null) {
                                SrsFlvMuxer.this.sendFlvTag(srsFlvFrame);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            if (SrsFlvMuxer.this.worker != null) {
                                SrsFlvMuxer.this.worker.interrupt();
                            }
                        }
                    }
                }
            }
        });
        this.worker.start();
    }

    public void stop() {
        Thread thread = this.worker;
        if (thread != null) {
            thread.interrupt();
            try {
                this.worker.join(100);
            } catch (InterruptedException unused) {
                this.worker.interrupt();
            }
            this.worker = null;
        }
        this.mFlvTagCache.clear();
        this.flv.reset();
        this.needToFindKeyFrame = true;
        Log.i(TAG, "SrsFlvMuxer closed");
        new Thread(new Runnable() {
            public void run() {
                SrsFlvMuxer srsFlvMuxer = SrsFlvMuxer.this;
                srsFlvMuxer.disconnect(srsFlvMuxer.connectCheckerRtmp);
            }
        }).start();
    }

    public void sendVideo(ByteBuffer byteBuffer, BufferInfo bufferInfo) {
        this.flv.writeVideoSample(byteBuffer, bufferInfo);
    }

    public void sendAudio(ByteBuffer byteBuffer, BufferInfo bufferInfo) {
        this.flv.writeAudioSample(byteBuffer, bufferInfo);
    }
}
