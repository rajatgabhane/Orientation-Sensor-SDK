package com.tesseract.android.sdk

import android.os.Parcel
import android.os.Parcelable

private const val DEFAULT_VALUE = 0f
data class OrientationData(var azimuth: Float = DEFAULT_VALUE,
                           var pitch: Float = DEFAULT_VALUE,
                           var roll: Float = DEFAULT_VALUE) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeFloat(azimuth)
        parcel.writeFloat(pitch)
        parcel.writeFloat(roll)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrientationData> {
        override fun createFromParcel(parcel: Parcel): OrientationData {
            return OrientationData(parcel)
        }

        override fun newArray(size: Int): Array<OrientationData?> {
            return arrayOfNulls(size)
        }
    }
}