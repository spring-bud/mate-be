package com.example.mate.product.application;

import com.example.mate.product.domain.Like;
import com.example.mate.product.domain.Product;
import com.example.mate.product.domain.repository.LikeRepository;
import com.example.mate.user.application.UserService;
import com.example.mate.user.domain.User;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class LikeScheduler {

    private final RedisTemplate<String, Object> redisTemplate;
    private final LikeService likeService;
    private final LikeRepository likeRepository;
    private final UserService userService;
    private final ProductService productService;

    @SchedulerLock(name = "syncLikeCountToDB", lockAtMostFor = "PT30S", lockAtLeastFor = "PT5S")
    @Scheduled(fixedRate = 5000)
    public void syncLikeCountToDB() {
        Set<Object> setProductId = redisTemplate.opsForSet().members("productId");

        if (!setProductId.isEmpty()) {
            for (Object productId : setProductId) {
                HashOperations<String, Long, Long> hashOperations = redisTemplate.opsForHash();
                Set<Long> hashUserIds = hashOperations.keys(String.valueOf(productId));
                Long longProductId = Long.valueOf(String.valueOf(productId));
                for (Long userId : hashUserIds) {
                    User findUser = userService.getUserById(Math.abs(userId));
                    Product findProduct = productService.findProductById(longProductId);

                    if (userId > 0) {
                        Like newLike = new Like(findUser, findProduct);
                        likeRepository.save(newLike);
                    } else {
                        Like newLike = likeService.getExistingLike(Math.abs(userId), longProductId);
                        likeRepository.delete(newLike);
                    }
                }
                redisTemplate.delete(String.valueOf(productId));
                redisTemplate.opsForSet().remove("productId", productId);
            }
        }
    }
}
