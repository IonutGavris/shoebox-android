package com.shoebox.android.util

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.fernandocejas.arrow.strings.Strings
import timber.log.Timber

fun Context.getDeviceData(): String {
	return ("ShoeBox " + getAppVersionName() + " on "
			+ Build.MANUFACTURER + " " + Build.MODEL + " (" + Build.VERSION.RELEASE + ")\n")
}

fun Context.getAppVersionName(): String {
	try {
		val pinfo = packageManager.getPackageInfo(packageName, 0)
		return pinfo.versionName
	} catch (e: PackageManager.NameNotFoundException) {
		Timber.e(e, "getAppVersionName: Version name not found")
	}

	return Strings.EMPTY
}

fun Context.useRomanianLanguage(): Boolean {
	val phoneLocale = resources.configuration.locale.language
	Timber.d("useRomanianLanguage: phoneLocale=$phoneLocale")
	return "ro" == phoneLocale
}