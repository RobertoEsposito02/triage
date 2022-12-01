package it.prova.triage.dto.dottore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Setter @Getter
@NoArgsConstructor @AllArgsConstructor
public class DottoreResponseDTO {
	private String nome;
	private String cognome;
	private String codiceDottore;
	private String codFiscalePazienteAttualmenteInVisita;
	private boolean inVisita;
	private boolean inServizio;
}
