package com.example.datetime

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class PersonInfoActivity : AppCompatActivity() {

    var person: Person? = null
    private lateinit var toolbarTB: Toolbar
    private lateinit var imgIV: ImageView
    private lateinit var nameTV: TextView
    private lateinit var familyTV: TextView
    private lateinit var yearTV: TextView
    private lateinit var birthdayTV: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_person_info)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        init()

        if (person!!.img != null) imgIV.setImageURI(person!!.img!!.toUri())
        nameTV.text = person!!.name
        familyTV.text = person!!.family

        getInfoToBirthday()
    }

    private fun init() {
        person = intent.extras?.getSerializable(Person::class.java.simpleName) as Person?
        toolbarTB = findViewById(R.id.toolbarTB)
        setSupportActionBar(toolbarTB)

        imgIV = findViewById(R.id.imgIV)
        nameTV = findViewById(R.id.nameTV)
        familyTV = findViewById(R.id.familyTV)
        yearTV = findViewById(R.id.yearTV)
        birthdayTV = findViewById(R.id.birthdayTV)
    }

    private fun getInfoToBirthday() {
        val datePerson = person!!.date
        val dayPerson = datePerson[0].toInt()
        val monthPerson = datePerson[1].toInt()
        val yearPerson = datePerson[2].toInt()
        val dateNow = LocalDate.now()
        val dayNow = dateNow.dayOfMonth
        var monthNow = dateNow.monthValue
        var yearNow = dateNow.year

        val start = LocalDate.of(yearPerson, monthPerson, dayPerson)
        val start2 = LocalDate.of(yearNow + 1, monthPerson, dayPerson)
        val end = LocalDate.of(yearNow, monthNow, dayNow)

        if (dayPerson < dayNow) monthNow++
        if (monthNow > 12) {
            monthNow = 1
            yearNow++
        }
        val end2 = LocalDate.of(yearNow, monthNow, dayPerson)

        val years = ChronoUnit.YEARS.between(start, end)
        val months = ChronoUnit.MONTHS.between(end, start2)
        val days = ChronoUnit.DAYS.between(end, end2)

        yearTV.text = "Возраст в годах -> $years"
        birthdayTV.text = "До ДР месяцев -> $months и дней -> $days"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }
}