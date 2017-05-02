package com.chinasofti.ark.bdadp.component.support;

import com.chinasofti.ark.bdadp.component.api.channel.Channel;

import java.util.Collection;

/**
 * Created by White on 2017/1/17.
 */
public interface ChannelInputable {

    void addIChannel(Channel e);

    Collection<Channel> getIChannel();
}
