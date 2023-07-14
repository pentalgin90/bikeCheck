package com.bragin.bike_theft_check.utils

import spock.lang.Specification

import static com.bragin.bike_theft_check.utils.CreateLocale.getLocale

class CreateLocaleSpec extends Specification {
    def "Should returned 'ru' locale" () {
        given:
            def languageCode = "ru"
        when:
            def result = getLocale(languageCode)
        then:
            def expect = Locale.forLanguageTag(languageCode)
            result.equals(expect)
    }

    def "Should returned 'en' locale" () {
        given:
        def languageCode = "lt"
        when:
        def result = getLocale(languageCode)
        then:
        def expect = Locale.ENGLISH
        result.equals(expect)
    }
}
