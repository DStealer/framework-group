package cn.com.coderd.framework.starter.gray;

import cn.com.coderd.framework.starter.constants.ConstVar;
import cn.com.coderd.framework.starter.utils.CompositeUtil;
import io.grpc.Attributes;
import io.grpc.NameResolver;
import io.grpc.internal.GrpcUtil;
import net.devh.boot.grpc.client.nameresolver.DiscoveryClientNameResolver;
import net.devh.boot.grpc.client.nameresolver.DiscoveryClientResolverFactory;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.util.Assert;

import java.util.Set;

public class GrpcDiscoveryClientResolverFactory extends DiscoveryClientResolverFactory {
    public static final Attributes.Key<String> GRAY_TAG_KEY = Attributes.Key.create(ConstVar.TAG_GRAY_KEY);

    /**
     * Creates a new discovery client based name resolver factory.
     *
     * @param client The client to use for the address discovery.
     */
    public GrpcDiscoveryClientResolverFactory(DiscoveryClient client) {
        super(client);
    }

    @Override
    protected GrpcDiscoveryClientNameResolver newNameResolver(String serviceName, NameResolver.Args args) {
        DiscoveryClient client = CompositeUtil.getPrivateField(DiscoveryClientResolverFactory.class,
                "client", this);
        Assert.notNull(client, "client");
        Set<DiscoveryClientNameResolver> discoveryClientNameResolvers = CompositeUtil.getPrivateField(
                DiscoveryClientResolverFactory.class, "discoveryClientNameResolvers", this);
        Assert.notNull(discoveryClientNameResolvers, "discoveryClientNameResolvers");
        return new GrpcDiscoveryClientNameResolver(serviceName, client, args,
                GrpcUtil.SHARED_CHANNEL_EXECUTOR, discoveryClientNameResolvers::remove);
    }
}
