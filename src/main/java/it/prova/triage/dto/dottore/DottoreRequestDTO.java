package it.prova.triage.dto.dottore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class DottoreRequestDTO {
	private String nome;
	private String cognome;
	private String codiceFiscale;
}
