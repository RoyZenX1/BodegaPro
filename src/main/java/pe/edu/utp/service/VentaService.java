package pe.edu.utp.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.dto.VentaDTO;
import pe.edu.utp.model.Cliente;
import pe.edu.utp.model.DetalleVenta;
import pe.edu.utp.model.Producto;
import pe.edu.utp.model.Venta;
import pe.edu.utp.repository.ClienteRepository;
import pe.edu.utp.repository.ProductoRepository;
import pe.edu.utp.repository.VentaRepository;

@Service
public class VentaService {
    private final VentaRepository repository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;
    public VentaService(VentaRepository repository, ClienteRepository clienteRepository, ProductoRepository productoRepository) {
        this.repository = repository; this.clienteRepository = clienteRepository; this.productoRepository = productoRepository;
    }
    @Transactional(readOnly = true)
    public List<Venta> listarTodos() { return repository.listarConRelaciones(); }
    @Transactional(readOnly = true)
    public Optional<Venta> buscar(Long id) { return repository.buscarConRelaciones(id); }
    @Transactional(readOnly = true)
    public double totalVendido() {
        return repository.listarConRelaciones().stream().map(Venta::getTotal).mapToDouble(BigDecimal::doubleValue).sum();
    }
    @Transactional(readOnly = true)
    public List<Venta> buscarPorTermino(String termino) {
        if (termino == null || termino.isBlank()) return listarTodos();
        String t = termino.trim().toLowerCase();
        return listarTodos().stream().filter(v ->
                String.valueOf(v.getId()).contains(t)
                || (v.getCliente() != null && v.getCliente().getNombre().toLowerCase().contains(t))
                || v.getDetalles().stream().anyMatch(d -> d.getProducto().getNombre().toLowerCase().contains(t)))
                .toList();
    }
    @Transactional
    public Venta registrar(VentaDTO formulario) {
        Venta venta;
        if (formulario.getId() != null) {
            venta = repository.buscarConRelaciones(formulario.getId())
                    .orElseThrow(() -> new IllegalArgumentException("La venta que intentas editar ya no existe."));
            // Revertir temporalmente el stock de los detalles anteriores. Si algo falla, la transacción revierte todo.
            for (DetalleVenta anterior : new ArrayList<>(venta.getDetalles())) {
                Producto productoAnterior = anterior.getProducto();
                productoAnterior.setStock(productoAnterior.getStock() + anterior.getCantidad());
                productoRepository.save(productoAnterior);
            }
            venta.getDetalles().clear();
        } else {
            venta = new Venta();
        }

        Cliente cliente = clienteRepository.findById(formulario.getClienteId())
                .orElseThrow(() -> new IllegalArgumentException("Selecciona un cliente registrado."));
        Producto producto = productoRepository.findWithRelationsById(formulario.getProductoId())
                .orElseThrow(() -> new IllegalArgumentException("Selecciona un producto registrado."));
        int cantidad = formulario.getCantidad();
        if (producto.getStock() < cantidad) throw new IllegalArgumentException("Stock insuficiente. Disponible: " + producto.getStock() + ".");

        producto.setStock(producto.getStock() - cantidad);
        productoRepository.save(producto);
        venta.setCliente(cliente);
        venta.setFecha(LocalDateTime.now());
        DetalleVenta detalle = new DetalleVenta(producto, cantidad, producto.getVenta());
        detalle.setVenta(venta);
        venta.getDetalles().add(detalle);
        return repository.save(venta);
    }
    @Transactional
    public void eliminar(Long id) {
        Venta venta = repository.buscarConRelaciones(id)
                .orElseThrow(() -> new IllegalArgumentException("La venta ya no existe."));
        for (DetalleVenta detalle : new ArrayList<>(venta.getDetalles())) {
            Producto producto = detalle.getProducto();
            producto.setStock(producto.getStock() + detalle.getCantidad());
            productoRepository.save(producto);
        }
        repository.delete(venta);
    }
}
