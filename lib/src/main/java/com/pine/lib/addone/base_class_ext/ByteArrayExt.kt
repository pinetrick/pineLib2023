package com.pine.lib.addone.base_class_ext

fun ByteArray.indexOfArray(smallerArray: ByteArray): Int {
    for (i in 0 until this.size - smallerArray.size + 1) {
        var found = true
        for (j in smallerArray.indices) {
            if (this[i + j] != smallerArray[j]) {
                found = false
                break
            }
        }
        if (found) return i
    }
    return -1
}