package it.prova.triage.dto.paziente;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import it.prova.triage.model.Paziente;
import it.prova.triage.model.StatoPaziente;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Setter @Getter
@NoArgsConstructor @AllArgsConstructor
public class PazienteDTO {
	private Long id;
	private String nome;
	private String cognome;
	private String codiceFiscale;
	private String codiceDottore;
	private LocalDate dataRegistrazione;
	private StatoPaziente stato;
	
	public Paziente buildPazienteModel() {
		Paziente result = Paziente.builder()
				.id(id)
				.nome(nome)
				.cognome(cognome)
				.codiceFiscale(codiceFiscale)
				.dataRegistrazione(dataRegistrazione)
				.stato(stato)
				.build();
		
		if(codiceDottore != null)
			result.setCodiceDottore(codiceDottore);
		
		return result;
	}
	
	public static PazienteDTO buildPazienteDTOFromModel(Paziente pazienteModel) {
		PazienteDTO result = PazienteDTO.builder()
				.id(pazienteModel.getId())
				.nome(pazienteModel.getNome())
				.cognome(pazienteModel.getCognome())
				.codiceFiscale(pazienteModel.getCodiceFiscale())
				.dataRegistrazione(pazienteModel.getDataRegistrazione())
				.stato(pazienteModel.getStato())
				.build();
		
		if(pazienteModel.getCodiceDottore() != null)
			result.setCodiceDottore(pazienteModel.getCodiceDottore());
		
		return result;
	}
	
	public static List<PazienteDTO> createListDTOFromModel(List<Paziente> listaPazienteModel){
		return listaPazienteModel.stream().map(paziente -> {
			return PazienteDTO.buildPazienteDTOFromModel(paziente);
		}).collect(Collectors.toList());
	}
	
	public static List<Paziente> createModelListFromDTO(List<PazienteDTO> listaPazienteDTO){
		return listaPazienteDTO.stream().map(paziente -> {
			return paziente.buildPazienteModel();
		}).collect(Collectors.toList());
	}
}
