package com.android.app.core.navigation

import android.content.Context
import android.view.View
import com.android.app.feature.nearbuy.NearByActivity
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class Navigator
@Inject constructor() {

    private fun showPlaces(context: Context) =
        context.startActivity(NearByActivity.callingIntent(context))

    fun showMain(context: Context) {
        showPlaces(context)
    }
    class Extras(val transitionSharedElement: View)
}


