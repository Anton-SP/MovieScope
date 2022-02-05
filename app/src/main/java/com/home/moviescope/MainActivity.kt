package com.home.moviescope

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.home.moviescope.R

class MainActivity : AppCompatActivity() {
 ///START
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val showDataButton: MaterialButton = findViewById(R.id.get_data_button)
        val showCycleButton: MaterialButton = findViewById(R.id.cycle_button)
        val text1: TextView = findViewById(R.id.text_data_1)
        val text2: TextView = findViewById(R.id.text_data_2)
        val text3: TextView = findViewById(R.id.text_data_3)

        //Создать Object.
        val repository = Repo.getMovieList()

        var movie1 = Movie("Blade", 7)
        var movie2 = Movie("Robocop", 6)

        repository.add(movie1)
        repository.add(movie2)
        //В Object вызвать copy и вывести значения скопированного класса на
        //экран.
        repository.add(repository[1].copy("Back to Future"))



        showDataButton.setOnClickListener {
            //data class с двумя свойствами и вывести их на экран приложения
            text1.text = movie1.title
            text2.text = movie2.rating.toString()
            //В Object вызвать copy и вывести значения скопированного класса на
            //экран.
            Toast.makeText(this, repository[2].title, Toast.LENGTH_SHORT).show()

        }
//Вывести значения из разных циклов в консоль,
        showCycleButton.setOnClickListener {
            var builder = StringBuilder()
            builder.append("Start <for each> cycle")
            builder.append("\n")
            for (movie in repository) {
                builder.append(movie.title).append(" ").append(movie.rating).append("\n")
            }
            builder.append("Start <for 1..10 step 2> cycle").append("\n")
            for (i in 1..10 step 2) {
                builder.append(i).append("\n")
            }
            builder.append("Start <for 5..1 step 1> cycle").append("\n")
            for (i in 5 downTo 1) {
                builder.append(i).append("\n")
            }

            text3.text = builder.toString();
        }

    }
}