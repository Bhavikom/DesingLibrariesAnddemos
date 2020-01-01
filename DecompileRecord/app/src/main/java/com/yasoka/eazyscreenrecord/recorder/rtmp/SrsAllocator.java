package com.yasoka.eazyscreenrecord.recorder.rtmp;

import java.util.Arrays;

public final class SrsAllocator {
    private Allocation[] availableAllocations;
    private volatile int availableSentinel;
    private final int individualAllocationSize;

    public class Allocation {
        private byte[] data;
        private int size = 0;

        public Allocation(int i) {
            this.data = new byte[i];
        }

        public byte[] array() {
            return this.data;
        }

        public int size() {
            return this.size;
        }

        public void appendOffset(int i) {
            this.size += i;
        }

        public void clear() {
            this.size = 0;
        }

        public void put(byte b) {
            byte[] bArr = this.data;
            int i = this.size;
            this.size = i + 1;
            bArr[i] = b;
        }

        public void put(byte b, int i) {
            int i2 = i + 1;
            this.data[i] = b;
            int i3 = this.size;
            if (i2 > i3) {
                i3 = i2;
            }
            this.size = i3;
        }

        public void put(short s) {
            put((byte) s);
            put((byte) (s >>> 8));
        }

        public void put(int i) {
            put((byte) i);
            put((byte) (i >>> 8));
            put((byte) (i >>> 16));
            put((byte) (i >>> 24));
        }

        public void put(byte[] bArr) {
            System.arraycopy(bArr, 0, this.data, this.size, bArr.length);
            this.size += bArr.length;
        }
    }

    public SrsAllocator(int i) {
        this(i, 0);
    }

    public SrsAllocator(int i, int i2) {
        this.individualAllocationSize = i;
        this.availableSentinel = i2 + 10;
        this.availableAllocations = new Allocation[this.availableSentinel];
        for (int i3 = 0; i3 < this.availableSentinel; i3++) {
            this.availableAllocations[i3] = new Allocation(i);
        }
    }

    public synchronized Allocation allocate(int i) {
        for (int i2 = 0; i2 < this.availableSentinel; i2++) {
            if (this.availableAllocations[i2].size() >= i) {
                Allocation allocation = this.availableAllocations[i2];
                this.availableAllocations[i2] = null;
                return allocation;
            }
        }
        if (i <= this.individualAllocationSize) {
            i = this.individualAllocationSize;
        }
        return new Allocation(i);
    }

    public synchronized void release(Allocation allocation) {
        allocation.clear();
        for (int i = 0; i < this.availableSentinel; i++) {
            if (this.availableAllocations[i].size() == 0) {
                this.availableAllocations[i] = allocation;
                return;
            }
        }
        if (this.availableSentinel + 1 > this.availableAllocations.length) {
            this.availableAllocations = (Allocation[]) Arrays.copyOf(this.availableAllocations, this.availableAllocations.length * 2);
        }
        Allocation[] allocationArr = this.availableAllocations;
        int i2 = this.availableSentinel;
        this.availableSentinel = i2 + 1;
        allocationArr[i2] = allocation;
    }
}
