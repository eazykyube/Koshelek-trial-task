package com.example.koshelek_trial_task

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.koshelek_trial_task.data_classes.SingleEntity
import kotlinx.android.synthetic.main.table_row.view.*


class Adapter(): RecyclerView.Adapter<Adapter.BinanceViewHolder>() {

    var data =  listOf<SingleEntity>()
        set(value) {
            field = value

            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BinanceViewHolder {
        return BinanceViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: BinanceViewHolder, position: Int) {
        val item = data[position]

        holder.bind(item)
    }

    override fun getItemCount() = data.size

    class BinanceViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val amount: TextView = itemView.amount
        val price: TextView = itemView.price
        val total: TextView = itemView.total

        fun bind(item: SingleEntity) {
            amount.text = item.values[1]
            price.text = item.values[0]
            total.text = (item.values[0].toFloat() * item.values[1].toFloat()).toString()
        }

        companion object {
            fun from(parent: ViewGroup): BinanceViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.table_row, parent, false)
                return BinanceViewHolder(view)
            }
        }
    }
}

