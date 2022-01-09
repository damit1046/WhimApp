package com.android.app.feature.nearbuy

import com.android.app.core.exception.Failure

class PlaceFailure {
    class ListNotAvailable : Failure.FeatureFailure()
    class NonExistentPlace : Failure.FeatureFailure()
}

