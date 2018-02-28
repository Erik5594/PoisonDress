package com.poisondress.entidades;

public enum StatusEntregaLDE {
    PENDENTE("09",3);


    private String status;
    private int codigo;

    StatusEntregaLDE(String status, int codigo) {
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
