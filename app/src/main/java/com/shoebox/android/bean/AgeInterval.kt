package com.shoebox.android.bean

import java.io.Serializable

/**
 * Created by vasile.mihalca on 02/12/15.
 */
data class AgeInterval @JvmOverloads constructor(val minAge: Int,
                       val maxAge: Int,
                       val custom: Boolean = false) : Serializable {

    val ageInterval: String
        get() = if (minAge == maxAge) maxAge.toString() else minAge.toString() + "-" + maxAge

    constructor(customAge: Int) : this(customAge, customAge, true)
}
