package com.allsaints.music.entity.listener;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CreateByAuditorAware implements AuditorAware<String> {
	
	@Value("${spring.profiles.active}")
    private String profiles;
	
	@Override
	public Optional<String> getCurrentAuditor() {
		
		if(profiles.equals("dev")) {
			return Optional.of("-");
		}
		
		SecurityContext ctx = SecurityContextHolder.getContext();
		if (ctx == null) {
            return null;
        }
        if (ctx.getAuthentication() == null) {
            return null;
        }
        if (ctx.getAuthentication().getPrincipal() == null) {
            return null;
        }
		return Optional.of(ctx.getAuthentication().getName());
	}

}
