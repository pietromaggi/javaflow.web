/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javaflow.web.flowhandler;

import javaflow.web.servlet.Context;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.javaflow.Continuation;

/**
 *
 * @author piter
 */
public abstract class FlowHandlerBase implements Runnable {

    public abstract void execute() throws Throwable;

    @Override
    public void run() {
        try {
            // set initial context value
            _updateContext();

            execute();
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            _flowEnded = Boolean.TRUE;
        }
    }

    public final Boolean isFlowEnded() {
        return _flowEnded;
    }

    private void _suspend() {
        Continuation.suspend();
    }

    protected void sendAndWait(String page, Object[] toPage) throws Exception {
        _setAttributeObjects(toPage);
        _request.getRequestDispatcher(page).forward(_request, _response);
        _suspend();

        // because context (eg: request/response) have been changed since previous 
        // call, context value needs to be updated for use by current execution 
        // after each suspend.
        _updateContext();

        // TODO return a filled object ?
    }

    protected void send(String page, Object[] toPage) throws Exception {
        _setAttributeObjects(toPage);
        _request.getRequestDispatcher(page).forward(
                _request, _response);

        // TODO return a filled object ?
    }

    protected HttpServletRequest getRequest() {
        return ((Context) Continuation.getContext()).getRequest();
    }

    protected HttpServletResponse getResponse() {
        return ((Context) Continuation.getContext()).getResponse();
    }

    private void _updateContext() {
        _request = getRequest();
        _response = getResponse();
    }

    private void _setAttributeObjects(Object[] objsNames) {
        for (int i = 0; i < objsNames.length; i++) {
            _request.setAttribute((String) objsNames[i], objsNames[++i]);
        }
    }
    private HttpServletRequest _request;
    private HttpServletResponse _response;
    private Boolean _flowEnded = Boolean.FALSE;
}
