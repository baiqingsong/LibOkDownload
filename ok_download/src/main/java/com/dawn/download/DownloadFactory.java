package com.dawn.download;


import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.liulishuo.okdownload.DownloadListener;
import com.liulishuo.okdownload.DownloadMonitor;
import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.OkDownload;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.breakpoint.DownloadStore;
import com.liulishuo.okdownload.core.breakpoint.RemitStoreOnSQLite;
import com.liulishuo.okdownload.core.cause.EndCause;
import com.liulishuo.okdownload.core.cause.ResumeFailedCause;
import com.liulishuo.okdownload.core.connection.DownloadOkHttp3Connection;
import com.liulishuo.okdownload.core.dispatcher.CallbackDispatcher;
import com.liulishuo.okdownload.core.dispatcher.DownloadDispatcher;
import com.liulishuo.okdownload.core.listener.DownloadListener3;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;

import java.io.File;
import java.util.List;
import java.util.Map;

public class DownloadFactory {
    //单例模式
    private static DownloadFactory instance;
    private Context context;

    private DownloadFactory(Context context) {
        this.context = context;
        init();
    }

    public static DownloadFactory getInstance(Context context) {
        if (instance == null) {
            synchronized (DownloadFactory.class) {
                if (instance == null) {
                    instance = new DownloadFactory(context);
                }
            }
        }
        return instance;
    }
    public void init(){
        // 初始化 OKDownload
//        OkDownload.Builder builder = new OkDownload.Builder(context)
//                .connectionFactory(new DownloadOkHttp3Connection.Factory());
//
////        OkDownload.Builder builder = new OkDownload.Builder(context)
////                .downloadStore(new DownloadStore.DatabaseProvider(context))  // 正确调用方式‌:ml-citation{ref="2,8" data="citationList"}
////                .downloadDispatcher(new DownloadDispatcher());
//
//        OkDownload.setSingletonInstance(builder.build());
//        // 设置最大下载任务数
//        DownloadDispatcher.setMaxParallelRunningCount(3);

        // 检查 OkDownload 是否已经初始化
//        if (OkDownload.with() == null) {
//            // 初始化 OkDownload
//            DownloadOkHttp3Connection.Factory connectionFactory = new DownloadOkHttp3Connection.Factory();
//
//            OkDownload.Builder builder = new OkDownload.Builder(context)
//                    .connectionFactory(connectionFactory);
//            OkDownload.setSingletonInstance(builder.build());
//        }
//        OkDownload.with().setMonitor(new DownloadMonitor() {
//            @Override
//            public void taskStart(DownloadTask task) {
//
//            }
//
//            @Override
//            public void taskDownloadFromBreakpoint(@NonNull DownloadTask task, @NonNull BreakpointInfo info) {
//
//            }
//
//            @Override
//            public void taskDownloadFromBeginning(@NonNull DownloadTask task, @NonNull BreakpointInfo info, @Nullable ResumeFailedCause cause) {
//
//            }
//
//            @Override
//            public void taskEnd(DownloadTask task, EndCause cause, @Nullable Exception realCause) {
//
//            }
//        });

        DownloadDispatcher.setMaxParallelRunningCount(3);

//        RemitStoreOnSQLite.setRemitToDBDelayMillis(3000);

//        OkDownload.with().downloadDispatcher().cancelAll();
//
//        OkDownload.with().breakpointStore().remove(taskId);


    }

    public void downloadFile(String url, String saveDir, String fileName, AutoDownloadListener listener) {
        DownloadTask task = new DownloadTask.Builder(url, new File(saveDir))
                .setFilename(fileName)               // 指定文件名（可选）
                .setMinIntervalMillisCallbackProcess(30)   // 进度回调间隔（单位：毫秒）
                .setPassIfAlreadyCompleted(false)// 是否跳过已完成任务
                .build();
        task.enqueue(new DownloadListener() {

            @Override
            public void taskStart(@NonNull DownloadTask task) {
                //当下载任务开始时调用 task 当前下载任务
                if(listener != null)
                    listener.onStart(task.getUrl());
            }

            @Override
            public void connectTrialStart(@NonNull DownloadTask task, @NonNull Map<String, List<String>> requestHeaderFields) {
                //当下载任务开始尝试连接时调用 task 当前下载任务，requestHeaderFields 请求头
            }

            @Override
            public void connectTrialEnd(@NonNull DownloadTask task, int responseCode, @NonNull Map<String, List<String>> responseHeaderFields) {
                //当下载任务结束尝试连接时调用 task 当前下载任务，responseCode 响应码，responseHeaderFields 响应头
            }

            @Override
            public void downloadFromBeginning(@NonNull DownloadTask task, @NonNull BreakpointInfo info, @NonNull ResumeFailedCause cause) {
                //当下载任务从头开始下载时调用 task 当前下载任务，info 断点信息，cause 回复失败原因
            }

            @Override
            public void downloadFromBreakpoint(@NonNull DownloadTask task, @NonNull BreakpointInfo info) {
                //当下载任务从断点下载时调用 task 当前下载任务，info 断点信息
            }

            @Override
            public void connectStart(@NonNull DownloadTask task, int blockIndex, @NonNull Map<String, List<String>> requestHeaderFields) {
                //当下载任务开始连接时调用 task 当前下载任务，blockIndex 块索引，requestHeaderFields 请求头
            }

            @Override
            public void connectEnd(@NonNull DownloadTask task, int blockIndex, int responseCode, @NonNull Map<String, List<String>> responseHeaderFields) {
                //当下载任务结束连接时调用 task 当前下载任务，blockIndex 块索引，responseCode 响应码，responseHeaderFields 响应头
            }

            @Override
            public void fetchStart(@NonNull DownloadTask task, int blockIndex, long contentLength) {
                //当下载任务开始获取数据时调用 task 当前下载任务，blockIndex 块索引，contentLength 内容长度
            }

            @Override
            public void fetchProgress(@NonNull DownloadTask task, int blockIndex, long increaseBytes) {
                //当下载任务获取数据进度更新时调用 task 当前下载任务，blockIndex 块索引，increaseBytes 增加的字节数
                try{
                    int progress = (int)task.getInfo().getTotalOffset();
                    int totalLength = (int)task.getInfo().getTotalLength();
                    if(listener != null)
                        listener.onProgress(task.getUrl(), progress, totalLength);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void fetchEnd(@NonNull DownloadTask task, int blockIndex, long contentLength) {
                //当下载任务结束获取数据时调用 task 当前下载任务，blockIndex 块索引，contentLength 内容长度
            }

            @Override
            public void taskEnd(@NonNull DownloadTask task, @NonNull EndCause cause, @Nullable Exception realCause) {
                //当下载任务结束时调用 task 当前下载任务，cause 原因，realCause 实际原因
                if (cause == EndCause.COMPLETED) {
                    // 下载完成
                    if(listener != null)
                        listener.onFinish(task.getUrl(), task.getFile().getAbsolutePath());
                } else {
                    // 下载失败
                    if(listener != null)
                        listener.onError(task.getUrl(), cause.toString());
                }
            }
        });
    }

}
