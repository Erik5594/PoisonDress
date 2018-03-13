package com.poisondress.webServiceCorreio;

import com.poisondress.entidades.*;

import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
    public static final int AGUARDANDO_ENTREGA = 8;

    public static String USUARIO = "ECT";
    public static String SENHA = "SRO";
    public static String TIPO = "L";
    public static String RESULTADO = "U";
    public static String LINGUA = "101";

    private static SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

    public static void consultarCorreios(ArquivoOberlo dadosOberlo) throws Exception{
        Rastro service = new Rastro();
        Service port = service.getServicePort();
        Sroxml result = port.buscaEventos(USUARIO, SENHA, TIPO, RESULTADO, LINGUA, dadosOberlo.getCodRastreamento());
        String atrasado = "ATRASADO";

        if(result != null && result.getObjeto() != null && !result.getObjeto().isEmpty()){
            Objeto objeto = result.getObjeto().get(0);
                if(objeto != null && objeto.getEvento() != null && !objeto.getEvento().isEmpty()){
                    Eventos eventos = objeto.getEvento().get(0);
                    dadosOberlo.setTipoCorreios(eventos.getTipo());
                    dadosOberlo.setStatusCorreios(eventos.getStatus());
                    dadosOberlo.setDataAlteracaoCorreios(getData(eventos.getData()));
                    definirEtapaAtual(dadosOberlo, atrasado, eventos);
                }
        }
    }

    private static void definirEtapaAtual(ArquivoOberlo dadosOberlo, String atrasado, Eventos eventos) {
        int etapaAtual = isEtapaAtual(eventos.getTipo(), eventos.getStatus());

        if((etapaAtual != DEVOLVIDO && etapaAtual != ENTREGUE) || etapaAtual == ATRASADO) {
            if(etapaAtual == FISCALIZACAO && isAtrasado(dadosOberlo.getDataAlteracaoCorreios(), 80)) {
                etapaAtual = ATRASADO;
                dadosOberlo.setAtrasado(true);
                dadosOberlo.setChina(false);
                atrasado = "ATRASADO-FISCALIZAÇÃO";
            } else if(isAtrasado(dadosOberlo.getDataAlteracaoCorreios(), 60)){
                if("81010971".equals(eventos.getCodigo())) {
                    etapaAtual = ATRASADO;
                    dadosOberlo.setAtrasado(true);
                    dadosOberlo.setChina(false);
                    atrasado = "ATRASADO-CORREIOS";
                }else if("00156000".equals(eventos.getCodigo())){
                    etapaAtual = ATRASADO;
                    dadosOberlo.setAtrasado(true);
                    dadosOberlo.setChina(true);
                    atrasado = "ATRASADO-ABRIR-DISPUTA-ALIEXPRESS";
                }
            }
        }

        switch (etapaAtual){
            case POSTADO:
                dadosOberlo.setEtapaAtual("POSTADO");
                break;
            case FISCALIZACAO:
                dadosOberlo.setEtapaAtual("FISCALIZACAO");
                break;
            case CORREIOS:
                dadosOberlo.setEtapaAtual("CORREIOS");
                break;
            case PENDENTE:
                dadosOberlo.setEtapaAtual("PENDENTE");
                break;
            case ENTREGUE:
                dadosOberlo.setEtapaAtual("ENTREGUE");
                break;
            case DEVOLVIDO:
                dadosOberlo.setEtapaAtual("DEVOLVIDO");
                break;
            case ATRASADO:
                dadosOberlo.setEtapaAtual(atrasado);
                break;
            case INDEFINIDO:
                dadosOberlo.setEtapaAtual("INDEFINIDO");
                break;
            case AGUARDANDO_ENTREGA:
                dadosOberlo.setEtapaAtual("AGUARDANDO RETIRADA");
                break;
            default:
                dadosOberlo.setEtapaAtual("INDEFINIDO");
                break;
        }
    }

    public static boolean isAtrasado(Calendar dataUltimaModificacao, int dias) {
        Calendar hoje = Calendar.getInstance();
        Calendar prazoFinal = dataUltimaModificacao;
        prazoFinal.add(Calendar.DAY_OF_YEAR, dias);
        return prazoFinal.compareTo(hoje) <= 0;
    }

    public static int isEtapaAtual(String tipo, String status){
        if("BDE".equals(tipo) || "BDI".equals(tipo) || "BDR".equals(tipo)){
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
        if (StatusEntregaLDI.AGUARDANDO_RETIRADA.getStatus().contains(status)) {
            return StatusEntregaLDI.AGUARDANDO_RETIRADA.getCodigo();
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

    public static void criarArquivo(List<ArquivoOberlo> objetos, String nomeArquivoEntrada){

        String nomeArquivo = "c:\\".concat(nomeArquivoEntrada);
        try {
            FileWriter writer = new FileWriter(nomeArquivo);
            for(ArquivoOberlo objeto : objetos){
                writer.append(objeto.getIdShopify());
                writer.append(',');
                writer.append(objeto.getIdAliexpress());
                writer.append(',');
                writer.append(objeto.getCodRastreamento());
                writer.append(',');
                writer.append(objeto.getTipoCorreios());
                writer.append(',');
                writer.append(objeto.getStatusCorreios());
                writer.append(',');
                writer.append(objeto.getEtapaAtual());
                writer.append('\n');
            }
            writer.flush();
            writer.close();
        }catch (IOException e){
            //TODO
        }
    }

    private static Calendar getData(String data) throws ParseException {
        if(data != null && !"".equals(data)){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(format.parse(data));
            return calendar;
        }
        return null;
    }
}
