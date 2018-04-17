package org.apache.camel.impl;

import java.util.Date;
import java.util.List;

import org.apache.camel.NamedNode;

/**
 * Created by kiryl_chepeleu on 4/18/18.
 */
public class TraceMessageHistory extends DefaultMessageHistory {

    private List<StackTraceElement> trace;

    public TraceMessageHistory(String routeId, NamedNode node, Date timestamp, List<StackTraceElement> trace) {
        super(routeId, node, timestamp);
        this.trace = trace;
    }

    public List<StackTraceElement> getTrace() {
        return trace;
    }
}
