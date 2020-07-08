package com.milla.study.netbase.expert.concurrent.io;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;

/**
 * @Package: com.milla.study.netbase.expert.concurrent.io
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/7/6 13:23
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/7/6 13:23
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Slf4j
public class NettyBufferTests {
    public static void main(String[] args) {
        sliceTests();
        wrapTests();
        compositeTests();
    }

    private static void compositeTests() {
        byte[] arr1 = {1, 2, 3};
        byte[] arr2 = {4, 5, 6};
        ByteBuf byteBuf1 = Unpooled.wrappedBuffer(arr1);
        ByteBuf byteBuf2 = Unpooled.wrappedBuffer(arr2);
        CompositeByteBuf byteBuf = Unpooled.compositeBuffer();
        CompositeByteBuf byteBufs = byteBuf.addComponents(true, byteBuf1, byteBuf2);
        log.info("合并的结果：{}", byteBufs);
    }

    private static void wrapTests() {
        byte[] arr = {1, 2, 3, 4, 5};
        ByteBuf byteBuf = Unpooled.wrappedBuffer(arr);
        log.info("第四个元素为：{}", byteBuf.getByte(4));
        arr[4] = 7;
        log.info("第四个元素为：{}", byteBuf.getByte(4));

    }

    private static void sliceTests() {
        ByteBuf byteBuf = Unpooled.wrappedBuffer("hello".getBytes());
        ByteBuf newBuf = byteBuf.slice(1, 2);
        newBuf.unwrap();
        System.out.println(newBuf.toString());


    }
}
