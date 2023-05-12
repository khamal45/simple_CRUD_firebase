package com.example.daftarteman

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.daftarteman.databinding.ActivityMyListDataBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MyListData : AppCompatActivity(), RecyclerViewAdapter.dataListener {
    private var recyclerView:RecyclerView? = null
    private var adapter: RecyclerView.Adapter<*>? = null
    private var layoutManager: RecyclerView.LayoutManager? = null

    val database = FirebaseDatabase.getInstance()
    private var dataTeman = ArrayList<data_teman>()
    private var auth : FirebaseAuth? = null

    private lateinit var binding: ActivityMyListDataBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyListDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recyclerView = findViewById(R.id.datalist)
        auth = FirebaseAuth.getInstance()
        MyRecyclerView()
        GetData()
    }

    private fun GetData() {
        Toast.makeText(applicationContext, "Mohon tunggu sebentar yak...", Toast.LENGTH_SHORT).show()
        val getUserID: String = auth?.getCurrentUser()?.getUid().toString()
        val getReference = database.getReference()
        getReference.child("Admin").child(getUserID).child("Data teman")
        .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(datasnapshot: DataSnapshot) {
                if (datasnapshot.exists()) {
                    dataTeman.clear()
                    for (snapshot in datasnapshot.children) {
                        val teman = snapshot.getValue(data_teman::class.java)
                        teman?.key = snapshot.key
                        dataTeman.add(teman!!)
                    }
                    adapter = RecyclerViewAdapter(dataTeman,this@MyListData)
                    recyclerView?.adapter = adapter
                    (adapter as RecyclerViewAdapter).notifyDataSetChanged()
                    Toast.makeText(applicationContext, "Data berhasil di muat...", Toast.LENGTH_SHORT).show()
                } else {
                    binding.kosong.isVisible = true
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Data gagal di muat...", Toast.LENGTH_SHORT).show()
                Log.e("MyListActivity", error.details + " " + error.message)
            }
        })
    }


    private fun MyRecyclerView() {
        layoutManager = LinearLayoutManager(this)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.setHasFixedSize(true)

        val itemDecoration = DividerItemDecoration(applicationContext,
            DividerItemDecoration.VERTICAL)
        itemDecoration.setDrawable(ContextCompat.getDrawable(applicationContext,
            R.drawable.line)!!)
        recyclerView?.addItemDecoration(itemDecoration)
    }

    override fun onDeleteData(data: data_teman?, position: Int) {
        val getUserID: String = auth?.getCurrentUser()?.getUid().toString()
        val getRefference = database.getReference()

        if (getRefference != null) {
            getRefference.child("Admin").child(getUserID).child("Data teman").child(data?.key.toString()).removeValue()
                .addOnSuccessListener {
                    Toast.makeText(this@MyListData, "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show()
                    intent = Intent(applicationContext, MyListData::class.java)
                    startActivity(intent)
                    finish()
                }
        } else {
            Toast.makeText(this@MyListData, "Reference kosong gengs", Toast.LENGTH_SHORT).show()
        }
    }
}