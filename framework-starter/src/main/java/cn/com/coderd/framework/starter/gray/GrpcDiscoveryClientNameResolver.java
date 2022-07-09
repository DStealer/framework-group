package cn.com.coderd.framework.starter.gray;

import cn.com.coderd.framework.starter.constants.ConstVar;
import io.grpc.Attributes;
import io.grpc.internal.SharedResourceHolder;
import net.devh.boot.grpc.client.nameresolver.DiscoveryClientNameResolver;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.concurrent.Executor;
import java.util.function.Consumer;

/**
 * grpc 命名解析工厂
 */

public class GrpcDiscoveryClientNameResolver extends DiscoveryClientNameResolver {
    /**
     * Creates a new DiscoveryClientNameResolver.
     *
     * @param name             The name of the service to look up.
     * @param client           The client used to look up the service addresses.
     * @param args             The name resolver args.
     * @param executorResource The executor resource.
     * @param shutdownHook     The optional cleaner used during {@link #shutdown()}
     */
    public GrpcDiscoveryClientNameResolver(String name, DiscoveryClient client, Args args,
                                           SharedResourceHolder.Resource<Executor> executorResource,
                                           Consumer<DiscoveryClientNameResolver> shutdownHook) {
        super(name, client, args, executorResource, shutdownHook);
    }

    @Override
    protected Attributes getAttributes(ServiceInstance serviceInstance) {
        Attributes attributes = super.getAttributes(serviceInstance);
        return attributes.toBuilder().set(GrpcDiscoveryClientResolverFactory.GRAY_TAG_KEY,
                serviceInstance.getMetadata().getOrDefault(ConstVar.TAG_GRAY_KEY, ConstVar.TAG_BASELINE)).build();

    }
}
