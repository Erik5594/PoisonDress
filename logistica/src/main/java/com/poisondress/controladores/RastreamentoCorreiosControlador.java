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

    private String etapa;

    @SuppressWarnings("unchecked")
    public void uploadDeArquivo(FileUploadEvent event) {
        try {
            List<ArquivoOberlo> objetosOberlo = getListaDeObjetoDoArquivo(obterBufferReader(event.getFile()), ",");

            List<ArquivoOberlo> objetosAlterado = new ArrayList<ArquivoOberlo>();



            for(ArquivoOberlo dadosOberlo : objetosOberlo) {
                UtilCorreios.consultarCorreios(dadosOberlo);
                if(dadosOberlo.getTipo() != null){
                    objetosAlterado.add(dadosOberlo);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private List<ArquivoOberlo> getListaDeObjetoDoArquivo(BufferedReader linhasArquivo, String separador) throws IOException{
        List<ArquivoOberlo> objsOberlo = new ArrayList<ArquivoOberlo>();
        String linha = linhasArquivo.readLine();
        linha = linhasArquivo.readLine();
        while(linha != null){
            ArquivoOberlo objOberlo = (ArquivoOberlo) obterObjeto(linha.split(separador));
            if(objOberlo != null){
                objsOberlo.add(objOberlo);
            }
            linha = linhasArquivo.readLine();
        }
        linhasArquivo.close();
        return objsOberlo;
    }


    private Object obterObjeto(String[] vetorObjeto) {
        ArquivoOberlo oberlo = new ArquivoOberlo();
        oberlo.setIdShopify(vetorObjeto[0]);
        oberlo.setCodRastreamentoCorreios(vetorObjeto[11]);
        oberlo.setIdAliexpress(vetorObjeto[12]);
        oberlo.setStatus(etapa);
        return oberlo;
    }


    private BufferedReader obterBufferReader(UploadedFile arquivo) throws IOException{
        InputStream inputStream = arquivo.getInputstream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        return new BufferedReader(inputStreamReader);
    }

    public String getEtapa() {
        return etapa;
    }

    public void setEtapa(String etapa) {
        this.etapa = etapa;
    }
}
