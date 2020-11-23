package com.aimsphm.nuclear.opc.client;


import lombok.extern.slf4j.Slf4j;
import org.jinterop.dcom.common.JIException;
import org.openscada.opc.dcom.list.ClassDetails;
import org.openscada.opc.lib.common.ConnectionInformation;
import org.openscada.opc.lib.da.Server;
import org.openscada.opc.lib.da.ServerConnectionStateListener;
import org.openscada.opc.lib.list.Categories;
import org.openscada.opc.lib.list.Category;
import org.openscada.opc.lib.list.ServerList;

import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Slf4j
public class OpcClient extends Observable {

    public static Server server = null;

    /**
     * 连接服务
     *
     * @param host
     * @param progId
     * @param username
     * @param password
     * @param domain
     * @return
     */
    public synchronized boolean connectServer(String host, String progId, String username, String password, String domain) {
        if (Objects.nonNull(server)) {
            return true;
        }
        boolean mState = false;
        ServerList serverList = null;
        try {
            serverList = new ServerList(host, username, password, domain);
            ConnectionInformation connectionInformation = new ConnectionInformation();
            connectionInformation.setClsid(serverList.getClsIdFromProgId(progId));
            connectionInformation.setDomain(domain);
            connectionInformation.setPassword(password);
            connectionInformation.setUser(username);
            connectionInformation.setHost(host);
            ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
            server = new Server(connectionInformation, executorService);
            server.connect();
            server.addStateListener(new ServerConnectionStateListener() {
                @Override
                public void connectionStateChanged(boolean state) {
                    log.info("connectionStateChanged state=" + state);
                }
            });

            mState = true;
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            if (!mState) {
                server = null;
            }
        }
        return mState;
    }

    public synchronized void disconnectServer() {
        if (server == null) {
            return;
        }
        server.disconnect();
        server = null;
    }


    public void showAllOpcServer(String host, String username, String password, String domain) {
        try {
            ServerList serverList = new ServerList(host, username, password, domain);
            Collection<ClassDetails> details = serverList.listServersWithDetails(new Category[]{Categories.OPCDAServer10, Categories.OPCDAServer20, Categories.OPCDAServer30}, new Category[]{});
            for (ClassDetails detail : details) {
                log.info("ClsId=" + detail.getClsId() + " ProgId=" + detail.getProgId() + " Description=" + detail.getDescription());
            }
        } catch (UnknownHostException e) {
            log.error(e.getMessage());
        } catch (JIException e) {
            log.error(e.getMessage());
        }
    }

    public Server getServer() {
        return server;
    }

    /**
     * 注册
     */
    public void subscribe(Observer observer) {
        this.addObserver(observer);
    }

}
