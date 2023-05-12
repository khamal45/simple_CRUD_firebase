package com.example.daftarteman

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter (private val listdata_teman: ArrayList<data_teman>, context: Context) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
        private val context: Context

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val nama : TextView
            val alamat : TextView
            val noHp : TextView
            val Jkel : TextView
            val jabatan: TextView
            val listItem : LinearLayout

            init {
                nama = itemView.findViewById(R.id.nama)
                alamat = itemView.findViewById(R.id.alamat)
                noHp = itemView.findViewById(R.id.noHp)
                Jkel = itemView.findViewById(R.id.jkel)
                jabatan = itemView.findViewById(R.id.jabatan)
                listItem = itemView.findViewById(R.id.listItem)
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val V:View = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_view,parent,false)
        return ViewHolder(V)
    }

    override fun getItemCount(): Int {
        return listdata_teman.size
    }

    var listener: dataListener? = null

    init {
        this.context = context
        this.listener = context as MyListData
    }

    interface dataListener {
        fun onDeleteData(data: data_teman?, position: Int)
    }

    @SuppressLint("RecyclerView")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val nama: String? = listdata_teman.get(position).nama
        val alamat: String? = listdata_teman.get(position).alamat
        val noHp: String? = listdata_teman.get(position).no_hp
        val Jkel: String? = listdata_teman.get(position).jkel
        val Jabatan: String? = listdata_teman.get(position).jabatan

        holder.nama.text = "Nama : $nama"
        holder.alamat.text = "Alamat : $alamat"
        holder.noHp.text = "NIP : $noHp"
        holder.Jkel.text = "Jkel : $Jkel"
        holder.jabatan.text = "Jabatan : $Jabatan"
        holder.listItem.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                holder.listItem.setOnClickListener{ view ->
                    val action = arrayOf("Update","Delete")
                    val alert : AlertDialog.Builder = AlertDialog.Builder(view.context)
                    alert.setItems(action, DialogInterface.OnClickListener{ dialog, i ->
                        when (i) { 0 -> {
                            val bundle = Bundle()
                            bundle.putString("dataNama", listdata_teman[position].nama)
                            bundle.putString("dataAlamat", listdata_teman[position].alamat)
                            bundle.putString("dataNoHp", listdata_teman[position].no_hp)
                            bundle.putString("dataJkel", listdata_teman[position].jkel)
                            bundle.putString("dataJabatan", listdata_teman[position].jabatan)
                            bundle.putString("getPrimaryKey", listdata_teman[position].key)

                            val intent = Intent(view.context, UpdateData::class.java)
                            intent.putExtras(bundle)
                            context.startActivity(intent)
                        } 1 -> {
                            listener?.onDeleteData(listdata_teman.get(position), position)
                        }
                        }
                    })
                    alert.create()
                    alert.show()
                    true
                }
                return true
            }
        })
    }
}