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

    private static String JA_ABERTO_RECLAMACAO = "*RF980340725CN*RY521129559CN*RY507925097CN*RY544278370CN*RY544273678CN*RY544396589CN*RY544276484CN*RF999406644CN*RY521494441CN*RY544396592CN*RY544273338N*RY539253622CN*RY539251555CN*RF919015806CN*RF923872751CN*RF980349993CN*RF980343284CN*RY507600762CN*RF999407565CN*RY507928005CN*RF999414212CN*RY507921991N*RL749760479CN*RL749760690CN*RL749025347CN*RY507737624CN*RL749025421CN*RL749025421CN*RY521493993CN*RL749760641CN*RL749760638CN*RL749760655CN*RL749760465N*RY507920996CN*RY507925106CN*RY507602255CN*RL749760536CN*RL752244299CN*RY507599905CN*RL752244342CN*RY521497329CN*RY521494472CN*RL752244360CN*RY521493976N*RY523326675CN*RY523639002CN*RY523635473CN*RY523637103CN*RY523632962CN*RY523634787CN*RY523633866CN*RL752244271CN*RY539250952CN*RY524169305CN*RY523630723N*RL752244373CN*RY523631193CN*RY539253239CN*RL752244268CN*RL752244268CN*RY523633353CN*RY523629362CN*RY523633336CN*RY523627429CN*RL752244325CN*RY523631851N*RY523634186CN*RY544280798CN*RY523632596CN*RY524306916CN*RY523633588CN*RY523628469CN*RY539253622CN*RY539316089CN*RY539250440CN*RY539251621CN*RY544276484N*RY553379062CN*RY544278851CN*RY553376826CN*RY544279667CN*RY550886082CN*RY544393494CN*RY544396589CN*RY544276776CN*RY544397023CN*RY544275435CN*RY544273885CN*RY550883735CN*RY544276025CN*RY550882182CN*RY544273338CN*RY544273678CN*RY553379102CN*RY544274611CN*RY550882125CN*RY550880955CN*RY550883611CN*RY544268491CN*RY557922460CN*Y550880822CN*RY551242061CN*RY550878470CN*RY553378243CN*RY558453913CN*RY553380437CN*RY557924315CN*RY558453493CN*RY553854177CN*RY553386559CN*RY553383858CN*RO65607489CN*RY553386633CN*RY555967715CN*RY553379748CN*RY553379765CN*RY553380542CN*RY553378932CN*RY553378963CN*RY553380750CN*RY553381786CN*RY553386253CN*RY553384819CN*RY553385893CN*RL755375449CN*";

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
                        if(!JA_ABERTO_RECLAMACAO.contains(pedido.getCodRastreamento())){
                            reclamacoes.add(pedido);
                        }
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
