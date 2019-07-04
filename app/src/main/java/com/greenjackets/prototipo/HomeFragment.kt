package com.greenjackets.prototipo


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.greenjackets.prototipo.RecycleView.Adapter
import com.greenjackets.prototipo.RecycleView.Animale
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lista_animali.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.HORIZONTAL))


        val animali=ArrayList<Animale?>()
        val adapter = Adapter(animali,requireContext())
        lista_animali.adapter= adapter

        lista_animali.layoutManager = LinearLayoutManager(activity)


        btn_add.setOnClickListener {
                    // passaggio da home a aggiungi
            Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_addFragment)

        }
        Procedi.setOnClickListener {

            Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_animal_fragment)
        }


    }



}
