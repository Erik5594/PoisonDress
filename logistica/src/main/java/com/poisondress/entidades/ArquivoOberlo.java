package com.poisondress.entidades;

import java.util.Date;

public class ArquivoOberlo {

    private int id;
    private String idShopify;
    private String idAliexpress;
    private String codRastreamentoCorreios;
    private String descricao;
    private String tipo;
    private String status;
    private Date dataOcorreu;
    private int etapaObjeto;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdShopify() {
        return idShopify;
    }

    public void setIdShopify(String idShopify) {
        this.idShopify = idShopify;
    }

    public String getIdAliexpress() {
        return idAliexpress;
    }

    public void setIdAliexpress(String idAliexpress) {
        this.idAliexpress = idAliexpress;
    }

    public String getCodRastreamentoCorreios() {
        return codRastreamentoCorreios;
    }

    public void setCodRastreamentoCorreios(String codRastreamentoCorreios) {
        this.codRastreamentoCorreios = codRastreamentoCorreios;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDataOcorreu() {
        return dataOcorreu;
    }

    public void setDataOcorreu(Date dataOcorreu) {
        this.dataOcorreu = dataOcorreu;
    }
}
