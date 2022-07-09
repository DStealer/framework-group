package cn.com.coderd.framework.gateway.gray;

import cn.com.coderd.framework.gateway.utils.ConstVar;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.loadbalancer.core.DelegatingServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * http服务定位灰度支持
 */
@Slf4j
public class GrayHttpServiceInstanceListSupplier extends DelegatingServiceInstanceListSupplier {
    public GrayHttpServiceInstanceListSupplier(ServiceInstanceListSupplier delegate) {
        super(delegate);
        Assert.notNull(delegate, "delegate can't be null");
    }

    /**
     * 灰度规则:
     * context  metadata           result
     * []        [] (无灰度)        [baseline]
     * [a]       [] (无灰度)        [baseline]
     * []        [a] (有灰度)       [baseline]
     * [a]       [a] (有灰度)       [a]
     *
     * @param request
     * @return
     */
    @Override
    @SuppressWarnings("unchecked")
    public Flux<List<ServiceInstance>> get(Request request) {
        return Flux.deferContextual(ctx -> delegate.get(request).map(e -> {
            if (e == null || e.isEmpty()) {
                return Collections.EMPTY_LIST;
            }
            String grayTag = ctx.getOrDefault(ConstVar.TAG_GRAY_KEY, ConstVar.TAG_BASELINE);
            Assert.notNull(grayTag, "grayTag can't be null");
            List<ServiceInstance> hitIns = new ArrayList<>(e.size());
            for (ServiceInstance instance : e) {
                if (grayTag.equals(instance.getMetadata()
                        .getOrDefault(ConstVar.TAG_GRAY_KEY, grayTag))) {
                    hitIns.add(instance);
                }
            }
            return hitIns;
        }));
    }

    @Override
    public Flux<List<ServiceInstance>> get() {
        return delegate.get();
    }
}
