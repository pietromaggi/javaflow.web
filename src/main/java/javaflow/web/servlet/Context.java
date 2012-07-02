/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javaflow.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author piter
 */
public class Context {

    public Context(HttpServletRequest request, HttpServletResponse response) {
        _request = request;
        _response = response;
    }

    public HttpServletRequest getRequest() {
        return _request;
    }

    public HttpServletResponse getResponse() {
        return _response;
    }

    private HttpServletRequest _request;
    private HttpServletResponse _response;
}
