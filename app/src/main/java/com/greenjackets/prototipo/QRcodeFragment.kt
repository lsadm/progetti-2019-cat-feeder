package com.greenjackets.prototipo


import android.app.Activity
import android.content.ContentValues
import android.content.Context

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.fragment_qrcode.*
import com.google.zxing.integration.android.IntentResult
import com.journeyapps.barcodescanner.CaptureActivity
import kotlinx.android.synthetic.main.fragment_add.*
import java.lang.Exception


class QRcodeFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_qrcode, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



            val scanner = IntentIntegrator.forSupportFragment(this)
            scanner.setPrompt("Scansiona il codice QR posto sulla ciotola!")
            scanner.setOrientationLocked(false)
           // scanner.setBarcodeImageEnabled(true)
            scanner.setCameraId(0)

            scanner.initiateScan()


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK){
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result != null) {
                if (result.contents == null) {
                    Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()

                    //dobbiamo controllare se il qrcode è stato già utilizzato sul database!
                    val database= FirebaseDatabase.getInstance().getReference(result.contents.toString()) //reference al database
                    // questo reference punta direttamente al qrcode, se esiste!


                        //dichiaro quello che deve fare il valueeventlistener
                    val qrListener = object : ValueEventListener {  // creazione ValueEventListener

                        override fun onDataChange(dataSnapshot: DataSnapshot) {

                            val  animale = dataSnapshot.getValue(Animale::class.java)
                            try {
                                if(animale?.qrcode==null)
                                    Toast.makeText(context, "Crea il tuo primo animale :)", Toast.LENGTH_LONG).show()
                                if(animale?.qrcode==result.contents.toString()){
                                    // se l'animale già esiste allora devo solo scrivere sul file il QRCODE già scannerizzato
                                    val filename="Qrcodes.txt"
                                    val fileContents = result.contents.toString()+"\n" // Gli dico di scrivere nel file il QR
                                    context?.openFileOutput(filename, Context.MODE_APPEND).use {
                                        it?.write(fileContents.toByteArray()) // uso openFileOutput
                                    }
                                    //SCRITTO SU FILE il QRCODES, così quando torno a home_frament lo ricarica!
                                    //Toast.makeText(context, "L'animale è già presente sul database", Toast.LENGTH_LONG).show()
                                    Navigation.findNavController(view!!).navigate(R.id.action_QRcodeFragment_to_homeFragment)
                                    Navigation.findNavController(view!!).navigate(R.id.action_QRcodeFragment_to_homeFragment)
                                }


                            } catch (e: Exception) {}

                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // Getting Post failed, log a message
                            Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
                            // ...
                        }


                    }
                    //chiamata alla lettura del database
                    database.addListenerForSingleValueEvent(qrListener)
                    // una volta chiamato il addListener, se è presente il valore allora non prosegue nel addFragment, altrimenti deve tornare
                    //indietro
                    //    Passaggio dati al fragment successivo

                    val b = Bundle()
                    b.putString("qrcode", result.contents)
                    b.putString("Controllo","Qrcodex")    //TODO: Il nome dell'ogggetto andrebbe inserito in un solo punto!
                    Navigation.findNavController(view!!).navigate(R.id.action_QRcodeFragment_to_addFragment,b)
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }

        if(resultCode == Activity.RESULT_CANCELED){
            Toast.makeText(context, "Annullato...", Toast.LENGTH_LONG).show()
            Navigation.findNavController(view!!).navigate(R.id.action_QRcodeFragment_to_homeFragment)
        }

    }





}