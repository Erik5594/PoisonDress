package com.poisondress.entidades;

public enum StatusEntregaPO {

    POSTADO("00*01*09",0);


    private String status;
    private int codigo;

    StatusEntregaPO(String status, int codigo) {
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
