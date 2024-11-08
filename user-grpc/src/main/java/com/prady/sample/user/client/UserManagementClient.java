package com.prady.sample.user.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import com.prady.sample.user.UserServiceGrpc.UserServiceBlockingStub;
import com.prady.sample.user.User.CreateUserRequest;
import com.prady.sample.user.User.GetUserRequest;
import com.prady.sample.user.User.UpdateUserRequest;
import com.prady.sample.user.User.DeleteUserRequest;
import com.prady.sample.user.User.UserResponse;
import com.prady.sample.user.UserServiceGrpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserManagementClient {
    private static final Logger logger = LoggerFactory.getLogger(UserManagementClient.class);

    private static final String USERNAME = "john_doe";

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build();
        UserServiceBlockingStub stub = UserServiceGrpc.newBlockingStub(channel);
        CreateUserRequest createUserRequest = CreateUserRequest.newBuilder()
                .setUsername(USERNAME)
                .setEmail("john.doe@example.com")
                .setPassword("password123")
                .build();
        UserResponse createUserResponse = stub.createUser(createUserRequest);
        logger.info("User created with ID: {} and username: {}", createUserResponse.getId(), createUserResponse.getUsername());

        // Example: Get a user
        GetUserRequest getUserRequest = GetUserRequest.newBuilder()
                .setUsername(USERNAME)
                .build();
        UserResponse getUserResponse = stub.getUser(getUserRequest);
        logger.info("User retrieved with ID: {} and username: {}", getUserResponse.getId(), getUserResponse.getUsername());

        // Example: Update a user
        UpdateUserRequest updateUserRequest = UpdateUserRequest.newBuilder()
                .setUsername(USERNAME)
                .setEmail("john.doe@newdomain.com")
                .setPassword("newpassword123")
                .build();
        UserResponse updateUserResponse = stub.updateUser(updateUserRequest);
        logger.info("User updated with ID: {} and username: {}", updateUserResponse.getId(), updateUserResponse.getUsername());

        // Example: Delete a user
        DeleteUserRequest deleteUserRequest = DeleteUserRequest.newBuilder()
                .setUsername(USERNAME)
                .build();
        UserResponse deleteUserResponse = stub.deleteUser(deleteUserRequest);
        logger.info("User deleted with ID: {} and username: {}", deleteUserResponse.getId(), deleteUserResponse.getUsername());

        channel.shutdown();
    }
}
