package com.example.robustatask.data.helpers.resourcesHelper

import android.content.Context

class ResourcesHelperImp(private val context: Context) : ResourcesHelper {

    override fun getStringRes(resId: Int): String = context.getString(resId)
}