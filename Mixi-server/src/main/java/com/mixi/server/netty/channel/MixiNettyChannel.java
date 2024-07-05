package com.mixi.server.netty.channel;

import java.net.InetSocketAddress;
import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentHashMap;

import com.mixi.server.netty.channel.support.ChannelAttrs;
import io.netty.channel.Channel;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description
 * @Author welsir
 * @Date 2024/7/1 1:26
 */
public class MixiNettyChannel implements MixiChannelManager{

    private static final Logger log = LoggerFactory.getLogger(MixiNettyChannel.class);
    private static final ConcurrentHashMap<String, MixiNettyChannel> CHANNEL_MAP = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String,Object> attributes = new ConcurrentHashMap<>();
    private Channel channel;
    private boolean close;

    public MixiNettyChannel(Channel channel){
        this.channel = channel;
    }

    public static MixiNettyChannel registerChannelIfAbsent(@NonNull Channel ch) {
        String channelId = ChannelAttrs.getChannelId(ch);
        MixiNettyChannel nettyChannel = CHANNEL_MAP.get(channelId);
        if(nettyChannel==null){
            //TODO:后续考虑并发问题
            MixiNettyChannel newChannel = new MixiNettyChannel(ch);
            if(ch.isActive()){
                CHANNEL_MAP.putIfAbsent(channelId,newChannel);
            }
            nettyChannel=newChannel;
        }
        return nettyChannel;
    }

    public static void removeChannel(@NonNull Channel channel) {
        MixiNettyChannel nettyChannel = CHANNEL_MAP.remove(ChannelAttrs.getChannelId(channel));
    }

    public String getChannelId() {
        return ChannelAttrs.getAttrs(channel).getChannelId();
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return (InetSocketAddress) channel.localAddress();
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return (InetSocketAddress) channel.remoteAddress();
    }

    @Override
    public boolean isConnected() {
        return !channel.isActive()&&close;
    }

    @Override
    public void send(Object message) throws RemoteException {
        if(!this.isConnected()){
            log.error("Netty channel is close :[channelId:"+attributes);
        }
    }

    @Override
    public void close() {
        channel.close();
        this.close = true;
    }

    @Override
    public boolean isClosed() {
        return this.close;
    }

    @Override
    public boolean hasAttribute(String key) {
        return false;
    }

    @Override
    public Object getAttribute(String key) {
        return null;
    }

    @Override
    public void setAttribute(String key, Object value) {

    }

    @Override
    public Object removeAttribute(String key) {
        return null;
    }
}