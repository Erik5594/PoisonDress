
package com.poisondress.webServiceCorreio;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 * 
 */
@WebService(name = "Service", targetNamespace = "http://resource.webservice.correios.com.br/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface Service {


    /**
     * 
     * @param usuario
     * @param tipo
     * @param resultado
     * @param lingua
     * @param senha
     * @param objetos
     * @return
     *     returns com.poisondress.webServiceCorreio.Sroxml
     */
    @WebMethod(action = "buscaEventos")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "buscaEventos", targetNamespace = "http://resource.webservice.correios.com.br/", className = "com.poisondress.webServiceCorreio.BuscaEventos")
    @ResponseWrapper(localName = "buscaEventosResponse", targetNamespace = "http://resource.webservice.correios.com.br/", className = "com.poisondress.webServiceCorreio.BuscaEventosResponse")
    @Action(input = "buscaEventos", output = "http://resource.webservice.correios.com.br/Service/buscaEventosResponse")
    public Sroxml buscaEventos(
        @WebParam(name = "usuario", targetNamespace = "")
        String usuario,
        @WebParam(name = "senha", targetNamespace = "")
        String senha,
        @WebParam(name = "tipo", targetNamespace = "")
        String tipo,
        @WebParam(name = "resultado", targetNamespace = "")
        String resultado,
        @WebParam(name = "lingua", targetNamespace = "")
        String lingua,
        @WebParam(name = "objetos", targetNamespace = "")
        String objetos);

    /**
     * 
     * @param usuario
     * @param tipo
     * @param resultado
     * @param lingua
     * @param senha
     * @param objetos
     * @return
     *     returns com.poisondress.webServiceCorreio.Sroxml
     */
    @WebMethod(action = "buscaEventosLista")
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "buscaEventosLista", targetNamespace = "http://resource.webservice.correios.com.br/", className = "com.poisondress.webServiceCorreio.BuscaEventosLista")
    @ResponseWrapper(localName = "buscaEventosListaResponse", targetNamespace = "http://resource.webservice.correios.com.br/", className = "com.poisondress.webServiceCorreio.BuscaEventosListaResponse")
    @Action(input = "buscaEventosLista", output = "http://resource.webservice.correios.com.br/Service/buscaEventosListaResponse")
    public Sroxml buscaEventosLista(
        @WebParam(name = "usuario", targetNamespace = "")
        String usuario,
        @WebParam(name = "senha", targetNamespace = "")
        String senha,
        @WebParam(name = "tipo", targetNamespace = "")
        String tipo,
        @WebParam(name = "resultado", targetNamespace = "")
        String resultado,
        @WebParam(name = "lingua", targetNamespace = "")
        String lingua,
        @WebParam(name = "objetos", targetNamespace = "")
        List<String> objetos);

}