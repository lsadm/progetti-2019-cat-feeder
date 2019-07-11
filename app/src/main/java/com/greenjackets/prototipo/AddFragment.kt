package com.greenjackets.prototipo


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.greenjackets.prototipo.RecycleView.Animale
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_animal_dettagli.*
import java.io.ByteArrayOutputStream


class AddFragment : Fragment() {

    private val REQUEST_IMAGE_CAPTURE = 1 // serve per la fotocamera
    private val firebaseDatabase = FirebaseDatabase.getInstance()       //Per accedere al database di firebase, per il rif
    private val storageRef = FirebaseStorage.getInstance().getReference() // riferimento allo storage, non si usa mai questo perchè punta al root. Serve avere almeno 1 child


    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val spinner_options = arrayOf("Maschio", "Femmina")//Spinner per selezionare il sesso
        spinner.adapter = ArrayAdapter<String>(context,android.R.layout.simple_list_item_1, spinner_options)


        // Estraggo il parametro (birra) dal bundle ed eventualmente lo visualizzo
        arguments?.let {
            val qrcode: String? = it?.getString("qrcode")   //TODO: Il nome dovrebbe essere in un unico punto!!
            val controllo: String? = it?.getString("Controllo")
            val animale: Animale? = it?.getParcelable("animale")

            controllo?.let{
            qrcode?.let {/**DA SCHERMATA QRCODE*/

                val QRCODE = it
                val imagesRef: StorageReference? = storageRef.child(QRCODE.toString() + "/") // questa punta ad una directory di prova creata su firebase
                val dataref = firebaseDatabase.getReference(QRCODE.toString()) // riferimento al database

                val filename = "Qrcodes.txt" // nome del file usato anche dopo eventualmente

                spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        spinnerResult.text = "Seleziona:"
                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        spinnerResult.text = spinner_options[position]
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
                    val checkster = check_sterile.isChecked // e sterilizzato
                    val profpic = ProfilePic   // faccio riferimento all'image view
                    val sesso = spinnerResult.text.toString()

                    var animale: Animale? = null

                    animale?.Età = età
                    animale?.Nome = nome
                    animale?.Peso = peso
                    animale?.Sesso = sesso
                    animale?.Sterilizzato = checkster.toString()
                    animale?.Vaccinato = checkvacc.toString()
                    animale?.razza = razza
                    animale?.qrcode = QRCODE


                    if (nome?.isNotEmpty() && età?.isNotEmpty() && peso?.isNotEmpty() && sesso?.isNotEmpty() && razza?.isNotEmpty() && QRCODE.toString() != "null") {
                        dataref.child("Animale").setValue(
                            Animale(
                                età,
                                nome,
                                sesso,
                                checkster.toString(),
                                checkvacc.toString(),
                                peso,
                                razza,
                                QRCODE
                            )
                        )

                        /**Inizializzo i valori di Cibo/Cronologia per il grafico*/

                        for (i in 0..47){
                            var ora= i/2
                            var minuti = (i%2)*3

                            var finale= ora.toString()+":"+minuti.toString()+"0"
                            dataref.child("Cibo/Cronologia").child(finale).setValue("null")
                        }

                        /**Inizializzo i valori di Cibo/Sheduling*/

                        for(i in 7..19 step 3){
                            dataref.child("Cibo/Scheduling").child(i.toString()+":00").child("abilitato").setValue("null")
                            dataref.child("Cibo/Scheduling").child(i.toString()+":00").child("quantità").setValue("null")
                        }

                        /**codice per caricare l'immagine sullo storage*/
                        val bitmap =(profpic.drawable as? BitmapDrawable)?.bitmap    // Rendo l'imageview drawable in bitmap
                        val baos = ByteArrayOutputStream()  // istanzio questa varaibile utile per caricare l'immagine
                        bitmap?.compress(Bitmap.CompressFormat.JPEG, 80, baos) // gli dico le dimensioni e la qualità
                        val data = baos.toByteArray()  // Converto in bytes l'immagine

                        if (data.isNotEmpty()) {

                            var uploadTask = imagesRef?.child("gatto.jpg")
                                ?.putBytes(data)  // la invio con uploadTask. Ha le info che mi serve per gestire l'upload
                            uploadTask?.addOnFailureListener {
                                Toast.makeText(getActivity(), "Impossibile caricare la foto", Toast.LENGTH_SHORT).show()

                            }?.addOnCompleteListener {

                                Toast.makeText(getActivity(), "Profilo aggiunto con successo", Toast.LENGTH_SHORT).show()
                            }?.addOnSuccessListener {


                                // prima di tornare alla schermata principale mi salvo in locale il qrcode usato
                                //filename e content file già usati!

                               val fileContents = QRCODE + "\n" // cosa scrivere nel file
                                context?.openFileOutput(filename, Context.MODE_APPEND).use {
                                    it?.write(fileContents.toByteArray()) // uso openFileOutput


                                }
                                Navigation.findNavController(btn_aggiungi).navigate(R.id.action_addFragment_to_homeFragment)
                            }
                        }
                    } else {
                        if (QRCODE != null)
                            Toast.makeText(getActivity(), "Completa tutti i campi!", Toast.LENGTH_SHORT).show()
                        if (QRCODE == "null")
                            Toast.makeText(getActivity(), "Il QRCODE è null", Toast.LENGTH_SHORT).show()
                    }

                }

            }
            }
                ?: run {/**UTILIZZATO PER FARE LA MODIFICA*/
                    Toast.makeText(getActivity(), "Vieni dal fragment modifica", Toast.LENGTH_SHORT).show()

                    animale.let {

                        val imagRef= storageRef.child(animale?.qrcode.toString()+"/gatto.jpg")
                        val dataref = firebaseDatabase.getReference(animale?.qrcode.toString()) // riferimento al database

                        spinnerResult.text = animale?.Sesso

                        txt_nome.setText(animale?.Nome)
                        txt_eta.setText(animale?.Età)
                        txt_peso.setText(animale?.Peso)
                        txt_razza.setText(animale?.razza)
                        if(animale?.Vaccinato == "true"){
                            check_vaccino.isChecked=true
                        }
                        if(animale?.Sterilizzato == "true"){
                            check_sterile.isChecked=true
                        }

                        downloadFoto(imagRef)

                        btn_aggiungi.setOnClickListener {
                            // quando clicco sul bottone allora

                            val nome = txt_nome.text.toString()  // carico il nome
                            val età = txt_eta.text.toString()       // carico l'età
                            val peso = txt_peso.text.toString()     // ...
                            val razza = txt_razza.text.toString()
                            val checkvacc = check_vaccino.isChecked // mi restituisce il valore di vaccinato
                            val checkster = check_sterile.isChecked // e sterilizzato
                            val profpic = ProfilePic   // faccio riferimento all'image view
                            val sesso = spinnerResult.text.toString()
                            val QRCODE = animale?.qrcode.toString()

                            var animalupload: Animale? = null

                            animalupload?.Età = età
                            animalupload?.Nome = nome
                            animalupload?.Peso = peso
                            animalupload?.Sesso = sesso
                            animalupload?.Sterilizzato = checkster.toString()
                            animalupload?.Vaccinato = checkvacc.toString()
                            animalupload?.razza = razza
                            animalupload?.qrcode = animale?.qrcode.toString()


                            if (nome?.isNotEmpty() && età?.isNotEmpty() && peso?.isNotEmpty() && sesso?.isNotEmpty() && razza?.isNotEmpty()) {
                                dataref.child("Animale").setValue(
                                    Animale(
                                        età,
                                        nome,
                                        sesso,
                                        checkster.toString(),
                                        checkvacc.toString(),
                                        peso,
                                        razza,
                                        QRCODE
                                    )
                                )


                                // codice per caricare l'immagine sullo storage
                                val bitmap =
                                    (profpic.drawable as? BitmapDrawable)?.bitmap    // Rendo l'imageview drawable in bitmap
                                val baos = ByteArrayOutputStream()  // istanzio questa varaibile utile per caricare l'immagine
                                bitmap?.compress(Bitmap.CompressFormat.JPEG, 80, baos) // gli dico le dimensioni e la qualità
                                val data = baos.toByteArray()  // Converto in bytes l'immagine

                                if (data.isNotEmpty()) {

                                    var uploadTask = imagRef.putBytes(data)  // la invio con uploadTask. Ha le info che mi serve per gestire l'upload
                                    uploadTask?.addOnFailureListener {
                                        Toast.makeText(getActivity(), "Impossibile caricare la foto", Toast.LENGTH_SHORT).show()

                                    }?.addOnCompleteListener {

                                        Toast.makeText(getActivity(), "Foto caricata con successo", Toast.LENGTH_SHORT).show()

                                    }?.addOnSuccessListener {

                                        Navigation.findNavController(btn_aggiungi).navigate(R.id.action_addFragment_to_homeFragment)
                                        Toast.makeText(getActivity(), "Profilo aggiunto con successo", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } else {
                                if (QRCODE != null)
                                    Toast.makeText(getActivity(), "Completa tutti i campi!", Toast.LENGTH_SHORT).show()
                                if (QRCODE == "null")
                                    Toast.makeText(getActivity(), "Il QRCODE è null", Toast.LENGTH_SHORT).show()
                            }

                        }
                    }


                }

        }


        btn_fotocamera.setOnClickListener {
            // Imposta il funzionamento del pulsante per l'acqisizione dell'immagine
            // Creo un intent di tipo implicito per acquisire l'immagine
            val takePhoto = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takePhoto.resolveActivity(activity!!.packageManager)?.also {
                startActivityForResult(takePhoto, REQUEST_IMAGE_CAPTURE)
            }
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


    private fun downloadFoto(imagRef: StorageReference?) {
        val picture = ArrayList<ImageView>() //Arraylist di immagini per caricare
        picture.add(ProfilePic)

        imagRef?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
            // Use the bytes to display the image
            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            picture.get(0).setImageBitmap(bitmap)
        }?.addOnFailureListener {
            // Handle any errors
        }
    }// DownloadFoto

    }







