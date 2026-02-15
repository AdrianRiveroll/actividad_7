package mx.ubam.inventarioapp.data

import mx.ubam.inventarioapp.data.model.Product

object CartStore {
    private val items = linkedMapOf<Long, Pair<Product, Int>>()

    fun add(p: Product) {
        val cur = items[p.id]
        val qty = (cur?.second ?: 0) + 1
        items[p.id] = Pair(p, qty)
    }

    fun plus(id: Long) {
        val cur = items[id] ?: return
        items[id] = Pair(cur.first, cur.second + 1)
    }

    fun minus(id: Long) {
        val cur = items[id] ?: return
        val newQty = cur.second - 1
        if (newQty <= 0) items.remove(id) else items[id] = Pair(cur.first, newQty)
    }

    fun remove(id: Long) {
        items.remove(id)
    }

    fun clear() = items.clear()

    fun snapshot(): List<Pair<Product, Int>> = items.values.toList()

    fun toSaleItems(): List<Pair<Long, Int>> =
        items.values.map { it.first.id to it.second }

    fun total(): Double =
        items.values.sumOf { (p, qty) -> (p.price ?: 0.0) * qty }
}
