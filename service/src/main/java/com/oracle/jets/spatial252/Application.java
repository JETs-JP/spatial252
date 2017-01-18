package com.oracle.jets.spatial252;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import oracle.spatial.network.NetworkMetadata;
import oracle.spatial.network.lod.NetworkIO;

@SpringBootApplication
public class Application {

    @Autowired
    private ApplicationContext context;

    private static ApplicationContext s_context;

    @PostConstruct
    void init() {
        s_context = context;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        NetworkIO networkIo = s_context.getBean(NetworkIO.class);
        printNetworkMetadata(networkIo.getNetworkMetadata());
    }

    private static void printNetworkMetadata(NetworkMetadata meta) {
        System.out.println(" +---[Network Metadata]-----------------------------------------+");
        System.out.println(" |  NodeTable:" + meta.getNodeTableName(true));
        System.out.println(" |  linkTable:" + meta.getLinkTableName(true));
        System.out.println(" |  pathLinkTable:" + meta.getPathLinkTableName(true));
        System.out.println(" |  Directed? " + meta.isDirected());
        System.out.println(" |  Hierarchical? " + meta.isHierarchical() );
        System.out.println(" |  Logical? " + meta.isLogical());
        System.out.println(" |  Spatial? " + meta.isSpatial());
        System.out.println(" |  Geodetic? " + meta.isGeodetic());
        System.out.println(" |  SDOGeometry? " + meta.isSDOGeometry());
        System.out.println(" |  Versioned? " + meta.isVersioned());
        System.out.println(" +--------------------------------------------------------------+");        
    }

}