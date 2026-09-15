package com.example.strovo.data.model

data class GetActivityMetaData(
    val allNull: Boolean,
    val anomalies: Any,
    val custom: Boolean,
    val `data`: List<Any>,
    val data2: List<Any>?,
    val name: Any,
    val type: String,
    val valueType: String,
    val valueTypeIsArray: Boolean
)