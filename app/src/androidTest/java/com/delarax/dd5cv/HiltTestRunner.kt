package com.delarax.dd5cv

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

/**
 * A custom instrumented test runner that supports Hilt, to replace AndroidJUnitRunner.
 * Hilt provides a test application for this called HiltTestApplication.
 *
 * In order for this class to be used as the default test runner, you have to tell
 * the "testInstrumentation" line in your module's gradle file to use this class.
 *
 * To set up a test class to run with Hilt, the class must have the `@HiltAndroidTest`
 * annotation and include the HiltAndroidRule, like so:
 *
 * @sample
 *      @HiltAndroidTest
 *      @RunWith(AndroidJUnit4::class)
 *      class ExampleInstrumentedTest {
 *
 *          @get:Rule
 *          var hiltRule = HiltAndroidRule(this)
 *          ...
 *      }
 *
 * To inject variables into a test, use the `@Inject` annotation and call the
 * `hiltRule.inject()` method in a setup function:
 *
 * @sample
 *      @Inject
 *      lateinit var exampleService: ExampleService
 *
 *      @Before
 *      fun setup() {
 *          hiltRule.inject()
 *      }
 */
class HiltTestRunner : AndroidJUnitRunner() {
    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }
}