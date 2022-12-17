package com.pine.lib.addone.sharepreference

val defaultSpHelper = SpHelper()

fun spGet(key: String, default: String?): String? {
    return defaultSpHelper.get(key, default)
}

fun spSet(key: String, default: String?): String? {
    return defaultSpHelper.set(key, default)
}

fun spGet(key: String, default: Int): Int {
    return defaultSpHelper.get(key, default)
}

fun spSet(key: String, default: Int): Int {
    return defaultSpHelper.set(key, default)
}

fun spGet(key: String, default: Long): Long {
    return defaultSpHelper.get(key, default)
}

fun spSet(key: String, default: Long): Long {
    return defaultSpHelper.set(key, default)
}

fun spGet(key: String, default: Float): Float {
    return defaultSpHelper.get(key, default)
}

fun spSet(key: String, default: Float): Float {
    return defaultSpHelper.set(key, default)
}

fun spGet(key: String, default: Boolean): Boolean {
    return defaultSpHelper.get(key, default)
}

fun spSet(key: String, default: Boolean): Boolean {
    return defaultSpHelper.set(key, default)
}