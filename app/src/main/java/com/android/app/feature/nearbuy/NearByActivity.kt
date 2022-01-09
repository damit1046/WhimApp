package com.android.app.feature.nearbuy


import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.android.app.core.platform.BaseActivity
import com.android.app.core.platform.BaseFragment
import com.android.app.feature.location.CurrentLocation


class NearByActivity : BaseActivity(), CurrentLocationProvider {

    private lateinit var currentLocation: CurrentLocation


    companion object {
        fun callingIntent(context: Context) = Intent(context, NearByActivity::class.java)
    }

    override fun fragment(): BaseFragment = NearByFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentLocation = CurrentLocation(this)
    }


    override fun onDestroy() {
        super.onDestroy()
        currentLocation.destroy()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        currentLocation.onRequestPermissionsResult(requestCode,permissions = permissions,grantResults)
    }

    override fun currentLocationProvider(): CurrentLocation = currentLocation
}