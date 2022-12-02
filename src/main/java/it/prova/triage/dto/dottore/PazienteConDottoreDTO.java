package it.prova.triage.dto.dottore;

import java.time.LocalDate;

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
	
	private String nomeDoc;
	private String cognomeDoc;
	private String codiceFiscaleDoc;
	
}
