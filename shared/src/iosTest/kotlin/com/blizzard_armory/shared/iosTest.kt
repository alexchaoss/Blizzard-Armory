package com.blizzard_armory.shared

import kotlin.test.Test
import kotlin.test.assertTrue

class GreetingTest {

    @Test
    fun testExample() {
        assertTrue(Greeting().greeting().contains("iOS"), "Check iOS is mentioned")
    }
}