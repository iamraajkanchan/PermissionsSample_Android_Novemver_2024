package com.permissions.chinky.utility

import java.util.logging.Level
import java.util.logging.Logger

object Utility {
    const val APPLICATION_TAG: String = "PermissionsSample"
    fun <T> printLog(klass: Class<T>, element: StackTraceElement, message: String?) {
        try {
            if (message != null) {
                println("$APPLICATION_TAG :: ${klass.simpleName} :: Line Number :: ${element.lineNumber} :: ${element.methodName} :: $message")
            } else {
                println("$APPLICATION_TAG :: ${klass.simpleName} :: Line Number :: ${element.lineNumber} :: ${element.methodName} :: Could not trace the message. Please try again!")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun <T> printExceptionLog(
        klass: Class<T>,
        element: StackTraceElement,
        logger: Logger,
        exception: Exception?
    ) {
        printLog(klass, element, exception?.message ?: "Could not trace the message. Please try again!")
        logger.log(Level.WARNING, exception?.message ?: "Could not trace the message. Please try again!")
    }
}