package ceui.lisa.download;

import com.liulishuo.okdownload.DownloadTask;

import java.io.File;

import ceui.lisa.database.IllustTask;
import ceui.lisa.model.GifResponse;
import ceui.lisa.model.IllustsBean;
import ceui.lisa.utils.Common;

public class GifDownload {

    private static final String MAP_KEY = "Referer";
    private static final String IMAGE_REFERER = "https://app-api.pixiv.net/";

    public static void downloadGif(GifResponse response, IllustsBean illustsBean){
        if(response == null){
            return;
        }


        File file = FileCreator.createGifFile(illustsBean);
        DownloadTask.Builder builder = new DownloadTask.Builder(
                response.getUgoira_metadata().getZip_urls().getMedium(),
                file.getParentFile())
                .setFilename(file.getName())
                .setMinIntervalMillisCallbackProcess(30)
                .setPassIfAlreadyCompleted(true);
        builder.addHeader(MAP_KEY, IMAGE_REFERER);
        DownloadTask task = builder.build();
        IllustTask illustTask = new IllustTask();
        illustTask.setIllustsBean(illustsBean);
        illustTask.setDownloadTask(task);
        TaskQueue.get().addTask(illustTask);
        task.enqueue(new QueueListener());
        Common.showToast("图组ZIP已加入下载队列");
    }
}
