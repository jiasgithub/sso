package com.allsaints.music.web;

import java.util.*;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;

import com.allsaints.music.entity.SysService;
import com.allsaints.music.entity.SysServicePermission;
import com.allsaints.music.entity.repository.SysServicePermissionRepository;
import com.allsaints.music.http.form.SysPermissionForm;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.allsaints.music.entity.SysPermission;
import com.allsaints.music.entity.repository.SysPermissionRepository;
import com.allsaints.music.entity.repository.SysServiceRepository;
import com.allsaints.music.http.Result;
import com.allsaints.music.http.ResultCodeTemplate;

@RestController
@RequestMapping("menu")
public class MenuController {

    @Autowired
    private SysPermissionRepository permissionRepository;
    @Autowired
    private SysServiceRepository serviceRepository;
    @Autowired
    private SysServicePermissionRepository servicePermissionRepository;

    /**
     * @param pageable
     * @param name
     * @param top
     * @param status
     * @param serviceId 主键
     * @return
     */
    @RequestMapping("query")
    public Result query(Pageable pageable, String name, Integer top, Integer status, @NotBlank String serviceId) {
        SysPermission permission = new SysPermission();
        if (!ObjectUtils.isEmpty(name)) permission.setName(name);
        if (!ObjectUtils.isEmpty(top)) permission.setTop(top);
        if (!ObjectUtils.isEmpty(status)) permission.setStatus(status);

        if (!StringUtils.isEmpty(serviceId)) {
            SysService sysService = new SysService();
            sysService.setCode(serviceId);
            sysService.setStatus(1);
            Optional<SysService> service = serviceRepository.findOne(Example.of(sysService));
            if (service.isPresent()) {
//                List<SysPermission> permissionList = service.get().getPermissions().stream()
//                        .filter(permissions -> permissions.getParentId() == 0)
//                        .map(permissions -> {
//                            return permissions;
//                        }).collect(Collectors.toList());

                List<SysPermission> permissions = service.get().getPermissions();
                List<SysPermissionForm> list = permissions.stream().map(per -> {
                    SysPermissionForm form = new SysPermissionForm();
                    BeanUtils.copyProperties(per, form);
                    return form;
                }).collect(Collectors.toList());

                Set<SysPermissionForm> parentList = list.stream()
                        .filter(per -> per.getTop() == 0)
                        .collect(Collectors.toSet());

                Set<SysPermissionForm> subList = list.stream()
                        .filter(per -> per.getTop() == 1)
                        .collect(Collectors.toSet());
                List<SysPermissionForm> parentSort = parentList.stream().sorted(Comparator.comparing(SysPermissionForm::getOrderNum)).collect(Collectors.toList());
                List<SysPermissionForm> subSort = subList.stream().sorted(Comparator.comparing(SysPermissionForm::getOrderNum)).collect(Collectors.toList());

                parentSort.stream().forEach(parSort -> {
                    List<SysPermissionForm> subPermissions = subSort.stream()
                            .filter(sub -> sub.getParentId().equals(parSort.getId()))
                            .collect(Collectors.toList());
                    parSort.setPermissions(subPermissions);
                });
                return new Result(ResultCodeTemplate.SUCCESS, parentSort);
            }
            return new Result(ResultCodeTemplate.OBJECT_NOT_FOUND);
        }

        Page<SysPermission> permissions = permissionRepository.findAll(Example.of(permission), pageable);
        return new Result(ResultCodeTemplate.SUCCESS, permissions);
    }

    @RequestMapping("get")
    public Result get(Long permissionId) {
        Optional<SysPermission> optional = permissionRepository.findById(permissionId);

        if (optional.isPresent()) {
            return new Result(ResultCodeTemplate.SUCCESS, optional.get());
        } else {
            return new Result(ResultCodeTemplate.OBJECT_NOT_FOUND);
        }
    }

    @RequestMapping("post")
    public Result post(Long parentId, String title, String name, String icon, String resource, Integer orderNum, Integer status, String description, @NotBlank String serviceId) {
        Optional<SysPermission> optional = permissionRepository.findById(parentId);
        SysPermission permission = new SysPermission();
        permission.setParentId(parentId);
        permission.setTop(0);
        permission.setTitle(title);
        permission.setName(name);
        permission.setIcon(icon);
        permission.setResource(resource);
        permission.setOrderNum(orderNum);
        permission.setStatus(status);
        permission.setDescription(description);
        optional.ifPresent(consumer -> {
            permission.setTop(consumer.getTop() + 1);
        });
        permissionRepository.save(permission);

        if (!ObjectUtils.isEmpty(serviceId)) {
            SysService sysService = new SysService();
            sysService.setCode(serviceId);
            List<SysService> services = serviceRepository.findAll(Example.of(sysService));
            services.stream().forEach(service -> {
                SysServicePermission sysServicePermission = new SysServicePermission();
                sysServicePermission.setPermissionId(permission.getId());
                sysServicePermission.setServiceId(service.getId());
                servicePermissionRepository.save(sysServicePermission);
            });
        }

        return new Result(ResultCodeTemplate.SUCCESS);
    }

    @RequestMapping("delete")
    public Result post(Long permissionId) {
        Optional<SysPermission> optional = permissionRepository.findById(permissionId);
        if (optional.isPresent()) {
            SysPermission consumer = optional.get();
            if (ObjectUtils.isEmpty(consumer.getPermissions())) {
                permissionRepository.delete(consumer);
                return new Result(ResultCodeTemplate.SUCCESS);
            } else {
                return new Result(ResultCodeTemplate.ERROR, "子菜单不为空");
            }
        } else {
            return new Result(ResultCodeTemplate.OBJECT_NOT_FOUND);
        }
    }

    @RequestMapping("update")
    public Result update(Long permissionId, Long parentId, String title, String name, String icon, String resource, Integer orderNum, Integer status, String description) {
        Optional<SysPermission> optional = permissionRepository.findById(permissionId);

        if (optional.isPresent()) {
            SysPermission permission = optional.get();
            permission.setParentId(parentId);
            permission.setTitle(title);
            permission.setName(name);
            permission.setIcon(icon);
            permission.setResource(resource);
            permission.setOrderNum(orderNum);
            permission.setStatus(status);
            permission.setDescription(description);

            permissionRepository.save(permission);
        }

        if (optional.isPresent()) {
            return new Result(ResultCodeTemplate.SUCCESS, optional.get());
        } else {
            return new Result(ResultCodeTemplate.OBJECT_NOT_FOUND);
        }
    }

}
