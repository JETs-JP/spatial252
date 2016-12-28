package com.oracle.jets.spatial252;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import oracle.spatial.network.NetworkMetadata;
import oracle.spatial.network.lod.LODNetworkException;
import oracle.spatial.network.lod.LODNetworkManager;
import oracle.spatial.network.lod.NetworkAnalyst;
import oracle.spatial.network.lod.NetworkIO;

/**
 * 地理ネットワーク関連のBeanを定義するConfigクラス
 * 
 * @author hhayakaw
 *
 */
@Configuration
class NetworkIoConfig {

    @Autowired
    private DataSource datasource;

    @Autowired
    private ApplicationContext context;

    private static final String NETWORK_NAME = "ZROAD_NET";

    private static NetworkMetadata metadata = null;

    @Bean
    NetworkIO getNetworkIo() {
        if (metadata == null) {
            loadNetworkMetadata();
        }
        try {
            return LODNetworkManager.getCachedNetworkIO(
                    datasource.getConnection(), NETWORK_NAME, NETWORK_NAME, metadata);
        } catch (SQLException | LODNetworkException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    /**
     * 
     * 
     * @param url       JDBCのURL
     * @param username  JDBCのユーザー名
     * @param password  JDBCのパスワード
     * 
     * @return
     */
    @Bean
    @Scope("prototype")
    NetworkAnalyst getNetworkAnalyst() {
        try {
            NetworkIO networkIo = context.getBean(NetworkIO.class);
            LODNetworkManager.getNetworkAnalyst(networkIo);
        } catch (LODNetworkException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
        return null;
    }

    private void loadNetworkMetadata() {
        try {
            metadata = LODNetworkManager.getNetworkMetadata(
                    datasource.getConnection(), NETWORK_NAME, NETWORK_NAME);
        } catch (SQLException | LODNetworkException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

}
