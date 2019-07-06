package com.greenjackets.prototipo


import android.app.Activity
import android.content.Context

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.fragment_qrcode.*
import com.google.zxing.integration.android.IntentResult
import com.journeyapps.barcodescanner.CaptureActivity


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







                    //    Passaggio dati al fragment successivo





                    val b = Bundle()
                    b.putString("qrcode", result.contents)     //TODO: Il nome dell'ogggetto andrebbe inserito in un solo punto!
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