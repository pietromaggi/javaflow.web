/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javaflow.web.servlet;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.javaflow.Continuation;
import org.apache.commons.javaflow.ContinuationClassLoader;

/**
 *
 * @author piter
 */
@WebServlet(name = "FlowServlet", urlPatterns = {"/FlowServlet"})
public class FlowServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {

            HttpSession session = request.getSession();
            FlowHandlerWithContinuation flowHandlerWithContinuation = (FlowHandlerWithContinuation) session.
                    getAttribute(_CONTINUATION_ATTRIBUTE);

            if (flowHandlerWithContinuation == null) {
                ContinuationClassLoader cl = new ContinuationClassLoader(
                        new URL[]{new File("/").toURI().toURL()},
                        FlowServlet.class.getClassLoader());
                cl.addLoaderPackageRoot("javaflow.web.flowhandler");

                // TODO make this servlet work with omre than one handler!
                Class flowHandlerClass = cl
                        .loadClass("javaflow.web.flowhandler.HelloWorld");

                Runnable flowHandlerInstance = (Runnable) flowHandlerClass
                        .newInstance();

                flowHandlerWithContinuation = new FlowHandlerWithContinuation(
                        flowHandlerInstance, Continuation.startWith(
                        flowHandlerInstance, new Context(request,
                        response)));
            } else {
                flowHandlerWithContinuation = new FlowHandlerWithContinuation(
                        flowHandlerWithContinuation.getHandler(),
                        Continuation.continueWith(
                        flowHandlerWithContinuation.getContinuation(),
                        new Context(request, response)));
            }

            if (_isFlowEnded(flowHandlerWithContinuation.getHandler())) {
                session.removeAttribute(_CONTINUATION_ATTRIBUTE);
            } else {
                session.setAttribute(_CONTINUATION_ATTRIBUTE,
                        flowHandlerWithContinuation);
            }

        } catch (Exception e) {
            request.getSession().removeAttribute(_CONTINUATION_ATTRIBUTE);
            e.printStackTrace();
        }
    }

    // <editor-fold defaultstate="collapsed"
    // desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    private static final String _CONTINUATION_ATTRIBUTE = "continuation";

    private class FlowHandlerWithContinuation {

        public FlowHandlerWithContinuation(Runnable handler,
                Continuation continuation) {
            _handler = handler;
            _continuation = continuation;
        }

        public Runnable getHandler() {
            return _handler;
        }

        public Continuation getContinuation() {
            return _continuation;
        }
        private final Runnable _handler;
        private final Continuation _continuation;
    }

    private Boolean _isFlowEnded(Object flowHandlerInstance) throws Exception {
        Class flowHandlerClass = flowHandlerInstance.getClass();
        Method method = flowHandlerClass.getMethod("isFlowEnded");
        return (Boolean) method.invoke(flowHandlerInstance);
    }
}
