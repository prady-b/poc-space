package com.prady.sample.user.server;

import io.grpc.stub.StreamObserver;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.prady.sample.user.UserServiceGrpc;
import com.prady.sample.user.User.CreateUserRequest;
import com.prady.sample.user.User.GetUserRequest;
import com.prady.sample.user.User.UpdateUserRequest;
import com.prady.sample.user.User.DeleteUserRequest;
import com.prady.sample.user.User.UserResponse;

public class UserManagementServiceImpl extends UserServiceGrpc.UserServiceImplBase {

    private static final Logger logger = LoggerFactory.getLogger(UserManagementServiceImpl.class);
    private Map<String, CreateUserRequest> users = new HashMap<>();

    @Override
    public void createUser(CreateUserRequest request, StreamObserver<UserResponse> responseObserver) {
        String username = request.getUsername();
        String userId = UUID.randomUUID().toString();
        if (users.containsKey(username)) {
            logger.info("User already exists: {}", username);
            responseObserver.onNext(UserResponse.newBuilder().setId(userId).setUsername(username).setEmail(request.getEmail()).build());
        } else {
            users.put(username, request);
            logger.info("User created: {}", username);
            responseObserver.onNext(UserResponse.newBuilder().setId(userId).setUsername(username).setEmail(request.getEmail()).build());
        }
        responseObserver.onCompleted();
    }

    @Override
    public void getUser(GetUserRequest request, StreamObserver<UserResponse> responseObserver) {
        String username = request.getUsername();
        CreateUserRequest user = users.get(username);
        if (user == null) {
            logger.info("User not found: {}", username);
            responseObserver.onNext(UserResponse.newBuilder().setId("").setUsername("").setEmail("").build());
        } else {
            logger.info("User retrieved: {}", username);
            responseObserver.onNext(UserResponse.newBuilder().setId(UUID.randomUUID().toString()).setUsername(user.getUsername()).setEmail(user.getEmail()).build());
        }
        responseObserver.onCompleted();
    }

    @Override
    public void updateUser(UpdateUserRequest request, StreamObserver<UserResponse> responseObserver) {
        String username = request.getUsername();
        if (users.containsKey(username)) {
            users.put(username, CreateUserRequest.newBuilder().setUsername(username).setEmail(request.getEmail()).setPassword(request.getPassword()).build());
            logger.info("User updated: {}", username);
            responseObserver.onNext(UserResponse.newBuilder().setId(UUID.randomUUID().toString()).setUsername(username).setEmail(request.getEmail()).build());
        } else {
            logger.info("User not found for update: {}", username);
            responseObserver.onNext(UserResponse.newBuilder().setId(UUID.randomUUID().toString()).setUsername(username).setEmail(request.getEmail()).build());
        }
        responseObserver.onCompleted();
    }

    @Override
    public void deleteUser(DeleteUserRequest request, StreamObserver<UserResponse> responseObserver) {
        String username = request.getUsername();
        if (users.remove(username) != null) {
            logger.info("User deleted: {}", username);
            responseObserver.onNext(UserResponse.newBuilder().setId(UUID.randomUUID().toString()).setUsername(username).setEmail("").build());
        } else {
            logger.info("User not found for deletion: {}", username);
            responseObserver.onNext(UserResponse.newBuilder().setId(UUID.randomUUID().toString()).setUsername(username).setEmail("").build());
        }
        responseObserver.onCompleted();
    }
}
