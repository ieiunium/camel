package org.apache.camel.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.MessageHistory;
import org.apache.camel.Processor;
import org.apache.camel.impl.TraceMessageHistory;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.model.ProcessorDefinitionHelper;
import org.apache.camel.processor.interceptor.DefaultChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by kiryl_chepeleu on 4/19/18.
 */
public final class TracingUtils {

    private static final Logger LOG = LoggerFactory.getLogger(TracingUtils.class);

    private TracingUtils() {
    }

    public static void trace(final Processor processor, Exchange exchange) {
        if (processor instanceof DefaultChannel) {
            DefaultChannel channel = DefaultChannel.class.cast(processor);
            ProcessorDefinition<?> childDefinition = channel.getChildDefinition();
            ProcessorDefinition<?> processorDefinition = channel.getProcessorDefinition();
            ProcessorDefinition<?> definition = childDefinition == null ? processorDefinition : childDefinition;
            String routeId = ProcessorDefinitionHelper.getRouteId(definition);
            List<StackTraceElement> stackTrace = definition.stackTrace;
            MessageHistory history = new TraceMessageHistory(routeId, definition, new Date(), stackTrace);
            List<MessageHistory> list = exchange.getProperty(Exchange.MESSAGE_HISTORY, List.class);
            if (list == null) {
                list = new ArrayList<>();
                exchange.setProperty(Exchange.MESSAGE_HISTORY, list);
            }
            list.add(history);
            for (final StackTraceElement element : stackTrace) {
                LOG.debug("Pipeline: " + definition.getLabel() + " " + element);
            }
        }
    }

    public static StackTraceElement getRouteBuilderStackTraceElement(){
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        for (final StackTraceElement i : elements) {
            if (!i.getClassName().contains("org.apache") &&
                !i.getClassName().contains("org.spring") &&
                !i.getClassName().contains("java.lang") &&
                i.getMethodName().contains("configure")
                ) {
                return i;
            }
        }
        return null;
    }

    public static List<StackTraceElement> getRouteBuilderStackTraceElements(){
        List<StackTraceElement> resList = new ArrayList();
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        for (final StackTraceElement i : elements) {
            if (!i.getClassName().contains("org.apache") &&
                !i.getClassName().contains("org.spring") &&
                !i.getClassName().contains("java.lang") &&
                i.getMethodName().contains("configure")
                ) {
                resList.add(i);
            }
        }
        return resList;
    }

}
