package com.aluracursos.desafio;

import com.aluracursos.desafio.principal.Principal;
import com.aluracursos.desafio.service.ConsumoAPI;
import com.aluracursos.desafio.service.ConvierteDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class DesafioApplication implements CommandLineRunner {

	private final Principal principal;

	public DesafioApplication(@Lazy Principal principal) {
		this.principal = principal;
	}

	public static void main(String[] args) {
		SpringApplication.run(DesafioApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		principal.muestraElMenu();
	}

	@Bean
	public ConsumoAPI consumoAPI() {
		return new ConsumoAPI();
	}

	@Bean
	public ConvierteDatos conversor() {
		return new ConvierteDatos();
	}

	public Principal getPrincipal() {
		return principal;
	}
}
