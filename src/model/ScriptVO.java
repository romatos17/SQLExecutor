package model;

import java.io.Serializable;
import java.util.List;

public class ScriptVO implements Comparable<ScriptVO>, Serializable {

	private static final long serialVersionUID = -1499077061318526539L;
	private String chave;
	private String nomeArquivo;
	private List<ComandoVO> comandos;
	private Boolean thread;
	private Boolean ativo;
	private List<ScriptVO> listaScript;
	private Boolean arquivo;
	private String extensao;
	private Integer index;

	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public List<ComandoVO> getComandos() {
		return comandos;
	}

	public void setComandos(List<ComandoVO> comandos) {
		this.comandos = comandos;
	}

	public Boolean getThread() {
		return thread;
	}

	public void setThread(Boolean thread) {
		this.thread = thread;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public List<ScriptVO> getListaScript() {
		return listaScript;
	}

	public void setListaScript(List<ScriptVO> listaScript) {
		this.listaScript = listaScript;
	}

	public Boolean getArquivo() {
		return arquivo;
	}

	public void setArquivo(Boolean arquivo) {
		this.arquivo = arquivo;
	}

	public String getExtensao() {
		return extensao;
	}

	public void setExtensao(String extensao) {
		this.extensao = extensao;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((index == null) ? 0 : index.hashCode());
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
		ScriptVO other = (ScriptVO) obj;
		if (index == null) {
			if (other.index != null)
				return false;
		} else if (!index.equals(other.index))
			return false;
		return true;
	}

	@Override
	public int compareTo(ScriptVO o) {
		if (this.index == null) {
			return -1;
		} else if (o.getIndex() == null) {
			return 1;
		} else if (this.index > o.getIndex()) {
			return -1;
		} else if (this.index < o.getIndex()) {
			return 1;
		} else {
			return 0;
		}
	}
	
}
