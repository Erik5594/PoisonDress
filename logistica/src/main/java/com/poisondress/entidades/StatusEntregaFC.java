package com.poisondress.entidades;

public enum StatusEntregaFC {
    PENDENTE("04",3);


    private String status;
    private int codigo;

    StatusEntregaFC(String status, int codigo) {
        this.status = status;
        this.codigo = codigo;
    }

    public String getStatus() {
        return status;
    }

    public int getCodigo() {
        int codigo;
    }
}
