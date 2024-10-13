package com.example.capx.model

data class StockResponse(
    val price: PriceInfo,
    val quoteType: QuoteType,
    val financialData: FinancialData
)

data class PriceInfo(
    val longName: String,
    val currencySymbol: String
)

data class QuoteType(
    val symbol: String
)

data class FinancialData(
    val currentPrice: Price
)

data class Price(
    val raw: Double,
    val fmt: String
)
