package by.matthewvirus.geoquiz.viewModel

import android.os.Build.VERSION.SDK_INT as sdkVersion
import androidx.lifecycle.ViewModel

class CheatViewModel: ViewModel() {

    var isCheater = false
    var apiVersion = sdkVersion

}