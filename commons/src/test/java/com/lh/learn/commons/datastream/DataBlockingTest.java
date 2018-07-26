package com.lh.learn.commons.datastream;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Random;

/**
 * @author liuhai Create at: 2018/7/26
 */
public class DataBlockingTest {

    @Test
    public void ee() throws IOException, InterruptedException {
        DataBlocking<byte[]> dataBlocking = new DataBlocking<>();

        DataConsumer<byte[]> dataConsumer = new DataConsumer<>(dataBlocking, byte.class);
        DataProducer<byte[]> dataProducer = new DataProducer<>(dataBlocking);

        int size = 10;
        Random random = new Random();
        String content="Hello world, welcome to Java.";
        ByteArrayInputStream in=new ByteArrayInputStream(content.getBytes());
        while (true) {
            byte[] bytes = new byte[random.nextInt(size) + 1];
            int len = in.read(bytes);
            if (len == -1) {
                break;
            }
            if (len == bytes.length) {
                dataProducer.add(bytes);
            } else {
                byte[] bytes1 = new byte[len];
                System.arraycopy(bytes, 0, bytes1, 0, len);
                dataProducer.add(bytes1);
            }
        }
        in.close();
        dataProducer.setWriteEnd();

        byte[] bytes = dataConsumer.getBytes(1);
        for (byte aByte : bytes) {
            System.out.print((char)aByte);
        }
        System.out.println();

        bytes = dataConsumer.getBytes(6);
        for (byte aByte : bytes) {
            System.out.print((char)aByte);
        }
        System.out.println();

        bytes = dataConsumer.getBytes(45);
        for (byte aByte : bytes) {
            System.out.print((char)aByte);
        }

        bytes = dataConsumer.getBytes(-1);
        for (byte aByte : bytes) {
            System.out.print((char)aByte);
        }

    }
}