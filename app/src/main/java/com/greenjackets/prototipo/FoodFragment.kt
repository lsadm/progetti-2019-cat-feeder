package com.greenjackets.prototipo


import android.content.ContentValues
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_food.*
import java.lang.Exception


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class FoodFragment : Fragment() {

    val databaseReference = FirebaseDatabase.getInstance().getReference()
    // val storageReference = FirebaseStorage.getInstance().getReference()
    val Cibo = databaseReference.child("Cibo")
    val Scheduling = Cibo.child("Scheduling")

    val schedul_7 = Scheduling.child("7:00")
    val schedul_10 = Scheduling.child("10:00")
    val schedul_13 = Scheduling.child("13:00")
    val schedul_16 = Scheduling.child("16:00")
    val schedul_19 = Scheduling.child("19:00")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_food, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)





        arguments.let {
            val QRCODE = arguments?.getString("qrcode")
            QRCODE.let {


                downloadDati()
                btn_salva.setOnClickListener {

                    schedul_7.child("abilitato").setValue(btn_7_am.isChecked.toString())
                    schedul_7.child("quantità").setValue(seek_7.progress.toString())

                    schedul_10.child("abilitato").setValue(btn_10_am.isChecked.toString())
                    schedul_10.child("quantità").setValue(seek_10.progress.toString())

                    schedul_13.child("abilitato").setValue(btn_13_pm.isChecked.toString())
                    schedul_13.child("quantità").setValue(seek_13.progress.toString())

                    schedul_16.child("abilitato").setValue(btn_16_pm.isChecked.toString())
                    schedul_16.child("quantità").setValue(seek_16.progress.toString())

                    schedul_19.child("abilitato").setValue(btn_19_pm.isChecked.toString())
                    schedul_19.child("quantità").setValue(seek_19.progress.toString())

                    Navigation.findNavController(it).navigateUp()

                }
            }


        }


    }

    private fun downloadDati() {



        try {


            schedul_7.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val abilitato: String = dataSnapshot.child("abilitato").getValue().toString()
                    val quantità: String = dataSnapshot.child("quantità").getValue().toString()
                    if (abilitato == "null" || abilitato == "false")
                        btn_7_am.isChecked = false
                    else
                        btn_7_am.isChecked = true

                    if (quantità != "null")
                        seek_7.setProgress(quantità.toInt())
                    else
                        seek_7.setProgress(1)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                }
            })

            schedul_10.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val abilitato: String = dataSnapshot.child("abilitato").getValue().toString()
                    val quantità: String = dataSnapshot.child("quantità").getValue().toString()
                    if (abilitato == "null" || abilitato == "false")
                        btn_10_am.isChecked = false
                    else
                        btn_10_am.isChecked = true
                    if (quantità != "null")
                        seek_10.setProgress(quantità.toInt())
                    else
                        seek_10.setProgress(1)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                }
            })

            schedul_13.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val abilitato: String = dataSnapshot.child("abilitato").getValue().toString()
                    val quantità: String = dataSnapshot.child("quantità").getValue().toString()
                    if (abilitato == "null" || abilitato == "false")
                        btn_13_pm.isChecked = false
                    else
                        btn_13_pm.isChecked = true
                    if (quantità != "null")
                        seek_13.setProgress(quantità.toInt())
                    else
                        seek_13.setProgress(1)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                }
            })

            schedul_16.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val abilitato: String = dataSnapshot.child("abilitato").getValue().toString()
                    val quantità: String = dataSnapshot.child("quantità").getValue().toString()
                    if (abilitato == "null" || abilitato == "false")
                        btn_16_pm.isChecked = false
                    else
                        btn_16_pm.isChecked = true
                    if (quantità != "null")
                        seek_16.setProgress(quantità.toInt())
                    else
                        seek_16.setProgress(1)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                }
            })

            schedul_19.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val abilitato: String = dataSnapshot.child("abilitato").getValue().toString()
                    val quantità: String = dataSnapshot.child("quantità").getValue().toString()
                    if (abilitato == "null" || abilitato == "false")
                        btn_19_pm.isChecked = false
                    else
                        btn_19_pm.isChecked = true
                    if (quantità != "null")
                        seek_19.setProgress(quantità.toInt())
                    else
                        seek_19.setProgress(1)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                }
            })


        } catch (e: Exception) {
        }

    }



 }

