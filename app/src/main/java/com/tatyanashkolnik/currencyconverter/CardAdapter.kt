package com.tatyanashkolnik.currencyconverter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.result_card.view.*

class CardAdapter(private val cardList : List<Card>): RecyclerView.Adapter<CardAdapter.CardViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.result_card, parent, false)
        return CardViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val currentItem = cardList[position]

        holder.fromText.text = currentItem.fromText
        holder.fromAmount.text = currentItem.fromAmount.toString()
        holder.toText.text = currentItem.toText
        holder.toAmount.text = currentItem.toAmount.toString()
    }

    override fun getItemCount() = cardList.size

    class CardViewHolder(itemview : View) : RecyclerView.ViewHolder (itemview){
        val fromText : TextView = itemview.fromText
        val fromAmount : TextView = itemview.fromAmount
        val toText : TextView = itemview.textViewResultAmount
        val toAmount : TextView = itemview.toAmount
    }
}