export interface OrderDTO {
  user_id: number,
  fullname: string,
  email: string,
  phone_number: string,
  address: string,
  note: string,
  total_money: number,
  payment_method: string,
  shipping_method: string,
  coupon_code: string,
  cart_items: []
}
