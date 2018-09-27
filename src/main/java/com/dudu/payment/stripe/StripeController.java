package com.dudu.payment.stripe;

import com.dudu.oauth.OAuthFilter;
import com.dudu.oauth.User;
import com.dudu.payment.exceptions.OrderNotFoundException;
import com.dudu.payment.stripe.exceptions.NoCustomerException;
import com.dudu.payment.stripe.exceptions.UserLockedException;
import com.stripe.exception.StripeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.sql.SQLException;
import java.util.List;

@RestController
public class StripeController {
    private StripeService stripeService;
    private static Logger logger = LoggerFactory.getLogger(StripeController.class);

    public StripeController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @PostMapping(value = "/payment/stripe/charge/oneTime")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void makePayment(@RequestAttribute(OAuthFilter.USER) User user,
                            @Valid PayRequest req) {
        try {
            stripeService.oneTimeCharge(req.getOrderId(), user.getUserId(), req.getSourceId());
        } catch (StripeException | SQLException e) {
            logger.warn("", e);
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (OrderNotFoundException e) {
            logger.warn("", e);
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "orderId not found");
        }
    }

    @PostMapping(value = "/payment/stripe/charge/remembered")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void makePaymentAndRemembered(@RequestAttribute(OAuthFilter.USER) User user,
                                         @Valid PayAndRememberRequest req) {
        try {
            stripeService.addSource(user.getUserId(), req.getSourceId(), req.getLastFour(),
                    req.getExpMonth(), req.getExpYear(), req.getFunding(), req.getBrand());
            stripeService.oneTimeCharge(req.getOrderId(), user.getUserId(), req.getSourceId());
        } catch (SQLException | NoCustomerException | StripeException | UserLockedException e) {
            logger.warn("", e);
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (OrderNotFoundException e) {
            logger.warn("", e);
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "orderId not found");
        }
    }


    @GetMapping(value = "/payment/stripe/source")
    @ResponseStatus(HttpStatus.OK)
    public List<StripeSource> listSources(@RequestAttribute(OAuthFilter.USER) User user) {
        try {
            return stripeService.getSources(user.getUserId());
        } catch (SQLException e) {
            logger.warn("", e);
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/payment/stripe/charge")
    @ResponseStatus(HttpStatus.OK)
    public StripeCharge getCharge(@RequestAttribute(OAuthFilter.USER) User user,
                                  @RequestParam("orderId") long orderId) {
        try {
            var stripeCharge = stripeService.getChargeByOrderId(orderId);
            if (stripeCharge == null || stripeCharge.getUserId() != user.getUserId())
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND);

            return stripeCharge;
        } catch (SQLException e) {
            logger.warn("", e);
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static class PayRequest {
        @Min(1)
        private long orderId;

        @NotEmpty
        private String sourceId;

        public long getOrderId() {
            return orderId;
        }

        public void setOrderId(long orderId) {
            this.orderId = orderId;
        }

        public String getSourceId() {
            return sourceId;
        }

        public void setSourceId(String sourceId) {
            this.sourceId = sourceId;
        }
    }

    public static class PayAndRememberRequest {
        @Min(1)
        private long orderId;

        @NotEmpty
        private String sourceId;

        @NotEmpty
        private String lastFour;
        private int expMonth;
        private int expYear;
        private String funding;
        private String brand;

        public long getOrderId() {
            return orderId;
        }

        public void setOrderId(long orderId) {
            this.orderId = orderId;
        }

        public String getSourceId() {
            return sourceId;
        }

        public void setSourceId(String sourceId) {
            this.sourceId = sourceId;
        }

        public String getLastFour() {
            return lastFour;
        }

        public void setLastFour(String lastFour) {
            this.lastFour = lastFour;
        }

        public int getExpMonth() {
            return expMonth;
        }

        public void setExpMonth(int expMonth) {
            this.expMonth = expMonth;
        }

        public int getExpYear() {
            return expYear;
        }

        public void setExpYear(int expYear) {
            this.expYear = expYear;
        }

        public String getFunding() {
            return funding;
        }

        public void setFunding(String funding) {
            this.funding = funding;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }
    }

}
