package com.poisondress.entidades;

public enum StatusEntregaPAR {
    DEVOLVIDO("15",5),
    FISCALIZACAO("16",1),
    CORREIOS("17",2),
    INDEFINIDO("18",7);


    private String status;
    private int codigo;

    StatusEntregaPAR(String status, int codigo) {
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
