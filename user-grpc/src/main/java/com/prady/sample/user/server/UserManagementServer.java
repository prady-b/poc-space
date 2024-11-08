package com.prady.sample.user.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserManagementServer {
    private static final Logger logger = LoggerFactory.getLogger(UserManagementServer.class);
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(PORT)
                .addService(new UserManagementServiceImpl())
                .build()
                .start();
        logger.info("Server started, listening on {}", PORT);
        server.awaitTermination();
    }
}
