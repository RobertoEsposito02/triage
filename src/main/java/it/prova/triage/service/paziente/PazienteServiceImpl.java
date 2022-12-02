package it.prova.triage.service.paziente;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.prova.triage.model.Paziente;
import it.prova.triage.model.StatoPaziente;
import it.prova.triage.repository.paziente.PazienteRepository;
import it.prova.triage.web.api.exception.PazienteNotFoundException;
import it.prova.triage.web.api.exception.PazienteNotInVisitaException;

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

	@Override
	public void ricovera(Long id) {
		Paziente result = pazienteRepository.findById(id).orElse(null);
		
		if(result == null)
			throw new PazienteNotFoundException("paziente non trovato");
		
		if(!result.getStato().equals(StatoPaziente.IN_VISITA))
			throw new PazienteNotInVisitaException("impossibile ricoverare un paziente non in visita");
		
		result.setStato(StatoPaziente.RICOVERATO);
		result.setCodiceDottore(null);
		
		pazienteRepository.save(result);
	}

	@Override
	public void impostaCodiceDottore(String cf, String cd) {
		Paziente result = pazienteRepository.findByCodiceFiscale(cf).orElse(null);
		
		if(result == null)
			throw new PazienteNotFoundException("paziente non trovato");
		
		result.setCodiceDottore(cd);
		pazienteRepository.save(result);
	}

	@Override
	public void dimetti(Long id) {
		Paziente result = pazienteRepository.findById(id).orElse(null);
		
		if(result == null)
			throw new PazienteNotFoundException("paziente non trovato");
		
		if(!result.getStato().equals(StatoPaziente.IN_VISITA))
			throw new PazienteNotInVisitaException("impossibile dimettere un paziente non in visita");
		
		result.setStato(StatoPaziente.DIMESSO);
		result.setCodiceDottore(null);
		
		pazienteRepository.save(result);
	}

}
