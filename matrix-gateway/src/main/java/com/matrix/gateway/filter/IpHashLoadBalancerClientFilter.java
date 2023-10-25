package com.matrix.gateway.filter;

import com.matrix.common.constant.CommonConstants;
import com.matrix.gateway.loadbalancer.IpHashLoadBalancer;
import com.matrix.gateway.order.FilterOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultRequest;
import org.springframework.cloud.client.loadbalancer.LoadBalancerUriTools;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.gateway.config.GatewayLoadBalancerProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.DelegatingServiceInstance;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.addOriginalRequestUrl;

/**
 * 一致性 IP Hash负载均衡器
 */
@Slf4j
public class IpHashLoadBalancerClientFilter implements GlobalFilter, Ordered {


    private final LoadBalancerClientFactory clientFactory;

    private final GatewayLoadBalancerProperties properties;

    public IpHashLoadBalancerClientFilter(LoadBalancerClientFactory clientFactory,
                                          GatewayLoadBalancerProperties properties) {
        this.clientFactory = clientFactory;
        this.properties = properties;
    }

    @Override
    public int getOrder() {
        return FilterOrder.IP_HASH_LOAD_BALANCER_CLIENT_FILTER;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        URI url = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
        String schemePrefix = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_SCHEME_PREFIX_ATTR);
        if (url != null && (CommonConstants.HASH_LB.equals(url.getScheme()) || CommonConstants.HASH_LB.equals(schemePrefix))) {

            addOriginalRequestUrl(exchange, url);

            if (log.isTraceEnabled()) {
                log.trace(IpHashLoadBalancerClientFilter.class.getSimpleName() + " url before: " + url);
            }

            return choose(exchange).doOnNext(response -> {

                if (!response.hasServer()) {
                    throw NotFoundException.create(properties.isUse404(),
                            "Unable to find instance for " + url.getHost());
                }

                URI uri = exchange.getRequest().getURI();
                String overrideScheme = null;
                if (schemePrefix != null) {
                    overrideScheme = url.getScheme();
                }
                DelegatingServiceInstance serviceInstance = new DelegatingServiceInstance(response.getServer(),
                        overrideScheme);
                URI requestUrl = reconstructUri(serviceInstance, uri);
                if (log.isTraceEnabled()) {
                    log.trace("LoadBalancerClientFilter url chosen: " + requestUrl);
                }
                exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, requestUrl);
            }).then(chain.filter(exchange));
        }
        return chain.filter(exchange);

    }

    protected URI reconstructUri(ServiceInstance serviceInstance, URI original) {
        return LoadBalancerUriTools.reconstructURI(serviceInstance, original);
    }

    private Mono<Response<ServiceInstance>> choose(ServerWebExchange exchange) {


        URI uri = (URI) exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
        IpHashLoadBalancer loadBalancer = new IpHashLoadBalancer(clientFactory.getLazyProvider(uri.getHost(), ServiceInstanceListSupplier.class), uri.getHost());
        return loadBalancer.choose(this.createRequest(exchange));


    }

    private Request createRequest(ServerWebExchange exchange) {
        String ip = exchange.getRequest().getLocalAddress().getAddress().getHostAddress();
        return new DefaultRequest<>(ip);
    }

}