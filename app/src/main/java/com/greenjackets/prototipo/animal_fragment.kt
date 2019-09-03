package com.greenjackets.prototipo


import android.content.ContentValues
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.Navigation
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.greenjackets.prototipo.RecycleView.Animale
import kotlinx.android.synthetic.main.fragment_animal_fragment.*
import java.lang.Exception
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.series.DataPoint
import android.graphics.Color
import android.widget.Toast
import com.jjoe64.graphview.GridLabelRenderer
import com.jjoe64.graphview.helper.StaticLabelsFormatter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class animal_fragment : Fragment() {

    val storageRef = FirebaseStorage.getInstance().getReference() //Per accedere allo storage , lo uso per creare il rif
    val database = FirebaseDatabase.getInstance().getReference() // creo il rif al database

    var dataRef : DatabaseReference? = null
    var imagRef: StorageReference? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_animal_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        arguments?.let {
            val animale: Animale? = it.getParcelable("animale")
            animale?.let {
                val  QRCODE= animale.qrcode

                imagRef = storageRef.child(QRCODE.toString() + "/gatto.jpg")
                dataRef = database.child(QRCODE.toString())

                /**Setto la data attuale*/
                val sdf = SimpleDateFormat("dd/M/yyyy")
                val currentDate = sdf.format(Date())
                textView5.text=getString(R.string.Recent_Info)+currentDate

                costruisciGrafico(QRCODE.toString())

                downloadFoto(imagRef)
                CaricaAnimale(it)

                btn_ImageCat.setOnClickListener {
                    val b = Bundle()
                    b.putParcelable("animale", animale)
                    Navigation.findNavController(it).navigate(R.id.action_animal_fragment_to_animal_dettagli, b)
                }

                Btn_pappa.setOnClickListener {
                    val g = Bundle()

                    g.putString("qrcode", QRCODE)
                    Navigation.findNavController(it).navigate(R.id.action_animal_fragment_to_foodFragment,g)
                }

                btn_tara.setOnClickListener {
                    database.child(QRCODE.toString()).child("Cibo").child("Tara").child("Taratura").setValue("true")
                    Toast.makeText(getActivity(),"Taratura in corso ", Toast.LENGTH_SHORT).show()
                }

            }

        }







    }
    private fun downloadFoto(imagRef : StorageReference?) {
        val picture = ArrayList<ImageView>() // arraylist di immageview
        picture.add(btn_ImageCat) //Passo alla lista l'Image Button View

        imagRef?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
            // Use the bytes to display the image
            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size) // offset resta zero altrimenti crasha
            picture.get(0).setImageBitmap(bitmap) // setto il valore dell'unico elemento della lista a quello decodificato
        }?.addOnFailureListener {
            // Handle any errors
        }


    }

    private fun CaricaAnimale(animale : Animale?){
        txt_età.text = animale?.Età.toString()
        txt_nome.text = animale?.Nome.toString()
        txt_peso.text = animale?.Peso.toString()
    }



    private fun costruisciGrafico(QRCODE : String) {

        /**Costruisco vettore y da database*/
        fun plotta(callback: (list: List<Double>) -> Unit){
            database.child(QRCODE+"/"+getString(R.string.Cibo_Cronologia)).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val list: MutableList<Double> = mutableListOf()
                    val children = dataSnapshot!!.children
                    children.forEach {
                        var valore = it.getValue(String::class.java)!!
                        list.add(valore.toDouble())
                    }
                    callback(list)
                }
                override fun onCancelled(databaseError: DatabaseError) {
                }
            })
        }
    plotta{
        try{
            /**Prelevo l'ora attuale */

            val c = Calendar.getInstance()
            val oraAttuale = c.get(Calendar.HOUR_OF_DAY)
            val minutiAttuale = c.get(Calendar.MINUTE)

            var indice=(oraAttuale)*2
            if(minutiAttuale>29){
                indice++
            }

            /**Costruisco il vettore di coppie (x,y) */

            val series = LineGraphSeries<DataPoint>()

            val y = arrayListOf<Double>()


            for(i in indice+1..47){//IERI
                y.add(it[i])
            }
            var contatore : Int =y.size

            for(i in 0..indice){//OGGI
                y.add(it[i])
            }

            for (i in 0 until 47) {
                val point = DataPoint(i.toDouble(), y[i])
                series.appendData(point, true, 48)
            }

            /**STRUTTURA
             * X[] = 0 1 2 ... 48
             * IT[] = valori da 0 a 47
             * INDICE = valori da 0 a 47 dove 17:40 -> (ora)*2 +(1 se dopo :30)
             */

            series.setColor(Color.parseColor("#1565C0"))
            series.setDrawDataPoints(true)
            series.setDataPointsRadius(10.toFloat())
            series.setThickness(8)
            series.setDrawBackground(true)
            series.setBackgroundColor(Color.parseColor("#4D2196F3"))

            grafico.getViewport().setXAxisBoundsManual(true)
            grafico.getViewport().setMinX(0.0)
            grafico.getViewport().setMaxX(48.0)

            val gridLabel : GridLabelRenderer= grafico.getGridLabelRenderer()
            gridLabel.setVerticalAxisTitle("Cibo nella ciotola [g]")

            /**Serve per nascondere i numeri sull'asse delle x*/
            val staticLabelsFormatter = StaticLabelsFormatter(grafico)
            staticLabelsFormatter.setHorizontalLabels(arrayOf(" ", " "))
            grafico.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter)

            /**Fai il grafico!*/
            grafico.addSeries(series)


            /**SETTO L'ASSE X CON UNA TEXT VIEW*/
            val disp1 : String
            val disp2 : String

            if(minutiAttuale>30){
                disp1= (oraAttuale+1).toString()+":00"
                disp2 = oraAttuale.toString()+":30"
            }else{
                disp1= oraAttuale.toString()+":30"
                disp2 = oraAttuale.toString()+":00"
            }
            txt_disp1.text=disp1
            txt_disp2.text=disp2

            /**Devo ora calcolare la mezzanotte e trasformrla in offset*/

            var dx=(grafico.width.toDouble()-guideline_offset.x+grafico.x)/48.0

            VerticalLine.x=guideline_offset.x+(contatore*dx).toFloat()
            txt_midnight.x=VerticalLine.x-20 //offset della text view

            txt_ieri.x=(guideline_offset.x+ VerticalLine.x)/2 - 20
            txt_oggi.x=(txt_midnight.x + grafico.width.toFloat())/2 - 20

        }catch(e: Exception){}
    }
    }
}

