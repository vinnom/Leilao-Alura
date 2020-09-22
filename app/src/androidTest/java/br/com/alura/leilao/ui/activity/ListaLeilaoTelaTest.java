package br.com.alura.leilao.ui.activity;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import br.com.alura.leilao.api.retrofit.client.WebClienteDeTeste;
import br.com.alura.leilao.model.Leilao;

import static androidx.test.core.app.ActivityScenario.launch;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class ListaLeilaoTelaTest{

   private static final String FALHA_LIMPEZA_BD_API_WEB = "Não foi possível resetar o banco de dados";
   private static final String FALHA_AO_SALVAR_LEILAO = "Não foi possível salvar o leilão";
   private final WebClienteDeTeste cliente = new WebClienteDeTeste();

   @Before
   public void setup() throws IOException{
      resetaBD();
   }

   @Test
   public void deve_CarregarView_QuandoBuscaNaAPIWeb() throws IOException{
      tentaSalvarLeilaoNaApiWeb(new Leilao("A"));

      launch(ListaLeilaoActivity.class);
      onView(withText("A")).check(matches(isDisplayed()));
   }

   @Test
   public void deve_CarregarDuasViews_QuandoBuscaNaAPIWeb() throws IOException{
      tentaSalvarLeilaoNaApiWeb(new Leilao("A"), new Leilao("B"));

      launch(ListaLeilaoActivity.class);
      onView(withText("A")).check(matches(isDisplayed()));
      onView(withText("B")).check(matches(isDisplayed()));
   }

   @After
   public void tearDown() throws IOException{
      resetaBD();
   }

   private void resetaBD() throws IOException{
      boolean resetado = cliente.reseta();
      if(!resetado){
         Assert.fail(FALHA_LIMPEZA_BD_API_WEB);
      }
   }

   private void tentaSalvarLeilaoNaApiWeb(Leilao... leiloes) throws IOException{
      for(Leilao l : leiloes){
         Leilao leilao = cliente.salva(l);
         if(leilao == null){
            Assert.fail(FALHA_AO_SALVAR_LEILAO);
         }
      }
   }
}