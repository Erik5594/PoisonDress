package com.poisondress.entidades;

public class PedidoCliente {
    private String nomePessoa;
    private EnderecoPedido endereco;
    private String nomeProduto;

    public String getNomePessoa() {
        return nomePessoa;
    }

    public void setNomePessoa(String nomePessoa) {
        this.nomePessoa = nomePessoa;
    }

    public EnderecoPedido getEndereco() {
        return endereco;
    }

    public void setEndereco(EnderecoPedido endereco) {
        this.endereco = endereco;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }
}
