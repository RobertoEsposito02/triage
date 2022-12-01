package it.prova.triage.web.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import it.prova.triage.dto.paziente.PazienteDTO;
import it.prova.triage.model.Paziente;
import it.prova.triage.service.paziente.PazienteService;
import it.prova.triage.web.api.exception.IdNotNullForInsertException;
import it.prova.triage.web.api.exception.IdNullForUpdateException;
import it.prova.triage.web.api.exception.PazienteNotFoundException;

@RestController
@RequestMapping("/api/paziente")
public class PazienteController {
	
	@Autowired
	private PazienteService pazienteService;
	
	@GetMapping
	public List<PazienteDTO> listAlList(){
		return PazienteDTO.createListDTOFromModel(pazienteService.listAll());
	}
	
	@GetMapping("/{id}")
	public PazienteDTO findById(@PathVariable(required = true) Long id) {
		Paziente pazienteDaCaricare = pazienteService.caricaSingoloElemento(id);
		
		if(pazienteDaCaricare == null)
			throw new PazienteNotFoundException("paziente non trovato");
		
		return PazienteDTO.buildPazienteDTOFromModel(pazienteDaCaricare);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public PazienteDTO inserisci(@RequestBody PazienteDTO paziente) {
		if(paziente.getId() != null)
			throw new IdNotNullForInsertException("impossibile aggiornare un record se viene inserito anche l'id");
		
		return PazienteDTO.buildPazienteDTOFromModel(pazienteService.inserisciNuovo(paziente.buildPazienteModel()));
	}
	
	@PutMapping
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void update(@RequestBody PazienteDTO paziente) {
		if(paziente.getId() == null)
			throw new IdNullForUpdateException("impossibile aggiornare un campo se non si inserisce l'id");
		
		pazienteService.aggiorna(paziente.buildPazienteModel());
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT) 
	public void delete(@PathVariable(required = true) Long id) {
		Paziente pazienteDaEliminare = pazienteService.caricaSingoloElemento(id);
		
		if(pazienteDaEliminare == null)
			throw new PazienteNotFoundException("paziente non trovato");
		
		pazienteService.rimuovi(id);
	}
}
