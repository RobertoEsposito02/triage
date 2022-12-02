package it.prova.triage.web.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import it.prova.triage.dto.dottore.DottorePazienteRequestDTO;
import it.prova.triage.dto.dottore.DottorePazienteResponseDTO;
import it.prova.triage.dto.dottore.DottoreResponseDTO;
import it.prova.triage.dto.dottore.PazienteConDottoreDTO;
import it.prova.triage.model.Paziente;
import it.prova.triage.service.paziente.PazienteService;
import it.prova.triage.web.api.exception.DottoreNonDisponibileEsception;
import it.prova.triage.web.api.exception.PazienteNotFoundException;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/assegnapaziente")
public class AssegnaPazienteController {

	private static final Logger LOGGER = LogManager.getLogger(AssegnaPazienteController.class);

	@Autowired
	private WebClient webClient;

	@Autowired
	private PazienteService pazienteService;

	@GetMapping("/{cd}")
	public DottoreResponseDTO verificaDisponibilitaDottore(@PathVariable(required = true) String cd) {

		LOGGER.info(".........invocazione servizio esterno............");

		DottoreResponseDTO dottoreResponseDTO = webClient.get().uri("/verificaDisponibilitaDottore/" + cd).retrieve()
				.onStatus(HttpStatus::is4xxClientError, response -> {
					throw new DottoreNonDisponibileEsception("dottore non disponibile");
				}).bodyToMono(DottoreResponseDTO.class).block();

		LOGGER.info(".........invocazione servizio esterno completata............");

		return dottoreResponseDTO;
	}

	@PostMapping("/impostaVisita")
	public DottorePazienteResponseDTO impostaVisita(@RequestBody DottorePazienteRequestDTO dottore) {

		pazienteService.impostaCodiceDottore(dottore.getCodFiscalePazienteAttualmenteInVisita(),
				dottore.getCodiceDottore());

		LOGGER.info(".........invocazione servizio esterno............");

		ResponseEntity<DottorePazienteResponseDTO> response = webClient
				.post().uri("/impostaVisita").body(
						Mono.just(DottorePazienteRequestDTO.builder().codiceDottore(dottore.getCodiceDottore())
								.codFiscalePazienteAttualmenteInVisita(
										dottore.getCodFiscalePazienteAttualmenteInVisita())
								.build()),
						DottorePazienteRequestDTO.class)
				.retrieve().toEntity(DottorePazienteResponseDTO.class).block();

		LOGGER.info(".........invocazione servizio esterno completata............");

		return DottorePazienteResponseDTO.builder().codiceDottore(response.getBody().getCodiceDottore())
				.codFiscalePazienteAttualmenteInVisita(response.getBody().getCodFiscalePazienteAttualmenteInVisita())
				.build();
	}

	@PostMapping("/verificaEImposta/{cd}")
	public PazienteConDottoreDTO verificaEImposta(@PathVariable(required = true) String cd,
			@RequestBody DottorePazienteRequestDTO dottore) {

		LOGGER.info(".........invocazione servizio esterno............");

		DottoreResponseDTO doc = verificaDisponibilitaDottore(cd);
		DottorePazienteResponseDTO codiceDottoreConCodicePaziente = impostaVisita(dottore);

		LOGGER.info(".........invocazione servizio esterno completata............");

		Paziente paziente = pazienteService
				.cercaPerCodiceFiscale(codiceDottoreConCodicePaziente.getCodFiscalePazienteAttualmenteInVisita());

		if (paziente == null)
			throw new PazienteNotFoundException("paziente non trovato");

		return PazienteConDottoreDTO.buildDTOFromPazienteModelAndDocDTO(paziente, doc);
	}
}
