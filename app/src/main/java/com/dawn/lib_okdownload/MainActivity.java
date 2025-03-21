package com.dawn.lib_okdownload;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.dawn.download.AutoDownloadListener;
import com.dawn.download.DownloadFactory;

public class MainActivity extends AppCompatActivity {
    long currentTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tvDownload = findViewById(R.id.tv_download);
        tvDownload.setOnClickListener(view -> {
            currentTime = System.currentTimeMillis();
            DownloadFactory downloadFactory = DownloadFactory.getInstance(MainActivity.this);
            downloadFactory.init();
            downloadFactory.downloadFile("\thttps://v5.jargee.cn/apk/lk8nq5PUZNqu8YLe7ygPH1i-Oy8520250318061832047796.apk", getExternalFilesDir(null).getAbsolutePath(), "filename.apk", new AutoDownloadListener(){

                @Override
                public void onStart(String url) {
                    Log.i("dawn", "on start " + url);
                }

                @Override
                public void onProgress(String url, long currentSize, long totalSize) {
                    Log.i("dawn", "on progress " + url + " currentSize " + currentSize + " totalSize " + totalSize);
                }

                @Override
                public void onFinish(String url, String path) {
                    Log.i("dawn", "on finish " + url + " path " + path);
                    long disTime = (System.currentTimeMillis() - currentTime)/1000;
                    Log.i("dawn", "dis time : " + disTime);
                }

                @Override
                public void onError(String url, String error) {
                    Log.e("dawn", "on error " + url + " error " + error);
                }
            } );

        });
    }
}