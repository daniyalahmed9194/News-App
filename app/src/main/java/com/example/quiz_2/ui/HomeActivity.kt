package com.example.quiz_2.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quiz_2.R
import com.example.quiz_2.adapter.NewsAdapter
import com.example.quiz_2.databinding.ActivityHomeBinding
import com.example.quiz_2.model.Article
import com.example.quiz_2.model.Country
import com.example.quiz_2.network.CountriesRetrofitInstance
import com.example.quiz_2.network.RetrofitInstance
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var newsAdapter: NewsAdapter
    private var countries: List<Country> = emptyList()
    private var selectedCountryCode: String = "us"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""

        newsAdapter = NewsAdapter(emptyList())
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = newsAdapter

        binding.btnRetry.setOnClickListener { loadNews(selectedCountryCode) }

        binding.spinnerCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (countries.isNotEmpty()) {
                    selectedCountryCode = countries[position].cca2.lowercase()
                    loadNews(selectedCountryCode)
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        loadCountries()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.action_refresh) {
            loadNews(selectedCountryCode)
            true
        } else super.onOptionsItemSelected(item)
    }

    private fun loadCountries() {
        lifecycleScope.launch {
            try {
                val result = CountriesRetrofitInstance.api.getAllCountries()
                countries = result.sortedBy { it.name.common }
                val names = countries.map { it.name.common }
                val adapter = ArrayAdapter(this@HomeActivity, android.R.layout.simple_spinner_item, names)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerCountry.adapter = adapter

                val defaultIdx = countries.indexOfFirst { it.cca2.equals("US", true) }
                if (defaultIdx >= 0) binding.spinnerCountry.setSelection(defaultIdx)
                else loadNews(selectedCountryCode)
            } catch (e: Exception) {
                showError()
            }
        }
    }

    private fun loadNews(countryCode: String) {
        showLoading()
        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.getTopHeadlines(
                    country = countryCode,
                    apiKey = getString(R.string.api_key)
                )
                val articles: List<Article> = response.articles
                if (articles.isEmpty()) {
                    showEmpty()
                } else {
                    showContent()
                    newsAdapter.updateData(articles)
                }
            } catch (e: Exception) {
                showError()
            }
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
        binding.errorLayout.visibility = View.GONE
    }

    private fun showContent() {
        binding.progressBar.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
        binding.errorLayout.visibility = View.GONE
    }

    private fun showError() {
        binding.progressBar.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
        binding.errorLayout.visibility = View.VISIBLE
        binding.txtError.text = getString(R.string.error_generic)
    }

    private fun showEmpty() {
        binding.progressBar.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
        binding.errorLayout.visibility = View.VISIBLE
        binding.txtError.text = getString(R.string.no_news)
    }
}
