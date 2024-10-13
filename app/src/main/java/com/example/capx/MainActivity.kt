package com.example.capx

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import com.example.capx.model.ApiService
import com.example.capx.repository.StockRepository
import com.example.capx.viewmodel.StockViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var stockViewModel: StockViewModel
    private lateinit var searchEditText: EditText
    private lateinit var searchButton: ImageView
    private lateinit var stockSymbolTextView: TextView
    private lateinit var stockPriceTextView: TextView
    private lateinit var stockNameTextView: TextView
    private lateinit var stockChangePercentageTextView: TextView
    private lateinit var welcomeCard: CardView
    private lateinit var stockCard: CardView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://apidojo-yahoo-finance-v1.p.rapidapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Create an instance of ApiService
        val apiService = retrofit.create(ApiService::class.java)

        // Create an instance of StockRepository
        val stockRepository = StockRepository(apiService)

        // Initialize ViewModel
        stockViewModel = StockViewModel(stockRepository)

        // Initialize UI elements
        searchEditText = findViewById(R.id.searchtext)
        searchButton = findViewById(R.id.searchButton)
        stockSymbolTextView = findViewById(R.id.stockSymbol)
        stockPriceTextView = findViewById(R.id.stockPrice)
        stockNameTextView = findViewById(R.id.stockName)
        stockChangePercentageTextView = findViewById(R.id.stockChangePercentage)
        welcomeCard = findViewById(R.id.WelcomeMSG)
        stockCard = findViewById(R.id.stockCard)
        progressBar = findViewById(R.id.progressBar)

        // Set up the search button click listener
        searchButton.setOnClickListener {
            val stockSymbol = searchEditText.text.toString().trim()
            if (stockSymbol.isNotEmpty()) {
                showLoadingState() // Show loading state when search is initiated
                stockViewModel.fetchStockAndHistoricalData(stockSymbol)
            } else {
                Toast.makeText(this, "Please enter a stock symbol", Toast.LENGTH_SHORT).show()
            }
        }

        // Observe stock data
        stockViewModel.stockData.observe(this, Observer { stockResponse ->
            stockSymbolTextView.text = stockResponse.quoteType.symbol
            stockPriceTextView.text = "$${stockResponse.financialData.currentPrice.fmt}"
            stockNameTextView.text = stockResponse.price.longName
        })

        // Observe price difference (percentage change)
        stockViewModel.priceDifference.observe(this, Observer { difference ->
            stockChangePercentageTextView.text = String.format("%.2f%%", difference ?: 0.0)

            // Change text color based on percentage change
            if (difference != null) {
                stockChangePercentageTextView.setTextColor(if (difference > 0) android.graphics.Color.GREEN else android.graphics.Color.RED) // Green for positive, Red for negative
                showStockCard()
            }
        })

        // Observe error message (handling stock not found)
        stockViewModel.errorMessage.observe(this, Observer { error ->
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                showWelcomeCard()
            }
        })

        // Observe loading state
        stockViewModel.isLoading.observe(this, Observer { isLoading ->
            if (isLoading == true) {
                progressBar.visibility = ProgressBar.VISIBLE
                welcomeCard.visibility = CardView.GONE
                stockCard.visibility = CardView.GONE
            } else {
                progressBar.visibility = ProgressBar.GONE
            }
        })
    }

    // Method to show the stock card
    private fun showStockCard() {
        stockCard.visibility = CardView.VISIBLE
        welcomeCard.visibility = CardView.GONE
    }

    // Method to show the welcome card
    private fun showWelcomeCard() {
        welcomeCard.visibility = CardView.VISIBLE
        stockCard.visibility = CardView.GONE
    }

    // Method to show loading state
    private fun showLoadingState() {
        progressBar.visibility = ProgressBar.VISIBLE
        welcomeCard.visibility = CardView.GONE
        stockCard.visibility = CardView.GONE
    }
}
