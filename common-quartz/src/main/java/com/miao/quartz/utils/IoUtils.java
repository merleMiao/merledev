//package com.miao.quartz.utils;
//
//
//import com.alibaba.druid.util.Utils;
//
//import java.io.*;
//
//public class IoUtils {
//
//    public final static int DEFAULT_BUFFER_SIZE = 1024 * 4;
//
//    public static String readFromResource(String resource) throws IOException {
//        InputStream in = null;
//        try {
//            in = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
//            if (in == null) {
//                in = Utils.class.getResourceAsStream(resource);
//            }
//
//            if (in == null) {
//                return null;
//            }
//
//            String text = Utils.read(in);
//            return text;
//        } finally {
//            close(in);
//        }
//    }
//
//    public static byte[] readByteArrayFromResource(String resource) throws IOException {
//        InputStream in = null;
//        try {
//            in = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
//            if (in == null) {
//                return null;
//            }
//            return readByteArray(in);
//        } finally {
//            close(in);
//        }
//    }
//
//    public static byte[] readByteArray(InputStream input) throws IOException {
//        ByteArrayOutputStream output = new ByteArrayOutputStream();
//        copy(input, output);
//        return output.toByteArray();
//    }
//
//    public static long copy(InputStream input, OutputStream output) throws IOException {
//        final int EOF = -1;
//        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
//        long count = 0;
//        int n = 0;
//        while (EOF != (n = input.read(buffer))) {
//            output.write(buffer, 0, n);
//            count += n;
//        }
//        return count;
//    }
//
//    public static void close(Closeable os) {
//        if (os != null) {
//            try {
//                os.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//    }
//}
