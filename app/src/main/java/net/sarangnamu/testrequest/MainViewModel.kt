package net.sarangnamu.testrequest

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 11. 2. <p/>
 */

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val statusMsg = MutableLiveData<String>()

    init {
        statusMsg.value = "TEST"
    }
}