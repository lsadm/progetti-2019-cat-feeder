package com.greenjackets.prototipo



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
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

import com.greenjackets.prototipo.RecycleView.Adapter
import com.greenjackets.prototipo.RecycleView.Animale
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader


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


        val animali=ArrayList<Animale?>()
        val keys = ArrayList<String>()
        val adapter = Adapter(animali,requireContext())
        lista_animali.adapter= adapter


        // una volta specificata la RecyclerView la riempiamo con childEventListener , sul child giusto
        // Il child giusto ce lo da il QRCODE
            readqr() // funzione che legge









        //Listener per aggiornare la schermata nel caso in cui un altro familiare cambiasse animale/dettagli
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.key!!)
                // A new comment has been added, add it to the displayed list
                val g = dataSnapshot.getValue(Animale::class.java)
                animali.add(g)
                keys.add(dataSnapshot.key.toString()) //aggiungo le varie key in un vettore
                adapter.notifyItemInserted(animali.indexOf(g))
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildChanged: ${dataSnapshot.key}")
                val g = dataSnapshot.getValue(Animale::class.java)
                val index = keys.indexOf(dataSnapshot.key.toString()) //ottengo l'indice del gioco aggiornato
                animali[index]=g
                adapter.notifyDataSetChanged()
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.key!!)
                val g = dataSnapshot.getValue(Animale::class.java)
                val index = animali.indexOf(g)
                    animali.remove(g)
                adapter.notifyItemRemoved(index)
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.key!!)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException())
                Toast.makeText(context, "Failed to load comments.", Toast.LENGTH_SHORT).show()
            }
        }

        database.addChildEventListener(childEventListener)



        

        btn_add.setOnClickListener {
                    // passaggio da home a aggiungi
            Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_QRcodeFragment)
        }





    }

    private fun readqr() {
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


}
