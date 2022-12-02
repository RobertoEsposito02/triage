package it.prova.triage.dto.dottore;

import java.time.LocalDate;

import it.prova.triage.model.Paziente;
import it.prova.triage.model.StatoPaziente;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter @Setter
public class PazienteConDottoreDTO {
	private String nome;
	private String cognome;
	private String codiceFiscale;
	private String codiceDottore;
	private LocalDate dataRegistrazione;
	private StatoPaziente stato;
	
	private String ___________ = "-------";
	
	private String nomeDoc;
	private String cognomeDoc;
	private String codiceFiscaleDoc;
	
	public static PazienteConDottoreDTO buildDTOFromPazienteModelAndDocDTO(Paziente paziente, DottoreResponseDTO doc) {
		PazienteConDottoreDTO result = PazienteConDottoreDTO.builder()
				.nomeDoc(doc.getNome())
				.cognomeDoc(doc.getCognome())
				.codiceFiscaleDoc(doc.getCodiceDottore())
				.___________("_________________")
				.nome(paziente.getNome())
				.cognome(paziente.getCognome())
				.codiceFiscale(paziente.getCodiceFiscale())
				.codiceDottore(paziente.getCodiceDottore())
				.dataRegistrazione(paziente.getDataRegistrazione())
				.stato(paziente.getStato())
				.build();
		
		return result;
	}
}
