package com.allsaints.music.web;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.allsaints.music.entity.*;
import com.allsaints.music.entity.repository.*;
import com.allsaints.music.http.form.SysRoleForm;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.allsaints.music.http.Result;
import com.allsaints.music.http.ResultCodeTemplate;

@RestController
@RequestMapping("role")
public class RoleController {

    @Autowired
    private SysRoleRepository roleRepository;
    @Autowired
    private SysRoleChannelRepository roleChannelRepository;
    @Autowired
    private SysRoleServiceRepository roleServiceRepository;
    @Autowired
    private SysUserRoleRepository userRoleRepository;
    @Autowired
    private SysDeptRoleRepository deptRoleRepository;
    @Autowired
    private SysServiceRepository serviceRepository;

    @RequestMapping("query")
    public Result query(Pageable pageable, String name, Integer status) {
        SysRole role = new SysRole();
        if (ObjectUtils.isNotEmpty(name)) {
            role.setName(name);
        }
        if (ObjectUtils.isNotEmpty(status)) {
            role.setStatus(status);
        }
        Page<SysRole> roles = roleRepository.findAll(Example.of(role), pageable.previousOrFirst());
        return new Result(ResultCodeTemplate.SUCCESS, roles);
    }

    @RequestMapping("get")
    public Result get(Long roleId, String serviceId) {
        Optional<SysRole> optional = roleRepository.findById(roleId);

        if (optional.isPresent()) {
            SysRoleForm roleForm = new SysRoleForm();
            SysRole sysRole = optional.get();
            BeanUtils.copyProperties(sysRole, roleForm);
            Optional<List<SysPermission>> permissions = sysRole.getSupportServices().stream()
                    .filter(service -> service.getCode().equals(serviceId + "_" + sysRole.getName()))
                    .map(service -> {
                        return service.getPermissions();
                    }).findFirst();

            if (permissions.isPresent()) {
                List<Long> list = permissions.get().stream().map(permission -> {
                    return permission.getId();
                }).collect(Collectors.toList());
                roleForm.setPermissions(list);
            }

            List<Long> channelIds = sysRole.getSupportChannels().stream().map(channel -> {
                return channel.getId();
            }).collect(Collectors.toList());

            List<Long> serviceIds = sysRole.getSupportServices().stream().map(service -> {
                return service.getId();
            }).collect(Collectors.toList());
            roleForm.setSupportChannels(channelIds);
            roleForm.setSupportServices(serviceIds);
            return new Result(ResultCodeTemplate.SUCCESS, roleForm);
        } else {
            return new Result(ResultCodeTemplate.OBJECT_NOT_FOUND);
        }
    }

    @RequestMapping("post")
    public Result post(String name, String description, Integer status,
                       @RequestParam(value = "channels", required = false) Set<Long> channels,
                       @RequestParam(value = "serviceCode", required = false) String serviceCode) {
        SysRole role = new SysRole();
        role.setName(name);
        role.setDescription(description);
        role.setStatus(status);
        SysRole sysRole = roleRepository.save(role);

        if (!CollectionUtils.isEmpty(channels)) {
            for (Long channelId : channels) {
                SysRoleChannel roleChannel = new SysRoleChannel();
                roleChannel.setChannelId(channelId);
                roleChannel.setRoleId(role.getId());
                roleChannelRepository.save(roleChannel);
            }
        }

        if (!StringUtils.isEmpty(serviceCode)) {
            SysService service = new SysService();
            service.setCode(serviceCode + "_" + name);
            service.setName(serviceCode);
            service.setStatus(1);
            service.setParentId(0l);
            service.setDescription(serviceCode + "的" + name + "权限");
            service.setStatus(1);
            SysService sysService = serviceRepository.save(service);

            SysRoleService roleService = new SysRoleService();
            roleService.setRoleId(sysRole.getId());
            roleService.setServiceId(sysService.getId());
            roleServiceRepository.save(roleService);
        }

        return new Result(ResultCodeTemplate.SUCCESS, role);
    }

    @RequestMapping("delete")
    public Result delete(Long roleId) {
        Optional<SysRole> optional = roleRepository.findById(roleId);
        if (optional.isPresent()) {
            SysRole role = optional.get();
            if (!ObjectUtils.isEmpty(role.getSupportChannels()) || !ObjectUtils.isEmpty(role.getSupportServices())) {
                return new Result(ResultCodeTemplate.ERROR, "角色有未删除的关联");
            }
            if (!ObjectUtils.isEmpty(userRoleRepository.findByRoleId(roleId)) || !ObjectUtils.isEmpty(deptRoleRepository.findByRoleId(roleId))) {
                return new Result(ResultCodeTemplate.ERROR, "角色有未删除的关联");
            }
            roleRepository.deleteById(roleId);
            return new Result(ResultCodeTemplate.SUCCESS);
        } else {
            return new Result(ResultCodeTemplate.OBJECT_NOT_FOUND);
        }
    }

    @RequestMapping("update")
    public Result update(Long roleId, String name, String description, Integer status,
                         @RequestParam(required = false) String serviceCode,
                         @RequestParam(value = "channels", required = false) Set<Long> channels,
                         @RequestParam(value = "services", required = false) Set<Long> services) {
        Optional<SysRole> optional = roleRepository.findById(roleId);

        if (optional.isPresent()) {
            SysRole role = optional.get();
            Optional<SysService> sysService = role.getSupportServices().stream()
                    .filter(service -> service.getCode().equals(serviceCode + "_" + role.getName()))
                    .findFirst();
            //如果没有这个service 就创建
            if (!sysService.isPresent()) {
                SysService service = sysService.get();
                if (name != null) {
                    service.setName(serviceCode);
                    service.setCode(serviceCode + "_" + name);
                    service.setDescription(serviceCode + "的" + name + "权限");
                }
                serviceRepository.save(service);
            }

            role.setName(name);
            role.setDescription(description);
            role.setStatus(status);
            roleRepository.save(role);


            if (!CollectionUtils.isEmpty(channels)) {
                roleChannelRepository.deleteByRoleId(role.getId());
                for (Long channelId : channels) {
                    SysRoleChannel roleChannel = new SysRoleChannel();
                    roleChannel.setChannelId(channelId);
                    roleChannel.setRoleId(role.getId());
                    roleChannelRepository.save(roleChannel);
                }
            }

            if (!CollectionUtils.isEmpty(services)) {
                roleServiceRepository.deleteByRoleId(role.getId());
                for (Long serviceId : services) {
                    SysRoleService roleService = new SysRoleService();
                    roleService.setServiceId(serviceId);
                    roleService.setRoleId(role.getId());
                    roleServiceRepository.save(roleService);
                }
            }
        }

        if (optional.isPresent()) {
            return new Result(ResultCodeTemplate.SUCCESS, optional.get());
        } else {
            return new Result(ResultCodeTemplate.OBJECT_NOT_FOUND);
        }
    }

}
