package com.jailbreakme3.setsuna.helper


import com.jailbreakme3.setsuna.listener.DownLoadListener
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream


class DownLoadHelper {
    companion object {
        private const val byteArraySize = 2048
        private const val hundred = 100

        /**
         * 根据形参初始化下载所需变量
         * @param download [@kotlin.ExtensionFunctionType] Function1<DownLoadListener, Unit>
         * @param savePath String
         * @return Pair<DownLoadListener, File>
         */
        internal fun initDownload(
            download: DownLoadListener.() -> Unit,
            savePath: String
        ): Pair<DownLoadListener, File> {
            val downloadListener = DownLoadListener().apply(download)
            val file = File(savePath)
            downloadListener.started?.invoke()
            return Pair(downloadListener, file)
        }

        /**
         * 将当前下载任务放到下载队列
         * @param call Call
         * @param downloadListener DownLoadListener
         * @param file File
         * @param savePath String
         * @param url String
         */
        internal fun downLoadTaskEnqueue(
            call: Call,
            downloadListener: DownLoadListener,
            file: File,
            savePath: String,
            url: String
        ) {
            call.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    downloadListener.failed?.invoke(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    var length: Int
                    val buf = ByteArray(byteArraySize)
                    var fileOutputStream: FileOutputStream? = null
                    var inputStream: InputStream? = null

                    runCatching {
                        if (!file.mkdir()) {
                            // 路径不合法会抛出异常
                            file.createNewFile()
                        }
                        // body非法会抛出异常
                        inputStream = response.body()!!.byteStream()
                        val total = response.body()!!.contentLength()
                        val downloadFile = File(savePath, url.substring(url.lastIndexOf("/") + 1))
                        var sum: Long = 0
                        var preNumber = 0
                        var progress: Int
                        inputStream = response.body()!!.byteStream()
                        fileOutputStream = FileOutputStream(downloadFile)
                        while (inputStream!!.read(buf).also { length = it } != -1) {
                            fileOutputStream!!.write(buf, 0, length)
                            sum += length.toLong()
                            progress = (sum * 1.0f / total * hundred).toInt()
                            if (progress != preNumber) {
                                downloadListener.progress?.invoke(progress)
                            }
                            preNumber = progress
                        }
                        fileOutputStream!!.flush()
                        downloadListener.completed?.invoke(file.absolutePath)
                    }.onFailure {
                        // 多为取消：socket close
                        downloadListener.failed?.invoke(it)
                        cancelDownload(call, downloadListener.completed)
                    }
                    inputStream?.close()
                    fileOutputStream?.close()
                }
            })
        }

        /**
         * 取消下载
         *
         * @param call Call
         * @param onDownloadEnd Function1<String?, Unit>?
         */
        private fun cancelDownload(call: Call, onDownloadEnd: ((String?) -> Unit)?) {
            onDownloadEnd?.invoke(null)
            call.cancel()
        }
    }
}
