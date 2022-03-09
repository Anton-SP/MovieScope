package com.home.moviescope.utils

import android.os.Handler
import android.util.Log
import com.google.gson.Gson
import com.home.moviescope.BuildConfig
import com.home.moviescope.model.Category
import com.home.moviescope.model.CategoryDTO
import com.home.moviescope.model.Movie
import com.home.moviescope.recycler.CategoryAdapter
import okhttp3.*
import java.io.IOException
import java.util.ArrayList

const val MAIN_LINK = "https://api.themoviedb.org/3/movie/"
const val PROCESS_ERROR = "Обработка ошибки"

fun loadMovies(categoryList: List<Category>, id: Int, categoryAdapter: CategoryAdapter) {
    Log.d("@@@", "On start Https")
    val client = OkHttpClient()
    val builder: Request.Builder = Request.Builder()
    builder.url(
        MAIN_LINK +
                "${categoryList[id].requestName}?api_key=${BuildConfig.MOVIEDB_API_KEY}&language=ru-RU&page=1"
    )
    Log.d(
        "@@@",
        "URL=" + MAIN_LINK + categoryList[id].requestName + "?api_key=" + BuildConfig.MOVIEDB_API_KEY + "&language=ru-RU&page=1"
    )
    val request: Request = builder.build()
    val call: Call = client.newCall(request)
    call.enqueue(object : Callback {
        val handler: Handler = Handler()
        override fun onFailure(call: Call, e: IOException) {
            Log.d("@@@", PROCESS_ERROR + 11)
        }

        @Throws(IOException::class)
        override fun onResponse(call: Call, response: Response) {
            val serverResponse: String? = response.body()?.string()
            Log.d("@@@", serverResponse!!)
            if (response.isSuccessful && serverResponse != null) {
                Log.d("@@@", "CALL NEW RENDER DATA")
                handler.post {
                    convertDTOtoCategory(
                        Gson().fromJson(serverResponse, CategoryDTO::class.java),
                        categoryList[id]
                    )
                    categoryAdapter.notifyDataSetChanged()
                    Log.d("@@@", "CALL END")
                }
            } else {
                Log.d("@@@", PROCESS_ERROR + 22)//NEED SHOW SOMETHING
            }
        }
    })
}

private fun convertDTOtoCategory(categoryDTO: CategoryDTO, category: Category) {
    /**
     * так как наши данные по жанрам это массив закодированых ID
     * (раскодируем позже покане придумал как)
     * то для надежности пробегаемся по всем полученным данным
     * и добавляем эти объекты по однному
     * затем создаем Фильм и добавляем к нашей категории
     */
    for (i in categoryDTO.results.indices) {
        var ids: ArrayList<Int> = arrayListOf()
        for (j in categoryDTO.results[i].genre_ids.indices) {
            ids.add(categoryDTO.results[i].genre_ids[j])
        }
        var movie = Movie(
            ids,
            categoryDTO.results[i].title,
            categoryDTO.results[i].overview
        )
        Log.d("@@@@", category.name)
        Log.d("@@@@", movie.title!!)
        category.members.add(movie)
    }
}