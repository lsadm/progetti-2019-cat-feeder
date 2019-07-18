package com.greenjackets.prototipo



import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.navigation.Navigation
import com.google.firebase.database.*

import com.greenjackets.prototipo.RecycleView.Adapter
import com.greenjackets.prototipo.RecycleView.Animale
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.BufferedReader
import java.io.File
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
        val adapter = Adapter(animali,requireContext())
        lista_animali.adapter= adapter


        // una volta specificata la RecyclerView la riempiamo con ValueEventListener , sul child giusto
        // Il child giusto ce lo da il QRCODE. Leggiamolo dal file!










        val CiotolaListener = object : ValueEventListener {  // creazione ValueEventListener

            override fun onDataChange(dataSnapshot: DataSnapshot) {


                try {

                    Log.d(TAG, "onChildAdded:" + dataSnapshot.key!!)
                    // A new comment has been added, add it to the displayed list
                    val a = dataSnapshot.getValue(Animale::class.java)
                    if(a?.Nome!= null ){
                    animali.add(a)

                    adapter.notifyItemInserted(animali.indexOf(a))}

                } catch (e: Exception) {}

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
                // ...
            }


        }

        readqr(CiotolaListener) // funzione che legge, va messa dopo aver creato il postListener
                                //implementata da noi

        btn_add.setOnClickListener {
                    // passaggio da home a aggiungi
            Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_QRcodeFragment)
        }





    }

    private fun readqr(CiotolaListener: ValueEventListener) {
        val filename = getString(R.string.QRCODEStxt) // nome del file

        context?.openFileOutput(filename, Context.MODE_APPEND).use {
            //crea file se non ci sta. Serve per non far crashare l'app. Il resto delle volte è inutilizzat
        }
        val filestream= context?.openFileInput(filename)
        val bufferedreader =filestream?.bufferedReader()


        bufferedreader?.forEachLine {
            database.child(it).child(getString(R.string.Animale)).addValueEventListener(CiotolaListener) // chiamo il value event listener su ognuno

        }


    }


}
