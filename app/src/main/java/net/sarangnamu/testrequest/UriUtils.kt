package net.sarangnamu.testrequest

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.support.v4.content.FileProvider
import java.io.File

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 11. 2. <p/>
 */

object UriUtils {
    //    private static final org.slf4j.Logger mLog = org.slf4j.LoggerFactory.getLogger(UriUtils.class);

    /** 파일 프로바이더 경로  */
    private val AUTH = ".fileprovider"

    /** 스키마  */
    private val CONTENT = "content"

    /** 전달 받은 uri 에서 split 할 기준  */
    private val EXTERNAL_FILES = "external_files"

    // https://developer.android.com/training/secure-file-sharing/setup-sharing.html
    // https://inthecheesefactory.com/blog/how-to-share-access-to-file-with-fileprovider-on-android-nougat/en
    // https://github.com/commonsguy/cw-omnibus/tree/master/ContentProvider/V4FileProvider
    // http://stackoverflow.com/questions/1910608/android-action-image-capture-intent

    /**
     * 파일을 Uri 로 변경한다.
     * @param context application context
     * @param file file 객체
     * @return uri 로 변환된 값
     */
    fun fromFile(context: Context, file: File): Uri {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(context, context.packageName + AUTH, file)
        } else Uri.fromFile(file)

    }
}
