package pe.edu.utp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.utp.model.Configuracion;
import pe.edu.utp.repository.ConfiguracionRepository;

@Service
public class ConfiguracionService {
    private final ConfiguracionRepository repository;
    public ConfiguracionService(ConfiguracionRepository repository) { this.repository = repository; }
    @Transactional
    public Configuracion obtener() {
        return repository.findFirstByOrderByIdAsc().orElseGet(() -> repository.save(new Configuracion()));
    }
    @Transactional
    public Configuracion guardar(Configuracion nueva) {
        Configuracion actual = obtener();
        actual.setNombreBodega(nueva.getNombreBodega().trim());
        actual.setMoneda(nueva.getMoneda());
        actual.setTema(nueva.getTema());
        return repository.save(actual);
    }
}
