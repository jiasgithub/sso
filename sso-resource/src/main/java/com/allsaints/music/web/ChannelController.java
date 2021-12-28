package com.allsaints.music.web;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.allsaints.music.entity.SysChannel;
import com.allsaints.music.entity.SysChannelRegion;
import com.allsaints.music.entity.repository.SysChannelRegionRepository;
import com.allsaints.music.entity.repository.SysChannelRepository;
import com.allsaints.music.http.Result;
import com.allsaints.music.http.ResultCodeTemplate;

@RestController
@RequestMapping("channel")
public class ChannelController {

    @Autowired
    private SysChannelRepository channelRepository;
    @Autowired
    private SysChannelRegionRepository channelRegionRepository;

    @RequestMapping("query")
    public Result query(String code, String name, Integer status, Long regionId) {
        List<SysChannel> channels = channelRepository.findAll();
        return new Result(ResultCodeTemplate.SUCCESS, channels);
    }

    @RequestMapping("get")
    public Result get(Long channelId) {
        Optional<SysChannel> optional = channelRepository.findById(channelId);

        if (optional.isPresent()) {
            return new Result(ResultCodeTemplate.SUCCESS, optional.get());
        } else {
            return new Result(ResultCodeTemplate.OBJECT_NOT_FOUND);
        }
    }

    @RequestMapping("post")
    public Result post(String code, String name, String description, Integer sequenceNum, Integer status, @RequestParam(value = "regions", required = false) Set<Long> regions) {
        SysChannel channel = new SysChannel();
        channel.setCode(code);
        channel.setName(name);
        channel.setDescription(description);
        channel.setStatus(status);
        channelRepository.save(channel);

        if (!CollectionUtils.isEmpty(regions)) {
            for (Long regionId : regions) {
                SysChannelRegion channelRegion = new SysChannelRegion();
                channelRegion.setChannelId(channel.getId());
                channelRegion.setRegionId(regionId);
                channelRegionRepository.save(channelRegion);
            }
        }

        return new Result(ResultCodeTemplate.SUCCESS, channel);
    }

    @RequestMapping("update")
    @Transactional
    public Result update(Long channelId, String code, String description, Integer sequenceNum, Integer status, @RequestParam(value = "regions", required = false) Set<Long> regions) {
        Optional<SysChannel> optional = channelRepository.findById(channelId);

        if (optional.isPresent()) {
            SysChannel channel = optional.get();
            channel.setCode(code);
            channel.setDescription(description);
            channel.setStatus(status);
            channelRepository.save(channel);

            channelRegionRepository.deleteByChannelId(channelId);

            if (!CollectionUtils.isEmpty(regions)) {
                for (Long regionId : regions) {
                    SysChannelRegion newChannelRegion = new SysChannelRegion();
                    newChannelRegion.setChannelId(channel.getId());
                    newChannelRegion.setRegionId(regionId);
                    channelRegionRepository.save(newChannelRegion);
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
