package model;

import java.io.Serializable;

public class ComandoVO implements Serializable {

	private static final long serialVersionUID = 5550395179466340177L;
	private String comando;
	private Boolean plSQL;

	public ComandoVO() {
		this.plSQL = Boolean.FALSE;
	}
	
	public ComandoVO(String comando) {
		this.comando = comando;
		this.plSQL = Boolean.FALSE;
	}
	
	public ComandoVO(String comando, Boolean plSQL) {
		this.comando = comando;
		this.plSQL = plSQL;
	}

	public String getComando() {
		return comando;
	}

	public void setComando(String comando) {
		this.comando = comando;
	}

	public Boolean getPlSQL() {
		return plSQL;
	}

	public void setPlSQL(Boolean plSQL) {
		this.plSQL = plSQL;
	}

	@Override
	public String toString() {
		return this.comando;
	}

}
