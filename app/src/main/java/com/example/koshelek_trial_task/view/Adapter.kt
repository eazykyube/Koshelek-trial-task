package com.example.koshelek_trial_task.view

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.koshelek_trial_task.R
import com.example.koshelek_trial_task.data_classes.SingleEntity
import kotlinx.android.synthetic.main.table_row.view.*


const val forest_green = "#228B22"
const val red = "#D0312D"
const val gray = "#FFE3E3E3"
const val white = "#FFFFFFFF"

class Adapter(): RecyclerView.Adapter<Adapter.BinanceViewHolder>() {

    var data =  listOf<SingleEntity>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var isBids = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BinanceViewHolder {
        return BinanceViewHolder.from(
            parent
        )
    }

    override fun onBindViewHolder(holder: BinanceViewHolder, position: Int) {
        val item = data[position]

        holder.bind(item, isBids, position)
    }

    override fun getItemCount() = data.size

    class BinanceViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val amount: TextView = itemView.amount
        val price: TextView = itemView.price
        val total: TextView = itemView.total


        fun bind(item: SingleEntity, isBids: Boolean, position: Int) {
            if (isBids)
                price.setTextColor(Color.parseColor(forest_green))
            else
                price.setTextColor(Color.parseColor(red))
            if (position % 2 == 0)
                itemView.setBackgroundColor(Color.parseColor(gray))
            else
                itemView.setBackgroundColor(Color.parseColor(white))
            amount.text = item.values[1]
            price.text = item.values[0]
            total.text = (item.values[0].toFloat() * item.values[1].toFloat()).toString()
        }

        companion object {
            fun from(parent: ViewGroup): BinanceViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.table_row, parent, false)

                return BinanceViewHolder(
                    view
                )
            }
        }
    }
}

