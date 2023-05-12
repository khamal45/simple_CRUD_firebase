package com.example.daftarteman

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.example.daftarteman.databinding.ActivityMainBinding
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var auth:FirebaseAuth? = null
    private val RC_SIGN_IN = 1
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.logout.setOnClickListener(this)
        binding.simpan.setOnClickListener(this)
        binding.lihatData.setOnClickListener(this)


        auth = FirebaseAuth.getInstance()
    }

    private fun isEmpty(s: String) : Boolean {
        return TextUtils.isEmpty(s)
    }

    private fun getJkel(): String {
        var gender = ""
        if (binding.laki.isChecked) {
            gender = "Laki Laki"
        } else if (binding.perempuan.isChecked) {
            gender = "Perempuan"
        }
        return gender
    }

    override fun onClick(v: View) {
        when (v.getId()) {
            R.id.simpan -> {
                val getUserId = auth!!.currentUser!!.uid
                val database = FirebaseDatabase.getInstance()

                val getNama: String = binding.nama.getText().toString()
                val getAlamat: String = binding.alamat.getText().toString()
                val getNoHp: String = binding.nomorHp.getText().toString()
                val getJabatan: String = binding.jabatan.selectedItem.toString()
                val getJkel: String = getJkel()

                val getReference : DatabaseReference = database.reference

                if (isEmpty(getNama) || isEmpty(getAlamat) || isEmpty(getNoHp) || isEmpty(getJkel) || isEmpty(getJabatan)){
                    Toast.makeText(this@MainActivity, "Data tidak boleh ada kosong", Toast.LENGTH_SHORT).show()
                } else {
                    getReference.child("Admin").child(getUserId).child("Data teman").push()
                        .setValue(data_teman(getNama, getAlamat, getNoHp, getJkel, getJabatan)).addOnCompleteListener(this){
                        binding.nama.setText("")
                        binding.alamat.setText("")
                        binding.jabatan.setSelection(0)
                        binding.nomorHp.setText("")
                        binding.laki.isChecked = false
                        binding.perempuan.isChecked = false
                        Toast.makeText(this@MainActivity, "Data tersimpan", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            R.id.logout -> {
                AuthUI.getInstance().signOut(this).addOnCompleteListener(object : OnCompleteListener<Void>{
                    override fun onComplete(p0: Task<Void>) {
                        Toast.makeText(this@MainActivity, "Logout berhasil", Toast.LENGTH_SHORT).show()
                        intent = Intent(applicationContext, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                })
            }
            R.id.lihatData -> {
                startActivity(Intent(this@MainActivity,MyListData::class.java))
            }
        }
    }
}