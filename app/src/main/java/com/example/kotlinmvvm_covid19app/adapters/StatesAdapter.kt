package com.example.kotlinmvvm_covid19app.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinmvvm_covid19app.R
import com.example.kotlinmvvm_covid19app.activities.CovidDetailsActivity
import com.example.kotlinmvvm_covid19app.models.StateItem

class StatesAdapter(private val stateList:List<StateItem>,private val context: Context):
    RecyclerView.Adapter<StatesAdapter.StateAdapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StateAdapterViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.state_item, parent, false)
        return StateAdapterViewHolder(v)
    }

    override fun onBindViewHolder(holder: StateAdapterViewHolder, position: Int) {
        val stateItem=stateList[position]
        holder.covidState.text=stateItem.state
        holder.covidStateName.text=stateItem.name

        holder.itemView.setOnClickListener {
            val intent = Intent(context, CovidDetailsActivity::class.java).apply {
                putExtra("State", stateItem.state)
                putExtra("StateName",stateItem.name)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return stateList.size
    }

    class StateAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val covidState:TextView=itemView.findViewById(R.id.covid_state)
        val covidStateName:TextView=itemView.findViewById(R.id.covid_stateName);
    }
}