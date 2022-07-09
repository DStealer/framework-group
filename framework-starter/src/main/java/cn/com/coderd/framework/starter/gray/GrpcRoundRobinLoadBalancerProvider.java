package cn.com.coderd.framework.starter.gray;

import io.grpc.LoadBalancer;
import io.grpc.LoadBalancerProvider;
import io.grpc.NameResolver;
import io.grpc.protobuf.services.HealthCheckingLoadBalancerUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 自定义灰度 grpc provider
 */
@Slf4j
public class GrpcRoundRobinLoadBalancerProvider extends LoadBalancerProvider {
    private final Provider provider = new Provider();

    @Override
    public boolean isAvailable() {
        return this.provider.isAvailable();
    }

    @Override
    public int getPriority() {
        return this.provider.getPriority() + 1;
    }

    @Override
    public String getPolicyName() {
        return this.provider.getPolicyName();
    }

    @Override
    public LoadBalancer newLoadBalancer(LoadBalancer.Helper helper) {
        return HealthCheckingLoadBalancerUtil.newHealthCheckingLoadBalancer(this.provider, helper);
    }

    public static final class Provider extends LoadBalancerProvider {
        private static final String NO_CONFIG = "no service config";

        @Override
        public boolean isAvailable() {
            return true;
        }

        @Override
        public int getPriority() {
            return 10;
        }

        @Override
        public String getPolicyName() {
            return "gray_round_robin";
        }

        @Override
        public LoadBalancer newLoadBalancer(LoadBalancer.Helper helper) {
            return new GrpcRoundRobinLoadBalancer(helper);
        }

        @Override
        public NameResolver.ConfigOrError parseLoadBalancingPolicyConfig(
                Map<String, ?> rawLoadBalancingPolicyConfig) {
            return NameResolver.ConfigOrError.fromConfig(NO_CONFIG);
        }
    }
}