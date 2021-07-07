package com.example.quranreader

import android.media.AudioManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import com.example.quranreader.data.Ayah
import com.example.quranreader.data.Quran
import com.example.quranreader.data.RetrofitService
import com.example.quranreader.data.RoomService
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_aya_detail.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class AyaDetailActivity : AppCompatActivity() {
    private var audio: String? = null
    private lateinit var numPageView :TextView
    private lateinit var translationView :TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aya_detail)

        var ayahView = findViewById<TextView>(R.id.aya)
        var idAyahView = findViewById<TextView>(R.id.id_ayah)
        var numSouratView = findViewById<TextView>(R.id.num_sourat)
        var numAyahView = findViewById<TextView>(R.id.num_ayah)
        var readersSpinner = findViewById<Spinner>(R.id.reader_spinner)
        var nbMotsView = findViewById<TextView>(R.id.nb_mots)


        var fabPlay = findViewById<FloatingActionButton>(R.id.fabPlay)

        var fabStop = findViewById<FloatingActionButton>(R.id.fabStop)
        fabStop.visibility = View.GONE

        var aya:Quran


        var ch=intent.getStringExtra("idAya")

        aya= RoomService.getDataBase(applicationContext).getQuranDAO().getAyaById(ch)
        ayahView.text=aya.texteAya
        idAyahView.text=ch
        numSouratView.text=aya.idSourat.toString()
        numAyahView.text=aya.numAya.toString()
        nbMotsView.text=aya.nbWord.toString()
        getAudioAndPage (ch,"1")
       getTranslation (ch,"149")
        readersSpinner.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                getAudioAndPage (ch,(position+1).toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        val mediaPlayer = MediaPlayer()
        fabPlay.setOnClickListener {
            try {
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
                mediaPlayer.setDataSource("https://verses.quran.com/"+audio)
                mediaPlayer.prepare()
                mediaPlayer.start()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            fabPlay.visibility = View.GONE
            fabStop.visibility = View.VISIBLE
        }

        fabStop.setOnClickListener {
            mediaPlayer.stop()
            mediaPlayer.reset()
            fabPlay.visibility = View.VISIBLE
            fabStop.visibility = View.GONE
        }
    }
    fun getAudioAndPage (ayaKey:String?,readerKey:String){
        var call =RetrofitService.endpoint.getDoctor(ayaKey,readerKey)
        call!!.enqueue(object : Callback<Ayah> {
            override fun onResponse(call: Call<Ayah>, response: Response<Ayah>) {
                if (response.isSuccessful) {
                    var ayah= response.body()!!

                     numPageView = findViewById<TextView>(R.id.num_page)

                    numPageView.text=ayah.verse.page_number.toString()

                    audio=ayah.verse.audio.url
                    println("TTTTTTTTTTTTTTTTTTTTTTT is the audio $audio")
                }

            }

            override fun onFailure(call: Call<Ayah>, t: Throwable) {

                println(t.message)

            }
        })
    }

    fun getTranslation (ayaKey:String?,translatiobKey:String){
        var call =RetrofitService.endpoint.getTranslation(ayaKey,translatiobKey)
        call!!.enqueue(object : Callback<Ayah> {
            override fun onResponse(call: Call<Ayah>, response: Response<Ayah>) {
                if (response.isSuccessful) {
                    var ayah= response.body()!!

                     translationView = findViewById<TextView>(R.id.translation)

                    translationView.text=ayah.verse.translations.get(0).text

                }

            }

            override fun onFailure(call: Call<Ayah>, t: Throwable) {

                println(t.message)

            }
        })
    }

}