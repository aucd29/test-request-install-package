package net.sarangnamu.testrequest

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*
import net.sarangnamu.testrequest.databinding.ActivityMainBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.internal.Util
import okio.Okio
import java.io.File
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private lateinit var binding_ : ActivityMainBinding
    private lateinit var vmodel_ : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding_ = DataBindingUtil.setContentView(this, R.layout.activity_main)
        vmodel_  = ViewModelProviders.of(this).get(MainViewModel::class.java)
        binding_.vmodel = vmodel_
        binding_.content.vmodel = vmodel_

        registerObserve()
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Executors.newSingleThreadExecutor().execute(::apkDownloadAndInstall)
        }
    }

    fun registerObserve() {
        vmodel_.statusMsg.observe(this, Observer {

        })
    }

     fun apkDownloadAndInstall() {
        synchronized(this) {
            runOnUiThread { vmodel_.statusMsg.value = "START" }

            val client = OkHttpClient()
            val request = Request.Builder()
                .url("https://github.com/aucd29/test-request-install-package/blob/master/apk/app-debug.apk?raw=true")
                .build()

            runOnUiThread { vmodel_.statusMsg.value = "DOWNLOADING" }
            val response = client.newCall(request).execute()
            val path = getExternalFilesDir(null).getAbsolutePath()
            val fp = File(path, "test.apk")
            if (fp.exists()) {
                fp.delete()
            }

            response.body()?.let {
                val sink = Okio.buffer(Okio.sink(fp))
                val src = it.source()

                try {
                    while (src.read(sink.buffer(), 8192) != -1L) {
                        sink.flush()
                    }
                } finally {
                    Util.closeQuietly(src)
                }

                vmodel_.statusMsg.postValue("INSTALL APK")

                val intent: Intent
                val fileUri = UriUtils.fromFile(applicationContext, fp.absoluteFile)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    intent = Intent(Intent.ACTION_INSTALL_PACKAGE)
                    intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    intent.setData(fileUri)

                } else {
                    intent = Intent(Intent.ACTION_VIEW)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.setDataAndType(fileUri, "application/vnd.android.package-archive")
                }

                startActivity(intent)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
