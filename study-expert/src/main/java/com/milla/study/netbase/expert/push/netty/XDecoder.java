package com.milla.study.netbase.expert.push.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class XDecoder extends ByteToMessageDecoder {
    static final int PACKET_SIZE = 220;

    // 用来临时保留没有处理过的请求报文
    ByteBuf tempMsg = Unpooled.buffer();

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("收到了一次数据包，长度是：" + in.readableBytes());
        // in 请求的数据
        // out 将粘在一起的报文拆分后的结果保留起来

        // 1、 合并报文
        ByteBuf message = null;
        int tmpMsgSize = tempMsg.readableBytes();
        // 如果暂存有上一次余下的请求报文，则合并
        if (tmpMsgSize > 0) {
            message = Unpooled.buffer();
            message.writeBytes(tempMsg);
            message.writeBytes(in);
            System.out.println("合并：上一数据包余下的长度为：" + tmpMsgSize + ",合并后长度为:" + message.readableBytes());
        } else {
            message = in;
        }

        // 2、 拆分报文
        // 这个场景下，一个请求固定长度为3，可以根据长度来拆分
        // i+1 i+1 i+1 i+1 i+1
        // 不固定长度，需要应用层协议来约定 如何计算长度
        // 在应用层中，根据单个报文的长度及特殊标记，来将报文进行拆分或合并
        // dubbo rpc协议 = header(16) + body(不固定)
        // header最后四个字节来标识body
        // 长度 = 16 + body长度
        // 0xda, 0xbb 魔数


        int size = message.readableBytes();
        int counter = size / PACKET_SIZE;
        for (int i = 0; i < counter; i++) {
            byte[] request = new byte[PACKET_SIZE];
            // 每次从总的消息中读取3个字节的数据
            message.readBytes(request);

            // 将拆分后的结果放入out列表中，交由后面的业务逻辑去处理
            out.add(Unpooled.copiedBuffer(request));
        }

        // 3、多余的报文存起来
        // 第一个报文： i+  暂存
        // 第二个报文： 1 与第一次
        size = message.readableBytes();
        if (size != 0) {
            System.out.println("多余的数据长度：" + size);
            // 剩下来的数据放到tempMsg暂存
            tempMsg.clear();
            tempMsg.writeBytes(message.readBytes(size));
        }

    }

}
