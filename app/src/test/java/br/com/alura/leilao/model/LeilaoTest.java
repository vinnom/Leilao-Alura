package br.com.alura.leilao.model;

import org.hamcrest.Matchers;
import org.junit.Test;

import br.com.alura.leilao.exception.QuantidadeMaximaDeLancesException;
import br.com.alura.leilao.exception.UsuarioDeuLancesSeguidosException;
import br.com.alura.leilao.exception.ValorMenorQueOAnteriorException;

import static org.hamcrest.Matchers.both;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class LeilaoTest{

   private static final Usuario USUARIO = new Usuario("usuario");
   private static final double DELTA = 0.001;
   private final Leilao LEILAO = new Leilao("teste");

   @Test
   public void deve_ConterDescricao(){
      String descricaoReal = LEILAO.getDescricao();

      assertThat(descricaoReal, is(equalTo("teste")));
   }

   @Test
   public void deve_ConterLanceProposto(){
      LEILAO.propoe(new Lance(USUARIO, 100.0));

      assertThat(LEILAO.getLances(), both(Matchers.<Lance>hasSize(1)).and(contains(new Lance(USUARIO, 100.0))));
   }

   @Test
   public void deve_DevolverMaiorLance_QuandoInseridoUmValor(){
      LEILAO.propoe(new Lance(USUARIO, 100.0));

      Lance maiorLanceReal = LEILAO.getMaiorLance();

      assertThat(maiorLanceReal, is(equalTo(new Lance(USUARIO, 100.0))));
   }

   @Test
   public void deve_DevolverMaiorLance_QuandoInseridoMaisQueUmValor(){
      LEILAO.propoe(new Lance(USUARIO, 100.0));
      LEILAO.propoe(new Lance(new Usuario("u"), 200.0));

      Lance maiorLanceReal = LEILAO.getMaiorLance();

      assertThat(maiorLanceReal, is(equalTo(new Lance(new Usuario("u"), 200.0))));
   }

   @Test
   public void deve_DevolverMenorLance_QuandoInseridoUmValor(){
      LEILAO.propoe(new Lance(USUARIO, 100.0));

      Lance menorLanceReal = LEILAO.getMenorLance();

      assertThat(menorLanceReal, is(equalTo(new Lance(USUARIO, 100.0))));
   }

   @Test
   public void deve_DevolverMenorLance_QuandoInseridoMaisQueUmValor(){
      LEILAO.propoe(new Lance(USUARIO, 100.0));
      LEILAO.propoe(new Lance(new Usuario("u"), 200.0));

      Lance menorLanceReal = LEILAO.getMenorLance();

      assertThat(menorLanceReal, is(equalTo(new Lance(USUARIO, 100.0))));
   }

   @Test
   public void deve_DevolverTresMaioresLances_QuandoInseridoMenosQueTresLances(){
      LEILAO.propoe(new Lance(USUARIO, 100.0));
      LEILAO.propoe(new Lance(new Usuario("u"), 200.0));

      assertThat(LEILAO.getTresMaioresLances(), both(Matchers.<Lance>hasSize(2)).and(
         contains(new Lance(new Usuario("u"), 200.0), new Lance(USUARIO, 100.0))));
   }

   @Test
   public void deve_DevolverTresMaioresLances_QuandoInseridoTresLances(){
      LEILAO.propoe(new Lance(USUARIO, 100.0));
      LEILAO.propoe(new Lance(new Usuario("u"), 200.0));
      LEILAO.propoe(new Lance(USUARIO, 300.0));

      assertThat(LEILAO.getTresMaioresLances(), both(
         Matchers.<Lance>hasSize(3)).and(
         contains(
            new Lance(USUARIO, 300.0),
            new Lance(new Usuario("u"), 200.0),
            new Lance(USUARIO, 100.0)
         )
      ));
   }

   @Test
   public void deve_DevolverTresMaioresLances_QuandoInseridoMaisQueTresLances(){
      final Usuario U = new Usuario("u");
      LEILAO.propoe(new Lance(USUARIO, 100.0));
      LEILAO.propoe(new Lance(U, 200.0));
      LEILAO.propoe(new Lance(USUARIO, 300.0));
      LEILAO.propoe(new Lance(U, 400.0));

      assertThat(LEILAO.getTresMaioresLances(), both(
         Matchers.<Lance>hasSize(3)).and(
         contains(
            new Lance(U, 400.0),
            new Lance(USUARIO, 300.0),
            new Lance(U, 200.0)
         )
      ));
   }

   @Test
   public void deve_ConterLanceInicializadoComZero(){
      assertThat(LEILAO.getLances(), both(
         Matchers.<Lance>hasSize(1)).and(
         contains(
            new Lance(new Usuario(""), 0.0)
         )
      ));
   }

   @Test(expected = ValorMenorQueOAnteriorException.class)
   public void deve_AdicionarLancesApenasEmOrdemCrescente(){
      LEILAO.propoe(new Lance(USUARIO, 400.0));
      LEILAO.propoe(new Lance(new Usuario("u"), 300.0));
   }

   @Test(expected = UsuarioDeuLancesSeguidosException.class)
   public void deve_AdicionarLancesApenas_QuandoUsuarioNaoRepeteLanceEmSeguida(){
      LEILAO.propoe(new Lance(USUARIO, 300.0));
      LEILAO.propoe(new Lance(USUARIO, 400.0));
   }

   @Test(expected = QuantidadeMaximaDeLancesException.class)
   public void deve_AdicionarNoMaximoCincoLancesDeUmMesmoUsuario(){
      final Usuario U = new Usuario("u");
      LEILAO.propoe(new Lance(USUARIO, 100.0));
      LEILAO.propoe(new Lance(U, 200.0));
      LEILAO.propoe(new Lance(USUARIO, 300.0));
      LEILAO.propoe(new Lance(U, 400.0));
      LEILAO.propoe(new Lance(USUARIO, 500.0));
      LEILAO.propoe(new Lance(U, 600.0));
      LEILAO.propoe(new Lance(USUARIO, 700.0));
      LEILAO.propoe(new Lance(U, 800.0));
      LEILAO.propoe(new Lance(USUARIO, 900.0));
      LEILAO.propoe(new Lance(U, 1000.0));
      LEILAO.propoe(new Lance(USUARIO, 1500.0));
   }
}