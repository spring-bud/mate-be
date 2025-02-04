package com.example.mate.product.application;

import com.example.mate.product.domain.Like;
import com.example.mate.product.domain.Product;
import com.example.mate.product.domain.repository.LikeRepository;
import com.example.mate.user.application.UserService;
import com.example.mate.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserService userService;
    private final ProductService productService;
    private final SimpMessageSendingOperations messagingTemplate;


    private final RedisTemplate<String, Long> redisTemplate;

    @Transactional
    public boolean createOrDeleteLike(Long userId, Long productIdL) {
        String productId = String.valueOf(productIdL);
        User findUser = userService.getUserById(userId);

        Product findProduct = productService.findProductById(productIdL);

        Long likeCount = 0L;
        Long returnCount = -1L;
        Boolean likeStatus = false;

        SetOperations<String, Long> setProductId = redisTemplate.opsForSet();

        HashOperations<String, Long, Long> hashOperations = redisTemplate.opsForHash();


        Long userProductPlus = hashOperations.get(productId, userId);
        Long userProductMinus = hashOperations.get(productId, -userId);

        boolean existsRedisProductId = Boolean.TRUE.equals(redisTemplate.hasKey(productId));

        if (!existsRedisProductId) {
            setProductId.add("productId", productIdL);
            Like existingLike = getExistingLike(userId, productIdL);
            likeCount = countLike(productIdL);
            if (existingLike == null) {
                hashOperations.put(productId, userId, likeCount);
                likeCount++;
            } else {
                hashOperations.put(productId, -userId, likeCount);
                likeCount--;
                likeStatus = true;
            }
        } else {
            Set<Long> hashUserId = hashOperations.keys(productId);
            Map<Long, Long> entries = hashOperations.entries(productId);

            Long keyUserId = hashUserId.iterator().next();

            likeCount = entries.get(keyUserId);

            returnCount = likeCount;

            if (userProductMinus == null) {
                hashOperations.delete(productId, userId);
                setProductId.remove("productId", productIdL);
            } else {
                hashOperations.delete(productId, -userId);
                setProductId.remove("productId", productIdL);
                likeStatus = true;
            }

            Set<Long> hashUserIdCount = hashOperations.keys(productId);
            for (Long key : hashUserIdCount) {
                if (key > 0) {
                    returnCount++;
                } else {
                    returnCount--;
                }
            }
        }

        messagingTemplate.convertAndSend("/sub/like-count/" + productId, existsRedisProductId ? returnCount : likeCount);

        return likeStatus;
    }

    public Long countLike(Long productId) {
        return getCountLike(productId);
    }

    public boolean getLikeStatus(Long productId, Long userId) {
        if (userId != null) {
            Like likeExist = getExistingLike(userId, productId);
            return likeExist != null;
        }
        return false;
    }

    public Like getExistingLike(Long userId, Long productId) {
        return likeRepository.findByUserIdAndProductId(userId, productId).orElse(null);
    }

    private Long getCountLike(Long findProductId) {
        return likeRepository.countLikeByProductId(findProductId);
    }


}
