package com.poisondress.entidades;

public enum StatusEntregaBDE {

    ENTREGUE("00*01",4),
    PENDENTE("02*03*04*05*06*07*08*10*12*19*20*21*25*33*34*35*36*38",3),
    DEVOLVIDO("22*23*26*48*50*51*52*80",5),
    ATRASADO("09*69",6);


    private String status;
    private int codigo;

    StatusEntregaBDE(String status, int codigo) {
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
