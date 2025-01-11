package com.example.mate.product.application;

import com.example.mate.product.domain.Like;
import com.example.mate.product.domain.Product;
import com.example.mate.product.domain.repository.LikeRepository;
import com.example.mate.user.application.UserService;
import com.example.mate.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserService userService;
    private final ProductService productService;

    @Transactional
    public void createOrDeleteLike(Long userId, Long productId) {
        User findUser = userService.getUserById(userId);

        Product findProduct = productService.findProductById(productId);

        Like existingLike = getExistingLike(userId, productId);

        if (existingLike != null) {
            likeRepository.delete(existingLike);
        } else {
            Like newLike = new Like(findUser, findProduct);
            likeRepository.save(newLike);
        }
    }

    public Long countLike(Long productId) {
        Long count = getCountLike(productId);

        return count;
    }

    public boolean getLikeStatus(Long productId, Long userId) {
        if (userId != null) {
            Like likeExist = getExistingLike(userId, productId);
            return likeExist != null;
        }
        return false;
    }

    private Like getExistingLike(Long userId, Long productId) {
        return likeRepository.findByUserIdAndProductId(userId, productId).orElse(null);
    }

    private Long getCountLike(Long findProductId) {
        return likeRepository.countLikeByProductId(findProductId);
    }


}
