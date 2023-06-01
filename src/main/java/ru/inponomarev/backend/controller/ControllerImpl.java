package ru.inponomarev.backend.controller;


import ru.inponomarev.backend.dto.ItemDTO;
import ru.inponomarev.backend.dto.OrderDTO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ControllerImpl implements Controller {
    @Override
    public String hello(HttpServletResponse response) {
        return "OK";
    }


    @Override
    public OrderDTO postOrder(OrderDTO request, HttpServletResponse response) {
        return null;
    }

    @Override
    public List<ItemDTO> getAllItems(HttpServletResponse response) {
        return null;
    }
}
