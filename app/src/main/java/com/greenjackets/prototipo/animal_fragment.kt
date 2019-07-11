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
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.greenjackets.prototipo.RecycleView.Animale
import kotlinx.android.synthetic.main.fragment_animal_fragment.*
import java.lang.Exception
import java.util.logging.Logger.global
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import android.R.attr.y
import android.R.attr.x
import android.R.attr.y
import android.R.attr.x
import java.nio.file.Files.size


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

                costruisciGrafico("280")

                downloadFoto(imagRef)
                downloadDati()

                btn_ImageCat.setOnClickListener {
                    val b = Bundle()
                    b.putParcelable("animale", animale)
                    Navigation.findNavController(it).navigate(R.id.action_animal_fragment_to_animal_dettagli, b)
                }
            }
        }




        Btn_pappa.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_animal_fragment_to_foodFragment)
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

    private fun downloadDati() {

        // Read from the database
        val postListener = object : ValueEventListener {  // creazione ValueEventListener

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val animale = dataSnapshot.getValue(com.greenjackets.prototipo.Animale::class.java)
                try {

                    txt_età.text = animale?.Età.toString()
                    txt_nome.text = animale?.Nome.toString()
                    txt_peso.text = animale?.Peso.toString()
                    val a= animale?.Sesso.toString()
                    val b= animale?.Sterilizzato.toString()
                    val c = animale?.Vaccinato.toString()
                    val d = animale?.razza.toString()

                } catch (e: Exception) {}

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        dataRef?.addValueEventListener(postListener)  // dichiarato sopra il ValueEventListener e poi chiamo la funzione passandoglielo
    }



    private fun costruisciGrafico(QRCODE : String) {

        /**Costruisco vettore y da database*/
        fun plotta(callback: (list: List<Double>) -> Unit){
            database.child(QRCODE+"/Cibo/Cronologia").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val list: MutableList<Double> = mutableListOf()
                    val children = dataSnapshot!!.children
                    children.forEach {
                        var valore = it.getValue(String::class.java)!!
                        if(valore!="null"){
                            list.add(valore.toDouble())
                        }else
                            list.add(0.0)
                    }
                    callback(list)
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(activity,"Fail to Load Data", Toast.LENGTH_SHORT).show()
                }
            })
        }
    plotta{
        try{
            /**Costruisco vettore x*/

            val x = arrayListOf<Double>()

            for(i in 0..47){
                x.add((i.toDouble())/2)     //Per generare da 0 a 24 ore
            }


            /**Costruisco il vettore di coppie (x,y) */

            val count = it.lastIndex
            val series = LineGraphSeries<DataPoint>()
            for (i in 0 until count) {
                val point = DataPoint(x[i], it[i])
                series.appendData(point, true, count)
            }

            grafico.addSeries(series)

        }catch(e: Exception){}
    }
    }
}

