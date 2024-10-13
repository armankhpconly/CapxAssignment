package com.example.capx.model

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiService {
    @GET("stock/v2/get-statistics")
    suspend fun getStockData(
        @Query("region") region: String,
        @Query("symbol") symbol: String,
        @Header("x-rapidapi-key") apiKey: String,
        @Header("x-rapidapi-host") host: String = "apidojo-yahoo-finance-v1.p.rapidapi.com"
    ): StockResponse

    @GET("stock/v3/get-historical-data")
    suspend fun getHistoricalData(
        @Query("symbol") symbol: String,
        @Query("region") region: String = "US",
        @Header("x-rapidapi-key") apiKey: String,
        @Header("x-rapidapi-host") host: String = "apidojo-yahoo-finance-v1.p.rapidapi.com"
    ): HistoricalDataResponse
}
