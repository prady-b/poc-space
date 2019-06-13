/**
 *
 */

package com.prady.sample.reactive.service;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.prady.sample.reactive.domain.UserAccount;
import com.prady.sample.reactive.dto.UserAccountDTO;
import com.prady.sample.reactive.exception.ItemAlreadyExistsException;
import com.prady.sample.reactive.exception.ItemNotFoundException;
import com.prady.sample.reactive.mapper.UserAccountMapper;
import com.prady.sample.reactive.repository.UserAccountRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Prady
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserAccountRepository userAccountRepository;
    @Autowired
    private UserAccountMapper userAccountMapper;

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private static final UserAccount PRESENT = new UserAccount();

    /*
     * (non-Javadoc)
     * @see com.prady.sample.microservices.userservice.service.UserService#getUsers()
     */
    @Override
    public Flux<UserAccountDTO> getUsers() {
        log.info("Getting All Users ");
        Flux<UserAccount> userAccounts = userAccountRepository.findAll();
        return userAccounts.map(userAccountMapper::toUserAccountDTO);
    }

    /*
     * (non-Javadoc)
     * @see com.prady.sample.microservices.userservice.service.UserService#getUser(java.lang.String)
     */
    @Override
    public Mono<UserAccountDTO> getUser(String id) {
        Assert.notNull(id, "User Id can not be null !!!");
        return userAccountRepository.findById(id).map(userAccountMapper::toUserAccountDTO)
                .switchIfEmpty(Mono.error(new ItemNotFoundException(id, "User")));
    }

    /*
     * (non-Javadoc)
     * @see
     * com.prady.sample.microservices.userservice.service.UserService#create(com.prady.sample.microservices.common.dto.user.UserAccountDTO)
     */
    @Override
    public Mono<UserAccountDTO> create(Mono<UserAccountDTO> userAccount) {

        return userAccount.flatMap(userAccountDTO -> {
            log.debug("Trying to Create User {} ", userAccountDTO.getUserLoginName());

            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<UserAccountDTO>> validationErrors = validator.validate(userAccountDTO);
            if (!CollectionUtils.isEmpty(validationErrors)) {
                log.error("Validation Errors {} ", validationErrors);
                throw new ConstraintViolationException(validationErrors);
            }

            Mono<UserAccount> existing = userAccountRepository.findByUserLoginName(userAccountDTO.getUserLoginName());
            return existing.defaultIfEmpty(PRESENT).flatMap(item -> {
                if (PRESENT != item) {
                    log.info("User {} already Exists ", item.getUserLoginName());
                    return Mono.error(new ItemAlreadyExistsException(item.getUserLoginName(), "User"));
                } else {
                    log.info("Creating User {} ", userAccountDTO.getUserLoginName());
                    UserAccount newUserAccount = userAccountMapper.toUserAccount(userAccountDTO);
                    String hash = encoder.encode(userAccountDTO.getPassword());
                    newUserAccount.setPassword(hash);
                    return userAccountRepository.save(newUserAccount).map(userAccountMapper::toUserAccountDTO);
                }
            });
        });
    }

    /*
     * (non-Javadoc)
     * @see com.prady.sample.microservices.userservice.service.UserService#update(java.lang.String, reactor.core.publisher.Mono)
     */
    @Override
    public Mono<UserAccountDTO> update(String id, Mono<UserAccountDTO> userAccount) {
        return userAccount.flatMap(userAccountDTO -> {
            log.debug("Trying to Update User {} ", userAccountDTO.getUserLoginName());
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<UserAccountDTO>> validationErrors = validator.validate(userAccountDTO);
            if (!CollectionUtils.isEmpty(validationErrors)) {
                log.error("Validation Errors {} ", validationErrors);
                throw new ConstraintViolationException(validationErrors);
            }

            Mono<UserAccount> existing = userAccountRepository.findById(id);
            return existing.flatMap(item -> {
                log.info("Updating User {} ", userAccountDTO.getUserLoginName());
                userAccountMapper.toUserAccount(userAccountDTO, item);
                if (StringUtils.isNotBlank(userAccountDTO.getPassword())) {
                    String hash = encoder.encode(userAccountDTO.getPassword());
                    item.setPassword(hash);
                }
                return userAccountRepository.save(item).map(userAccountMapper::toUserAccountDTO);
            }).switchIfEmpty(Mono.error(new ItemNotFoundException(id, "User")));
        });
    }

    /*
     * (non-Javadoc)
     * @see com.prady.sample.microservices.userservice.service.UserService#delete(java.lang.String)
     */
    @Override
    public Mono<Void> delete(String id) {
        Mono<UserAccount> existing = userAccountRepository.findById(id);
        return existing.switchIfEmpty(Mono.error(new ItemNotFoundException(id, "User"))).flatMap(item -> {
            log.info("Deleting User {} ", item.getUserLoginName());
            return userAccountRepository.deleteById(id);
        });
    }
}
