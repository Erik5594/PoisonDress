package com.poisondress.controladores;

import com.poisondress.entidades.ArquivoOberlo;
import com.poisondress.entidades.EnderecoPedido;
import com.poisondress.entidades.PedidoCliente;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name = "reclamacao")
@ViewScoped
public class MontaDadosReclamacaoCorreiosControlador {

    private String tipo;

    private List<ArquivoOberlo> dadosPedido = new ArrayList<ArquivoOberlo>();
    private List<ArquivoOberlo> atrasados = new ArrayList<ArquivoOberlo>();

    @SuppressWarnings("unchecked")
    public void uploadDeArquivo(FileUploadEvent event) {
        try {
            getListaDeObjetoDoArquivo(obterBufferReader(event.getFile()), ",", tipo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getListaDeObjetoDoArquivo(BufferedReader linhasArquivo, String separador, String aplicativo) throws IOException{
        String linha = linhasArquivo.readLine();
        if("0".equals(aplicativo)) {
            linha = linhasArquivo.readLine();
            while (linha != null) {
                ArquivoOberlo objOberlo = (ArquivoOberlo) obterObjetoOberlo(linha.split(separador));
                if (objOberlo != null) {
                    dadosPedido.add(objOberlo);
                }
                linha = linhasArquivo.readLine();
            }
        }else{
            while (linha != null) {
                ArquivoOberlo objOberlo = (ArquivoOberlo) obterObjetoAplicacao(linha.split(separador));
                if (objOberlo != null) {
                    atrasados.add(objOberlo);
                }
                linha = linhasArquivo.readLine();
            }
        }
        linhasArquivo.close();
    }


    private Object obterObjetoAplicacao(String[] vetorObjeto) {
        ArquivoOberlo oberlo = null;
        if("ATRASADO-CORREIOS".equals(vetorObjeto[5])){
            oberlo = new ArquivoOberlo();
            oberlo.setIdShopify(vetorObjeto[0]);
            oberlo.setIdAliexpress(vetorObjeto[1]);
            oberlo.setCodRastreamento(vetorObjeto[2]);
            oberlo.setTipoCorreios(vetorObjeto[3]);
            oberlo.setStatusCorreios(vetorObjeto[4]);
            oberlo.setEtapaAtual(vetorObjeto[5]);
        }
        return oberlo;
    }

    public void gerarArquivo(){
        if(atrasados == null || atrasados.isEmpty() || dadosPedido == null || dadosPedido.isEmpty()){
            return;
        }
        try {
            List<ArquivoOberlo> oberlos = getObjetosParaGeracaoArquivo();
            if (oberlos != null && !oberlos.isEmpty()) {
                criarArquivo(oberlos, "reclamacao-correios.txt");
            }
        } catch (Exception e) {
        e.printStackTrace();
        }
    }

    public List<ArquivoOberlo> getObjetosParaGeracaoArquivo() {
        List<ArquivoOberlo> reclamacoes = new ArrayList<ArquivoOberlo>();
        if(atrasados != null && !atrasados.isEmpty()
                && dadosPedido != null && !dadosPedido.isEmpty()){
            for(ArquivoOberlo reclamacao : atrasados){
                for (ArquivoOberlo pedido : dadosPedido){
                    if(reclamacao.getIdShopify().equals(pedido.getIdShopify())
                            && reclamacao.getIdAliexpress().equals(pedido.getIdAliexpress())
                            && reclamacao.getCodRastreamento().equals(pedido.getCodRastreamento())){
                        reclamacoes.add(pedido);
                    }
                }
            }
        }
        return reclamacoes;
    }

    private Object obterObjetoOberlo(String[] vetorObjeto) {
        ArquivoOberlo oberlo = new ArquivoOberlo();
        PedidoCliente pedido = new PedidoCliente();
        EnderecoPedido endereco = new EnderecoPedido();

        endereco.setCep(vetorObjeto[17]);
        endereco.setEndereco1(vetorObjeto[14]);
        endereco.setEndereco2(vetorObjeto[15]);

        pedido.setNomePessoa(vetorObjeto[13]);
        pedido.setNomeProduto(vetorObjeto[8].substring(0,15));
        pedido.setEndereco(endereco);

        oberlo.setIdShopify(vetorObjeto[0]);
        oberlo.setCodRastreamento(vetorObjeto[11]);
        oberlo.setIdAliexpress(vetorObjeto[12]);
        oberlo.setPedido(pedido);

        return oberlo;
    }


    private BufferedReader obterBufferReader(UploadedFile arquivo) throws IOException{
        InputStream inputStream = arquivo.getInputstream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        return new BufferedReader(inputStreamReader);
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void criarArquivo(List<ArquivoOberlo> objetos, String nomeArquivoEntrada){

        String nomeArquivo = "c:\\".concat(nomeArquivoEntrada);
        try {
            FileWriter writer = new FileWriter(nomeArquivo);
            for(ArquivoOberlo objeto : objetos){
                writer.append("Cód. Rast: "+objeto.getCodRastreamento());
                writer.append("\n");
                writer.append("Nome Cliente: "+objeto.getPedido().getNomePessoa());
                writer.append("\n");
                writer.append("CEP: "+objeto.getPedido().getEndereco().getCep());
                writer.append("\n");
                writer.append("Endereço: "+objeto.getPedido().getEndereco().getEndereco1());
                writer.append(" - ");
                writer.append(objeto.getPedido().getEndereco().getEndereco2());
                writer.append("\n");
                writer.append("Produto: "+objeto.getPedido().getNomeProduto());
                writer.append("\n");
                writer.append("\n");
                writer.append("----------------------------------------------------------------------");
                writer.append("\n");
                writer.append("\n");
            }
            writer.flush();
            writer.close();
        }catch (IOException e){
            //TODO
        }
    }

    public List<ArquivoOberlo> getDadosPedido() {
        return dadosPedido;
    }

    public void setDadosPedido(List<ArquivoOberlo> dadosPedido) {
        this.dadosPedido = dadosPedido;
    }

    public List<ArquivoOberlo> getAtrasados() {
        return atrasados;
    }

    public void setAtrasados(List<ArquivoOberlo> atrasados) {
        this.atrasados = atrasados;
    }

}
