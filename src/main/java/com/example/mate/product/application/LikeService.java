package com.example.mate.product.application;

import com.example.mate.product.domain.Like;
import com.example.mate.product.domain.Product;
import com.example.mate.product.domain.repository.LikeRepository;
import com.example.mate.user.application.UserService;
import com.example.mate.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserService userService;
    private final ProductService productService;
    private final SimpMessageSendingOperations messagingTemplate;

    @Transactional
    public boolean createOrDeleteLike(Long userId, Long productId) {
        User findUser = userService.getUserById(userId);

        Product findProduct = productService.findProductById(productId);

        Like existingLike = getExistingLike(userId, productId);

        Long likeCount = 0L;
        Boolean likeStatus = false;
        if (existingLike != null) {
            likeRepository.delete(existingLike);
            likeCount = countLike(productId);
        } else {
            Like newLike = new Like(findUser, findProduct);
            likeRepository.save(newLike);
            likeCount = countLike(productId);
            likeStatus = true;
        }

        messagingTemplate.convertAndSend("/sub/like-count/" + productId, likeCount);

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

    private Like getExistingLike(Long userId, Long productId) {
        return likeRepository.findByUserIdAndProductId(userId, productId).orElse(null);
    }

    private Long getCountLike(Long findProductId) {
        return likeRepository.countLikeByProductId(findProductId);
    }


}
