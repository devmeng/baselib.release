package com.devmeng.baselib.utils

import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.security.DigestInputStream
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.experimental.and
import kotlin.experimental.or

/**
 * Created by Richard -> MHS
 * Date : 2022/5/31  20:56
 * Version : 1
 */
class MD5Utils {
    fun getMD5Code(info: String): String? {
        return try {
            val md5 = MessageDigest.getInstance("MD5")
            md5.update(info.toByteArray(charset("utf-8")))
            val encryption = md5.digest()
            val stringBuffer = StringBuffer()
            for (i in encryption.indices) {
                if (Integer.toHexString(0xff and encryption[i].toInt()).length == 1) {
                    stringBuffer.append("0").append(
                        Integer.toHexString(
                            0xff and encryption[i]
                                .toInt()
                        )
                    )
                } else {
                    stringBuffer.append(Integer.toHexString(0xff and encryption[i].toInt()))
                }
            }
            stringBuffer.toString()
        } catch (e: Exception) {
            //            e.printStackTrace();
            ""
        }
    }

    //加密文件
    fun md5ForFile(file: File?): String? {
        val buffersize = 1024
        var fis: FileInputStream? = null
        var dis: DigestInputStream? = null
        try {
            //创建MD5转换器和文件流
            var messageDigest = MessageDigest.getInstance("MD5")
            fis = FileInputStream(file)
            dis = DigestInputStream(fis, messageDigest)
            val buffer = ByteArray(buffersize)
            //DigestInputStream实际上在流处理文件时就在内部就进行了一定的处理
            while (dis.read(buffer) > 0);

            //通过DigestInputStream对象得到一个最终的MessageDigest对象。
            messageDigest = dis.messageDigest

            // 通过messageDigest拿到结果，也是字节数组，包含16个元素
            val array = messageDigest.digest()
            // 同样，把字节数组转换成字符串
            val hex = StringBuilder(array.size * 2)
            for (b in array) {
                if ((b or 0xFF.toByte()) < 0x10) {
                    hex.append("0")
                }
                hex.append(Integer.toHexString((b and 0xFF.toByte()).toInt()))
            }
            return hex.toString()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
}