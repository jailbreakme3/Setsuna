package com.jailbreakme3.setsuna


import java.io.ByteArrayInputStream
import java.io.Closeable
import java.nio.charset.Charset
import java.util.*
import java.util.zip.GZIPInputStream
import java.util.zip.Inflater

/**
 * Zlib解压
 *
 * @param charsetName String?
 * @return String?
 */
internal fun ByteArray.decompressToStringForZlib(charsetName: String? = "UTF-8"): String? {
    val bytesDecompressed = decompressForZlib(this)
    return runCatching {
        String(
            bytesDecompressed!!,
            0,
            bytesDecompressed.size,
            Charset.forName(charsetName)
        )
    }.getOrNull()
}

/**
 * gzip解压
 *
 * @param charsetName String?
 * @return String?
 */
internal fun ByteArray.decompressForGzip(charsetName: String? = "UTF-8"): String? {
    val bufferSize = size
    var gis: GZIPInputStream? = null
    var byteArrayInputStream: ByteArrayInputStream? = null
    val result = runCatching {
        byteArrayInputStream = ByteArrayInputStream(this)
        gis = GZIPInputStream(byteArrayInputStream, bufferSize)
        val string = StringBuilder()
        val data = ByteArray(bufferSize)
        var bytesRead: Int
        while (gis!!.read(data).also { bytesRead = it } != -1) {
            string.append(String(data, 0, bytesRead, Charset.forName(charsetName)))
        }
        string.toString()
    }
    closeQuietly(gis)
    closeQuietly(byteArrayInputStream)
    return result.getOrNull()
}

internal fun decompressForZlib(bytesToDecompress: ByteArray): ByteArray? {
    var returnValues: ByteArray? = null
    val inflater = Inflater()
    val numberOfBytesToDecompress = bytesToDecompress.size
    inflater.setInput(
        bytesToDecompress,
        0,
        numberOfBytesToDecompress
    )
    var numberOfBytesDecompressedSoFar = 0
    val bytesDecompressedSoFar: MutableList<Byte> = ArrayList()
    runCatching {
        while (!inflater.needsInput()) {
            val bytesDecompressedBuffer = ByteArray(numberOfBytesToDecompress)
            val numberOfBytesDecompressedThisTime = inflater.inflate(
                bytesDecompressedBuffer
            )
            numberOfBytesDecompressedSoFar += numberOfBytesDecompressedThisTime
            for (b in 0 until numberOfBytesDecompressedThisTime) {
                bytesDecompressedSoFar.add(bytesDecompressedBuffer[b])
            }
        }
        returnValues = ByteArray(bytesDecompressedSoFar.size)
        for (b in returnValues!!.indices) {
            returnValues!![b] = bytesDecompressedSoFar[b]
        }
    }
    inflater.end()
    return returnValues
}

internal fun closeQuietly(closeable: Closeable?) {
    if (closeable != null) {
        runCatching {
            closeable.close()
        }
    }
}