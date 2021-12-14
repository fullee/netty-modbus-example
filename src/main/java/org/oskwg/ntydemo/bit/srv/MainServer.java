package org.oskwg.ntydemo.bit.srv;

import cn.hutool.core.util.ByteUtil;
import org.osgl.util.S;

import java.util.ArrayList;
import java.util.List;

public class MainServer {

    public static void main(String[] args) {
        // 0001 0001 - 0020 0030
        int a = 'a';
        int b = '1';

        List<Byte> bs = new ArrayList<>();
        bs.add((byte) 1);
        bs.add((byte) 2);


        System.out.println(Integer.toBinaryString(a));
        System.out.println(Integer.toBinaryString(b));

        // b 是byte转换为int的值
        System.out.println(S.padLeadingZero(Integer.parseInt(Integer.toBinaryString(b)), 8));


        System.out.println(ByteUtil.byteToUnsignedInt((byte) 'a'));

        System.out.println(Integer.toBinaryString(ByteUtil.byteToUnsignedInt((byte) 129)));

        System.out.println(S.padLeadingZero(Integer.parseInt(Integer.toBinaryString(0xF1)), 8));
    }

}
