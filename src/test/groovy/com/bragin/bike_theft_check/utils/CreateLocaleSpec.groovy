package com.bragin.bike_theft_check.utils

import spock.lang.Specification

import static com.bragin.bike_theft_check.utils.CreateLocale.getLocale

class CreateLocaleSpec extends Specification {
    def "Should returned 'ru' locale" () {
        given:
            final def languageCode = "ru"
        when:
            final def result = getLocale(languageCode)
        then:
            final def expect = Locale.forLanguageTag(languageCode)
            result == expect
    }

    def "Should returned 'en' locale" () {
        given:
            final def languageCode = "lt"
        when:
            final def result = getLocale(languageCode)
        then:
            final def expect = Locale.ENGLISH
            result == expect
    }
}
