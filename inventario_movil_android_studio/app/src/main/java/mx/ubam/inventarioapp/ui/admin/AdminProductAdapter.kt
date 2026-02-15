package mx.ubam.inventarioapp.ui.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mx.ubam.inventarioapp.data.model.Product
import mx.ubam.inventarioapp.databinding.RowProductBinding

class AdminProductAdapter(
    private val onEdit: (Product) -> Unit,
    private val onDelete: (Product) -> Unit
) : RecyclerView.Adapter<AdminProductAdapter.VH>() {

    private val items = mutableListOf<Product>()

    fun submit(list: List<Product>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = RowProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(items[position])
    override fun getItemCount(): Int = items.size

    inner class VH(private val b: RowProductBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(p: Product) {
            b.tvName.text = p.name
            val cap = p.storageCapacity ?: "N/A"
            b.tvMeta.text = "Marca: ${p.brand} · Cap: $cap · Stock: ${p.stock} · ${p.price}"
            b.btnAdd.text = "Editar"
            b.btnAdd.setOnClickListener { onEdit(p) }
            b.root.setOnLongClickListener { onDelete(p); true }
        }
    }
}
