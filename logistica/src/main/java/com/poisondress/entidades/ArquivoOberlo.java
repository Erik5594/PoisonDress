package com.poisondress.entidades;

import java.util.Calendar;
import java.util.Date;

public class ArquivoOberlo {

    private String idShopify;
    private String idAliexpress;
    private String codRastreamento;
    private Calendar dataAlteracaoCorreios;
    private String tipoCorreios;
    private String statusCorreios;
    private String etapaAtual;
    private boolean atrasado;
    private PedidoCliente pedido;
    private boolean china;

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

    public String getCodRastreamento() {
        return codRastreamento;
    }

    public void setCodRastreamento(String codRastreamento) {
        this.codRastreamento = codRastreamento;
    }

    public Calendar getDataAlteracaoCorreios() {
        return dataAlteracaoCorreios;
    }

    public void setDataAlteracaoCorreios(Calendar dataAlteracaoCorreios) {
        this.dataAlteracaoCorreios = dataAlteracaoCorreios;
    }

    public String getTipoCorreios() {
        return tipoCorreios;
    }

    public void setTipoCorreios(String tipoCorreios) {
        this.tipoCorreios = tipoCorreios;
    }

    public String getStatusCorreios() {
        return statusCorreios;
    }

    public void setStatusCorreios(String statusCorreios) {
        this.statusCorreios = statusCorreios;
    }

    public String getEtapaAtual() {
        return etapaAtual;
    }

    public void setEtapaAtual(String etapaAtual) {
        this.etapaAtual = etapaAtual;
    }

    public boolean isAtrasado() {
        return atrasado;
    }

    public void setAtrasado(boolean atrasado) {
        this.atrasado = atrasado;
    }

    public PedidoCliente getPedido() {
        return pedido;
    }

    public void setPedido(PedidoCliente pedido) {
        this.pedido = pedido;
    }

    public boolean isChina() {
        return china;
    }

    public void setChina(boolean china) {
        this.china = china;
    }
}
