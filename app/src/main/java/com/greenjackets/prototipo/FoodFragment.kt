package com.greenjackets.prototipo


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_food.*
import java.lang.Exception


class FoodFragment : Fragment() {

    val databaseReference = FirebaseDatabase.getInstance().getReference()
    // val storageReference = FirebaseStorage.getInstance().getReference()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_food, container, false)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        arguments?.let {

            val QRCODE : String?  = it?.getString(getString(R.string.qrcode))
            QRCODE?.let {
               val QRCODE = it
                val Qr = databaseReference.child(QRCODE)
                val Cibo = Qr.child(getString(R.string.Food_ref))
                val Scheduling = Cibo.child(getString(R.string.Scheduling_ref))

                val schedul_7 = Scheduling.child("7:00")
                val schedul_10 = Scheduling.child("10:00")
                val schedul_13 = Scheduling.child("13:00")
                val schedul_16 = Scheduling.child("16:00")
                val schedul_19 = Scheduling.child("19:00")
                val Immediato = Scheduling.child(getString(R.string.realtime))

                seek_istant.progress= 0
                downloadDati( Scheduling)



                btn_salva.setOnClickListener {

                    schedul_7.setValue(Orario(btn_7_am.isChecked.toString(),seek_7.progress.toString()))
                    schedul_10.setValue(Orario(btn_10_am.isChecked.toString(),seek_10.progress.toString()))
                    schedul_13.setValue(Orario(btn_13_pm.isChecked.toString(),seek_13.progress.toString()))
                    schedul_16.setValue(Orario(btn_16_pm.isChecked.toString(),seek_16.progress.toString()))
                    schedul_19.setValue(Orario(btn_19_pm.isChecked.toString(),seek_19.progress.toString()))
                    Navigation.findNavController(it).navigateUp()

                }
                btn_Pappa.setOnClickListener {
                    Immediato.setValue(Orario(true.toString(),seek_istant.progress.toString()))
                   // Toast.makeText(getActivity(), getString(R.string.Giving_now)+seek_istant.progress.toString()+getString(
                  //                          R.string.rations), Toast.LENGTH_SHORT).show()
                }

                seek_7.setOnSeekBarChangeListener( object : SeekBar.OnSeekBarChangeListener{
                    override fun onProgressChanged(seekBar: SeekBar, progress: Int, b: Boolean) {
                        // Display the current progress of SeekBar
                        val value = (progress-1) * (seekBar.width - 2 * seekBar.thumbOffset) / (seekBar.max-1)
                        txt_progress7.text=progress.toString()
                        txt_progress7.x = seekBar.x + value + seekBar.thumbOffset / 2
                        txt_finalprogress7.text=progress.toString()
                    }
                    override fun onStartTrackingTouch(seekBar: SeekBar) {
                        txt_progress7.visibility = View.VISIBLE
                        txt_finalprogress7.visibility = View.GONE
                    }
                    override fun onStopTrackingTouch(seekBar: SeekBar) {
                        txt_progress7.visibility = View.GONE
                        txt_finalprogress7.visibility = View.VISIBLE
                    }
                })
                seek_10.setOnSeekBarChangeListener( object : SeekBar.OnSeekBarChangeListener{
                    override fun onProgressChanged(seekBar: SeekBar, progress: Int, b: Boolean) {
                        // Display the current progress of SeekBar
                        val value = (progress-1) * (seekBar.width - 2 * seekBar.thumbOffset) / (seekBar.max-1)
                        txt_progress10.text=progress.toString()
                        txt_progress10.x = seekBar.x + value + seekBar.thumbOffset / 2
                        txt_progressfinal10.text=progress.toString()
                    }
                    override fun onStartTrackingTouch(seekBar: SeekBar) {
                        txt_progress10.visibility = View.VISIBLE
                        txt_progressfinal10.visibility = View.GONE
                    }
                    override fun onStopTrackingTouch(seekBar: SeekBar) {
                        txt_progress10.visibility = View.GONE
                        txt_progressfinal10.visibility = View.VISIBLE
                    }
                })
                seek_13.setOnSeekBarChangeListener( object : SeekBar.OnSeekBarChangeListener{
                    override fun onProgressChanged(seekBar: SeekBar, progress: Int, b: Boolean) {
                        // Display the current progress of SeekBar
                        val value = (progress-1) * (seekBar.width - 2 * seekBar.thumbOffset) / (seekBar.max-1)
                        txt_progress13.text=progress.toString()
                        txt_progress13.x = seekBar.x + value + seekBar.thumbOffset / 2
                        txt_progressfinal13.text=progress.toString()
                    }
                    override fun onStartTrackingTouch(seekBar: SeekBar) {
                        txt_progress13.visibility = View.VISIBLE
                        txt_progressfinal13.visibility = View.GONE
                    }
                    override fun onStopTrackingTouch(seekBar: SeekBar) {
                        txt_progress13.visibility = View.GONE
                        txt_progressfinal13.visibility = View.VISIBLE
                    }
                })
                seek_16.setOnSeekBarChangeListener( object : SeekBar.OnSeekBarChangeListener{
                    override fun onProgressChanged(seekBar: SeekBar, progress: Int, b: Boolean) {
                        // Display the current progress of SeekBar
                        val value = (progress-1) * (seekBar.width - 2 * seekBar.thumbOffset) / (seekBar.max-1)
                        txt_progress16.text=progress.toString()
                        txt_progress16.x = seekBar.x + value + seekBar.thumbOffset / 2
                        txt_progressfinal16.text=progress.toString()
                    }
                    override fun onStartTrackingTouch(seekBar: SeekBar) {
                        txt_progress16.visibility = View.VISIBLE
                        txt_progressfinal16.visibility = View.GONE
                    }
                    override fun onStopTrackingTouch(seekBar: SeekBar) {
                        txt_progress16.visibility = View.GONE
                        txt_progressfinal16.visibility = View.VISIBLE
                    }
                })
                seek_19.setOnSeekBarChangeListener( object : SeekBar.OnSeekBarChangeListener{
                    override fun onProgressChanged(seekBar: SeekBar, progress: Int, b: Boolean) {
                        // Display the current progress of SeekBar
                        val value = (progress-1) * (seekBar.width - 2 * seekBar.thumbOffset) / (seekBar.max-1)
                        txt_progress19.text=progress.toString()
                        txt_progress19.x = seekBar.x + value + seekBar.thumbOffset / 2
                        txt_progressfinal19.text=progress.toString()
                    }
                    override fun onStartTrackingTouch(seekBar: SeekBar) {
                        txt_progress19.visibility = View.VISIBLE
                        txt_progressfinal19.visibility = View.GONE
                    }
                    override fun onStopTrackingTouch(seekBar: SeekBar) {
                        txt_progress19.visibility = View.GONE
                        txt_progressfinal19.visibility = View.VISIBLE
                    }
                })
                seek_istant.setOnSeekBarChangeListener( object : SeekBar.OnSeekBarChangeListener{
                    override fun onProgressChanged(seekBar: SeekBar, progress: Int, b: Boolean) {
                        // Display the current progress of SeekBar
                        val value = (progress-1) * (seekBar.width - 2 * seekBar.thumbOffset) / (seekBar.max-1)
                        txt_progressEroga.text=progress.toString()
                        txt_progressEroga.x = seekBar.x + value + seekBar.thumbOffset / 2
                        txt_progressfinalEroga.text=progress.toString()
                    }
                    override fun onStartTrackingTouch(seekBar: SeekBar) {
                        txt_progressEroga.visibility = View.VISIBLE
                        txt_progressfinalEroga.visibility = View.GONE
                    }
                    override fun onStopTrackingTouch(seekBar: SeekBar) {
                        txt_progressEroga.visibility = View.GONE
                        txt_progressfinalEroga.visibility = View.VISIBLE
                    }
                })

                txt_progressEroga.text=seek_istant.progress.toString()

            }


        }



    }

    private fun downloadDati(Scheduling : DatabaseReference ) {



        val listener = object: ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                try {
                    val orario = p0.getValue(Orario::class.java)



                    if(p1=="19:00"){ //7 am
                       // Toast.makeText(getActivity(), p1 , Toast.LENGTH_SHORT).show()
                        if(orario?.abilitato == getString(R.string.nullstring) || orario?.abilitato == getString(R.string.false_string) )
                            btn_7_am.isChecked = false
                        else
                            btn_7_am.isChecked = true
                        if (orario?.quantità != getString(R.string.nullstring)){
                            seek_7.progress=orario?.quantità!!.toInt()
                            txt_progress7.text=orario?.quantità!!

                        }
                        else {
                            seek_7.progress = 0

                        }
                    }

                    if(p1==null){ // E' il primo child, quindi è 10:00
                        if(orario?.abilitato == getString(R.string.nullstring) || orario?.abilitato == getString(R.string.false_string)  )
                            btn_10_am.isChecked = false
                        else
                            btn_10_am.isChecked = true

                        if (orario?.quantità != getString(R.string.nullstring)){
                            seek_10.progress=orario?.quantità!!.toInt()
                            txt_progress10.text=orario?.quantità!!}
                        else
                            seek_10.progress=0


                    }
                    if(p1=="10:00"){ // Se p1 è 10 allora sto controllando il 13:00
                        if(orario?.abilitato == getString(R.string.nullstring) || orario?.abilitato == getString(R.string.false_string)  )
                            btn_13_pm.isChecked = false
                        else
                            btn_13_pm.isChecked = true

                        if (orario?.quantità != getString(R.string.nullstring)){
                            seek_13.progress=orario?.quantità!!.toInt()
                            txt_progress13.text=orario?.quantità!!}
                        else
                            seek_13.progress=0

                    }
                    if(p1=="13:00"){ // se p1 è 13 allora sto controllando 16:00

                        if(orario?.abilitato == getString(R.string.nullstring) || orario?.abilitato == getString(R.string.false_string) )
                            btn_16_pm.isChecked = false
                        else
                            btn_16_pm.isChecked = true

                        if (orario?.quantità != getString(R.string.nullstring)){
                            seek_16.progress=orario?.quantità!!.toInt()
                            txt_progress16.text=orario?.quantità!!}
                        else
                            seek_16.progress=0

                    }
                    if(p1=="16:00"){ // se p1 è 16 allora sto controllando 19:00
                     //   Toast.makeText(getActivity(), p1 , Toast.LENGTH_SHORT).show()
                        if(orario?.abilitato == getString(R.string.nullstring) || orario?.abilitato == getString(R.string.false_string)  )
                            btn_19_pm.isChecked = false
                        else
                            btn_19_pm.isChecked = true

                        if (orario?.quantità != getString(R.string.nullstring) ){
                            seek_19.progress=orario?.quantità!!.toInt()
                            txt_progress19.text=orario?.quantità!!}
                        else
                            seek_19.progress=0


                    }






                }
                catch(e:Exception){}
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }
        }

        Scheduling.addChildEventListener(listener)

    }



 }

