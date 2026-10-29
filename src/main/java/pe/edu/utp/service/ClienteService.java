package pe.edu.utp.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.model.Cliente;
import pe.edu.utp.repository.ClienteRepository;
import pe.edu.utp.repository.VentaRepository;

@Service
public class ClienteService {
    private final ClienteRepository repository;
    private final VentaRepository ventaRepository;
    public ClienteService(ClienteRepository repository, VentaRepository ventaRepository) {
        this.repository = repository; this.ventaRepository = ventaRepository;
    }
    @Transactional(readOnly = true)
    public List<Cliente> listarTodos() { return repository.findAll(); }
    @Transactional(readOnly = true)
    public Optional<Cliente> buscar(Long id) { return repository.findById(id); }
    @Transactional(readOnly = true)
    public boolean existe(String documento) { return documento != null && repository.existsByDocumento(documento.trim()); }
    @Transactional(readOnly = true)
    public boolean existeOtro(String documento, Long id) {
        if (documento == null) return false;
        return id == null ? existe(documento) : repository.existsByDocumentoAndIdNot(documento.trim(), id);
    }
    @Transactional
    public Cliente guardar(Cliente cliente) {
        cliente.setNombre(cliente.getNombre().trim());
        cliente.setDocumento(cliente.getDocumento().trim());
        cliente.setTelefono(cliente.getTelefono().trim());
        if (cliente.getDireccion() != null) cliente.setDireccion(cliente.getDireccion().trim());
        return repository.save(cliente);
    }
    @Transactional
    public void eliminar(Long id) {
        if (!repository.existsById(id)) return;
        if (ventaRepository.existsByCliente_Id(id)) {
            throw new IllegalStateException("No se puede eliminar el cliente porque tiene ventas registradas.");
        }
        repository.deleteById(id);
    }
    @Transactional(readOnly = true)
    public List<Cliente> buscar(String termino) {
        if (termino == null || termino.isBlank()) return repository.findAll();
        String t = termino.trim();
        return repository.findByNombreContainingIgnoreCaseOrDocumentoContainingIgnoreCase(t, t);
    }
}
