package com.home.moviescope.view

import android.os.Build
import android.os.Handler
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.home.moviescope.BuildConfig
import com.home.moviescope.model.Category
import com.home.moviescope.model.CategoryDTO
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.MalformedURLException
import java.net.URL
import java.net.URLConnection
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

class Loader(
    private val listener: LoaderListener,
    private val category: Category
) {

    @RequiresApi(Build.VERSION_CODES.N)
    fun loadCategory(categoryReq: String?) =
        try {
            val uri =
                URL("https://api.themoviedb.org/3/movie/${categoryReq}?api_key=d59e795a41d61b167481e02a00402add&language=ru-RU&page=1")
            val handler = Handler()

            Thread {
                lateinit var urlConnection: HttpsURLConnection
                try {
                    urlConnection = (uri.openConnection() as HttpsURLConnection).apply {
                        requestMethod = "GET"
                        readTimeout = 10000

                        //     addRequestProperty("api_key", BuildConfig.MOVIEDB_API_KEY )
                    }
                    val bufferedReader =
                        BufferedReader(InputStreamReader(urlConnection.inputStream))

                    val response = getLines(bufferedReader)
                    val categoryDTO: CategoryDTO =
                        Gson().fromJson(response, CategoryDTO::class.java)

                    handler.post {
                        listener.onLoaded(categoryDTO, category)
                    }
                } catch (e: Exception) {
                    Log.e("", "Fail connection", e)
                    e.printStackTrace()
                    listener.onFailed(e)
                } finally {
                    urlConnection.disconnect()
                }
            }.start()
        } catch (e: MalformedURLException) {
            Log.e("", "Fail URI", e)
            e.printStackTrace()
            listener.onFailed(e)
        }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLines(reader: BufferedReader): String {
        return reader.lines().collect(Collectors.joining("\n"))
    }
    interface LoaderListener {
        fun onLoaded(categoryDTO: CategoryDTO,category: Category)
        fun onFailed(throwable: Throwable)
    }

}