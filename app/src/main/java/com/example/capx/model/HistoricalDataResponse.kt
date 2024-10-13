package com.example.capx.model

data class HistoricalDataResponse(
    val prices: List<PriceData>
)

data class PriceData(
    val date: Long,
    val close: Double
)

