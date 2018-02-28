package com.poisondress.entidades;

public enum StatusEntregaOEC {
    PENDENTE("00",3);


    private String status;
    private int codigo;

    StatusEntregaOEC(String status, int codigo) {
        this.status = status;
        this.codigo = codigo;
    }

    public String getStatus() {
        return status;
    }

    public int getCodigo() {
        return codigo;
    }
}
