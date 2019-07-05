package com.greenjackets.prototipo


import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.pm.ActivityInfo
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_animal_dettagli.*
import com.google.firebase.database.DataSnapshot
import java.lang.Exception



class animal_dettagli : Fragment() {

    val storage = FirebaseStorage.getInstance() //Per accedere allo storage , lo uso per creare il rif
    val database = FirebaseDatabase.getInstance().getReference() // creo il rif al database

    val storageRef = storage.reference // reference allo storage
    val dataRef: DatabaseReference = database.child("2")
    val imagRef: StorageReference = storageRef.child("/Immagini prova/gatto")
    private var animale: Animale? = null  // lo uso per inizializzare la variabile animale
    lateinit var animalList: MutableList<Animale>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_animal_dettagli, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // setHasOptionsMenu(true)
        activity?.requestedOrientation =
            (ActivityInfo.SCREEN_ORIENTATION_NOSENSOR) //impedisce la rotazione dello schermo



        downloadFoto(imagRef)
        downloadDati()


        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(ContentValues.TAG, "onChildAdded:" + dataSnapshot.key!!)

                val animale = dataSnapshot.getValue(Animale::class.java)
                try {



                } catch (e: Exception) {}

            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(ContentValues.TAG, "onChildChanged: ${dataSnapshot.key}")
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                Log.d(ContentValues.TAG, "onChildRemoved:" + dataSnapshot.key!!)
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(ContentValues.TAG, "onChildMoved:" + dataSnapshot.key!!)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(ContentValues.TAG, "postComments:onCancelled", databaseError.toException())
                Toast.makeText(
                    context, "Failed to load comments.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


    }

    private fun downloadFoto(imagRef: StorageReference) {
        val picture = ArrayList<ImageView>() //Arraylist di immagini per caricare
        picture.add(picture0)

        imagRef.getBytes(Long.MAX_VALUE).addOnSuccessListener {
            // Use the bytes to display the image
            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            picture.get(0).setImageBitmap(bitmap)
        }.addOnFailureListener {
            // Handle any errors
        }
    }// DownloadFoto

    private fun downloadDati() {

        // Read from the database
        val postListener = object : ValueEventListener {  // creazione ValueEventListener

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val animale = dataSnapshot.getValue(Animale::class.java)
                try {

                    txt_età_dett.text = animale?.età.toString()
                    txt_nome_dett.text = animale?.nome.toString()
                    txt_peso_dett.text = animale?.peso.toString()
                    txt_sesso_dett.text = animale?.sesso.toString()
                    txt_steril_dett.text = animale?.sterilizzato.toString()
                    txt_vacc_dett.text = animale?.vaccinato.toString()
                    txt_razza_dett.text = animale?.razza.toString()


                } catch (e: Exception) {}

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        }

        dataRef.addValueEventListener(postListener)  // dichiarato sopra il ValueEventListener e poi chiamo la funzione passandoglielo



    }


}




