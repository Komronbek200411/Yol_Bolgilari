package com.bunyodjon.yolbelgilari

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bunyodjon.yolbelgilari.MyDate.count
import com.bunyodjon.yolbelgilari.databinding.DeletedialogBinding
import com.bunyodjon.yolbelgilari.databinding.EditDialogBinding
import com.bunyodjon.yolbelgilari.databinding.DialogShowBinding
import com.bunyodjon.yolbelgilari.databinding.ItemRvBinding
import com.bunyodjon.yolbelgilari.dbhelper.MyDBHelper
import com.bunyodjon.yolbelgilari.dbhelper.RoadSign
import kotlinx.android.synthetic.main.edit_dialog.*
import java.io.File

class RecyclerViewAdapter(
    val context: Context,
    private val arrayListRoadSign: ArrayList<RoadSign>,
    val rvClick: RVClick,
) :
RecyclerView.Adapter<RecyclerViewAdapter.VH>() {

    lateinit var dialog: AlertDialog
    lateinit var dialoga:AlertDialog
    lateinit var dialogas:AlertDialog
    private var booleanAntiBag = true

    inner class VH(private var itemRV: ItemRvBinding) : RecyclerView.ViewHolder(itemRV.root) {
        fun onBind(roadSign: RoadSign, position: Int) {

            itemRV.setText.text = roadSign.name

            if (roadSign.image != "") {
                val file = File(context.filesDir, "${roadSign.image}.jpg")
                Glide.with(context).load(file).centerCrop().into(itemRV.setPhoto)
            }else{
                itemRV.setPhoto.setImageResource(0)
            }

            if (roadSign.like.toBoolean()) {
                itemRV.setLike.setImageResource(R.drawable.ic_baseline_favorite)
            } else {
                itemRV.setLike.setImageResource(R.drawable.ic_baseline_favorite_border_24)
            }

            itemRV.setLike.setOnClickListener {
                val myDBHelper = MyDBHelper(context)
                if (count ==0) {
                    roadSign.like = "true"
                    itemRV.setLike.setImageResource(R.drawable.ic_baseline_favorite)
                    notifyItemChanged(position)
                }else if (count == 1){
                    roadSign.like="false"
                    itemRV.setLike.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                    notifyItemChanged(position)
                }
                myDBHelper.updateRoadSign(roadSign)
            }

            itemRV.checkChange.setOnClickListener {
                buildDialog(roadSign,position)
            }
            itemRV.setPhoto.setOnClickListener {
                buildDialogshow(roadSign,position)
            }
            itemRV.checkDelete.setOnClickListener {
                if (booleanAntiBag) {
                    buildDeleteDialog(roadSign , position)
                    booleanAntiBag = false
                }
            }

            itemRV.root.setOnClickListener {
                rvClick.show(roadSign)
            }
        }
    }

    private fun buildDialogshow(roadSign: RoadSign, position: Int) {
        val bindingDialog = DialogShowBinding.inflate(LayoutInflater.from(context))
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setView(bindingDialog.root)
        val file = File(context.filesDir, "${roadSign.image}.jpg")
        Glide.with(context).load(file).centerCrop().into(bindingDialog.setPhoto)
        bindingDialog.btnOk.setOnClickListener {
            dialogas.dismiss()
        }
        dialogas = alertDialog.create()
        dialogas.show()
    }

    private fun buildDialog(roadSign: RoadSign, position: Int) {
        val bindingDialog = EditDialogBinding.inflate(LayoutInflater.from(context))
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setView(bindingDialog.root)
        val arrayListTypes = ArrayList<String>()
        arrayListTypes.add("Ogohlantiruvchi")
        arrayListTypes.add("Imtiyozli")
        arrayListTypes.add("Ta'qiqlovchi")
        arrayListTypes.add("Buyuruvchi")
        val arrayAdapterTypes = ArrayAdapter(context, R.layout.item_spinner, arrayListTypes)
        bindingDialog.spinnerTimes.setAdapter(arrayAdapterTypes)
        bindingDialog.edtName.setText(roadSign.name)
        val file = File(context.filesDir, "${roadSign.image}.jpg")
        Glide.with(context).load(file).centerCrop().into(bindingDialog.setPhoto)
        bindingDialog.edtAbout.setText(roadSign.about)
        bindingDialog.btnSave.setOnClickListener {
            if (dialoga.edt_name.text.toString().trim().isNotEmpty() && dialoga.spinner_times.text.toString().trim().isNotEmpty()){
                val myDBHelper =MyDBHelper(context)
                roadSign.name = bindingDialog.edtName.text.toString()
                roadSign.about = bindingDialog.edtAbout.text.toString()
                roadSign.type = bindingDialog.spinnerTimes.text.toString()
                myDBHelper.updateRoadSign(roadSign)
                notifyItemChanged(position)
                dialoga.dismiss()
            }
        }
        dialoga = alertDialog.create()
        dialoga.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialoga.show()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(ItemRvBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(arrayListRoadSign[position], position)

    }

    override fun getItemCount(): Int = arrayListRoadSign.size

    fun buildDeleteDialog(
        roadSign: RoadSign,
        position: Int,
    ) {

        val bindingDialog = DeletedialogBinding.inflate(LayoutInflater.from(context))
        val alertDialog = AlertDialog.Builder(context)

        val file = File(context.filesDir, "${roadSign.image}.jpg")
        Glide.with(context).load(file).centerCrop().into(bindingDialog.imageProfile)

        bindingDialog.tvCancel.setOnClickListener {
            dialog.cancel()
        }

        bindingDialog.tvDelete.setOnClickListener {
            deleteRoadSign(roadSign, position)
        }

        alertDialog.setOnCancelListener {
            booleanAntiBag = true
        }

        alertDialog.setView(bindingDialog.root)
        dialog = alertDialog.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun deleteRoadSign(
        roadSign: RoadSign,
        position: Int,
    ) {
        val file = File(context.filesDir, "${roadSign.image}.jpg")
        if (context.filesDir.isDirectory) {
            for (i in context.filesDir.listFiles().indices) {
                if (context.filesDir.listFiles()[i] == file) {
                    context.filesDir.listFiles()[i].delete()
                    break
                }
            }
        }
        Toast.makeText(context, "O'chirildi", Toast.LENGTH_SHORT).show()
        dialog.cancel()

        rvClick.delete(roadSign, position)
        arrayListRoadSign.removeAt(position)
        val myDBHelper =MyDBHelper(context)
        myDBHelper.deleteRoadSign(roadSign)
        notifyItemRemoved(position)
        notifyItemRangeRemoved(position , arrayListRoadSign.size)
    }

    interface RVClick {
        fun like(roadSign: RoadSign, position: Int) {

        }

        fun delete(roadSign: RoadSign , position: Int) {

        }

        fun edit(roadSign: RoadSign) {
        }

        fun show(roadSign: RoadSign) {

        }
    }
}