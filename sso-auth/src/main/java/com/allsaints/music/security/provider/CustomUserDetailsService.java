package com.allsaints.music.security.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.allsaints.music.entity.*;
import com.allsaints.music.entity.repository.*;
import com.allsaints.music.extentity.TblCpoaUser;
import com.allsaints.music.extentity.repository.TblCpoaUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.util.ObjectUtils;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private SysUserRepository userRepository;

    @Autowired
    private TblCpoaUserRepository cpoaUserRepository;

    @Autowired
    private SysRoleChannelRepository roleChannelRepository;

    @Autowired
    private SysChannelRegionRepository channelRegionRepository;

    @Autowired
    private SysRoleRepository roleRepository;

    @Autowired
    private SysChannelRepository channelRepository;

    @Autowired
    private SysRegionRepository regionRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = userRepository.getByUsername(username);
        TblCpoaUser cpoaUser = cpoaUserRepository.findByUsername(username);
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if (!ObjectUtils.isEmpty(sysUser)) {
            List<SysRole> roles = sysUser.getSupportRoles();
            for (SysRole sysRole : roles) {
                Long roleId = roleRepository.findByNameAndStatus(sysRole.getName(), 1).getId();
                List<SysRoleChannel> roleChannels = roleChannelRepository.findByRoleId(roleId);
                if (!ObjectUtils.isEmpty(roleChannels)) {
                    List<SysChannel> channels = channelRepository.findAllById(roleChannels.stream().map(SysRoleChannel::getChannelId).collect(Collectors.toList()));
                    if (!ObjectUtils.isEmpty(channels)) {
                        for (SysChannel channel : channels) {
                            authorities.add(new SimpleGrantedAuthority("CHANNEL_" + channel.getName()));

                            List<SysChannelRegion> channelRegions = channelRegionRepository.findByChannelId(channel.getId());
                            if (!ObjectUtils.isEmpty(channelRegions)) {
                                List<SysRegion> regions = regionRepository.findAllById(channelRegions.stream().map(SysChannelRegion::getRegionId).collect(Collectors.toList()));
                                for (SysRegion region : regions) {
                                    authorities.add(new SimpleGrantedAuthority("REGION_" + region.getName()));
                                }
                            }
                        }
                    }
                }
                authorities.add(new SimpleGrantedAuthority(sysRole.getName()));
            }
            return new User(sysUser.getUsername(), sysUser.getPassword(), authorities);
        }
        authorities.add(new SimpleGrantedAuthority("CPOA"));
        return new User(cpoaUser.getUsername(), cpoaUser.getPassword(), authorities);
    }

}
