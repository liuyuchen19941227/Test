package com.example.dllo.myapplication.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.android.volley.toolbox.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DiskCache implements ImageLoader.ImageCache {

    private final File cacheDir;

    public DiskCache(Context context) {
        cacheDir = context.getCacheDir();
        if (cacheDir.exists()) {
            cacheDir.mkdir();
        }
    }

    @Override
    public Bitmap getBitmap(String url) {

        String name = MD5Util.getMD5String(url);
        File image = new File(cacheDir, name);
        if (!image.exists()) {
            return null;
        }
        Bitmap bitmap = BitmapFactory.decodeFile(image.getPath());
        Log.d("DiskCache", image.getParent());// 打印图片地址
        return bitmap;

    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {

        String name = MD5Util.getMD5String(url);
        File image = new File(cacheDir, name);
        ByteArrayOutputStream byteArrayOutputStream;
        FileOutputStream fileOutputStream;


        try {
            if (!image.exists()) {
                image.createNewFile();
            }
            fileOutputStream = new FileOutputStream(image);
            byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 10, byteArrayOutputStream);

            byte[] bytes = byteArrayOutputStream.toByteArray();
            fileOutputStream.write(bytes);

            byteArrayOutputStream.close();
            fileOutputStream.close();


        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }


    }
}
