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


    private final RedisTemplate<String, Object> redisTemplate;

    @Transactional
    public boolean toggleLike(Long userId, Long longProductId) {
        String productId = String.valueOf(longProductId);
        User findUser = userService.getUserById(userId);
        Product findProduct = productService.findProductById(longProductId);

        HashOperations<String, Long, Long> hashOperations = redisTemplate.opsForHash();
        SetOperations<String, Object> setProductId = redisTemplate.opsForSet();

        boolean existsRedisProductId = Boolean.TRUE.equals(redisTemplate.hasKey(productId));

        Long existsRedisLikeCount = existsRedisLikeCount(existsRedisProductId, productId, hashOperations);

        boolean likeStatus = handleLikeStatus(userId, productId, hashOperations, setProductId, existsRedisLikeCount);
        Long likeCount = redisLikeCount(productId, hashOperations);

        messagingTemplate.convertAndSend("/sub/like-count/" + productId, likeCount);

        return likeStatus;
    }

    private boolean handleLikeStatus(
            Long userId,
            String productId,
            HashOperations<String, Long, Long> hashOperations,
            SetOperations<String, Object> setProductId,
            Long likeCount
    ) {
        Object userProductPlusObj = hashOperations.get(productId, userId);
        Object userProductMinusObj = hashOperations.get(productId, -userId);

        Long userProductpLUS = convertToLong(userProductPlusObj);
        Long userProductMinus = convertToLong(userProductMinusObj);

        if (userProductpLUS == null && userProductMinus == null) {
            return handleNewRedis(userId, productId, hashOperations, setProductId, likeCount);
        } else {
            return handleExistingRedis(userId, productId, userProductMinus, hashOperations, setProductId);
        }
    }

    private boolean handleNewRedis(
            Long userId,
            String productId,
            HashOperations<String, Long, Long> hashOperations,
            SetOperations<String, Object> setProductId,
            Long likeCount
    ) {
        setProductId.add("productId", Long.parseLong(productId));
        Like existingLike = getExistingLike(userId, Long.parseLong(productId));

        if (existingLike == null) {
            hashOperations.put(productId, userId, likeCount);
        } else {
            hashOperations.put(productId, -userId, likeCount);
            return true;
        }
        return false;
    }

    private boolean handleExistingRedis(
            Long userId,
            String productId,
            Long userProductMinus,
            HashOperations<String, Long, Long> hashOperations,
            SetOperations<String, Object> setProductId
    ) {
        if (userProductMinus == null) {
            hashOperations.delete(productId, userId);
            setProductId.remove("productId", Long.parseLong(productId));
        } else {
            hashOperations.delete(productId, -userId);
            setProductId.remove("productId", Long.parseLong(productId));
            return true;
        }
        return false;
    }

    private Long redisLikeCount(
            String productId,
            HashOperations<String, Long, Long> hashOperations
    ) {
        Map<Long, Long> entries = hashOperations.entries(productId);
        Set<Long> hashUserId = hashOperations.keys(productId);

        long returnCount = 0L;

        if (!hashUserId.isEmpty()) {
            Object keyUserId = hashUserId.iterator().next();
            Object value = entries.get(keyUserId);

            if (value instanceof Number) {
                returnCount = ((Number) value).longValue();
            } else if (value instanceof String) {
                returnCount = Long.parseLong((String) value);
            }
        }

        if (!hashUserId.isEmpty()) {
            for (Object keyObj : hashUserId) {
                Long key;
                if (keyObj instanceof Number) {
                    key = ((Number) keyObj).longValue();
                } else if (keyObj instanceof String) {
                    key = Long.parseLong((String) keyObj);
                } else {
                    continue;
                }
                returnCount += (key > 0) ? 1 : -1;
            }
        }
        return returnCount;
    }

    private Long existsRedisLikeCount(boolean existsRedisProductId, String productId, HashOperations<String, Long, Long> hashOperations) {
        Long likeCount = 0L;
        if (!existsRedisProductId) {
            likeCount = countLike(Long.parseLong(productId));
        } else {
            Map<Long, Long> entries = hashOperations.entries(productId);
            Set<Long> hashUserId = hashOperations.keys(productId);

            if (!hashUserId.isEmpty()) {
                Object keyUserId = hashUserId.iterator().next();
                Object value = entries.get(keyUserId);

                if (value instanceof Number) {
                    likeCount = ((Number) value).longValue();
                } else if (value instanceof String) {
                    likeCount = Long.parseLong((String) value);
                }
            }
        }
        return likeCount;
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

    private Long convertToLong(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Long) {
            return (Long) value;
        } else if (value instanceof Integer) {
            return ((Integer) value).longValue();
        } else if (value instanceof String) {
            return Long.parseLong((String) value);
        } else if (value instanceof Number) {
            return ((Number) value).longValue();
        }

        return null;
    }


}
