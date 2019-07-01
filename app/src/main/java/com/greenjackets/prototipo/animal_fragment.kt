package com.greenjackets.prototipo


import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.Navigation
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_animal_dettagli.*
import kotlinx.android.synthetic.main.fragment_animal_fragment.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class animal_fragment : Fragment() {

    val storage = FirebaseStorage.getInstance() //Per accedere allo storage , lo uso per creare il rif


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_animal_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val storageRef= storage.reference
        val imagRef: StorageReference = storageRef.child("/Immagini prova/gatto")
        downloadFoto(imagRef)


        Btn_pappa.setOnClickListener {

            Navigation.findNavController(it).navigate(R.id.action_animal_fragment_to_foodFragment)
        }


        btn_ImageCat.setOnClickListener {

            Navigation.findNavController(it).navigate(R.id.action_animal_fragment_to_animal_dettagli)
        }


    }
    private fun downloadFoto(imagRef : StorageReference) {
        val picture = ArrayList<ImageView>()
        picture.add(btn_ImageCat)

        imagRef.getBytes(Long.MAX_VALUE).addOnSuccessListener {
            // Use the bytes to display the image
            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            picture.get(0).setImageBitmap(bitmap)
        }.addOnFailureListener {
            // Handle any errors
        }


    }

}

