package com.poisondress.entidades;

public enum StatusEntregaLDI {
    AGUARDANDO_RETIRADA("00*01*02*03*14",8);

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
        return codigo;
    }
}
