package com.home.moviescope.view

import android.app.IntentService
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import com.home.moviescope.BuildConfig.MOVIEDB_API_KEY
import com.home.moviescope.model.CategoryDTO
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection
/**
 * 2 парамтера
 * 1.CATEGORY_REQUEST_NAME для запроса нужной ктаегории
 * 2.CATEGORY_ID это id категории из репозитория
 * т.к.сервис обрабатывает данные только для 1 категоории необходимо знать к какой ктегории
 * прикрепиьт результат работы сервиса
 */
const val CATEGORY_REQUEST_NAME = "CATEGORY_REQUEST_NAME"
const val CATEGORY_ID = "CATEGORY_ID"

class LoadService(name: String = "LoadService") : IntentService(name) {

    private val broadcastIntent = Intent(LOAD_INTENT_FILTER)

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onHandleIntent(intent: Intent?) {
        if (intent == null) {
            onEmptyIntent()
        } else {
            val requestName = intent.getStringExtra(CATEGORY_REQUEST_NAME)
            val id = intent.getIntExtra(CATEGORY_ID, -1)
            if (requestName == null && id != -1) {
                onEmptyData()
            } else {
                Log.d("@@@", "Working service" + id)
                loadCategory(requestName, id)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun loadCategory(requestName: String?, id: Int?) {
        try {
            val uri =
                URL("https://api.themoviedb.org/3/movie/${requestName}?api_key=${MOVIEDB_API_KEY}&language=ru-RU&page=1")
            lateinit var urlConnection: HttpsURLConnection
            try {
                urlConnection = (uri.openConnection() as HttpsURLConnection).apply {
                    requestMethod = "GET"
                    readTimeout = 10000
                }
                val categoryDTO: CategoryDTO =
                    Gson().fromJson(
                        getLines(BufferedReader(InputStreamReader(urlConnection.inputStream))),
                        CategoryDTO::class.java
                    )
                Log.d("@@@", "DTO from JSON=" + categoryDTO.results[0].title)
                onResponse(categoryDTO, id)
            } catch (e: Exception) {

                onErrorRequest(e.message ?: "Empty error")
            } finally {
                urlConnection.disconnect()
            }
        } catch (e: MalformedURLException) {
            onMalformedURLException()
        }
    }

    private fun getLines(bufferedReader: BufferedReader): String {
        return bufferedReader.lines().collect(Collectors.joining("\n"))
    }

    private fun onResponse(categoryDTO: CategoryDTO, id: Int?) {
        val results = categoryDTO.results
        if (results.size == 0) {
            onEmptyResponse()
        } else {
            Log.d("@@@", "On Response DTO =" + categoryDTO.results[0].title)
            onSuccessResponse(categoryDTO, id)
        }
    }

    private fun onSuccessResponse(categoryDTO: CategoryDTO, id: Int?) {
        Log.d("@@@", "BUNDLE DTO =" + categoryDTO.results.toString())
        broadcastIntent.putExtra(ID_EXTRA, id)
        broadcastIntent.putExtra(RESULTS_DTO_EXTRA, categoryDTO.results)
        putLoadResult(RESPONSE_SUCCESS_EXTRA)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun onMalformedURLException() {
        putLoadResult(URL_MALFORMED_EXTRA)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun onErrorRequest(error: String) {
        putLoadResult(REQUEST_ERROR_EXTRA)
        broadcastIntent.putExtra(REQUEST_ERROR_MESSAGE_EXTRA, error)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun onEmptyResponse() {
        putLoadResult(RESPONSE_EMPTY_EXTRA)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun onEmptyIntent() {
        putLoadResult(INTENT_EMPTY_EXTRA)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun onEmptyData() {
        putLoadResult(DATA_EMPTY_EXTRA)
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)
    }

    private fun putLoadResult(result: String) {
        broadcastIntent.putExtra(LOAD_RESULT_EXTRA, result)
    }

}




