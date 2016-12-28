package com.oracle.jets.spatial252.searcher;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;

/**
 * データソースのBeanを定義するConfigクラス
 * 
 * @author hhayakaw
 *
 */
@Configuration
@PropertySource("classpath:jdbc.properties")
public class PoolingDataSourceConfig {

    /**
     * データソースを取得する
     * 
     * Spring Bootが提供するコネクションプールを利用すると、Spatialの
     * 機能を呼び出す際にエラーとなるため、ここで独自のコネクションプール
     * を作成している
     * 
     * @param url       JDBCのURL
     * @param username  JDBCのユーザー名
     * @param password  JDBCのパスワード
     *
     * @return データソース
     */
    @Bean
    public DataSource dataSource(
            @Value("${database.url}") String url,
            @Value("${database.username}") String username,
            @Value("${database.password}") String password) {
        PoolDataSource pds = PoolDataSourceFactory.getPoolDataSource();
        try {
            pds.setConnectionFactoryClassName("oracle.jdbc.pool.OracleDataSource");
            pds.setURL(url);
            pds.setUser(username);
            pds.setPassword(password);
            pds.setInitialPoolSize(5);
            return pds;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

}
