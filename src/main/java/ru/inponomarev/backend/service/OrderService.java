package ru.inponomarev.backend.service;

import org.springframework.stereotype.Service;
import ru.curs.celesta.CallContext;
import ru.curs.celesta.transaction.CelestaTransaction;
import ru.inponomarev.backend.dto.ItemDTO;
import ru.inponomarev.backend.dto.OrderDTO;
import ru.inponomarev.backend.map.CursorMapper;
import ru.inponomarev.celestademo.ItemCursor;
import ru.inponomarev.celestademo.ItemViewCursor;
import ru.inponomarev.celestademo.OrderCursor;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    @CelestaTransaction
    public OrderDTO postOrder(CallContext ctx, OrderDTO orderDTO) {
        OrderCursor orderCursor = new OrderCursor(ctx);

        CursorMapper.INSTANCE.map(orderDTO, orderCursor);
        ItemCursor item = new ItemCursor(ctx);
        item.get(orderDTO.getItemId());
        if (orderCursor.getPrice() == null) {
            orderCursor.setPrice(item.getDefaultPrice());
        }
        orderCursor.setAmount(orderCursor.getPrice()
                * orderDTO.getQuantity());
        orderCursor.insert();
        return CursorMapper.INSTANCE.map(orderCursor);

    }

    @CelestaTransaction
    public List<ItemDTO> getAggregateReport(CallContext ctx) {
        ItemViewCursor item = new ItemViewCursor(ctx);
        List<ItemDTO> result = new ArrayList<>();
        for (ItemViewCursor rec : item) {
            result.add(CursorMapper.INSTANCE.map(rec));
        }

        return result;
    }
}
