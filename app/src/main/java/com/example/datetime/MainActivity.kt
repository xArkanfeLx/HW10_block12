package com. example.datetime

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract.Colors
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {

    var imageUri:Uri? = null
    val GALLERY_REQUEST: Int = 302

    private lateinit var imgIV:ImageView
    private lateinit var nameET:EditText
    private lateinit var familyET:EditText
    private lateinit var dateET:EditText
    private lateinit var saveBTN:Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        init()
    }

    fun init(){
        imgIV = findViewById(R.id.imgIV)
        nameET = findViewById(R.id.nameET)
        familyET = findViewById(R.id.familyET)
        dateET = findViewById(R.id.dateET)
        saveBTN = findViewById(R.id.saveBTN)

        imgIV.setOnClickListener{
            val photoPicketIntent = Intent(Intent.ACTION_PICK)
            photoPicketIntent.type = "image/*"
            startActivityForResult(photoPicketIntent,GALLERY_REQUEST)
        }

        saveBTN.setOnClickListener{
            val date = dateET.text
            if(nameET.text.isEmpty() || familyET.text.isEmpty() || date.isEmpty()) return@setOnClickListener
            var textDate:MutableList<String> = date.toString().replace("[-./]".toRegex()," ").split(" ").toMutableList()
            val red = ContextCompat.getColor(this, R.color.red)
            if(textDate.size==1) {
                val txt = textDate[0]
                if(txt.length<6) {
                    dateET.setTextColor(red)
                    return@setOnClickListener
                }
                else {
                    textDate[0] = txt.substring(0, 2)
                    textDate.add(1, txt.substring(2, 4))
                    textDate.add(2, txt.substring(4))
                    if (textDate[2].length == 2) textDate[2] = "19${textDate[2]}"
                }
            }
            if(!checkDateText(textDate)) {
                dateET.setTextColor(red)
                return@setOnClickListener
            } else {
                val person = Person(nameET.text.toString(),familyET.text.toString(),textDate,imageUri.toString())
                intent = Intent(this, PersonInfoActivity::class.java)
                intent.putExtra(Person::class.java.simpleName, person)
                startActivity(intent)
                resetInfoFields()
            }
        }
    }

    fun resetInfoFields(){
        dateET.setTextColor(ContextCompat.getColor(this, R.color.white))
        dateET.text.clear()
        nameET.text.clear()
        familyET.text.clear()
        imgIV.setImageResource(R.drawable.person_default_icon)
    }

    fun checkDateText(date:MutableList<String>):Boolean {
        var result = true
        val day = date[0].toInt()
        val month = date[1].toInt()
        val year = date[2].toInt()
        if(day>31) result=false
        else if(day>30 && month%2==0) result=false
        else if(day>28 && month==2) {
            if(year%4!=0) result=false
        }
        return result
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        imgIV = findViewById(R.id.imgIV)
        when(requestCode) {
            GALLERY_REQUEST -> if(resultCode === RESULT_OK){
                imageUri = data?.data
                imgIV.setImageURI(imageUri)
            }
        }
    }
}