package com.poisondress.controladores;

import com.poisondress.entidades.ArquivoOberlo;
import com.poisondress.entidades.Consts;
import com.poisondress.webServiceCorreio.UtilCorreios;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.*;
import java.util.ArrayList;
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


    private String app;

    @SuppressWarnings("unchecked")
    public void uploadDeArquivo(FileUploadEvent event) {
        try {


            List<ArquivoOberlo> objetosOberlo = getListaDeObjetoDoArquivo(obterBufferReader(event.getFile()), ",", app);

            List<ArquivoOberlo> pedidosEntreques = new ArrayList<ArquivoOberlo>();
            List<ArquivoOberlo> pedidosAtrasados = new ArrayList<ArquivoOberlo>();
            List<ArquivoOberlo> pedidosPendenteRetirada = new ArrayList<ArquivoOberlo>();
            List<String> estenderPrazoProtecao = new ArrayList<String>();
            List<String> disputa = new ArrayList<String>();

            for(ArquivoOberlo dadosOberlo : objetosOberlo) {
                if(!(Consts.JA_ENTREGUES.contains(dadosOberlo.getIdAliexpress())
                        || Consts.JA_DISPUTA_ALIEXPRESS.contains(dadosOberlo.getIdAliexpress()))) {
                    UtilCorreios.consultarCorreios(dadosOberlo);
                    if(dadosOberlo.isAtrasado()){
                        if(!dadosOberlo.isChina()
                                && !Consts.JA_ABERTO_RECLAMACAO.contains(dadosOberlo.getCodRastreamento())) {
                            pedidosAtrasados.add(dadosOberlo);
                        }
                        if (!Consts.JA_TRATADO_ALIEXPRESS.contains(dadosOberlo.getIdAliexpress())) {
                            estenderPrazoProtecao.add(Consts.LINK_PEDIDO_ALIEXPRESS + dadosOberlo.getIdAliexpress());
                        }
                    }else if(UtilCorreios.isEtapaAtual(dadosOberlo.getTipoCorreios(), dadosOberlo.getStatusCorreios()) == entregue){
                        pedidosEntreques.add(dadosOberlo);
                    }else if(UtilCorreios.isEtapaAtual(dadosOberlo.getTipoCorreios(), dadosOberlo.getStatusCorreios()) == devolvido){
                        if(!Consts.JA_DISPUTA_ALIEXPRESS.contains(dadosOberlo.getIdAliexpress())){
                            disputa.add(Consts.LINK_PEDIDO_ALIEXPRESS + dadosOberlo.getIdAliexpress());
                        }
                    }else if(UtilCorreios.isEtapaAtual(dadosOberlo.getTipoCorreios(), dadosOberlo.getStatusCorreios()) == pendenteRetirada){
                        if(!Consts.JA_AGUARDA_RETIRADA.contains(dadosOberlo.getIdAliexpress())){
                            pedidosPendenteRetirada.add(dadosOberlo);
                        }
                    }else {
                        if(!Consts.JA_DISPUTA_ALIEXPRESS.contains(dadosOberlo.getIdAliexpress()) && dadosOberlo.getStatusCorreios() == null){
                            disputa.add(Consts.LINK_PEDIDO_ALIEXPRESS + dadosOberlo.getIdAliexpress());
                        }
                    }
                }
            }

            if(pedidosEntreques != null && !pedidosEntreques.isEmpty()) {
                UtilCorreios.criarArquivo(pedidosEntreques, "entregues.csv");
            }
            if(pedidosAtrasados != null && !pedidosAtrasados.isEmpty()) {
                UtilCorreios.criarArquivo(pedidosAtrasados, "atrasados.csv");
            }
            if(estenderPrazoProtecao != null && !estenderPrazoProtecao.isEmpty()) {
                UtilCorreios.criarArquivoComLinks(estenderPrazoProtecao, "atrasados-china.txt");
            }
            if(pedidosPendenteRetirada != null && !pedidosPendenteRetirada.isEmpty()) {
                UtilCorreios.criarArquivo(pedidosPendenteRetirada, "pendente-entrega.csv");
            }
            if(disputa != null && !disputa.isEmpty()) {
                UtilCorreios.criarArquivoComLinks(disputa, "disputa.txt");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private List<ArquivoOberlo> getListaDeObjetoDoArquivo(BufferedReader linhasArquivo, String separador, String aplicativo) throws IOException{
        List<ArquivoOberlo> objsOberlo = new ArrayList<ArquivoOberlo>();
        String linha = linhasArquivo.readLine();
        if("0".equals(aplicativo)) {
            linha = linhasArquivo.readLine();
            while (linha != null) {
                ArquivoOberlo objOberlo = (ArquivoOberlo) obterObjetoOberlo(linha.split(separador));
                if (objOberlo != null) {
                    objsOberlo.add(objOberlo);
                }
                linha = linhasArquivo.readLine();
            }
        }else{
            while (linha != null) {
                ArquivoOberlo objOberlo = (ArquivoOberlo) obterObjetoAplicacao(linha.split(separador));
                if (objOberlo != null) {
                    objsOberlo.add(objOberlo);
                }
                linha = linhasArquivo.readLine();
            }
        }
        linhasArquivo.close();
        return objsOberlo;
    }


    private Object obterObjetoAplicacao(String[] vetorObjeto) {
        ArquivoOberlo oberlo = new ArquivoOberlo();
        oberlo.setIdShopify(vetorObjeto[0]);
        oberlo.setIdAliexpress(vetorObjeto[1]);
        oberlo.setCodRastreamento(vetorObjeto[2]);
        oberlo.setTipoCorreios(vetorObjeto[3]);
        oberlo.setStatusCorreios(vetorObjeto[4]);
        oberlo.setEtapaAtual(vetorObjeto[5]);
        return oberlo;
    }

    private Object obterObjetoOberlo(String[] vetorObjeto) {
        ArquivoOberlo oberlo = new ArquivoOberlo();
        oberlo.setIdShopify(vetorObjeto[0]);
        oberlo.setCodRastreamento(vetorObjeto[11]);
        oberlo.setIdAliexpress(vetorObjeto[12]);
        return oberlo;
    }


    private BufferedReader obterBufferReader(UploadedFile arquivo) throws IOException{
        InputStream inputStream = arquivo.getInputstream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        return new BufferedReader(inputStreamReader);
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }
}
