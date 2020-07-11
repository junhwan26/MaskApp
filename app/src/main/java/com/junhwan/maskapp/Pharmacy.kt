package com.junhwan.maskapp

import android.os.Parcel
import android.os.Parcelable

class Pharmacy( val addr:String,  val latitude : Double,
                val longitude : Double,  val name : String,
                val remain_stat :String,  val stock_at : String,
                val type : String) :Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun toString(): String {
        return "$addr $latitude $longitude $name $remain_stat $stock_at $type"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(addr)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
        parcel.writeString(name)
        parcel.writeString(remain_stat)
        parcel.writeString(stock_at)
        parcel.writeString(type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Pharmacy> {
        override fun createFromParcel(parcel: Parcel): Pharmacy {
            return Pharmacy(parcel)
        }

        override fun newArray(size: Int): Array<Pharmacy?> {
            return arrayOfNulls(size)
        }
    }
}