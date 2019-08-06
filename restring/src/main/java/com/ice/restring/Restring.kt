package com.ice.restring

import android.content.Context
import android.content.ContextWrapper

/**
 * Entry point for Restring. it will be used for initializing Restring components, setting new strings,
 * wrapping activity context.
 */
object Restring {

    private var isInitialized = false
    private var stringRepository: StringRepository? = null
    private var viewTransformerManager: ViewTransformerManager? = null
    private var missingTranslationHandler: MissingTranslationHandler? = null

    /**
     * Initialize Restring with the specified configuration.
     *
     * @param context of the application.
     * @param config  of the Restring.
     */
    @JvmOverloads
    fun init(context: Context, config: RestringConfig = RestringConfig.default) {
        if (isInitialized) {
            return
        }
        missingTranslationHandler = config.missingTranslationHandler
        isInitialized = true
        initStringRepository(context, config)
        initViewTransformer()
    }

    /**
     * Wraps context of an activity to provide Restring features.
     *
     * @param base context of an activity.
     * @return the Restring wrapped context.
     */
    fun wrapContext(base: Context): ContextWrapper {
        return RestringContextWrapper.wrap(base, stringRepository!!, viewTransformerManager!!, missingTranslationHandler)
    }

    /**
     * Set strings of a language.
     *
     * @param language   the strings are for.
     * @param newStrings the strings of the language.
     */
    fun setStrings(language: String, newStrings: Map<String, String>) {
        stringRepository!!.setStrings(language, newStrings)
    }

    /**
     * Set a single string for a language.
     *
     * @param language the string is for.
     * @param key      the string key.
     * @param value    the string value.
     */
    fun setString(language: String, key: String, value: String) {
        stringRepository!!.setString(language, key, value)
    }

    private fun initStringRepository(context: Context, config: RestringConfig) {
        if (config.persist) {
            stringRepository = SharedPrefStringRepository(context)
        } else {
            stringRepository = MemoryStringRepository()
        }

        if (config.stringsLoader != null) {
            StringsLoaderTask(config.stringsLoader, stringRepository!!).run()
        }
    }

    private fun initViewTransformer() {
        viewTransformerManager = ViewTransformerManager()
        viewTransformerManager!!.registerTransformer(TextViewTransformer())
        viewTransformerManager!!.registerTransformer(ToolbarTransformer())
        viewTransformerManager!!.registerTransformer(SupportToolbarTransformer())
        viewTransformerManager!!.registerTransformer(BottomNavigationViewTransformer())
    }


    /**
     * Loader of strings skeleton. Clients can implement this interface if they want to load strings on initialization.
     * First the list of languages will be asked, then strings of each language.
     */
    interface StringsLoader {

        /**
         * Get supported languages.
         *
         * @return the list of languages.
         */
        val languages: List<String>

        /**
         * Get strings of a language as keys &amp; values.
         *
         * @param language of the strings.
         * @return the strings as (key, value).
         */
        fun getStrings(language: String): Map<String, String>
        fun onComplete() {

        }
    }

    interface MissingTranslationHandler {
        fun missingTranslation(language: String, key: String, localValue: String)
    }
}
