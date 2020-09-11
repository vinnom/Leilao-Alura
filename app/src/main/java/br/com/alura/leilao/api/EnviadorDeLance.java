package br.com.alura.leilao.api;

import android.content.Context;

import br.com.alura.leilao.api.retrofit.client.LeilaoWebClient;
import br.com.alura.leilao.api.retrofit.client.RespostaListener;
import br.com.alura.leilao.exception.QuantidadeMaximaDeLancesException;
import br.com.alura.leilao.exception.UsuarioDeuLancesSeguidosException;
import br.com.alura.leilao.exception.ValorMenorQueOAnteriorException;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;

import static br.com.alura.leilao.ui.dialog.AvisoDialogManager.mostraAvisoLanceMenorQueUltimoLance;
import static br.com.alura.leilao.ui.dialog.AvisoDialogManager.mostraAvisoLanceSeguidoDoMesmoUsuario;
import static br.com.alura.leilao.ui.dialog.AvisoDialogManager.mostraAvisoUsuarioJaDeuCincoLances;
import static br.com.alura.leilao.ui.dialog.AvisoDialogManager.mostraToastFalhaNoEnvio;

public class EnviadorDeLance {

   private final LeilaoWebClient client;
   private final LanceProcessadoListener listener;
   private final Context context;

   public EnviadorDeLance(LeilaoWebClient client,
                          LanceProcessadoListener listener,
                          Context context) {
      this.client = client;
      this.listener = listener;
      this.context = context;
   }

   public void envia(final Leilao leilao, Lance lance) {
      try {
         leilao.propoe(lance);
         client.propoe(lance, leilao.getId(), new RespostaListener<Void>() {
            @Override
            public void sucesso(Void resposta) {
               listener.processado(leilao);
            }

            @Override
            public void falha(String mensagem) {
               mostraToastFalhaNoEnvio(context);
            }
         });
      } catch(ValorMenorQueOAnteriorException exception) {
         mostraAvisoLanceMenorQueUltimoLance(context);
      } catch(UsuarioDeuLancesSeguidosException exception) {
         mostraAvisoLanceSeguidoDoMesmoUsuario(context);
      } catch(QuantidadeMaximaDeLancesException exception) {
         mostraAvisoUsuarioJaDeuCincoLances(context);
      }
   }

   public interface LanceProcessadoListener {
      void processado(Leilao leilao);
   }

}
