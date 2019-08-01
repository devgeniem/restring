package com.ice.restring

/**
 * Contains configuration properties for initializing Restring.
 */
class RestringConfig private constructor(
    val persist: Boolean,
    val stringsLoader: Restring.StringsLoader?,
    val missingTranslationHandler: Restring.MissingTranslationHandler?
) {

    data class Builder(
        private var persist: Boolean = false,
        private var stringsLoader: Restring.StringsLoader? = null,
        private var missingTranslationHandler: Restring.MissingTranslationHandler? = null
    ) {

        fun persist(persist: Boolean) = apply {
            this.persist = persist
        }
        fun stringsLoader(loader: Restring.StringsLoader) = apply {
            this.stringsLoader = loader
        }

        fun missingTranslationHandler(handler: Restring.MissingTranslationHandler) = apply {
            this.missingTranslationHandler = handler
        }

        fun build() = RestringConfig(persist, stringsLoader, missingTranslationHandler)
    }

    companion object {
        @JvmStatic
        val default: RestringConfig
            get() = Builder()
                .persist(true)
                .build()
    }
}