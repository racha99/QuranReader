package com.example.quranreader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.quranreader.data.Ayah
import com.example.quranreader.data.Quran
import com.example.quranreader.data.RetrofitService
import com.example.quranreader.data.RoomService
import kotlinx.android.synthetic.main.activity_aya_detail.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AyaDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aya_detail)

        var ayahView = findViewById<TextView>(R.id.aya)
        var idAyahView = findViewById<TextView>(R.id.id_ayah)
        var numSouratView = findViewById<TextView>(R.id.num_sourat)
        var numAyahView = findViewById<TextView>(R.id.num_ayah)
        var numPageView = findViewById<TextView>(R.id.num_page)
        var nbMotsView = findViewById<TextView>(R.id.nb_mots)

        var aya:Quran


        var ch=intent.getStringExtra("idAya")

        aya= RoomService.getDataBase(applicationContext).getQuranDAO().getAyaById(ch)
        println("THHHHIS ID AYA §§§§§§ $ch")
        var call =RetrofitService.endpoint.getDoctor(ch)
        call!!.enqueue(object : Callback<Ayah> {
            override fun onResponse(call: Call<Ayah>, response: Response<Ayah>) {
                if (response.isSuccessful) {
                    var ayah= response.body()!!
                    ayahView.text=aya.texteAya
                     idAyahView.text=ch
                     numSouratView.text=aya.idSourat.toString()
                     numAyahView.text=aya.numAya.toString()
                     numPageView.text=ayah.verse.page_number.toString()

                     nbMotsView.text=aya.nbWord.toString()
                }

            }

            override fun onFailure(call: Call<Ayah>, t: Throwable) {

                println(t.message)

            }
        })
    }
}