package com.poisondress.entidades;

public enum StatusEntregaRO {
    CORREIOS("00*01",2);


    private String status;
    private int codigo;

    StatusEntregaRO(String status, int codigo) {
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
