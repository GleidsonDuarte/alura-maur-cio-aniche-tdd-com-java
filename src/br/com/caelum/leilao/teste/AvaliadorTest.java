package br.com.caelum.leilao.teste;

import static org.junit.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.leilao.builder.CriadorDeLeilao;
import br.com.caelum.leilao.dominio.Lance;
import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.dominio.Usuario;
import br.com.caelum.leilao.servico.Avaliador;

public class AvaliadorTest {

	private Avaliador leiloeiro;
	private Usuario joao;
	private Usuario jose;
	private Usuario maria;

	@Before
	public void criaAvaliador()	{
		this.leiloeiro = new Avaliador();
		this.joao = new Usuario("João");
		this.jose = new Usuario("José");
		this.maria = new Usuario("Maria");
	}
	
	@After
	public void finaliza() {
	  System.out.println("fim");
	}
	
	@Test(expected = RuntimeException.class)
	public void naoDeveAvaliarLeiloesSemNenhumLanceDado() {
		Leilao leilao = new CriadorDeLeilao().para("Playstation 3 Novo").constroi();

		leiloeiro.avalia(leilao);
	}
	
	@Test
	public void deveEntenderLancesEmOrdemCrescente() {
		Leilao leilao = new Leilao("Playstation 3 Novo");
		leilao.propoe(new Lance(this.joao, 250));
		leilao.propoe(new Lance(this.jose, 300));
		leilao.propoe(new Lance(this.maria, 400));

		this.leiloeiro.avalia(leilao);

		assertThat(leiloeiro.getMenorLance(), equalTo(250.0));
		assertThat(leiloeiro.getMaiorLance(), equalTo(400.0));
	}

	@Test
	public void deveEntenderLeilaoComApenasUmLance() {
		Leilao leilao = new Leilao("Playstation 3 Novo");

		leilao.propoe(new Lance(this.joao, 1000));

		this.leiloeiro.avalia(leilao);

		assertEquals(1000, this.leiloeiro.getMenorLance(), 0.00001);
		assertEquals(1000, this.leiloeiro.getMaiorLance(), 0.00001);
	}

	@Test
	public void deveEncontrarOsTresMaioresLances() {
		Leilao leilao = new CriadorDeLeilao().para("Playstation 3 Novo")
				.lance(joao, 100.0)
				.lance(maria, 200.0)
				.lance(joao, 300.0)
				.lance(maria, 400.0)
				.constroi();
		
		this.leiloeiro.avalia(leilao);

		List<Lance> maiores = this.leiloeiro.getTresMaiores();
		assertEquals(3, maiores.size());

		assertThat(maiores, hasItems(
			new Lance(maria, 400),
			new Lance(joao, 300),
			new Lance(maria, 200)
		));
	}

}
