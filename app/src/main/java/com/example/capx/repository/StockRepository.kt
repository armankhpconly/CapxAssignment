package com.example.capx.repository

import com.example.capx.model.ApiService
import com.example.capx.model.HistoricalDataResponse
import com.example.capx.model.StockResponse

class StockRepository(private val apiService: ApiService) {

    suspend fun getStockData(symbol: String, region: String): StockResponse {
        return apiService.getStockData(
            region = "US",
            symbol = symbol,
            apiKey = "42785feaebmsh3efc6d33a485c7ap1dcd31jsn36baad49607b"
        )
    }
    suspend fun getHistoricalData(symbol: String): HistoricalDataResponse {
        return apiService.getHistoricalData(
            region = "US",
            symbol = symbol,
            apiKey = "42785feaebmsh3efc6d33a485c7ap1dcd31jsn36baad49607b"
        )    }
}
