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

}
