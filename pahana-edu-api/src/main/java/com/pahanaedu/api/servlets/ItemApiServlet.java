package com.pahanaedu.api.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pahanaedu.api.dto.ItemDTO;
import com.pahanaedu.api.mappers.ItemMapper;
import com.pahanaedu.models.Item;
import com.pahanaedu.services.IItemService;
import com.pahanaedu.services.ItemService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet("/api/items/*")
public class ItemApiServlet extends HttpServlet {

    private IItemService itemService;
    private Gson gson;

    @Override
    public void init() {
        this.itemService = new ItemService();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                List<Item> items = itemService.getAllItems();
                List<ItemDTO> itemDTOs = ItemMapper.toDTOList(items);
                String jsonResponse = gson.toJson(itemDTOs);
                resp.getWriter().write(jsonResponse);
            } else {
                String idString = pathInfo.substring(1);
                long id = Long.parseLong(idString);
                Optional<Item> itemOptional = itemService.getItemById(id);

                if (itemOptional.isPresent()) {
                    ItemDTO itemDTO = ItemMapper.toDTO(itemOptional.get());
                    String jsonResponse = gson.toJson(itemDTO);
                    resp.getWriter().write(jsonResponse);
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().write(gson.toJson(new ErrorResponse("Item with ID " + id + " not found.")));
                }
            }
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(gson.toJson(new ErrorResponse("Invalid ID format.")));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(gson.toJson(new ErrorResponse("An unexpected error occurred.")));
            e.printStackTrace();
        }
    }

    private static class ErrorResponse {
        private final String error;
        public ErrorResponse(String error) { this.error = error; }
    }
}