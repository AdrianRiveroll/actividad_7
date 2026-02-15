package mx.ubam.inventarioapp.ui.client

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mx.ubam.inventarioapp.data.model.Product
import mx.ubam.inventarioapp.databinding.RowCartItemBinding

class CartAdapter(
    private val onPlus: (Long) -> Unit,
    private val onMinus: (Long) -> Unit,
    private val onRemove: (Long) -> Unit
) : RecyclerView.Adapter<CartAdapter.VH>() {

    private val items = mutableListOf<Pair<Product, Int>>()

    fun submit(list: List<Pair<Product, Int>>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = RowCartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(items[position])
    override fun getItemCount(): Int = items.size

    inner class VH(private val b: RowCartItemBinding) : RecyclerView.ViewHolder(b.root) {

        fun bind(item: Pair<Product, Int>) {
            val p = item.first
            val qty = item.second

            b.tvName.text = "${p.name} (${p.brand})"
            b.tvQty.text = qty.toString()


            val price = p.price ?: 0.0
            val subtotal = price * qty
            b.tvSubtotal.text = "Subtotal: ${"%.2f".format(subtotal)}"

            b.btnPlus.setOnClickListener { onPlus(p.id) }
            b.btnMinus.setOnClickListener { onMinus(p.id) }

           
            b.btnRemove.setOnClickListener { onRemove(p.id) }
        }
    }
}
