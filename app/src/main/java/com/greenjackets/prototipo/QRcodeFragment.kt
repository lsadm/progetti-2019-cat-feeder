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
import com.greenjackets.prototipo.RecycleView.Animale
import com.journeyapps.barcodescanner.CaptureActivity
import kotlinx.android.synthetic.main.fragment_add.*
import java.io.FileInputStream
import java.lang.Exception
import java.lang.Thread.sleep
import java.util.concurrent.locks.ReentrantLock


class QRcodeFragment : Fragment() {


    var foundLocal=false


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
            scanner.setPrompt(getString(R.string.Scan_qrcode))
            scanner.setOrientationLocked(false)
           // scanner.setBarcodeImageEnabled(true)
            //scanner.setCameraId(0)
            scanner.initiateScan()


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {



        if(resultCode == Activity.RESULT_OK){
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result != null) {
                if (result.contents == null) {
                    Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG).show()
                } else {

                    /** Verifico se il QRCODE è gia memorizzato in locale
                     */

                    val filename="Qrcodes.txt"
                    var filestream = context?.openFileInput(filename)
                    var bufferedreader =filestream?.bufferedReader()

                    //Controllo se il qrcode è già stato scritto su file. Se è già scritto su file allora non devo controllare
                    //se sta sul database. Sicuro ci sarà!
                    bufferedreader?.forEachLine {
                        if(result.contents.toString()==it) {
                            foundLocal = true // se trovo il qrcode già scritto metto true
                            Toast.makeText(context, getString(R.string.already_scanned), Toast.LENGTH_LONG).show()
                            Navigation.findNavController(view!!).navigate(R.id.action_QRcodeFragment_to_homeFragment)
                        }
                    }

                    /** Verifico se il QRCODE è presente sul database
                     */
                    if(foundLocal==false) {
                        val database= FirebaseDatabase.getInstance().getReference(result.contents.toString()+getString(R.string.slash_animale)) //reference al database

                        database.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                val  animale = dataSnapshot.getValue(Animale::class.java)
                                try{
                                    if(animale?.qrcode==result.contents.toString()){
                                        // se l'animale già esiste allora devo solo scrivere sul file il QRCODE già scannerizzato
                                        val filename=getString(R.string.QRCODEStxt)
                                        val fileContents = result.contents.toString()+"\n" // Gli dico di scrivere nel file il QR
                                        context?.openFileOutput(filename, Context.MODE_APPEND).use {
                                            it?.write(fileContents.toByteArray()) // uso openFileOutput
                                        }
                                        //SCRITTO SU FILE il QRCODES, così quando torno a home_frament lo ricarica!
                                        Toast.makeText(context, getString(R.string.Already_registered), Toast.LENGTH_LONG).show()
                                        Navigation.findNavController(view!!).navigate(R.id.action_QRcodeFragment_to_homeFragment)

                                    }
                                    else{
                                            val b = Bundle()
                                            b.putString(getString(R.string.qrcode), result.contents)
                                            b.putString("Controllo","Qrcodex")
                                            Navigation.findNavController(view!!).navigate(R.id.action_QRcodeFragment_to_addFragment, b)
                                    }
                                } catch (e: Exception){}
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                            }
                        })
                    }
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }

        if(resultCode == Activity.RESULT_CANCELED)
        {
            Toast.makeText(context, getString(R.string.canceled), Toast.LENGTH_LONG).show()
            Navigation.findNavController(view!!).navigate(R.id.action_QRcodeFragment_to_homeFragment)
        }

    }


}