package com.poisondress.controladores;

import com.poisondress.entidades.ArquivoOberlo;
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
    private static String LINK_PEDIDO_ALIEXPRESS = "https://trade.aliexpress.com/order_detail.htm?spm=a2g0s.9042311.0.0.Xd5mg8&orderId=";
    private static String JA_TRATADO_ALIEXPRESS = "87021575702768*87255763632768*87671630732768*87494521662768*87670677602768*87531216552768*87665275362768*87530939322768*87488561182768*87590062522768*8758864232768*87590301012768*87532131542768*87665238832768*87590422642768*87665438112768*87536817882768*87537775672768*87595462322768*87494720932768*8753745455768*87595267052768*87537377232768*87538290532768*87537814022768*87494048642768*87671757042768*87494685132768*87671319772768*87595902862768*87494408572768*8596300252768*87595705432768*87672154962768*87672314792768*87538530382768*87495125342768*87596023252768*87931291082768*88088043082768*88374257532768*8846238602768*88608449242768*88608449242768*88608329422768*";

    private String app;

    @SuppressWarnings("unchecked")
    public void uploadDeArquivo(FileUploadEvent event) {
        try {


            List<ArquivoOberlo> objetosOberlo = getListaDeObjetoDoArquivo(obterBufferReader(event.getFile()), ",", app);

            List<ArquivoOberlo> pedidosAnalise = new ArrayList<ArquivoOberlo>();
            List<ArquivoOberlo> pedidosDevolvido = new ArrayList<ArquivoOberlo>();
            List<ArquivoOberlo> pedidosEntreques = new ArrayList<ArquivoOberlo>();
            List<ArquivoOberlo> pedidosAtrasados = new ArrayList<ArquivoOberlo>();
            List<ArquivoOberlo> pedidosAtrasadosChina = new ArrayList<ArquivoOberlo>();
            List<ArquivoOberlo> pedidosPendenteRetirada = new ArrayList<ArquivoOberlo>();
            List<String> estenderPrazoProtecao = new ArrayList<String>();

            for(ArquivoOberlo dadosOberlo : objetosOberlo) {
                UtilCorreios.consultarCorreios(dadosOberlo);
                if(dadosOberlo.isAtrasado()){
                    pedidosAnalise.add(dadosOberlo);
                    if(dadosOberlo.isChina()) {
                        if(!JA_TRATADO_ALIEXPRESS.contains(dadosOberlo.getIdAliexpress())){
                            estenderPrazoProtecao.add(LINK_PEDIDO_ALIEXPRESS + dadosOberlo.getIdAliexpress());
                        }
                    }else{
                        pedidosAtrasados.add(dadosOberlo);
                    }
                }else if(UtilCorreios.isEtapaAtual(dadosOberlo.getTipoCorreios(), dadosOberlo.getStatusCorreios()) == entregue){
                    pedidosEntreques.add(dadosOberlo);
                }else if(UtilCorreios.isEtapaAtual(dadosOberlo.getTipoCorreios(), dadosOberlo.getStatusCorreios()) == devolvido){
                    pedidosDevolvido.add(dadosOberlo);
                }else if(UtilCorreios.isEtapaAtual(dadosOberlo.getTipoCorreios(), dadosOberlo.getStatusCorreios()) == pendenteRetirada){
                    pedidosPendenteRetirada.add(dadosOberlo);
                    pedidosAnalise.add(dadosOberlo);
                }else{
                    pedidosAnalise.add(dadosOberlo);
                }
            }

            if(pedidosAnalise != null && !pedidosAnalise.isEmpty()) {
                UtilCorreios.criarArquivo(pedidosAnalise, "arq-completo.csv");
            }
            if(pedidosDevolvido != null && !pedidosDevolvido.isEmpty()) {
                UtilCorreios.criarArquivo(pedidosDevolvido, "devolvidos.csv");
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
