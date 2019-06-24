package it.polito.tdp.newufosightings.model;

public class Confini {
	
	private String stato;
	private String stato2;
	
	public Confini(String stato, String stato2) {
		super();
		this.stato = stato;
		this.stato2 = stato2;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public String getStato2() {
		return stato2;
	}

	public void setStato2(String stato2) {
		this.stato2 = stato2;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((stato == null) ? 0 : stato.hashCode());
		result = prime * result + ((stato2 == null) ? 0 : stato2.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Confini other = (Confini) obj;
		if (stato == null) {
			if (other.stato != null)
				return false;
		} else if (!stato.equals(other.stato))
			return false;
		if (stato2 == null) {
			if (other.stato2 != null)
				return false;
		} else if (!stato2.equals(other.stato2))
			return false;
		return true;
	}
	
	
	
	

}
