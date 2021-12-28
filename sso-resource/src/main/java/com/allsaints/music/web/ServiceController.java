package com.allsaints.music.web;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.allsaints.music.entity.SysService;
import com.allsaints.music.entity.SysServicePermission;
import com.allsaints.music.entity.repository.SysServicePermissionRepository;
import com.allsaints.music.entity.repository.SysServiceRepository;
import com.allsaints.music.http.Result;
import com.allsaints.music.http.ResultCodeTemplate;

@RestController
@RequestMapping("service")
public class ServiceController {

    @Autowired
    private SysServiceRepository serviceRepository;
    @Autowired
    private SysServicePermissionRepository permissionRepository;

    @RequestMapping("query")
    public Result query(String code, String name, Integer status) {
        Page<SysService> services = serviceRepository.findAll(PageRequest.of(0, 1));
        return new Result(ResultCodeTemplate.SUCCESS, services);
    }

    @RequestMapping("get")
    public Result get(Long serviceId) {
        Optional<SysService> optional = serviceRepository.findById(serviceId);

        if (optional.isPresent()) {
            return new Result(ResultCodeTemplate.SUCCESS, optional.get());
        } else {
            return new Result(ResultCodeTemplate.OBJECT_NOT_FOUND);
        }
    }

    @RequestMapping("post")
    public Result post(String code, String name, String description, Integer sequenceNum, Integer status,
                       @RequestParam(value = "permissions", required = false) Set<Long> permissions,
                       @RequestParam(required = false) String serviceId) {

        if (serviceId == null) {
            SysService service = new SysService();
            service.setCode(code);
            service.setName(name);
            service.setDescription(description);
            service.setSequenceNum(sequenceNum);
            service.setStatus(status);
            serviceRepository.save(service);

            if (!CollectionUtils.isEmpty(permissions)) {
                for (Long permissionId : permissions) {
                    SysServicePermission servicePermission = new SysServicePermission();
                    servicePermission.setPermissionId(permissionId);
                    servicePermission.setServiceId(service.getId());
                    permissionRepository.save(servicePermission);
                }
            }
            return new Result(ResultCodeTemplate.SUCCESS, service);
        } else {
            SysService sysService = new SysService();
            sysService.setCode(serviceId);
            Optional<SysService> optional = serviceRepository.findOne(Example.of(sysService));

            if (!CollectionUtils.isEmpty(permissions)) {
                for (Long permissionId : permissions) {
                    SysServicePermission servicePermission = new SysServicePermission();
                    servicePermission.setPermissionId(permissionId);
                    servicePermission.setServiceId(optional.get().getId());
                    permissionRepository.save(servicePermission);
                }
            }
            return new Result(ResultCodeTemplate.SUCCESS);
        }

    }

    @RequestMapping("update")
    @Transactional
    public Result update(Long serviceId, String code, String name, String description, Integer sequenceNum, Integer status,
                         @RequestParam(required = false) String serviceCode,
                         @RequestParam(value = "permissions", required = false) Set<Long> permissions) {

        if (!StringUtils.isEmpty(serviceCode)) {
            SysService service = new SysService();
            service.setCode(serviceCode);
            Optional<SysService> sysService = serviceRepository.findOne(Example.of(service));
            permissionRepository.deleteByServiceId(sysService.get().getId());
            if (!CollectionUtils.isEmpty(permissions)) {
                for (Long permissionId : permissions) {
                    SysServicePermission servicePermission = new SysServicePermission();
                    servicePermission.setPermissionId(permissionId);
                    servicePermission.setServiceId(sysService.get().getId());
                    permissionRepository.save(servicePermission);
                }
                return new Result(ResultCodeTemplate.SUCCESS);
            }
            return new Result(ResultCodeTemplate.OBJECT_NOT_FOUND);
        }

        if (serviceId != null) {
            Optional<SysService> optional = serviceRepository.findById(serviceId);

            if (optional.isPresent()) {
                SysService service = optional.get();
                service.setCode(code);
                service.setName(name);
                service.setDescription(description);
                service.setSequenceNum(sequenceNum);
                service.setStatus(status);
                serviceRepository.save(service);

                permissionRepository.deleteByServiceId(serviceId);
                if (!CollectionUtils.isEmpty(permissions)) {
                    for (Long permissionId : permissions) {
                        SysServicePermission servicePermission = new SysServicePermission();
                        servicePermission.setPermissionId(permissionId);
                        servicePermission.setServiceId(service.getId());
                        permissionRepository.save(servicePermission);
                    }
                }
                return new Result(ResultCodeTemplate.SUCCESS, optional.get());
            }
            return new Result(ResultCodeTemplate.OBJECT_NOT_FOUND);
        }
        return new Result(ResultCodeTemplate.OBJECT_NOT_FOUND);
    }

}
