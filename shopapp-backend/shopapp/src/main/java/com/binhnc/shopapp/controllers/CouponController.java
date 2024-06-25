package com.binhnc.shopapp.controllers;

import com.binhnc.shopapp.responses.CouponCalculationResponse;
import com.binhnc.shopapp.services.coupon.ICouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/coupons")
@RequiredArgsConstructor
public class CouponController {
    private final ICouponService couponService;

    @GetMapping("/calculate")
    public ResponseEntity<CouponCalculationResponse> calculateCouponValue(
            @RequestParam("couponCode") String couponCode,
            @RequestParam("totalAmount") double totalAmount) {
        try {
            double finalAmount = couponService.calculateCouponValue(couponCode, totalAmount);
            CouponCalculationResponse response = CouponCalculationResponse.builder()
                    .result(totalAmount)
                    .errorMessage("")
                    .build();
            response.setResult(finalAmount);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(CouponCalculationResponse.builder()
                    .result(totalAmount)
                    .errorMessage(e.getMessage())
                    .build());
        }
    }

}
