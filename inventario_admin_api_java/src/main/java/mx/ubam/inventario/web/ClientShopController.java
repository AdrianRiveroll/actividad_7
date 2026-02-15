package mx.ubam.inventario.web;

import mx.ubam.inventario.repo.ProductRepository;
import mx.ubam.inventario.repo.SaleRepository;
import mx.ubam.inventario.service.SaleService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/client")
public class ClientShopController {

    private final ProductRepository productRepository;
    private final SaleRepository saleRepository;
    private final SaleService saleService;

    public ClientShopController(ProductRepository productRepository, SaleRepository saleRepository, SaleService saleService) {
        this.productRepository = productRepository;
        this.saleRepository = saleRepository;
        this.saleService = saleService;
    }

    @GetMapping("/products")
    public String products(@RequestParam(value="q", required=false) String q, Model model, HttpSession session) {
        var products = (q == null || q.isBlank()) ? productRepository.findAll() : productRepository.search(q);
        model.addAttribute("products", products);
        model.addAttribute("q", q == null ? "" : q);
        model.addAttribute("cartCount", getCart(session).values().stream().mapToInt(Integer::intValue).sum());
        return "client/products";
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long productId, @RequestParam int quantity, HttpSession session) {
        if (quantity <= 0) quantity = 1;
        Map<Long, Integer> cart = getCart(session);
        cart.put(productId, cart.getOrDefault(productId, 0) + quantity);
        session.setAttribute("CART", cart);
        return "redirect:/client/cart";
    }

    @GetMapping("/cart")
    public String cart(Model model, HttpSession session) {
        Map<Long, Integer> cart = getCart(session);

        var items = new ArrayList<Map<String, Object>>();
        double total = 0;

        for (var e : cart.entrySet()) {
            var p = productRepository.findById(e.getKey()).orElse(null);
            if (p == null) continue;

            int qty = e.getValue();
            double line = p.getPrice().doubleValue() * qty;
            total += line;

            items.add(Map.of(
                    "product", p,
                    "quantity", qty,
                    "lineTotal", line
            ));
        }

        model.addAttribute("items", items);
        model.addAttribute("total", total);
        return "client/cart";
    }

    @PostMapping("/cart/remove")
    public String remove(@RequestParam Long productId, HttpSession session) {
        Map<Long, Integer> cart = getCart(session);
        cart.remove(productId);
        session.setAttribute("CART", cart);
        return "redirect:/client/cart";
    }

    @PostMapping("/checkout")
    public String checkout(Authentication auth, HttpSession session, Model model) {
        Map<Long, Integer> cart = getCart(session);
        if (cart.isEmpty()) {
            model.addAttribute("error", "Tu carrito está vacío.");
            return cart(model, session);
        }

        try {
            String username = auth.getName(); // "client"
            var items = cart.entrySet().stream()
                    .map(e -> new SaleService.ItemRequest(e.getKey(), e.getValue()))
                    .toList();

            var sale = saleService.createSale(username, items);

            session.setAttribute("CART", new HashMap<Long, Integer>()); // vaciar carrito
            return "redirect:/client/orders/" + sale.getId();
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return cart(model, session);
        }
    }

    @GetMapping("/orders")
    public String orders(Authentication auth, Model model) {
        var sales = saleRepository.findByCustomerNameOrderByCreatedAtDesc(auth.getName());
        model.addAttribute("sales", sales);
        return "client/orders";
    }

    @GetMapping("/orders/{id}")
    public String orderDetail(@PathVariable Long id, Authentication auth, Model model) {
        var sale = saleRepository.findById(id).orElseThrow();
        if (!sale.getCustomerName().equals(auth.getName())) {
            return "redirect:/client/orders"; // no ver compras de otros
        }
        model.addAttribute("sale", sale);
        return "client/order_detail";
    }

    @SuppressWarnings("unchecked")
    private Map<Long, Integer> getCart(HttpSession session) {
        Object obj = session.getAttribute("CART");
        if (obj instanceof Map<?, ?>) {
            return (Map<Long, Integer>) obj;
        }
        Map<Long, Integer> cart = new HashMap<>();
        session.setAttribute("CART", cart);
        return cart;
    }
}
