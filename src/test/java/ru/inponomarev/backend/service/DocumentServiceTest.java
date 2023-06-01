package ru.inponomarev.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.approvaltests.Approvals;
import org.approvaltests.JsonApprovals;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.curs.celesta.CallContext;
import ru.curs.celestaunit.CelestaTest;
import ru.inponomarev.backend.dto.ItemDTO;
import ru.inponomarev.backend.dto.OrderDTO;
import ru.inponomarev.celestademo.CustomerCursor;
import ru.inponomarev.celestademo.ItemCursor;
import ru.inponomarev.celestademo.OrderCursor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@CelestaTest
class DocumentServiceTest {

    private final OrderService orderService = new OrderService();
    private CustomerCursor customer;
    private ItemCursor item;

    @BeforeEach
    void setUp(CallContext ctx) {
        customer = new CustomerCursor(ctx);
        customer.setName("John Doe")
                .setEmail("john@example.com")
                .insert();
        item = new ItemCursor(ctx);
        item.setId("12345")
                .setName("cheese")
                .setDefaultPrice(42)
                .insert();
    }

    @Test
    void orderIsPostedWithDefaultPrice(CallContext ctx) throws JsonProcessingException {
        //ARRANGE
        OrderDTO dto =  new OrderDTO();
                dto.setCustomerId(customer.getId());
                dto.setItemId(item.getId());
                dto.setQuantity(100);

        //ACT
        OrderDTO result = orderService.postOrder(ctx, dto);

        //ASSERT
        OrderCursor orderCursor = new OrderCursor(ctx);
        Assertions.assertThat(orderCursor.count()).isEqualTo(1);
        orderCursor.first();
        Assertions.assertThat(orderCursor.getPrice()).isEqualTo(item.getDefaultPrice());
        Assertions.assertThat(orderCursor.getQuantity()).isEqualTo(100);
        Assertions.assertThat(orderCursor.getAmount()).isEqualTo(item.getDefaultPrice() * orderCursor.getQuantity());
        JsonApprovals.verifyJson(new ObjectMapper().writer().writeValueAsString(result));
    }

    @Test
    void orderedItemsMethodReturnsAggregatedValues(CallContext ctx) throws JsonProcessingException {
        //ARRANGE
        ItemCursor item2 = new ItemCursor(ctx);
        item2.setId("2").setName("item 2").insert();
        OrderCursor orderCursor = new OrderCursor(ctx);
        orderCursor.setId(null).setItemId(item.getId())
                .setCustomerId(customer.getId()).setQuantity(1).setAmount(2).insert();
        orderCursor.setId(null).setItemId(item.getId())
                .setCustomerId(customer.getId()).setQuantity(3).setAmount(3).insert();
        orderCursor.setId(null).setItemId(item2.getId())
                .setCustomerId(customer.getId()).setQuantity(5).setAmount(4).insert();
        //ACT
        List<ItemDTO> result = orderService.getAggregateReport(ctx);

        //ASSERT
        JsonApprovals.verifyJson(new ObjectMapper().writer().writeValueAsString(result));
    }
}