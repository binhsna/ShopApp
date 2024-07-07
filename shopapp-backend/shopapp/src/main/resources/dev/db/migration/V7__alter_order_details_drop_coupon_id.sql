ALTER TABLE order_details
DROP CONSTRAINT fk_order_details_coupon;
ALTER TABLE order_details DROP coupon_id;
