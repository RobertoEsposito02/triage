package it.prova.triage;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import it.prova.triage.model.Paziente;
import it.prova.triage.model.Ruolo;
import it.prova.triage.model.Utente;
import it.prova.triage.service.paziente.PazienteService;
import it.prova.triage.service.ruolo.RuoloService;
import it.prova.triage.service.utente.UtenteService;

@SpringBootApplication
public class TriageApplication implements CommandLineRunner{

	@Autowired
	private RuoloService ruoloServiceInstance;
	@Autowired
	private PazienteService pazienteService;
	@Autowired
	private UtenteService utenteServiceInstance;
	
	public static void main(String[] args) {
		SpringApplication.run(TriageApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if (ruoloServiceInstance.cercaPerDescrizioneECodice("Administrator", Ruolo.ROLE_ADMIN) == null) {
			ruoloServiceInstance
					.inserisciNuovo(Ruolo.builder().descrizione("Administrator").codice(Ruolo.ROLE_ADMIN).build());
		}

		if (ruoloServiceInstance.cercaPerDescrizioneECodice("SUB_OPERATOR", Ruolo.ROLE_SUB_OPERATOR) == null) {
			ruoloServiceInstance
					.inserisciNuovo(Ruolo.builder().descrizione("SUB_OPERATOR").codice(Ruolo.ROLE_SUB_OPERATOR).build());
		}

		if (utenteServiceInstance.findByUsername("admin") == null) {
			Utente admin = Utente.builder()
					.nome("mario")
					.cognome("rossi")
					.username("admin")
					.password("admin")
					.dataRegistrazione(LocalDate.of(2002, 8, 10))
					.email("email@admin.it")
					.build();
			admin.getRuoli().add(ruoloServiceInstance.cercaPerDescrizioneECodice("Administrator", Ruolo.ROLE_ADMIN));
			utenteServiceInstance.inserisciNuovo(admin);
			utenteServiceInstance.changeUserAbilitation(admin.getId());
		}

		if (utenteServiceInstance.findByUsername("user") == null) {
			Utente classicUser = Utente.builder()
					.nome("luigi")
					.cognome("bianchi")
					.username("user")
					.password("user")
					.dataRegistrazione(LocalDate.of(2002, 8, 10))
					.email("email@user.it")
					.build();
			classicUser.getRuoli()
					.add(ruoloServiceInstance.cercaPerDescrizioneECodice("SUB_OPERATOR", Ruolo.ROLE_SUB_OPERATOR));
			utenteServiceInstance.inserisciNuovo(classicUser);
			utenteServiceInstance.changeUserAbilitation(classicUser.getId());
		}

		pazienteService.inserisciNuovo(Paziente.builder()
				.nome("giorgio")
				.cognome("bianchi")
				.codiceFiscale("giorgioBianchi")
				.build());
		
		pazienteService.inserisciNuovo(Paziente.builder()
				.nome("francesco")
				.cognome("parisi")
				.codiceFiscale("francescoParisi")
				.build());
	}

}
