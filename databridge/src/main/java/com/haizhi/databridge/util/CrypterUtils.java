package com.haizhi.databridge.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.sun.deploy.util.StringUtils;


/**
 * Gzip压缩与解压
 *
 * @author zhaohuanhuan
 * @create 05 21, 2021
 * @since 1.0.0
 */

public final class CrypterUtils {

    private static String key1 = "WoBig5Liu0HaiZhi";
    private static String key2 = "PingNiuBi007";
    private static int pos1 = 8;
    private static int pos2 = 6;
    private static Integer num1 = 138;
    private static Integer num2 = 15;
    private static Integer lastWordByteLength = 4;
    private static Integer radix = 16;


    private CrypterUtils() {
    }
    public static String encryptData(String inData, String ssid) throws IOException {
        // 我这翻译的是python代码，所以为什么是6为什么是8我也搞不清楚
        String outData = "";
        inData = Base64Utils.encodeBase64(inData.getBytes("UTF-8"));
        if (!ssid.isEmpty()) {

            key1 = ssid.substring(0, pos1) + ssid.substring(ssid.length() - pos1);
            key2 =  ssid.substring(0, pos2) + ssid.substring(ssid.length() - pos2);
        }
        String allData = "";
        if (inData.length() >= num1) {
            Integer size = num1 + num2;
            String head = getxorvalue(inData.substring(0, num1), key1);
            String tail = getxorvalue(inData.substring(num1, size), key2);
            allData = head;
            allData += tail;
            allData += inData.substring(size);

        } else {
            String anotherKey = "007_" + key2 + "_HaiZHI";
            while (anotherKey.length() < inData.length()) {
                anotherKey += anotherKey;
            }
            allData = getxorvalue(inData, anotherKey);
        }
        byte[] b1 = allData.getBytes();
        byte[] b2 = structPack(num1);
        byte[] bt3 = new byte[b1.length + b2.length];
        System.arraycopy(b1, 0, bt3, 0, b1.length);
        System.arraycopy(b2, 0, bt3, b1.length, b2.length);
        outData = Base64Utils.encodeBase64(bt3);
        return "H" + outData;
    }

    public static String decryptData(String inData, String ssid) throws IOException {
        String allData = "";
        if (inData == null || !inData.startsWith("H")) {
            return inData;
        }
        byte[] inDataByte = Base64Utils.decodeBase64(inData.substring(1));
        if (!ssid.isEmpty()) {
            key1 = ssid.substring(0, pos1) + ssid.substring(ssid.length() - pos1);
            key2 =  ssid.substring(0, pos2) + ssid.substring(ssid.length() - pos2);
        }
        Integer module = structUnPack(subByte(inDataByte, inDataByte.length - lastWordByteLength, lastWordByteLength));
        if (inData.length() >= module) {
            String inDataByte2Str = new String(inDataByte);
            Integer size = module + num2;
            allData = getxorvalue(inDataByte2Str.substring(0, module), key1);
            allData += getxorvalue(inDataByte2Str.substring(module, size), key2);
            allData += inDataByte2Str.substring(size, inDataByte2Str.length() - lastWordByteLength);

        } else {
            String anotherKey = "007_" + key2 + "_HaiZHI";
            while (anotherKey.length() < inData.length()) {
                anotherKey += anotherKey;
            }
            String d = new String(inDataByte);
            allData = getxorvalue(d.substring(0, d.length() - lastWordByteLength), anotherKey);

        }
        return new String(Base64Utils.decodeBase64(allData));
    }

    public static byte[] subByte(byte[] b, int off, int length) {
        byte[] b1 = new byte[length];
        System.arraycopy(b, off, b1, 0, length);
        return b1;
    }

    //    https://www.cnblogs.com/hopeofthevillage/p/12917113.html
    public static int structUnPack(byte[] b) throws IOException {
        return bytes2IntLittle(b);
    }

    public static String getxorvalue(String org, String key) throws IOException {
        List<String> myList = new ArrayList<>();
        List<String> orghex = str2hex(org);
        List<String> keyhex = str2hex(key);
        Integer minlen = Math.min(orghex.size(), keyhex.size());
        for (int i = 0; i < minlen; i++) {
            Integer a = Integer.valueOf(String.valueOf(orghex.get(i).charAt(0)), radix) * radix
                    + Integer.valueOf(String.valueOf(orghex.get(i).charAt(1)), radix);

            Integer b = Integer.valueOf(String.valueOf(keyhex.get(i).charAt(0)), radix) * radix
                    + Integer.valueOf(String.valueOf(keyhex.get(i).charAt(1)), radix);
            int x = a ^ b;
            myList.add(structPack((char) x));
        }
        return StringUtils.join(myList, "")  + org.substring(minlen);
    }

    public static byte[] structPack(int i) throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream d = new DataOutputStream(b);
        d.write(intToByteLittle(i));
        return b.toByteArray();
    }
    public static String structPack(char c) {
        ByteArrayOutputStream bOutput = new ByteArrayOutputStream();
        bOutput.write(c);
        return new String(bOutput.toByteArray());
    }

    /**
     * 将int转为低字节在前，高字节在后的byte数组（小端）
     * @param n int
     * @return byte[]
     */
    public static byte[] intToByteLittle(int n) {
        final int defaultLength = 4;
        final int doubleLength = 8;
        final int fourLength = 16;
        final int sixLength = 24;
        final int sign = 0xff;

        final int post0 = 0;
        final int post1 = 1;
        final int post2 = 2;
        final int post3 = 3;
        byte[] b = new byte[defaultLength];
        b[post0] = (byte) (n & sign);
        b[post1] = (byte) (n >> doubleLength & sign);
        b[post2] = (byte) (n >> fourLength & sign);
        b[post3] = (byte) (n >> sixLength & sign);
        return b;
    }

    public static List<String> str2hex(String value) {
        List<String> hexs = new ArrayList<>();
        for (char s: value.toCharArray()) {
            String rst = Integer.toHexString(Integer.valueOf(s));
            if (rst.length() == 2) {
                hexs.add(rst);
            } else {
                hexs.add("0" + rst);
            }
        }
        return hexs;
    }

    /**
     * byte数组到int的转换(小端)
     * @param bytes
     * @return
     */
    public static int bytes2IntLittle(byte[] bytes) {
        final int doubleLength = 8;
        final int fourLength = 16;
        final int sixLength = 24;

        final int post0 = 0;
        final int post1 = 1;
        final int post2 = 2;
        final int post3 = 3;

        final int sign = 0xff;

        int int1 = bytes[post0] & sign;
        int int2 = (bytes[post1] & sign) << doubleLength;
        int int3 = (bytes[post2] & sign) << fourLength;
        int int4 = (bytes[post3] & sign) << sixLength;

        return int1 | int2 | int3 | int4;
    }
}

