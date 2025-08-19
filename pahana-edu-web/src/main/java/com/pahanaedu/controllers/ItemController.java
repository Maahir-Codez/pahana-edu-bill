package com.pahanaedu.controllers;

import com.pahanaedu.api.dto.ItemDTO;
import com.pahanaedu.clients.ItemApiClient;
import com.pahanaedu.exceptions.ApiClientException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@WebServlet("/app/items/*")
public class ItemController extends HttpServlet {

    private ItemApiClient itemApiClient;

    @Override
    public void init() {
        this.itemApiClient = new ItemApiClient();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo() == null ? "/list" : request.getPathInfo();

        try {
            switch (action) {
                case "/list":
                    listItems(request, response);
                    break;
                case "/add":
                    showAddForm(request, response);
                    break;
                case "/edit":
                    showEditForm(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (ApiClientException | InterruptedException e) {
            handleApiError(request, response, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo();
        if (action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            switch (action) {
                case "/add":
                    addItem(request, response);
                    break;
                case "/edit":
                    updateItem(request, response);
                    break;
                case "/delete":
                    deleteItem(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (ApiClientException | InterruptedException e) {
            handleApiError(request, response, e);
        }
    }

    // --- GET Handlers ---
    private void listItems(HttpServletRequest request, HttpServletResponse response) throws IOException, InterruptedException, ApiClientException, ServletException {
        List<ItemDTO> items = itemApiClient.getAllItems();
        request.setAttribute("itemList", items);
        request.getRequestDispatcher("/views/view-items.jsp").forward(request, response);
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/views/add-item.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws IOException, InterruptedException, ApiClientException, ServletException {
        long id = Long.parseLong(request.getParameter("id"));
        Optional<ItemDTO> itemOpt = itemApiClient.getItemById(id);
        if (itemOpt.isPresent()) {
            request.setAttribute("item", itemOpt.get());
            request.getRequestDispatcher("/views/edit-item.jsp").forward(request, response);
        } else {
            request.setAttribute("errorMessage", "Item not found.");
            listItems(request, response);
        }
    }

    // --- POST Handlers ---
    private void addItem(HttpServletRequest request, HttpServletResponse response) throws IOException, InterruptedException, ApiClientException, ServletException {
        ItemDTO dto = extractItemDTOFromRequest(request);
        try {
            itemApiClient.createItem(dto);
            response.sendRedirect(request.getContextPath() + "/app/items/list");
        } catch (ApiClientException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.setAttribute("item", dto);
            showAddForm(request, response);
        }
    }

    private void updateItem(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, InterruptedException {
        long id = Long.parseLong(request.getParameter("id"));
        ItemDTO dto = extractItemDTOFromRequest(request);
        dto.setId(id);
        try {
            itemApiClient.updateItem(dto);
            response.sendRedirect(request.getContextPath() + "/app/items/list");
        } catch (ApiClientException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.setAttribute("item", dto);
            request.getRequestDispatcher("/views/edit-item.jsp").forward(request, response);
        }
    }

    private void deleteItem(HttpServletRequest request, HttpServletResponse response) throws IOException, InterruptedException, ApiClientException {
        long id = Long.parseLong(request.getParameter("id"));
        itemApiClient.deleteItem(id);
        response.sendRedirect(request.getContextPath() + "/app/items/list");
    }

    // --- Helper Methods ---
    private ItemDTO extractItemDTOFromRequest(HttpServletRequest request) {
        ItemDTO dto = new ItemDTO();
        dto.setSku(request.getParameter("sku"));
        dto.setName(request.getParameter("name"));
        dto.setDescription(request.getParameter("description"));
        dto.setCategory(request.getParameter("category"));
        dto.setAuthor(request.getParameter("author"));
        try {
            dto.setPrice(new BigDecimal(request.getParameter("price")));
        } catch (NumberFormatException e) {
            dto.setPrice(BigDecimal.ZERO);
        }
        try {
            dto.setStockQuantity(Integer.parseInt(request.getParameter("stockQuantity")));
        } catch (NumberFormatException e) {
            dto.setStockQuantity(0);
        }
        // We don't get the 'active' status from the form, it defaults to false in the DTO
        // The backend's "Fetch-Then-Update" logic handles preserving the correct status.
        return dto;
    }

    private void handleApiError(HttpServletRequest request, HttpServletResponse response, Exception e) throws ServletException, IOException {
        e.printStackTrace();
        request.setAttribute("errorMessage", "Error communicating with the backend API: " + e.getMessage());
        request.getRequestDispatcher("/views/dashboard.jsp").forward(request, response);
    }
}