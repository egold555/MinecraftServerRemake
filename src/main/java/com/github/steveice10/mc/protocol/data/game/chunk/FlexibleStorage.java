package com.github.steveice10.mc.protocol.data.game.chunk;

import com.github.steveice10.mc.protocol.util.ObjectUtil;

import java.util.Arrays;

public class FlexibleStorage {
    private final long[] data;
    private final int bitsPerEntry;
    private final int size;
    private final long maxEntryValue;

    public FlexibleStorage(int bitsPerEntry, int size) {
        this(bitsPerEntry, new long[roundToNearest(size * bitsPerEntry, 64) / 64]);
    }

    public FlexibleStorage(int bitsPerEntry, long[] data) {
        if(bitsPerEntry < 4) {
            bitsPerEntry = 4;
        }

        this.bitsPerEntry = bitsPerEntry;
        this.data = data;

        this.size = this.data.length * 64 / this.bitsPerEntry;
        this.maxEntryValue = (1L << this.bitsPerEntry) - 1;
    }

    private static int roundToNearest(int value, int roundTo) {
        if(roundTo == 0) {
            return 0;
        } else if(value == 0) {
            return roundTo;
        } else {
            if(value < 0) {
                roundTo *= -1;
            }

            int remainder = value % roundTo;
            return remainder != 0 ? value + roundTo - remainder : value;
        }
    }

    public long[] getData() {
        return this.data;
    }

    public int getBitsPerEntry() {
        return this.bitsPerEntry;
    }

    public int getSize() {
        return this.size;
    }

    public int get(int index) {
        if(index < 0 || index > this.size - 1) {
            throw new IndexOutOfBoundsException();
        }

        int bitIndex = index * this.bitsPerEntry;
        int startIndex = bitIndex / 64;
        int endIndex = ((index + 1) * this.bitsPerEntry - 1) / 64;
        int startBitSubIndex = bitIndex % 64;
        if(startIndex == endIndex) {
            return (int) (this.data[startIndex] >>> startBitSubIndex & this.maxEntryValue);
        } else {
            int endBitSubIndex = 64 - startBitSubIndex;
            return (int) ((this.data[startIndex] >>> startBitSubIndex | this.data[endIndex] << endBitSubIndex) & this.maxEntryValue);
        }
    }

    public void set(int index, int value) {
        if(index < 0 || index > this.size - 1) {
            throw new IndexOutOfBoundsException();
        }

        if(value < 0 || value > this.maxEntryValue) {
            throw new IllegalArgumentException("Value cannot be outside of accepted range.");
        }

        int bitIndex = index * this.bitsPerEntry;
        int startIndex = bitIndex / 64;
        int endIndex = ((index + 1) * this.bitsPerEntry - 1) / 64;
        int startBitSubIndex = bitIndex % 64;
        this.data[startIndex] = this.data[startIndex] & ~(this.maxEntryValue << startBitSubIndex) | ((long) value & this.maxEntryValue) << startBitSubIndex;
        if(startIndex != endIndex) {
            int endBitSubIndex = 64 - startBitSubIndex;
            this.data[endIndex] = this.data[endIndex] >>> endBitSubIndex << endBitSubIndex | ((long) value & this.maxEntryValue) >> endBitSubIndex;
        }
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof FlexibleStorage)) return false;

        FlexibleStorage that = (FlexibleStorage) o;
        return Arrays.equals(this.data, that.data) &&
                this.bitsPerEntry == that.bitsPerEntry &&
                this.size == that.size &&
                this.maxEntryValue == that.maxEntryValue;
    }

    @Override
    public int hashCode() {
        return ObjectUtil.hashCode(this.data, this.bitsPerEntry, this.size, this.maxEntryValue);
    }

    @Override
    public String toString() {
        return ObjectUtil.toString(this);
    }
}
