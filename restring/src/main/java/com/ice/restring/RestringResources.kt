package com.ice.restring

import android.content.res.Resources
import android.os.Build
import android.text.Html

/**
 * This is the wrapped resources which will be provided by Restring.
 * For getting strings and texts, it checks the strings repository first and if there's a new string
 * that will be returned, otherwise it will fallback to the original resource strings.
 */
class RestringResources(
    res: Resources,
    private val stringRepository: StringRepository,
    private val missingTranslationHandler: Restring.MissingTranslationHandler? = null
) : Resources(res.assets, res.displayMetrics, res.configuration) {


    @Throws(NotFoundException::class)
    override fun getString(id: Int): String {
        val value = getStringFromRepository(id)
        return value ?: super.getString(id)
    }

    @Throws(NotFoundException::class)
    override fun getString(id: Int, vararg formatArgs: Any): String {
        val value = getStringFromRepository(id)
        return if (value != null) {
            String.format(value, *formatArgs)
        } else super.getString(id, *formatArgs)
    }

    @Throws(NotFoundException::class)
    override fun getText(id: Int): CharSequence {
        val value = getStringFromRepository(id)
        return value?.let { fromHtml(it) } ?: super.getText(id)
    }

    override fun getText(id: Int, def: CharSequence): CharSequence {
        val value = getStringFromRepository(id)
        return value?.let { fromHtml(it) } ?: super.getText(id, def)
    }

    private fun getStringFromRepository(id: Int): String? {
        val lang = RestringUtil.currentLanguage
        val stringKey = getResourceEntryName(id)
        val str = stringRepository.getString(lang, stringKey)
        if (str == null) {
            val local: String? = try {
                super.getString(id)
            } catch (e: Exception) {
                null
            }
            missingTranslationHandler?.missingTranslation(lang, stringKey, local)
        }
        return str
    }

    private fun fromHtml(source: String): CharSequence {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Html.fromHtml(source)
        } else {
            Html.fromHtml(source, Html.FROM_HTML_MODE_COMPACT)
        }
    }
}
