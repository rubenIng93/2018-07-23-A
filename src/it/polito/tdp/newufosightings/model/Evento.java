package it.polito.tdp.newufosightings.model;

import java.time.LocalDateTime;

public class Evento implements Comparable<Evento>{
	
	public enum TipoEvento{
		CESSATA_ALLERTA,
		AVVISTAMENTO
	}
	
	private TipoEvento tipo;
	private LocalDateTime data;
	private State stato;
	
	public Evento(TipoEvento tipo, LocalDateTime data, State stato) {
		super();
		this.tipo = tipo;
		this.data = data;
		this.stato = stato;
	}

	public TipoEvento getTipo() {
		return tipo;
	}

	public void setTipo(TipoEvento tipo) {
		this.tipo = tipo;
	}

	public LocalDateTime getData() {
		return data;
	}

	public void setData(LocalDateTime data) {
		this.data = data;
	}

	public State getStato() {
		return stato;
	}

	public void setStato(State stato) {
		this.stato = stato;
	}

	@Override
	public String toString() {
		return String.format("Evento -> tipo = %s, data = %s, stato = %s", tipo, data, stato);
	}

	@Override
	public int compareTo(Evento altro) {
		return this.data.compareTo(altro.getData());
	}
	
	
	
	
	
	

}
