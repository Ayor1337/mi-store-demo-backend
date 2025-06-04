package com.example.filter;

import com.example.result.Result;
import com.example.util.CONST;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
@Order(CONST.LIMIT_CORS)
public class FlowLimitFilter extends HttpFilter {

    @Resource(name = "stringRedisTemplate")
    StringRedisTemplate redisTemplate;

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 获取访问的IP地址
        String address = request.getRemoteAddr();
        // 如果没有超出次数
        if (this.tryCount(address)) {
            chain.doFilter(request, response);
            // 如果超出次数
        } else {
            this.writeBlockMessage(response);
        }
    }

    private void writeBlockMessage(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(Result.fail(403, "操作频繁，请稍后再试").toJSONString());
    }

    private boolean tryCount(String ip) {
        synchronized (ip.intern()) {
            if (redisTemplate.hasKey(CONST.FLOW_LIMIT_BLOCK + ip))
                return false;
            return this.limitPeriodCheck(ip);
        }
    }

    private boolean limitPeriodCheck(String ip) {
        if (redisTemplate.hasKey(CONST.FLOW_LIMIT_COUNTER + ip)) {
            long increment = Optional.ofNullable(redisTemplate.opsForValue().increment(CONST.FLOW_LIMIT_COUNTER + ip)).orElse(0L);
            if (increment > 1000) {
                redisTemplate.opsForValue().set(CONST.FLOW_LIMIT_BLOCK + ip, "", 10, TimeUnit.SECONDS);
                return false;
            }
        } else {
            redisTemplate.opsForValue().set(CONST.FLOW_LIMIT_COUNTER + ip, "1", 3, TimeUnit.SECONDS);
        }
        return true;
    }
}
