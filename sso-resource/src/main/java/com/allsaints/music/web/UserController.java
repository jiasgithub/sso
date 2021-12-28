package com.allsaints.music.web;

import java.util.*;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;

import com.allsaints.music.extentity.TblCpoaUser;
import com.allsaints.music.extentity.repository.TblCpoaUserRepository;
import com.allsaints.music.http.form.SysPermissionForm;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.allsaints.music.entity.SysPermission;
import com.allsaints.music.entity.SysRole;
import com.allsaints.music.entity.SysService;
import com.allsaints.music.entity.SysUser;
import com.allsaints.music.entity.SysUserRole;
import com.allsaints.music.entity.repository.SysRoleRepository;
import com.allsaints.music.entity.repository.SysServiceRepository;
import com.allsaints.music.entity.repository.SysUserRepository;
import com.allsaints.music.entity.repository.SysUserRoleRepository;
import com.allsaints.music.http.Result;
import com.allsaints.music.http.ResultCodeTemplate;

@Validated
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private SysUserRepository userRepository;
    @Autowired
    private SysUserRoleRepository userRoleRepository;
    @Autowired
    private SysRoleRepository roleRepository;
    @Autowired
    private TblCpoaUserRepository cpoaUserRepository;

    @RequestMapping("info")
    public Result info(Authentication auth) {
        Map<String, Object> info = new HashMap<>();
        TblCpoaUser cpoaUser = cpoaUserRepository.findByUsername(auth.getName());
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();

        info.put("name", auth.getName());
        info.put("roles", authorities.stream().map(grantedAuthority -> grantedAuthority.getAuthority()));
        authorities.stream().forEach(authority -> {
            if (((GrantedAuthority) authority).getAuthority().equals("CPOA")) {
                info.put("CPID", cpoaUser.getId());
            }
        });

        return new Result(ResultCodeTemplate.SUCCESS, info);
    }

    @RequestMapping("menus")
    public Result menus(Authentication auth, @NotBlank String serviceId) {
        Set<SysPermissionForm> menus = new HashSet<>();

        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities) {
            String serviceCode = serviceId + "_" + grantedAuthority.getAuthority();
            SysRole role = new SysRole();
            role.setStatus(1);
            role.setName(grantedAuthority.getAuthority());
            Optional<SysRole> optional = roleRepository.findOne(Example.of(role));
            optional.ifPresent(_role -> {
                List<SysService> services = _role.getSupportServices();
                if (!ObjectUtils.isEmpty(services)) {
                    Optional<SysService> serviceOptional = services.stream().filter(pp -> pp.getCode().startsWith(serviceCode)).findFirst();
                    if (serviceOptional.isPresent()) {
                        List<SysPermission> permissions = serviceOptional.get().getPermissions();
                        List<SysPermissionForm> list = permissions.stream().map(permission -> {
                            SysPermissionForm form = new SysPermissionForm();
                            BeanUtils.copyProperties(permission, form);
                            return form;
                        }).collect(Collectors.toList());

                        Set<SysPermissionForm> parentList = list.stream()
                                .filter(permission -> permission.getTop() == 0)
                                .collect(Collectors.toSet());

                        Set<SysPermissionForm> subList = list.stream()
                                .filter(permission -> permission.getTop() == 1)
                                .collect(Collectors.toSet());
                        List<SysPermissionForm> parentSort = parentList.stream().sorted(Comparator.comparing(SysPermissionForm::getOrderNum)).collect(Collectors.toList());
                        List<SysPermissionForm> subSort = subList.stream().sorted(Comparator.comparing(SysPermissionForm::getOrderNum)).collect(Collectors.toList());

                        parentSort.stream().forEach(parSort -> {
                            List<SysPermissionForm> subPermissions = subSort.stream()
                                    .filter(sub -> sub.getParentId().equals(parSort.getId()))
                                    .collect(Collectors.toList());
                            parSort.setPermissions(subPermissions);
                        });
                        menus.addAll(parentSort);
                    }
                }
            });
        }
        TreeSet<SysPermissionForm> permissions = new TreeSet<>(((o1, o2) -> Integer.compare(o1.getOrderNum(), o2.getOrderNum())));
        permissions.addAll(menus);
        return new Result(ResultCodeTemplate.SUCCESS, permissions);
    }

    @RequestMapping("profile")
    public Result profile(Long userId) {
        Optional<SysUser> optional = userRepository.findById(userId);

        if (optional.isPresent()) {
            return new Result(ResultCodeTemplate.SUCCESS, optional.get());
        } else {
            return new Result(ResultCodeTemplate.OBJECT_NOT_FOUND);
        }
    }

    @RequestMapping("query")
    public Result query(@PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
                        @RequestParam(required = false) String name,
                        @RequestParam(required = false) Integer status, Long deptId) {
        SysUser user = new SysUser();
        if (!StringUtils.isEmpty(name)) user.setUsername(name);
        if (status != null) user.setStatus(status);
        if (deptId != null) user.setDeptId(deptId);

        Page<SysUser> users = userRepository.findAll(Example.of(user), pageable);
        return new Result(ResultCodeTemplate.SUCCESS, users);
    }

    @RequestMapping("get")
    public Result get(Long userId) {
        Optional<SysUser> optional = userRepository.findById(userId);

        if (optional.isPresent()) {
            SysUser user = optional.get();
            if (StringUtils.isEmpty(user.getNickname())) {
                user.setNickname("");
            }
            return new Result(ResultCodeTemplate.SUCCESS, user);
        } else {
            return new Result(ResultCodeTemplate.OBJECT_NOT_FOUND);
        }
    }

    @RequestMapping("post")
    public Result post(String username, String nickname, String password, String phone, Long deptId, String email, Integer adminFlag, Integer superFlag, Integer lockStatus, Integer status, @RequestParam(value = "roles", required = false) Set<Long> roles) {
        SysUser sysUser = userRepository.getByUsername(username);
        TblCpoaUser cpoaUser = cpoaUserRepository.findByUsername(username);
        if (!ObjectUtils.isEmpty(sysUser) || !ObjectUtils.isEmpty(cpoaUser)) {
            return new Result(ResultCodeTemplate.BAD_REQUEST, "用户名重复");
        }

        SysUser user = new SysUser();
        user.setUsername(username);
        user.setNickname(nickname);
        user.setPassword(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(password));
        user.setPhone(phone);
        user.setDeptId(deptId);
        user.setEmail(email);
        user.setAdminFlag(adminFlag);
        user.setSuperFlag(superFlag);
        user.setLockStatus(lockStatus);
        user.setStatus(status);
        userRepository.save(user);

        if (!CollectionUtils.isEmpty(roles)) {
            for (Long roleId : roles) {
                SysUserRole userRole = new SysUserRole();
                userRole.setUserId(user.getId());
                userRole.setRoleId(roleId);
                userRoleRepository.save(userRole);
            }
        }

        return new Result(ResultCodeTemplate.SUCCESS, user);
    }

    @RequestMapping("update")
    @Transactional
    public Result update(Long userId, String username, String nickname, String password, String phone, Long deptId, String email, Integer adminFlag, Integer superFlag, Integer lockStatus, Integer status, @RequestParam(value = "roles", required = false) Set<Long> roles) {
        Optional<SysUser> optional = userRepository.findById(userId);

        SysUser sysUser = userRepository.getByUsername(username);
//        TblCpoaUser cpoaUser = cpoaUserRepository.findByUsername(username);
//        if (!ObjectUtils.isEmpty(sysUser) || !ObjectUtils.isEmpty(cpoaUser)) {
//            return new Result(ResultCodeTemplate.BAD_REQUEST, "用户名重复");
//        }

        if (optional.isPresent()) {
            SysUser user = optional.get();
            user.setUsername(username);
            user.setNickname(nickname);
            if (!StringUtils.isEmpty(password)) {
                user.setPassword(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(password));
            }
            user.setPhone(phone);
            user.setDeptId(deptId);
            user.setEmail(email);
            user.setAdminFlag(adminFlag);
            user.setSuperFlag(superFlag);
            user.setLockStatus(lockStatus);
            user.setStatus(status);
            userRepository.save(user);

            userRoleRepository.deleteByUserId(userId);

            if (!CollectionUtils.isEmpty(roles)) {
                for (Long roleId : roles) {
                    SysUserRole userRole = new SysUserRole();
                    userRole.setUserId(user.getId());
                    userRole.setRoleId(roleId);
                    userRoleRepository.save(userRole);
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
