package ru.market.pricing_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.market.pricing_service.model.dto.PriceOrderRequest;
import ru.market.pricing_service.model.dto.PriceOrderResponse;
import ru.market.pricing_service.service.PricingService;

@RestController
@RequestMapping("/pricing")
public class PricingController {

    private final PricingService service;

    @Autowired
    public PricingController(PricingService service) {
        this.service = service;
    }

    @PostMapping("/form-order")
    public PriceOrderResponse formOrder(@RequestBody PriceOrderRequest request) {
        return service.formPrices(request.getDirectorId());
    }
}