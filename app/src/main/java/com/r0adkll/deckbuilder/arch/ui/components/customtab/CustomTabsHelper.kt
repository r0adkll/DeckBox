// Copyright 2015 Google Inc. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.r0adkll.deckbuilder.arch.ui.components.customtab

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.text.TextUtils
import timber.log.Timber
import java.util.*

/**
 * Helper class for Custom Tabs.
 */
object CustomTabsHelper {

    private const val TAG = "CustomTabsHelper"
    internal const val STABLE_PACKAGE = "com.android.chrome"
    internal const val BETA_PACKAGE = "com.chrome.beta"
    internal const val DEV_PACKAGE = "com.chrome.dev"
    internal const val LOCAL_PACKAGE = "com.google.android.apps.chrome"
    private const val EXTRA_CUSTOM_TABS_KEEP_ALIVE = "android.support.customtabs.extra.KEEP_ALIVE"
    private const val ACTION_CUSTOM_TABS_CONNECTION = "android.support.customtabs.action.CustomTabsService"
    private var sPackageNameToUse: String? = null

    /**
     * Goes through all apps that handle VIEW intents and have a warmup service. Picks
     * the one chosen by the user if there is one, otherwise makes a best effort to return a
     * valid package name.
     *
     * This is **not** threadsafe.
     *
     * @param context [Context] to use for accessing [PackageManager].
     * @return The package name recommended to use for connecting to custom tabs related components.
     */
    fun getPackageNameToUse(context: Context): String? {
        if (sPackageNameToUse != null) return sPackageNameToUse
        val pm: PackageManager = context.packageManager

        // Get default VIEW intent handler.
        val activityIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.example.com"))
        val defaultViewHandlerInfo: ResolveInfo? = pm.resolveActivity(activityIntent, 0)
        var defaultViewHandlerPackageName: String? = null
        if (defaultViewHandlerInfo != null) {
            defaultViewHandlerPackageName = defaultViewHandlerInfo.activityInfo.packageName
        }

        // Get all apps that can handle VIEW intents.
        val resolvedActivityList: List<ResolveInfo> = pm.queryIntentActivities(activityIntent, 0)
        val packagesSupportingCustomTabs: MutableList<String> = ArrayList()
        for (info in resolvedActivityList) {
            val serviceIntent = Intent()
            serviceIntent.action = ACTION_CUSTOM_TABS_CONNECTION
            serviceIntent.setPackage(info.activityInfo.packageName)
            if (pm.resolveService(serviceIntent, 0) != null) {
                packagesSupportingCustomTabs.add(info.activityInfo.packageName)
            }
        }

        // Now packagesSupportingCustomTabs contains all apps that can handle both VIEW intents
        // and service calls.
        applySupportingPackages(context, activityIntent, packagesSupportingCustomTabs, defaultViewHandlerPackageName)
        return sPackageNameToUse
    }

    private fun applySupportingPackages(
        context: Context,
        activityIntent: Intent,
        packagesSupportingCustomTabs: List<String>,
        defaultViewHandlerPackageName: String?
    ) {
        when {
            packagesSupportingCustomTabs.isEmpty() -> sPackageNameToUse = null
            packagesSupportingCustomTabs.size == 1 -> sPackageNameToUse = packagesSupportingCustomTabs[0]
            !TextUtils.isEmpty(defaultViewHandlerPackageName) &&
                !hasSpecializedHandlerIntents(context, activityIntent) &&
                packagesSupportingCustomTabs.contains(defaultViewHandlerPackageName) -> {
                sPackageNameToUse = defaultViewHandlerPackageName
            }
            packagesSupportingCustomTabs.contains(STABLE_PACKAGE) -> sPackageNameToUse = STABLE_PACKAGE
            packagesSupportingCustomTabs.contains(BETA_PACKAGE) -> sPackageNameToUse = BETA_PACKAGE
            packagesSupportingCustomTabs.contains(DEV_PACKAGE) -> sPackageNameToUse = DEV_PACKAGE
            packagesSupportingCustomTabs.contains(LOCAL_PACKAGE) -> sPackageNameToUse = LOCAL_PACKAGE
        }
    }

    /**
     * Used to check whether there is a specialized handler for a given intent.
     * @param intent The intent to check with.
     * @return Whether there is a specialized handler for the given intent.
     */
    @Suppress("TooGenericExceptionCaught", "ReturnCount")
    private fun hasSpecializedHandlerIntents(context: Context, intent: Intent): Boolean {
        try {
            val pm: PackageManager = context.packageManager
            val handlers: List<ResolveInfo>? = pm.queryIntentActivities(
                intent,
                PackageManager.GET_RESOLVED_FILTER)
            if (handlers == null || handlers.isEmpty()) {
                return false
            }
            for (resolveInfo in handlers) {
                val filter = resolveInfo.filter ?: continue
                if (filter.countDataAuthorities() == 0 || filter.countDataPaths() == 0) continue
                if (resolveInfo.activityInfo == null) continue
                return true
            }
        } catch (error: RuntimeException) {
            Timber.e(error, "Runtime exception while getting specialized handlers")
        }
        return false
    }

    /**
     * @return All possible chrome package names that provide custom tabs feature.
     */
    val packages: Array<String>
        get() = arrayOf("", STABLE_PACKAGE, BETA_PACKAGE, DEV_PACKAGE, LOCAL_PACKAGE)
}
