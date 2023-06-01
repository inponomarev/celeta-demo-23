package ru.inponomarev.backend.map;

import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import ru.inponomarev.backend.dto.ItemDTO;
import ru.inponomarev.backend.dto.OrderDTO;
import ru.inponomarev.celestademo.ItemViewCursor;
import ru.inponomarev.celestademo.OrderCursor;
import org.mapstruct.Mapper;

@Mapper
public interface CursorMapper {
    CursorMapper INSTANCE = Mappers.getMapper(CursorMapper.class);

    void map(OrderDTO src, @MappingTarget OrderCursor target);

    OrderDTO map(OrderCursor rec);

    ItemDTO map(ItemViewCursor rec);
}
