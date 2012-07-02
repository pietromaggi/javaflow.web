/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javaflow.web.flowhandler;

import domain.Person;

/**
 *
 * @author piter
 */
public class HelloWorld extends FlowHandlerBase {

    @Override
    public void execute() throws Throwable {

        Person person = new Person();

        sendAndWait("helloworldform.jsp", new Object[]{"person", person});

        while (true) {
            person.setName(getRequest().getParameter("name"));
            person.setLastName(getRequest().getParameter("lastname"));

            if (_isPersonValid(person)) {
                send("helloworldshow.jsp", new Object[]{"person", person});
                _store(person);
                break;
            }

            sendAndWait("helloworldform.jsp", new Object[]{
                        "person", person,
                        "error", "fill both fields"});
        }
    }

    private boolean _isPersonValid(Person person) {
        return person.getName() != null && person.getLastName() != null && person.
                getName().length() > 0 && person.getLastName().length() > 0;
    }

    private void _store(Person person) {
        // store domain object somewhere...
    }
}
