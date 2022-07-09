package cn.com.coderd.framework.starter.gray;

import cn.com.coderd.framework.starter.constants.ConstVar;
import org.slf4j.MDC;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerProperties;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.client.loadbalancer.reactive.ReactiveLoadBalancer;
import org.springframework.cloud.loadbalancer.blocking.client.BlockingLoadBalancerClient;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 阻塞负载均衡客户端
 */
public class ContextBlockingLoadBalancerClient extends BlockingLoadBalancerClient {
    private final LoadBalancerClientFactory loadBalancerClientFactory;

    public ContextBlockingLoadBalancerClient(LoadBalancerClientFactory loadBalancerClientFactory, LoadBalancerProperties properties) {
        super(loadBalancerClientFactory, properties);
        this.loadBalancerClientFactory = loadBalancerClientFactory;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> ServiceInstance choose(String serviceId, Request<T> request) {
        ReactiveLoadBalancer<ServiceInstance> loadBalancer = this.loadBalancerClientFactory.getInstance(serviceId);
        if (loadBalancer == null) {
            return null;
        }
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        Map<String, String> map;
        if (contextMap != null && !contextMap.isEmpty()) {
            map = contextMap.entrySet().stream()
                    .filter(e -> e.getKey() != null && e.getKey().startsWith(ConstVar.TRACE_PREFIX) && e.getValue() != null)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        } else {
            map = Collections.EMPTY_MAP;
        }
        Response<ServiceInstance> loadBalancerResponse = Mono.from(loadBalancer.choose(request))
                .contextWrite(Context.of(map)).block();
        if (loadBalancerResponse == null) {
            return null;
        }
        return loadBalancerResponse.getServer();
    }

}
