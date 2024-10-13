package com.example.capx.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capx.model.StockResponse
import com.example.capx.model.HistoricalDataResponse
import com.example.capx.repository.StockRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StockViewModel(private val repository: StockRepository) : ViewModel() {

    val stockData: MutableLiveData<StockResponse> = MutableLiveData()
    val historicalData: MutableLiveData<HistoricalDataResponse> = MutableLiveData()
    val priceDifference: MutableLiveData<Double?> = MutableLiveData()
    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val isLoading: MutableLiveData<Boolean> = MutableLiveData()

    fun fetchStockAndHistoricalData(symbol: String, region: String = "US") {
        isLoading.postValue(true) // Show loading state

        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Fetch stock data
                val stockResponse = repository.getStockData(symbol, region)
                stockData.postValue(stockResponse)

                // Fetch historical data
                val historicalResponse = repository.getHistoricalData(symbol)
                historicalData.postValue(historicalResponse)

                // Calculate percentage change
                val currentPrice = stockResponse.financialData.currentPrice.raw
                val yesterdayPrice = historicalResponse.prices[1].close

                if (yesterdayPrice != 0.0) {
                    val difference = ((currentPrice - yesterdayPrice) / yesterdayPrice) * 100
                    priceDifference.postValue(difference)
                } else {
                    priceDifference.postValue(null)
                }

            } catch (e: Exception) {
                // Handle stock not found or other errors
                if (e.message?.contains("404") == true) {
                    errorMessage.postValue("Stock not found") // Stock not found
                } else {
                    errorMessage.postValue("Error fetching data: ${e.message}") // Generic error
                }
            } finally {
                isLoading.postValue(false) // Stop loading
            }
        }//change
    }
}
