package com.greenjackets.prototipo


import android.content.pm.ActivityInfo
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_animal_dettagli.*


class animal_dettagli : Fragment() {

    val storage = FirebaseStorage.getInstance() //Per accedere allo storage , lo uso per creare il rif

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
        activity?.requestedOrientation=(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR) //impedisce la rotazione dello schermo


        val storageRef= storage.reference
        val imagRef: StorageReference = storageRef.child("/Immagini prova/gatto")
        downloadFoto(imagRef)


    }
    
    private fun downloadFoto(imagRef : StorageReference) {
        val picture=ArrayList<ImageView>()
        picture.add(picture0)

        imagRef.getBytes(Long.MAX_VALUE).addOnSuccessListener { // Use the bytes to display the image
            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            picture.get(0).setImageBitmap(bitmap)
        }.addOnFailureListener {
            // Handle any errors
        }



    }

}
