package com.poisondress.entidades;

public class ArquivoOberlo {

    private int id;
    private String idShopify;
    private String idAliexpress;
    private String codRastreamentoCorreios;
    private boolean observar;

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

    public boolean isObservar() {
        return observar;
    }

    public void setObservar(boolean observar) {
        this.observar = observar;
    }
}
