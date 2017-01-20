package com.oracle.jets.spatial252.service.oracle_spatial;

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
import oracle.spatial.network.lod.NetworkUpdate;

/**
 * 地理ネットワーク関連のBeanを定義するConfigクラス
 * 
 * @author hhayakaw
 *
 */
@Configuration
class NetworkIoConfig {

    /**
     * データソース
     */
    @Autowired
    private DataSource datasource;
    /**
     * Springのアプリケーション・コンテキスト
     */
    @Autowired
    private ApplicationContext context;
    /**
     * NetworkMetadataのキャッシュ
     */
    private static NetworkMetadata metadata = null;
    /**
     * Spatialデータベースのネットワーク名
     */
    private static final String NETWORK_NAME = "ZROAD_NET";

    /**
     * このアプリケーション唯一のNetworkIOオブジェクトを取得する。
     * 初めてNetworkIOオブジェクトを取得する際、NetworkMetadataがロードされ、以降
     * 単一のNetworkIOオブジェクトを使いまわす
     * 
     * @return NetworkIOのインスタンス
     */
    @Bean
    @Scope("singleton")
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
     * このアプリケーション唯一のNetworkAnalystオブジェクト
     * 
     * @return NetworkAnalystのインスタンス
     */
    @Bean
    @Scope("singleton")
    NetworkAnalyst getNetworkAnalyst() {
        try {
            NetworkIO networkIo = context.getBean(NetworkIO.class);
            return LODNetworkManager.getNetworkAnalyst(networkIo);
        } catch (LODNetworkException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    /**
     * このアプリケーション唯一のNetworkUpdateオブジェクト。
     * このデモアプリではネットワークの更新を永続化せず、このNetworkUpdate
     * オブジェクトを使ってメモリ内で管理する。
     * 
     * @return NetworkUpdateのインスタンス
     */
    @Bean
    @Scope("singleton")
    NetworkUpdate getManagedNetworkUpdate() {
        return new NetworkUpdate();
    }

    @Bean
    @Scope("singleton")
    DisabledPolygonsCache getDisabledPolygonsCache() {
        return DisabledPolygonsCache.getInstance();
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
