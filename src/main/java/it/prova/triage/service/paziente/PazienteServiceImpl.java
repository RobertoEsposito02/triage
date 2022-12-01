package it.prova.triage.service.paziente;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.prova.triage.model.Paziente;
import it.prova.triage.model.StatoPaziente;
import it.prova.triage.repository.paziente.PazienteRepository;

@Service
public class PazienteServiceImpl implements PazienteService {

	@Autowired
	private PazienteRepository pazienteRepository;

	@Override
	public List<Paziente> listAll() {
		return (List<Paziente>) pazienteRepository.findAll();
	}

	@Override
	public Paziente caricaSingoloElemento(Long id) {
		return pazienteRepository.findById(id).orElse(null);
	}

	@Override
	public void aggiorna(Paziente pazienteInstance) {
		pazienteRepository.save(pazienteInstance);
	}

	@Override
	public Paziente inserisciNuovo(Paziente pazienteInstance) {
		pazienteInstance.setDataRegistrazione(LocalDate.now());
		pazienteInstance.setStato(StatoPaziente.INT_ATTESA_VISITA);
		return pazienteRepository.save(pazienteInstance);
	}

	@Override
	public void rimuovi(Long idToRemove) {
		pazienteRepository.deleteById(idToRemove);
	}

}
