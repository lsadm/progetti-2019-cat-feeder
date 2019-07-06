package com.greenjackets.prototipo



import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.database.*

import com.greenjackets.prototipo.RecycleView.Adapter
import com.greenjackets.prototipo.RecycleView.Animale
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader
import java.lang.Exception


class HomeFragment : Fragment() {

    private val database = FirebaseDatabase.getInstance().getReference()
    private val TAG = "MainActivity"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //Per avere lo scorrimento in un unica direzione
        lista_animali.layoutManager = LinearLayoutManager(activity)


        val animali =ArrayList<Animale?>()
        val keys = ArrayList<String?>()
        val adapter = Adapter(animali,requireContext())
        lista_animali.adapter= adapter


        // una volta specificata la RecyclerView la riempiamo con childEventListener , sul child giusto
        // Il child giusto ce lo da il QRCODE
           // readqr() // funzione che legge












        val postListener = object : ValueEventListener {  // creazione ValueEventListener

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val  animale = dataSnapshot.getValue(com.greenjackets.prototipo.Animale::class.java)
                try {

                    Log.d(TAG, "onChildAdded:" + dataSnapshot.key!!)
                    // A new comment has been added, add it to the displayed list
                    val g = dataSnapshot.getValue(Animale::class.java)
                    animali.add(g)
                    keys.add(dataSnapshot.key.toString()) //aggiungo le varie key in un vettore
                    adapter.notifyItemInserted(animali.indexOf(g))

                } catch (e: Exception) {}

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
                // ...
            }


        }

       database.child("1").addValueEventListener(postListener)  // dichiarato sopra il ValueEventListener e poi chiamo la funzione passandoglielo
        //database.child("33").addValueEventListener(postListener)
        database.child("2").addValueEventListener(postListener)

        btn_add.setOnClickListener {
                    // passaggio da home a aggiungi
            Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_QRcodeFragment)
        }





    }

    /*private fun readqr() {
        val filename = "Qrcodes.txt" // nome del file
        var filestream :FileInputStream?=null
         filestream= context?.openFileInput(filename)
        var bufferedreader =filestream?.bufferedReader()
        var sb= StringBuilder()
        var cont=0
        bufferedreader?.forEachLine {
            cont++
            sb.append(it)
            Toast.makeText(context, "Letto :"+sb.toString(), Toast.LENGTH_LONG).show()

        }



    }
*/

}
