package com.example.samyaksau.e_bulletin.httputil;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * @Author   Samyak Sau
 * @College Jaipur National University, Jaipur
 */
public class FileCache {

    private File cacheDir;

    public FileCache(Context context) {
        // Find the dir to save cached images
        if (android.os.Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            cacheDir = new File(android.os.Environment.getExternalStorageDirectory(), "LazyList");
        else
            cacheDir = context.getCacheDir();
        if (!cacheDir.exists())
            cacheDir.mkdir();
    }

    public File getFile(String url) {
        // I identify images by hashcode. Not a perfect solution, good for the demo.
        String filename = String.valueOf(url.hashCode());
        // Another possible solution (thanks to grantland)
        //String filename = URLEncoder.encode(url);
        File f = new File(cacheDir, filename);
        return f;

    }

    public  void  clear() {
        File[] files = cacheDir.listFiles();
        if (files == null)
            return;
        for (File f: files)
            f.delete();
    }
}
