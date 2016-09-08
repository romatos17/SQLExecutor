package model;

import java.io.Serializable;

public class BancoDadosVO implements Serializable {

	private static final long serialVersionUID = -3160767019527756449L;
	private String chave;
	private String urlConexao;
	private String usuario;
	private String senha;
	private String classForName;
	private Integer qtdConexoes;
	private Integer qtdParticoes;
	private Boolean ativo;

	public BancoDadosVO(String chave, String urlConexao, String usuario, String senha, String classForName, Integer qtdConexoes, Integer qtdParticoes) {
		this.chave = chave;
		this.urlConexao = urlConexao;
		this.usuario = usuario;
		this.senha = senha;
		this.classForName = classForName;
		this.qtdConexoes = qtdConexoes;
		this.qtdParticoes = qtdParticoes;
	}

	public BancoDadosVO() {
	}

	public String getUrlConexao() {
		return urlConexao;
	}

	public void setUrlConexao(String urlConexao) {
		this.urlConexao = urlConexao;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getClassForName() {
		return classForName;
	}

	public void setClassForName(String classForName) {
		this.classForName = classForName;
	}

	public Integer getQtdConexoes() {
		return qtdConexoes;
	}

	public void setQtdConexoes(Integer qtdConexoes) {
		this.qtdConexoes = qtdConexoes;
	}

	public Integer getQtdParticoes() {
		return qtdParticoes;
	}

	public void setQtdParticoes(Integer qtdParticoes) {
		this.qtdParticoes = qtdParticoes;
	}

	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((chave == null) ? 0 : chave.hashCode());
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
		BancoDadosVO other = (BancoDadosVO) obj;
		if (chave == null) {
			if (other.chave != null)
				return false;
		} else if (!chave.equals(other.chave))
			return false;
		return true;
	}

}
