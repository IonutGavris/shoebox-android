package com.shoebox.android.bean

/**
 * The bean used for box content suggestions.
 * Created by vasile.mihalca on 24/11/15.
 */
data class Suggestion(val key: String? = null,
                      val name: String? = null,
                      val description: String? = null,
                      val category: Int = 0,
                      val sex: String? = null,
                      val minAge: Int = 0,
                      val maxAge: Int = 0) {

    fun isValid(givenIsMale: Boolean, givenMinAge: Int, givenMaxAge: Int): Boolean {
        if (givenIsMale && FEMALE == sex) return false
        if (!givenIsMale && MALE == sex) return false

        if (givenMaxAge < minAge) return false
        return givenMinAge <= maxAge
    }

    companion object {
        const val ORDER_BY = "category"

        private const val MALE = "male"
        private const val FEMALE = "female"
    }
}
