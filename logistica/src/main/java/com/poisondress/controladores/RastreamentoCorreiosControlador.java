package com.poisondress.controladores;

import com.poisondress.entidades.ArquivoOberlo;
import com.poisondress.entidades.Consts;
import com.poisondress.webServiceCorreio.UtilCorreios;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@ManagedBean
@ViewScoped
public class RastreamentoCorreiosControlador {

    private int postado = 0;
    private int fiscalizacao = 1;
    private int correios = 2;
    private int pendente = 3;
    private int entregue = 4;
    private int devolvido = 5;
    private int atrasado = 6;
    private int indefinido = 7;
    private int pendenteRetirada = 8;

    @SuppressWarnings("unchecked")
    public void uploadDeArquivo(FileUploadEvent event) {
        try {


            List<ArquivoOberlo> objetosOberlo = getListaDeObjetoDoArquivo(obterBufferReader(event.getFile()), ",");

            List<ArquivoOberlo> pedidosAtrasados = new ArrayList<ArquivoOberlo>();
            List<ArquivoOberlo> pedidosPendenteRetirada = new ArrayList<ArquivoOberlo>();
            List<String> pedidosEntreques = new ArrayList<String>();
            List<String> estenderPrazoProtecao = new ArrayList<String>();
            List<String> disputa = new ArrayList<String>();

            for(ArquivoOberlo dadosOberlo : objetosOberlo) {
                if(!(Consts.JA_ENTREGUES.contains(dadosOberlo.getIdAliexpress())
                        || Consts.JA_DISPUTA_ALIEXPRESS.contains(dadosOberlo.getIdAliexpress()))) {
                    UtilCorreios.consultarCorreios(dadosOberlo);
                    if(!Consts.JA_TRATADO_ALIEXPRESS.contains(dadosOberlo.getIdAliexpress())
                            && UtilCorreios.isAtrasado(dadosOberlo.getDataCriacaoPedido(), 90)
                            && !estenderPrazoProtecao.contains(Consts.LINK_PEDIDO_ALIEXPRESS + dadosOberlo.getIdAliexpress())){
                        estenderPrazoProtecao.add(Consts.LINK_PEDIDO_ALIEXPRESS + dadosOberlo.getIdAliexpress());
                    }
                    if(Consts.JA_TRATADO_ALIEXPRESS.contains(dadosOberlo.getIdAliexpress())
                            && UtilCorreios.isAtrasado(dadosOberlo.getDataCriacaoPedido(), 210)
                            && !estenderPrazoProtecao.contains(Consts.LINK_PEDIDO_ALIEXPRESS + dadosOberlo.getIdAliexpress() + " 3º")){
                        estenderPrazoProtecao.add(Consts.LINK_PEDIDO_ALIEXPRESS + dadosOberlo.getIdAliexpress() + " 3°");
                    }
                    if(Consts.JA_TRATADO_ALIEXPRESS.contains(dadosOberlo.getIdAliexpress())
                            && UtilCorreios.isAtrasado(dadosOberlo.getDataCriacaoPedido(), 150)
                            && !estenderPrazoProtecao.contains(Consts.LINK_PEDIDO_ALIEXPRESS + dadosOberlo.getIdAliexpress() + " 2º")){
                        estenderPrazoProtecao.add(Consts.LINK_PEDIDO_ALIEXPRESS + dadosOberlo.getIdAliexpress() + " 2º");
                    }
                    if(dadosOberlo.isAtrasado()){
                        if(!dadosOberlo.isChina()
                                && !Consts.JA_ABERTO_RECLAMACAO.contains(dadosOberlo.getCodRastreamento())
                                && !pedidosAtrasados.contains(dadosOberlo)) {
                            pedidosAtrasados.add(dadosOberlo);
                        }
                        if (!Consts.JA_TRATADO_ALIEXPRESS.contains(dadosOberlo.getIdAliexpress())
                                && !estenderPrazoProtecao.contains(Consts.LINK_PEDIDO_ALIEXPRESS + dadosOberlo.getIdAliexpress())) {
                            estenderPrazoProtecao.add(Consts.LINK_PEDIDO_ALIEXPRESS + dadosOberlo.getIdAliexpress());
                        }
                    }else if(UtilCorreios.isEtapaAtual(dadosOberlo.getTipoCorreios(), dadosOberlo.getStatusCorreios()) == entregue
                            && !pedidosEntreques.contains(dadosOberlo.getIdAliexpress())){
                        pedidosEntreques.add(dadosOberlo.getIdAliexpress());
                    }else if(UtilCorreios.isEtapaAtual(dadosOberlo.getTipoCorreios(), dadosOberlo.getStatusCorreios()) == devolvido){
                        if(!Consts.JA_DISPUTA_ALIEXPRESS.contains(dadosOberlo.getIdAliexpress())
                                && !disputa.contains(Consts.LINK_PEDIDO_ALIEXPRESS + dadosOberlo.getIdAliexpress())){
                            disputa.add(Consts.LINK_PEDIDO_ALIEXPRESS + dadosOberlo.getIdAliexpress());
                        }
                    }else if(UtilCorreios.isEtapaAtual(dadosOberlo.getTipoCorreios(), dadosOberlo.getStatusCorreios()) == pendenteRetirada){
                        if(!Consts.JA_AGUARDA_RETIRADA.contains(dadosOberlo.getIdAliexpress())
                                && !pedidosPendenteRetirada.contains(dadosOberlo)){
                            pedidosPendenteRetirada.add(dadosOberlo);
                        }
                    }else {
                        if(!Consts.JA_DISPUTA_ALIEXPRESS.contains(dadosOberlo.getIdAliexpress()) && dadosOberlo.getStatusCorreios() == null
                                && !disputa.contains(Consts.LINK_PEDIDO_ALIEXPRESS + dadosOberlo.getIdAliexpress())){
                            disputa.add(Consts.LINK_PEDIDO_ALIEXPRESS + dadosOberlo.getIdAliexpress());
                        }
                    }
                }
            }

            if(pedidosEntreques != null && !pedidosEntreques.isEmpty()) {
                UtilCorreios.criarArquivoComLinks(pedidosEntreques, "pedidos-entregues.txt");
            }
            if(pedidosAtrasados != null && !pedidosAtrasados.isEmpty()) {
                UtilCorreios.criarArquivo(pedidosAtrasados, "abrir-reclamacao-correios.csv");
            }
            if(estenderPrazoProtecao != null && !estenderPrazoProtecao.isEmpty()) {
                UtilCorreios.criarArquivoComLinks(estenderPrazoProtecao, "estender-prazo-aliexpress.txt");
            }
            if(pedidosPendenteRetirada != null && !pedidosPendenteRetirada.isEmpty()) {
                UtilCorreios.criarArquivo(pedidosPendenteRetirada, "pendente-retirada-correios.csv");
            }
            if(disputa != null && !disputa.isEmpty()) {
                UtilCorreios.criarArquivoComLinks(disputa, "abrir-disputa-aliexpress.txt");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private List<ArquivoOberlo> getListaDeObjetoDoArquivo(BufferedReader linhasArquivo, String separador) throws IOException{
        List<ArquivoOberlo> objsOberlo = new ArrayList<ArquivoOberlo>();
        String linha = linhasArquivo.readLine();
        linha = linhasArquivo.readLine();
        while (linha != null) {
            ArquivoOberlo objOberlo = (ArquivoOberlo) obterObjetoOberlo(linha.split(separador));
            if (objOberlo != null) {
                objsOberlo.add(objOberlo);
            }
            linha = linhasArquivo.readLine();
        }
        linhasArquivo.close();
        return objsOberlo;
    }


    private Object obterObjetoOberlo(String[] vetorObjeto) {
        ArquivoOberlo oberlo = new ArquivoOberlo();
        oberlo.setIdShopify(vetorObjeto[0]);
        oberlo.setDataCriacaoPedido(getDataCalendar(vetorObjeto[1]));
        oberlo.setCodRastreamento(vetorObjeto[11]);
        oberlo.setIdAliexpress(vetorObjeto[12]);
        return oberlo;
    }

    private Calendar getDataCalendar(String dataCriacao) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            calendar.setTime(sdf.parse(dataCriacao));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }


    private BufferedReader obterBufferReader(UploadedFile arquivo) throws IOException{
        InputStream inputStream = arquivo.getInputstream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        return new BufferedReader(inputStreamReader);
    }

}
