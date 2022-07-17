package com.example;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class ByteArrayConversion {
    public static void main(String[] args) {

        // int to byte[]
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        byteBuffer.putInt(2747833);
        byteBuffer.rewind();
        byte[] sizeArrayBytes = byteBuffer.array();

        // byte[] to int
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put(sizeArrayBytes);
        buffer.rewind();
        int value = buffer.getInt();

        // Print to console
        System.out.println("Byte array size: " + sizeArrayBytes.length);
        System.out.println("Byte array object: " + Arrays.toString(sizeArrayBytes));
        System.out.println("Decimal value in the array: " + value);

    }
}
