package com.example.daftarteman

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.example.daftarteman.databinding.ActivityUpdateDataBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UpdateData : AppCompatActivity() {
    private var database: DatabaseReference? = null
    private var auth: FirebaseAuth? = null
    private var cekNama: String? = null
    private var cekAlamat: String? = null
    private var cekNoHp: String? = null
    private var cekJabatan: String? = null

    private lateinit var binding: ActivityUpdateDataBinding

    private fun getJkel(): String {
        var gender = ""
        if (binding.laki.isChecked) {
            gender = "Laki Laki"
        } else if (binding.perempuan.isChecked) {
            gender = "Perempuan"
        }
        return gender
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        data
        binding.update.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                cekNama = binding.namaBaru.getText().toString().trim()
                cekAlamat = binding.alamatBaru.getText().toString().trim()
                cekNoHp = binding.noHpBaru.getText().toString().trim()
                cekJabatan = binding.jabatanBaru.selectedItem.toString().trim()
                val getJkel: String = getJkel().trim()

                if (isEmpty(cekNama!!) || isEmpty(cekAlamat!!) || isEmpty(cekNoHp!!) || isEmpty(getJkel!!) || isEmpty(cekJabatan!!)) {
                    Toast.makeText(this@UpdateData, "Data tidak boleh kosong", Toast.LENGTH_SHORT).show()
                } else {
                    val setdata_teman = data_teman().apply {
                        nama = cekNama
                        alamat = cekAlamat
                        no_hp = cekNoHp
                        jabatan = cekJabatan
                        jkel = getJkel()
                    }
                    updateTeman(setdata_teman)
                }
            }
        })
    }

    private fun isEmpty(s: String): Boolean {
        return TextUtils.isEmpty(s)
    }

    private val data: Unit
    private get() {
        val getNama = intent.extras!!.getString("dataNama")
        val getAlamat = intent.extras!!.getString("dataAlamat")
        val getNoHp = intent.extras!!.getString("dataNoHp")
        val getJkel = intent.extras!!.getString("dataJkel")
        val getJabatan = intent.extras!!.getString("dataJabatan")

        binding.namaBaru!!.setText(getNama)
        binding.alamatBaru!!.setText(getAlamat)
        binding.noHpBaru!!.setText(getNoHp)

        // Ambil data jabatan dari intent extras
        val selectedJabatan = getJabatan

        // Set data jabatan ke dalam spinner
        val jabatanList = resources.getStringArray(R.array.jabatan_spinner)
        val spinnerPosition = jabatanList.indexOf(selectedJabatan)
        binding.jabatanBaru.setSelection(spinnerPosition)

        if (getJkel == "Laki Laki") {
            binding.laki.isChecked = true
        } else if (getJkel == "Perempuan") {
            binding.perempuan.isChecked = true
        }
    }

    private fun updateTeman(setdataTeman: data_teman) {
        val userID = auth!!.uid
        val getKey = intent.extras!!.getString("getPrimaryKey")
        database!!.child("Admin")
            .child(userID!!)
            .child("Data teman")
            .child(getKey!!)
            .setValue(setdataTeman)
            .addOnSuccessListener {
            binding.namaBaru!!.setText("")
            binding.alamatBaru!!.setText("")
            binding.noHpBaru!!.setText("")
                binding.jabatanBaru!!.setSelection(0)
                binding.laki!!.isChecked = false
                binding.perempuan!!.isChecked = false

            Toast.makeText(this@UpdateData, "Data berhasil diubah", Toast.LENGTH_SHORT).show()
            finish()
        }
            .addOnFailureListener{
                Toast.makeText(this@UpdateData, "Data gagal diubah", Toast.LENGTH_SHORT).show()
            }
    }
}
