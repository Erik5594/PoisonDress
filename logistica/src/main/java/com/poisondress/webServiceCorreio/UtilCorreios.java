package com.poisondress.webServiceCorreio;

import com.poisondress.entidades.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UtilCorreios {

    public static final int POSTADO = 0;
    public static final int FISCALIZACAO = 1;
    public static final int CORREIOS = 2;
    public static final int PENDENTE = 3;
    public static final int ENTREGUE = 4;
    public static final int DEVOLVIDO = 5;
    public static final int ATRASADO = 6;
    public static final int INDEFINIDO = 7;

    public static String USUARIO = "ECT";
    public static String SENHA = "SRO";
    public static String TIPO = "L";
    public static String RESULTADO = "U";
    public static String LINGUA = "101";

    public static void consultarCorreios(ArquivoOberlo dadosOberlo) throws Exception{
        Rastro service = new Rastro();
        Service port = service.getServicePort();
        Sroxml result = port.buscaEventos(USUARIO, SENHA, TIPO, RESULTADO, LINGUA, dadosOberlo.getCodRastreamentoCorreios());

        if(result != null && result.getObjeto() != null && !result.getObjeto().isEmpty()){
            Objeto objeto = result.getObjeto().get(0);
                if(objeto != null && objeto.getEvento() != null && !objeto.getEvento().isEmpty()){
                    Eventos eventos = objeto.getEvento().get(0);
                    String tipo = eventos.getTipo();
                    String status = eventos.getStatus();
                    int etapaAtualCorreios = etapaAtual(tipo, status);
                    int etapaAtualOberlo = etapaAtual(dadosOberlo.getTipo(), dadosOberlo.getStatus());
                    if(etapaAtualCorreios != etapaAtualOberlo){
                        dadosOberlo.setStatus(status);
                        dadosOberlo.setTipo(tipo);
                    }else{
                        dadosOberlo.setStatus(null);
                        dadosOberlo.setTipo(null);
                    }
                }
        }
    }

    public void imprimirArquivos(List<ArquivoOberlo> oberlos){
        List<ArquivoOberlo> novosPostados = new ArrayList<ArquivoOberlo>();
        List<ArquivoOberlo> novosFiscalizacao = new ArrayList<ArquivoOberlo>();
        List<ArquivoOberlo> novosCorreios = new ArrayList<ArquivoOberlo>();
        List<ArquivoOberlo> novosPendentes = new ArrayList<ArquivoOberlo>();
        List<ArquivoOberlo> novosEntregues = new ArrayList<ArquivoOberlo>();
        List<ArquivoOberlo> novosDevolvido = new ArrayList<ArquivoOberlo>();
        List<ArquivoOberlo> novosAtrasado = new ArrayList<ArquivoOberlo>();
        List<ArquivoOberlo> novosIndefinido = new ArrayList<ArquivoOberlo>();

        for(ArquivoOberlo oberlo : oberlos){
            switch (etapaAtual(oberlo.getTipo(), oberlo.getStatus())){
                case POSTADO :
                    novosPostados.add(oberlo);
                    break;
                case FISCALIZACAO :
                    novosFiscalizacao.add(oberlo);
                    break;
                case CORREIOS :
                    novosFiscalizacao.add(oberlo);
                    break;
                case PENDENTE :
                    novosFiscalizacao.add(oberlo);
                    break;
                case ENTREGUE :
                    novosFiscalizacao.add(oberlo);
                    break;
                case DEVOLVIDO :
                    novosFiscalizacao.add(oberlo);
                    break;
                case ATRASADO :
                    novosFiscalizacao.add(oberlo);
                    break;
                case INDEFINIDO :
                    novosFiscalizacao.add(oberlo);
                    break;
                default:
                    novosIndefinido.add(oberlo);
                    break;
            }
        }

        if(novosPostados != null && !novosPostados.isEmpty()){
            criarArquivo(novosPostados, "novosPostados.csv");
        }

        if(novosPendentes != null && !novosPendentes.isEmpty()){
            criarArquivo(novosPendentes, "novosPendentes.csv");
        }

        if(novosCorreios != null && !novosCorreios.isEmpty()){
            criarArquivo(novosCorreios, "novosCorreios.csv");
        }

        if(novosDevolvido != null && !novosDevolvido.isEmpty()){
            criarArquivo(novosDevolvido, "novosDevolvido.csv");
        }

        if(novosEntregues != null && !novosEntregues.isEmpty()){
            criarArquivo(novosEntregues, "novosEntregues.csv");
        }

        if(novosFiscalizacao != null && !novosFiscalizacao.isEmpty()){
            criarArquivo(novosFiscalizacao, "novosFiscalizacao.csv");
        }

        if(novosIndefinido != null && !novosIndefinido.isEmpty()){
            criarArquivo(novosIndefinido, "novosIndefinido.csv");
        }

        if(novosAtrasado != null && !novosAtrasado.isEmpty()){
            criarArquivo(novosAtrasado, "novosAtrasado.csv");
        }
    }

    private static int etapaAtual(String tipo, String status){
        if("BDE".equals(tipo)){
            return validacaoBDE(status);
        }else if("FC".equals(tipo)){
            return validacaoFC(status);
        }else if("LDE".equals(tipo)){
            return validacaoLDE(status);
        }else if ("LDI".equals(tipo)){
            return validacaoLDI(status);
        }else if ("OEC".equals(tipo)){
            return validacaoOEC(status);
        }else if ("PAR".equals(tipo)){
            return validacaoPAR(status);
        }else if ("PO".equals(tipo)){
            return validacaoPO(status);
        }else if ("RO".equals(tipo)){
            return validacaoRO(status);
        }
        return 7;
    }

    private static int validacaoBDE(String status) {
        if (StatusEntregaBDE.ENTREGUE.getStatus().contains(status)) {
            return StatusEntregaBDE.ENTREGUE.getCodigo();
        }
        if (StatusEntregaBDE.PENDENTE.getStatus().contains(status)) {
            return StatusEntregaBDE.PENDENTE.getCodigo();
        }
        if (StatusEntregaBDE.DEVOLVIDO.getStatus().contains(status)) {
            return StatusEntregaBDE.DEVOLVIDO.getCodigo();
        }
        if (StatusEntregaBDE.ATRASADO.getStatus().contains(status)) {
            return StatusEntregaBDE.ATRASADO.getCodigo();
        }
        return 7;
    }

    private static int validacaoFC(String status) {
        if (StatusEntregaFC.PENDENTE.getStatus().contains(status)) {
            return StatusEntregaFC.PENDENTE.getCodigo();
        }
        return 7;
    }

    private static int validacaoLDE(String status) {
        if (StatusEntregaLDE.PENDENTE.getStatus().contains(status)) {
            return StatusEntregaLDE.PENDENTE.getCodigo();
        }
        return 7;
    }

    private static int validacaoLDI(String status) {
        if (StatusEntregaLDI.PENDENTE.getStatus().contains(status)) {
            return StatusEntregaLDI.PENDENTE.getCodigo();
        }
        return 7;
    }

    private static int validacaoOEC(String status) {
        if (StatusEntregaOEC.PENDENTE.getStatus().contains(status)) {
            return StatusEntregaBDE.PENDENTE.getCodigo();
        }
        return 7;
    }

    private static int validacaoPAR(String status) {
        if (StatusEntregaPAR.CORREIOS.getStatus().contains(status)) {
            return StatusEntregaPAR.CORREIOS.getCodigo();
        }
        if (StatusEntregaPAR.FISCALIZACAO.getStatus().contains(status)) {
            return StatusEntregaPAR.FISCALIZACAO.getCodigo();
        }
        if (StatusEntregaPAR.INDEFINIDO.getStatus().contains(status)) {
            return StatusEntregaPAR.INDEFINIDO.getCodigo();
        }
        if (StatusEntregaPAR.DEVOLVIDO.getStatus().contains(status)) {
            return StatusEntregaPAR.DEVOLVIDO.getCodigo();
        }
        return 7;
    }

    private static int validacaoPO(String status) {
        if (StatusEntregaPO.POSTADO.getStatus().contains(status)) {
            return StatusEntregaPO.POSTADO.getCodigo();
        }
        return 7;
    }

    private static int validacaoRO(String status) {
        if (StatusEntregaRO.CORREIOS.getStatus().contains(status)) {
            return StatusEntregaRO.CORREIOS.getCodigo();
        }
        return 7;
    }

    private void criarArquivo(List<ArquivoOberlo> objetos, String nomeArquivoEntrada){

        String nomeArquivo = "c:\\".concat(nomeArquivoEntrada);
        try {
            FileWriter writer = new FileWriter(nomeArquivo);
            for(ArquivoOberlo objeto : objetos){
                writer.append(objeto.getIdAliexpress());
                writer.append(',');
                writer.append(objeto.getCodRastreamentoCorreios());
                writer.append(',');
                writer.append(objeto.getDescricao());
                writer.append('\n');
            }
            writer.flush();
            writer.close();
        }catch (IOException e){
            //TODO
        }

    }
}
