package com.nju.urbangreen.zhenjiangurbangreen.util;

/**
 * Created by HCQIN on 2016/12/27.
 */
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import android.util.Base64;
// 将一个字符串按照zip方式压缩和解压缩
public class ZipUtils {

    // 压缩
    public static String compress(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip=null;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(str.getBytes("UTF-8"));
            gzip.close();

            return Base64.encodeToString(out.toByteArray(), Base64.DEFAULT);
            //return out.toString("UTF-16").getBytes(charset);
        } catch (IOException e) {
            // ignore
            e.printStackTrace();
            return "";
        }
        finally
        {
            try {
                gzip.close();
            } catch (IOException e) {
                // ignore
            }
        }
    }

    // 解压缩
    public static String uncompress(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(Base64.decode(str, Base64.DEFAULT));
        GZIPInputStream gunzip = new GZIPInputStream(in);
        try {
            byte[] buffer = new byte[256];
            int n;
            while ((n = gunzip.read(buffer))>= 0) {
                out.write(buffer, 0, n);
            }
            return out.toString("UTF-8");
        } catch (Exception e) {
            // ignore
            return "";
        }
        finally
        {
            gunzip.close();
        }

    }

}
