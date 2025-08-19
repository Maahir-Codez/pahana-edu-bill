package com.pahanaedu.api.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pahanaedu.api.dto.ItemDTO;
import com.pahanaedu.api.mappers.ItemMapper;
import com.pahanaedu.exceptions.ValidationException;
import com.pahanaedu.models.Item;
import com.pahanaedu.services.IItemService;
import com.pahanaedu.services.ItemService;

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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo != null && !pathInfo.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            resp.getWriter().write(gson.toJson(new ErrorResponse("POST method is not allowed on a specific item.")));
            return;
        }

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = req.getReader().readLine()) != null) {
                sb.append(line);
            }

            ItemDTO newItemDTO = gson.fromJson(sb.toString(), ItemDTO.class);

            Item newItem = ItemMapper.toModel(newItemDTO);

            Item createdItem = itemService.addItem(newItem);

            ItemDTO createdItemDTO = ItemMapper.toDTO(createdItem);

            resp.setStatus(HttpServletResponse.SC_CREATED); // 201 Created status
            resp.getWriter().write(gson.toJson(createdItemDTO));

        } catch (ValidationException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
            resp.getWriter().write(gson.toJson(new ErrorResponse(e.getMessage())));
        } catch (com.google.gson.JsonSyntaxException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(gson.toJson(new ErrorResponse("Invalid JSON format.")));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            resp.getWriter().write(gson.toJson(new ErrorResponse("An unexpected error occurred.")));
            e.printStackTrace();
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        if (pathInfo == null || pathInfo.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(gson.toJson(new ErrorResponse("Item ID is required for an update.")));
            return;
        }

        try {
            long id = Long.parseLong(pathInfo.substring(1));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = req.getReader().readLine()) != null) {
                sb.append(line);
            }

            ItemDTO itemDTO = gson.fromJson(sb.toString(), ItemDTO.class);
            Item itemToUpdate = ItemMapper.toModel(itemDTO);

            itemToUpdate.setId(id);

            Item updatedItem = itemService.updateItem(itemToUpdate);

            ItemDTO updatedItemDTO = ItemMapper.toDTO(updatedItem);

            resp.setStatus(HttpServletResponse.SC_OK); // 200 OK
            resp.getWriter().write(gson.toJson(updatedItemDTO));

        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(gson.toJson(new ErrorResponse("Invalid ID format.")));
        } catch (ValidationException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(gson.toJson(new ErrorResponse(e.getMessage())));
        } catch (com.google.gson.JsonSyntaxException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(gson.toJson(new ErrorResponse("Invalid JSON format.")));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(gson.toJson(new ErrorResponse("An unexpected error occurred.")));
            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        if (pathInfo == null || pathInfo.equals("/") || pathInfo.substring(1).isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(gson.toJson(new ErrorResponse("Item ID is required for deletion.")));
            return;
        }

        try {
            long id = Long.parseLong(pathInfo.substring(1));

            if (!itemService.getItemById(id).isPresent()) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write(gson.toJson(new ErrorResponse("Item with ID " + id + " not found.")));
                return;
            }

            itemService.deleteItem(id);

            resp.setStatus(HttpServletResponse.SC_NO_CONTENT); // 204 No Content

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