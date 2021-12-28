package com.allsaints.music.security.provider.token;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import com.allsaints.music.entity.*;
import com.allsaints.music.entity.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

@Component
public class CustomTokenEnhancer implements TokenEnhancer {

    @Autowired
    private SysUserRepository userRepository;
 
    @Autowired
    private SysRoleChannelRepository roleChannelRepository;

    @Autowired
    private SysRoleRepository roleRepository;

    @Autowired
    private SysChannelRepository channelRepository;

    @Autowired
    private SysChannelRegionRepository channelRegionRepository;

    @Autowired
    private SysRegionRepository regionRepository;
    
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        final Map<String, Object> additionalInfo = new LinkedHashMap<>(accessToken.getAdditionalInformation());
        SysUser user = userRepository.getByUsername(authentication.getName());
        Collection<? extends GrantedAuthority> authorities = authentication.getUserAuthentication().getAuthorities();
        if (user != null) {
            List<String> channelCode = new ArrayList<>();
            List<String> channelRegin = new ArrayList<>();
            for (GrantedAuthority authority : authorities) {
                String role = authority.getAuthority();
                SysRole sysRole = new SysRole();
                sysRole.setName(role);
                Optional<SysRole> optional = roleRepository.findOne(Example.of(sysRole));
                if (optional.isPresent()) {
                    SysRoleChannel roleChannel = new SysRoleChannel();
                    roleChannel.setRoleId(optional.get().getId());
                    List<SysRoleChannel> roleChannels = roleChannelRepository.findAll(Example.of(roleChannel));
                    List<Long> channelIds = roleChannels.stream().map(channel -> {
                        return channel.getChannelId();
                    }).collect(Collectors.toList());

                    List<Serializable> ids = new ArrayList<>();
                    ids.addAll(channelIds);

                    List<SysChannel> channels = channelRepository.findAllById(ids);
                    List<String> channelCodes = channels.stream().map(channel -> {
                        return channel.getCode();
                    }).collect(Collectors.toList());
                    channelCode.addAll(channelCodes);

                    List<SysChannelRegion> all = channelRegionRepository.findAll();
                    List<SysChannelRegion> sysChannelRegions = all.stream()
                            .filter(channel -> channelIds.contains(channel.getChannelId()))
                            .collect(Collectors.toList());
                    List<Long> channelRegions = sysChannelRegions.stream().map(sysChannelRegion -> {
                        return sysChannelRegion.getRegionId();
                    }).collect(Collectors.toList());

                    List<Serializable> regionIds = new ArrayList<>();
                    regionIds.addAll(channelRegions);
                    List<SysRegion> sysRegions = regionRepository.findAllById(regionIds);
                    List<String> regions = sysRegions.stream().map(sysRegion -> {
                        return sysRegion.getCode();
                    }).collect(Collectors.toList());
                    channelRegin.addAll(regions);
                }
            }
            StringBuffer channels = new StringBuffer();
            StringBuffer regions = new StringBuffer();
            for (int i = 0; i < channelCode.size(); i++) {
                if (i == channelCode.size() - 1) {
                    channels.append(channelCode.get(channelCode.size() - 1));
                } else {
                    channels.append(channelCode.get(i) + ",");
                }
            }
            for (int i = 0; i < channelRegin.size(); i++) {
                if (i == channelRegin.size() - 1) {
                    regions.append(channelRegin.get(channelRegin.size() - 1));
                } else {
                    regions.append(channelRegin.get(i) + ",");
                }
            }
            additionalInfo.put("uid", user.getId());
            additionalInfo.put("region", regions.toString());
            additionalInfo.put("channel", channels.toString());
        }
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }

}
