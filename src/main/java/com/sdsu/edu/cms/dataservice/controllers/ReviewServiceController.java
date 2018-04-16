package com.sdsu.edu.cms.dataservice.controllers;

import com.sdsu.edu.cms.common.models.response.ServiceResponse;
import com.sdsu.edu.cms.common.models.review.Review;
import com.sdsu.edu.cms.dataservice.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class ReviewServiceController {
    @Autowired
    ReviewService reviewService;

    @PostMapping("/reviewers/get")
    public ServiceResponse getAllReviewers(@RequestParam Map<String,String> confId){
        return reviewService.getAllReviewers(confId.get("cid"));
    }

    @PostMapping("/reviews/create")
    public ServiceResponse addReview(@RequestBody Review review){
        return reviewService.addReview(review);

    }

    @PostMapping("/reviews/get/sid")
    public ServiceResponse getReviewsBySid(@RequestParam Map<String, String> mp){
        return  reviewService.getReviewBySubmissionID(mp.get("sid"));
    }

    @PostMapping("/reviews/get/rid")
    public ServiceResponse getReviewsByRid(@RequestParam Map<String, String> params){
        return reviewService.getReviewByReviewId(params.get("rid"));
    }

    @PostMapping("/reviews/update")
    public ServiceResponse updateReviews(@RequestBody Review review){
        return reviewService.updateReview(review);
    }

    @PostMapping("/reviews/publish")
    public ServiceResponse publishReviews(@RequestParam Map<String, String> params){
        return  reviewService.publishReviews(params.get("sid"));
    }

    @PostMapping("/reviews/delete")
    public ServiceResponse deleteReviews(@RequestParam Map<String, String> params){
        return reviewService.deleteReview(params.get("rid"));
    }

}
