package com.huuloc.bookstore.bbook.dto.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@Data
@Getter
@Setter
@AllArgsConstructor
public class AddCartRequest {
    @NotNull(message = "Order id is required")
    private Long orderId;
    @NotNull(message = "Book id is required")
    private Long bookId;
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be greater than 0")
    private Long quantity;
}
