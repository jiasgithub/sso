package com.allsaints.music.web;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.allsaints.music.entity.SysDept;
import com.allsaints.music.entity.SysDeptRole;
import com.allsaints.music.entity.repository.SysDeptRepository;
import com.allsaints.music.entity.repository.SysDeptRoleRepository;
import com.allsaints.music.http.Result;
import com.allsaints.music.http.ResultCodeTemplate;

@RestController
@RequestMapping("dept")
public class DeptController {

    @Autowired
    private SysDeptRepository deptRepository;
    @Autowired
    private SysDeptRoleRepository deptRoleRepository;

    @RequestMapping("query")
    public Result query(Pageable pageable, String name, Integer status, Long roleId) {
        SysDept dept = new SysDept();
        if (!ObjectUtils.isEmpty(name)) dept.setName(name);
        if (!ObjectUtils.isEmpty(status)) dept.setStatus(status);

        Page<SysDept> depts = deptRepository.findAll(Example.of(dept), pageable);
        return new Result(ResultCodeTemplate.SUCCESS, depts);
    }

    @RequestMapping("get")
    public Result get(Long deptId) {
        Optional<SysDept> optional = deptRepository.findById(deptId);

        if (optional.isPresent()) {
            return new Result(ResultCodeTemplate.SUCCESS, optional.get());
        } else {
            return new Result(ResultCodeTemplate.OBJECT_NOT_FOUND);
        }
    }

    @RequestMapping("post")
    public Result post(String name, String description, Integer sequenceNum, Integer status, @RequestParam(value = "roles", required = false) Set<Long> roles) {
        SysDept dept = new SysDept();
        dept.setName(name);
        dept.setDescription(description);
        dept.setStatus(status);
        deptRepository.save(dept);

        if (!CollectionUtils.isEmpty(roles)) {
            for (Long roleId : roles) {
                SysDeptRole deptRole = new SysDeptRole();
                deptRole.setDeptId(dept.getId());
                deptRole.setRoleId(roleId);
                deptRoleRepository.save(deptRole);
            }
        }

        return new Result(ResultCodeTemplate.SUCCESS, dept);
    }

    @RequestMapping("delete")
    public Result delete(Long deptId) {
        Optional<SysDept> optional = deptRepository.findById(deptId);

        if (optional.isPresent()) {
            SysDept dept = optional.get();
            if (ObjectUtils.isEmpty(dept.getSupportRoles())) {
                deptRepository.deleteById(deptId);
                return new Result(ResultCodeTemplate.SUCCESS);
            }
            return new Result(ResultCodeTemplate.ERROR, "有未删除的关联");
        } else {
            return new Result(ResultCodeTemplate.OBJECT_NOT_FOUND);
        }
    }

    @RequestMapping("update")
    public Result update(Long deptId, String name, String description, Integer sequenceNum, Integer status, @RequestParam(value = "roles", required = false) Set<Long> roles) {
        Optional<SysDept> optional = deptRepository.findById(deptId);

        if (optional.isPresent()) {
            SysDept dept = new SysDept();
            dept.setName(name);
            dept.setDescription(description);
            dept.setStatus(status);
            deptRepository.save(dept);

            deptRepository.deleteById(deptId);
            deptRoleRepository.deleteByDeptId(deptId);

            if (!CollectionUtils.isEmpty(roles)) {
                for (Long roleId : roles) {
                    SysDeptRole deptRole = new SysDeptRole();
                    deptRole.setDeptId(deptId);
                    deptRole.setRoleId(roleId);
                    deptRoleRepository.save(deptRole);
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
