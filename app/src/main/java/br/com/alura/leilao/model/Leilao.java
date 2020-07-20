package br.com.alura.leilao.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.alura.leilao.exception.QuantidadeMaximaDeLancesException;
import br.com.alura.leilao.exception.UsuarioDeuLancesSeguidosException;
import br.com.alura.leilao.exception.ValorMenorQueOAnteriorException;

public class Leilao implements Serializable {

	private final String descricao;
	private final List<Lance> lances;

	public Leilao(String descricao) {
		this.descricao = descricao;
		this.lances = new ArrayList<>();
		this.lances.add(new Lance(new Usuario(""), 0.0));
	}

	public void propoe(Lance lance) {
		if(lances.get(0).getValor() == 0.0) {
			lances.set(0, lance);
		} else {
			valida(lance);
			lances.add(lance);
			Collections.sort(lances);
		}
	}

	private void valida(Lance lance) {
		validaLanceSemRepeticaoDoUsuario(lance);
		validaQuantidadeMaximaDeLancesDeUmMesmoUsuario(lance);
		validaValorMaiorQueAnterior(lance);
	}

	private void validaLanceSemRepeticaoDoUsuario(Lance lance) {
		if(lance.getUsuario().equals(lances.get(0).getUsuario())) {
			throw new UsuarioDeuLancesSeguidosException();
		}
	}

	private void validaQuantidadeMaximaDeLancesDeUmMesmoUsuario(Lance lance) {
		int contador = 0;
		for(Lance l : lances) {
			if(lance.getUsuario().equals(l.getUsuario())) {
				contador++;
			}
		}
		if(contador == 5) {
			throw new QuantidadeMaximaDeLancesException();
		}
	}

	private void validaValorMaiorQueAnterior(Lance lance) {
		if(lance.getValor() < lances.get(0).getValor())
			throw new ValorMenorQueOAnteriorException();
	}

	public String getDescricao() {
		return descricao;
	}

	public int getQuantidadeLances() {
		return lances.size();
	}

	public List<Lance> getLances() {
		return lances;
	}

	public double getMaiorLance() {
		if(lances.isEmpty()) {
			return 0;
		}
		return lances.get(0).getValor();
	}

	public double getMenorLance() {
		if(lances.isEmpty()) {
			return 0;
		}
		return lances.get(lances.size() - 1).getValor();
	}

	public List<Lance> getTresMaioresLances() {
		if(lances.isEmpty()) {
			return lances;
		}
		if(lances.size() < 3) {
			return lances.subList(0, lances.size());
		}
		return lances.subList(0, 3);
	}
}
