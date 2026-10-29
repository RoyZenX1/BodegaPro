package pe.edu.utp;

import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Validator;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pe.edu.utp.dto.ProductoDTO;
import pe.edu.utp.model.Categoria;
import pe.edu.utp.model.Producto;
import pe.edu.utp.repository.CategoriaRepository;
import pe.edu.utp.repository.ProductoRepository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class BodegaProApplicationTests {
    @Autowired private Validator validator;
    @Autowired private CategoriaRepository categoriaRepository;
    @Autowired private ProductoRepository productoRepository;

    @Test
    void contextLoads() { }

    @Test
    void dtoInvalidaUnPrecioDeVentaCero() {
        ProductoDTO dto = new ProductoDTO();
        dto.setCodigo("TEST-VAL");
        dto.setNombre("Producto de prueba");
        dto.setCategoriaId(1L);
        dto.setCompra(new BigDecimal("0.50"));
        dto.setVenta(BigDecimal.ZERO);
        dto.setStock(5);
        dto.setStockMinimo(1);
        assertTrue(validator.validate(dto).stream().anyMatch(v -> v.getPropertyPath().toString().equals("venta")));
    }

    @Test
    @Transactional
    void productoSePersisteConSuCategoriaRelacionada() {
        Categoria categoria = new Categoria();
        categoria.setNombre("Categoría de prueba JPA");
        categoria.setDescripcion("Verifica la relación ManyToOne");
        categoria = categoriaRepository.saveAndFlush(categoria);

        Producto producto = new Producto();
        producto.setCodigo("TEST-JPA-001");
        producto.setNombre("Producto de prueba JPA");
        producto.setCategoria(categoria);
        producto.setCompra(new BigDecimal("1.25"));
        producto.setVenta(new BigDecimal("2.50"));
        producto.setStock(10);
        producto.setStockMinimo(2);
        producto = productoRepository.saveAndFlush(producto);

        Producto recuperado = productoRepository.findWithRelationsById(producto.getId()).orElseThrow();
        assertTrue(recuperado.getCategoria().getId().equals(categoria.getId()));
        assertFalse(recuperado.getCategoria().getNombre().isBlank());
    }
}
