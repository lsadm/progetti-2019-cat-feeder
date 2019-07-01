package com.greenjackets.prototipo


import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.widget.Toast
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_add.*
import java.io.ByteArrayOutputStream


class AddFragment : Fragment() {


    val QRCODE : Int=5    // TODO: aggiungere qrcode variabile

    val REQUEST_IMAGE_CAPTURE = 1 // serve per la fotocamera
    val firebaseDatabase = FirebaseDatabase.getInstance()       //Per accedere al database di firebase, per il rif
    val storage = FirebaseStorage.getInstance() //Per accedere allo storage , lo uso per creare il rif

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false)




    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        val storageRef= storage.reference // riferimento allo storage, non si usa mai questo perchè punta al root. Serve avere almeno 1 child
        var imagesRef: StorageReference? = storageRef.child("/Immagini prova/gatto") // questa punta ad una directory di prova creata su firebase
        // getRoot() e getParent() per spostarsi tra le directory


        val dataref = firebaseDatabase.getReference("Utente/"+QRCODE) // riferimento al database

        btn_fotocamera.setOnClickListener {
            // Imposta il funzionamento del pulsante per l'acqisizione dell'immagine
            // Creo un intent di tipo implicito per acquisire l'immagine
            val takePhoto = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takePhoto.resolveActivity(activity!!.packageManager)?.also {
                startActivityForResult(takePhoto, REQUEST_IMAGE_CAPTURE)
            }

        }

        //Bottone per aggiungere elementi su firebase
        btn_aggiungi.setOnClickListener {
            // quando clicco sul bottone allora

            val nome = txt_nome.text.toString()  // carico il nome
            val età = txt_eta.text.toString()       // carico l'età
            val peso = txt_peso.text.toString()     // ...
            val razza = txt_razza.text.toString()
            val checkvacc = check_vaccino.isChecked // mi restituisce il valore di vaccinato
            val checkster= check_sterile.isChecked // e sterilizzato
            val profpic= ProfilePic   // faccio riferimento all'image view
            val sesso=txt_sesso.text.toString()



            // codice per caricare l'immagine sullo storage
            val bitmap = (profpic.drawable as BitmapDrawable).bitmap    // Rendo l'imageview drawable in bitmap
            val baos = ByteArrayOutputStream()  // istanzio questa varaibile utile per caricare l'immagine
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos) // gli dico le dimensioni e la qualità
            val data = baos.toByteArray()  // Converto in bytes l'immagine

            var uploadTask = imagesRef?.putBytes(data)  // la invio con uploadTask. Ha le info che mi serve per gestire l'upload
            uploadTask?.addOnFailureListener {
                // Handle unsuccessful uploads

            }?.addOnSuccessListener {
                // taskSnapshot.metadata contains file metadata such as size, content-type, etc.

                Navigation.findNavController(btn_aggiungi).navigate(R.id.action_addFragment_to_homeFragment)
                Toast.makeText(getActivity(), "Profilo aggiunto con successo", Toast.LENGTH_SHORT).show()
            }



            dataref.child("Sesso").setValue(sesso)
            dataref.child("Nome").setValue(nome)
            dataref.child("Età").setValue(età)
            dataref.child("peso").setValue(peso)
            dataref.child("razza").setValue(razza)
            dataref.child("Vaccinato").setValue(checkvacc)
            dataref.child("Sterilizzato").setValue(checkster)



            //devo scrivere il contenuto nel database








        }
    }


    /**
     * Questo metodo viene invocato per gestire il risultato al ritorno da una activity
     * occorre determinare chi aveva generato la richiesta
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {     // Acquisizione immagine
            val immagineCatturata = data?.extras?.get("data") as Bitmap
            ProfilePic.setImageBitmap(immagineCatturata)

        }
    }





    }





