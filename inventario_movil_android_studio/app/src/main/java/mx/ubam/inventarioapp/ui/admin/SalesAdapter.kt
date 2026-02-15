package mx.ubam.inventarioapp.ui.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mx.ubam.inventarioapp.data.model.Sale
import mx.ubam.inventarioapp.databinding.RowSaleBinding

class SalesAdapter(private val onView: (Long) -> Unit) : RecyclerView.Adapter<SalesAdapter.VH>() {
    private val items = mutableListOf<Sale>()

    fun submit(list: List<Sale>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = RowSaleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(items[position])
    override fun getItemCount(): Int = items.size

    inner class VH(private val b: RowSaleBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(s: Sale) {
            b.tvSale.text = "Venta #${s.id} · ${s.customerName ?: ""} · Total: ${s.total ?: 0.0}"
            b.btnView.setOnClickListener { onView(s.id) }
        }
    }
}
