package com.do_not_douch.antitheifproject.model

import android.os.Parcel
import android.os.Parcelable

class MainMenuModel(
    val maniTextTitle: String,
    val icon: Int,
    val textTitle: String,
    val text: String,
    val iconActive: Boolean,
    val subMenuIcon: Int,
    val isActive: String,
    val isFlash: String,
    val isVibration: String,
    val remoteValue: Boolean,
    val idAds: String,
    val remoteValueHigh: Boolean,
    val bottomText: String,
    val nativeId: String,
    val nativeSoundId: String,
    val nativeLayout: Int,
    val nativeSoundLayout: Int,
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readByte() != 0.toByte(),
        parcel.readString() ?: "",
        parcel.readByte() != 0.toByte(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt(),
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(maniTextTitle)
        parcel.writeInt(icon)
        parcel.writeString(textTitle)
        parcel.writeString(text)
        parcel.writeByte(if (iconActive) 1 else 0)
        parcel.writeInt(subMenuIcon)
        parcel.writeString(isActive)
        parcel.writeString(isFlash)
        parcel.writeString(isVibration)
        parcel.writeByte(if (remoteValue) 1 else 0)
        parcel.writeString(idAds)
        parcel.writeByte(if (remoteValue) 1 else 0)
        parcel.writeString(bottomText)
        parcel.writeString(nativeId)
        parcel.writeString(nativeSoundId)
        parcel.writeInt(nativeLayout)
        parcel.writeInt(nativeSoundLayout)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MainMenuModel> {
        override fun createFromParcel(parcel: Parcel): MainMenuModel {
            return MainMenuModel(parcel)
        }

        override fun newArray(size: Int): Array<MainMenuModel?> {
            return arrayOfNulls(size)
        }
    }
}
