package cn.com.coderd.framework.starter.web;

import cn.com.coderd.framework.starter.constants.ConstVar;
import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.StringJoiner;

/**
 * 标记支持过滤器
 */
public class TraceSupportFilter implements Filter {
    private static final String TracedFlag = TraceSupportFilter.class.getSimpleName() + ".traced";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            chain.doFilter(request, response);
        }
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        if (httpRequest.getAttribute(TracedFlag) != null) {
            chain.doFilter(httpRequest, httpResponse);
        }
        httpRequest.setAttribute(TracedFlag, Boolean.TRUE);
        try {
            Enumeration<String> names = httpRequest.getHeaderNames();
            while (names.hasMoreElements()) {
                String headerName = names.nextElement();
                if (!headerName.startsWith(ConstVar.TRACE_PREFIX)) {
                    continue;
                }
                StringJoiner joiner = new StringJoiner(",");
                Enumeration<String> values = httpRequest.getHeaders(headerName);
                while (values.hasMoreElements()) {
                    joiner.add(values.nextElement());
                }
                MDC.put(headerName, joiner.toString());
            }
            chain.doFilter(httpRequest, httpResponse);
        } finally {
            httpRequest.removeAttribute(TracedFlag);
            Enumeration<String> names = httpRequest.getHeaderNames();
            while (names.hasMoreElements()) {
                String headerName = names.nextElement();
                if (!headerName.startsWith(ConstVar.TRACE_PREFIX)) {
                    continue;
                }
                MDC.remove(headerName);
            }
        }
    }
}
