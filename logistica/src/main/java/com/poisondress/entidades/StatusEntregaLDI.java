package com.poisondress.entidades;

public enum StatusEntregaLDI {
    PENDENTE("00*01*02*03*14",3);


    private String status;
    private int codigo;

    StatusEntregaLDI(String status, int codigo) {
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
