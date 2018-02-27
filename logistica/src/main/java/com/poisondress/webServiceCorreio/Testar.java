package com.poisondress.webServiceCorreio;

import java.util.ArrayList;
import java.util.List;

public class Testar {

    public static String USUARIO = "ECT";
    public static String SENHA = "SRO";
    public static String TIPO = "L";
    public static String RESULTADO = "T";
    public static String LINGUA = "101";


    public static void main(String[] args){

        /*List<String> rastreado = new ArrayList<String>();
        String objeto = "RY726961614CN";
        rastreado.add(objeto);
        Service service = new Rastro().getServicePort();
        //Sroxml retorno = service.buscaEventos(USUARIO,SENHA,TIPO,RESULTADO,LINGUA,objeto);
        Sroxml retorno = service.buscaEventosLista(USUARIO,SENHA,TIPO,RESULTADO,LINGUA,rastreado);

        System.out.println(retorno.getTipoPesquisa());*/


        try {
            Rastro service = new Rastro();
            Service port = service.getServicePort();
            // TODO initialize WS operation arguments here
            java.lang.String usuario = "ECT";
            java.lang.String senha = "SRO";
            java.lang.String tipo = "L";
            java.lang.String resultado = "T";
            java.lang.String lingua = "101";
            java.lang.String objetos = "RY800982416CNRY800985562CNRY828398258CNRY840669197CN";
// TODO process result here
            Sroxml result = port.buscaEventos(usuario, senha, tipo, resultado, lingua, objetos);
            System.out.println("Quantidade = "+ result.getQtd() +"<br>");
            System.out.println("Size = "+ result.getObjeto().size()+"<br>");
            System.out.println("Número = "+ result.getObjeto().get(0).getNumero()+"<br>");
            System.out.println("Sigla = "+ result.getObjeto().get(0).getSigla()+"<br>");
            System.out.println("Status = "+ result.getObjeto().get(0).getEvento().get(0).getDescricao()+"<br>");
            System.out.println("Data = "+ result.getObjeto().get(0).getEvento().get(0).getData()+"<br>");
            System.out.println("Cidade = "+ result.getObjeto().get(0).getEvento().get(0).getCidade()+"<br>");


        } catch (Exception ex) {
// TODO handle custom exceptions here
        }

    }
}
