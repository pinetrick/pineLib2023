package com.pine.lib.provider.server_socket

data class RequestData(
    var method: String = "",
    var urls: List<String> = ArrayList(),
    var args: MutableMap<String, String> = mutableMapOf()
)
