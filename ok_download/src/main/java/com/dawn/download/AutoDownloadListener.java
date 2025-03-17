package com.dawn.download;

public interface AutoDownloadListener {
    void onStart(String url);

    void onProgress(String url, long currentSize, long totalSize);

    void onFinish(String url, String path);

    void onError(String url, String error);
}
