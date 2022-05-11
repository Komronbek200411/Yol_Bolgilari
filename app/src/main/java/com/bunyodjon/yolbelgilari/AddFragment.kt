package com.bunyodjon.yolbelgilari

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bunyodjon.yolbelgilari.MyDate.roadSign
import com.bunyodjon.yolbelgilari.databinding.FragmentAddBinding
import com.bunyodjon.yolbelgilari.dbhelper.MyDBHelper
import com.bunyodjon.yolbelgilari.dbhelper.RoadSign
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime

class AddFragment : Fragment() {
    private lateinit var arrayListTypes: ArrayList<String>
    private lateinit var arrayAdapterTypes: ArrayAdapter<String>
    private lateinit var binding: FragmentAddBinding
    private lateinit var myDBHelper: MyDBHelper
    private var time: String? = null
    private var uri: Uri? = null
    private var booleanEditImage = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        MyDate.a  = 1
        binding = FragmentAddBinding.inflate(layoutInflater)
        loadData()
        binding.spinnerTimes.setAdapter(arrayAdapterTypes)

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
            MyDate.a = 0
        }

        binding.setPhoto.setOnClickListener {
            getImageContent.launch("image/*")
        }

        binding.btnSave.setOnClickListener {
            if (binding.edtName.text.toString().trim().isNotEmpty() && binding.spinnerTimes.text.toString().trim().isNotEmpty()) {
                val boolean = checkName(binding.edtName.text.toString().trim())
                if (boolean) {
                    if (roadSign == null) {
                        saveRoadSign()
                    } else {
                        editRoadSign()
                    }
                } else {
                    if (roadSign != null){
                        if (binding.edtName.text.toString().trim() != roadSign!!.name) {
                            Toast.makeText(requireActivity(), "Ma'lumot saqlanmadi!\nChunki bu nomdagi belgi avvaldan mavjud", Toast.LENGTH_SHORT).show()
                        }else{
                            editRoadSign()
                        }
                    }else{
                        Toast.makeText(requireActivity(), "Ma'lumot saqlanmadi!\nChunki bu nomdagi belgi avvaldan mavjud", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(requireActivity(), "Ma'lumot yetarli emas", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    private fun editRoadSign() {
        val id = roadSign!!.id
        val name = binding.edtName.text.toString().trim()
        val type = binding.spinnerTimes.text.toString().trim()
        val about = binding.edtAbout.text.toString().trim()
        val like = roadSign!!.like
        var image = roadSign!!.image


        if (booleanEditImage) {
            image = if (uri != null) {
                deleteImageFromFilesDir()
                val inputStream = requireActivity().contentResolver?.openInputStream(uri!!)
                val file = File(requireActivity().filesDir, "$time.jpg")
                val fileOutputStream = FileOutputStream(file)
                inputStream?.copyTo(fileOutputStream)
                inputStream?.close()
                fileOutputStream.close()
                time.toString()
            } else {
                deleteImageFromFilesDir()
                ""
            }
        }

        val roadSign = RoadSign(id, name, about, type, image, like)
        val boolean = myDBHelper.updateRoadSign(roadSign)
        if (boolean) {
            Toast.makeText(requireActivity(), "Ma'lumot o'zgartirildi", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
    }

    private fun deleteImageFromFilesDir() {
        if (requireActivity().filesDir.isDirectory) {
            val file = File(requireActivity().filesDir, "${roadSign!!.image}.jpg")
            for (i in requireActivity().filesDir.listFiles().indices) {
                if (requireActivity().filesDir.listFiles()[i] == file) {
                    requireActivity().filesDir.listFiles()[i].delete()
                    break
                }
            }
        }
    }

    private fun checkName(name: String): Boolean {
        val arrayList = myDBHelper.showRoadSign()
        val hashSet = HashSet<String>()
        for (i in arrayList) {
            hashSet.add(i.name!!)
        }
        return hashSet.add(name)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveRoadSign() {
        val name = binding.edtName.text.toString().trim()
        val type = binding.spinnerTimes.text.toString().trim()
        val about = binding.edtAbout.text.toString().trim()
        var image = ""

        if (uri != null) {
            val inputStream = requireActivity().contentResolver?.openInputStream(uri!!)
            val file = File(requireActivity().filesDir, "${time.toString()}.jpg")
            val fileOutputStream = FileOutputStream(file)
            inputStream?.copyTo(fileOutputStream)
            inputStream?.close()
            fileOutputStream.close()
            image = time.toString()
        }
        val roadSign = RoadSign(name, about, type, image, "false")
        myDBHelper.addRoadSign(roadSign)

        binding.edtName.text.clear()
        binding.edtAbout.text!!.clear()
        binding.spinnerTimes.text!!.clear()
        binding.setPhoto.setImageResource(0)
        time = buildPath(LocalDateTime.now().toString())
        /*val layoutParams: ViewGroup.LayoutParams = binding.setPhoto.layoutParams
        layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        binding.setPhoto.layoutParams = layoutParams*/
        Toast.makeText(requireActivity(), "Saqlandi", Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadData() {
        time = buildPath(LocalDateTime.now().toString())
        myDBHelper = MyDBHelper(requireActivity())
        arrayListTypes = ArrayList()
        arrayListTypes.add("Ogohlantiruvchi")
        arrayListTypes.add("Imtiyozli")
        arrayListTypes.add("Ta'qiqlovchi")
        arrayListTypes.add("Buyuruvchi")
        arrayAdapterTypes = ArrayAdapter(requireActivity(), R.layout.item_spinner, arrayListTypes)

        // edit mode
        if (roadSign != null) {
            binding.spinnerTimes.setText(roadSign!!.type)
            binding.edtName.setText(roadSign!!.name)
            binding.edtAbout.setText(roadSign!!.about)
            binding.imageDelete.visibility = View.VISIBLE

            // install image in imageView
            if (roadSign!!.image!!.isNotEmpty()) {
                val file = File(requireActivity().filesDir, "${roadSign!!.image}.jpg")
                Glide.with(requireActivity()).load(file).centerCrop().into(binding.setPhoto)
                /*val layoutParams: ViewGroup.LayoutParams = binding.setPhoto.layoutParams
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                binding.setPhoto.layoutParams = layoutParams*/
                uri = Uri.fromFile(file)
            }

            binding.imageDelete.setOnClickListener {
                binding.setPhoto.setImageResource(0)
                /*val layoutParams: ViewGroup.LayoutParams = binding.setPhoto.layoutParams
                layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                binding.setPhoto.layoutParams = layoutParams*/
                uri = null
                booleanEditImage = true
            }

        }

    }

    private fun buildPath(time: String): String {
        var path = ""
        for (i in time) {
            if (i.toString() != "-" && i.toString() != "T" && i.toString() != ":" && i.toString() != ".") {
                path += i
            }
        }
        return path
    }

    private val getImageContent = registerForActivityResult(ActivityResultContracts.GetContent()) {
        it ?: return@registerForActivityResult
        Glide.with(requireActivity()).load(it).centerCrop().into(binding.setPhoto)
        /*val layoutParams: ViewGroup.LayoutParams = binding.setPhoto.layoutParams
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        binding.setPhoto.layoutParams = layoutParams*/
        uri = it
        booleanEditImage = true
    }
}