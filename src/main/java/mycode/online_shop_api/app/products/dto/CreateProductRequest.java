package mycode.online_shop_api.app.products.dto;

import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDate;

@Builder
public record CreateProductRequest(String category, String description, String name, double price, int stock, double weight) {

}