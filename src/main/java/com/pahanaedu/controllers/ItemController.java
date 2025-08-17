package com.pahanaedu.controllers;

import com.pahanaedu.exceptions.ValidationException;
import com.pahanaedu.models.Item;
import com.pahanaedu.services.IItemService;
import com.pahanaedu.services.ItemService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@WebServlet("/items")
public class ItemController extends HttpServlet {

    private IItemService itemService;

    @Override
    public void init() {
        this.itemService = new ItemService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "add":
                showAddItemForm(request, response);
                break;
            case "edit":
                showEditItemForm(request, response);
                break;
            case "list":
            default:
                listItems(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action) {
            case "add":
                addItem(request, response);
                break;
            case "edit":
                updateItem(request, response);
                break;
            case "delete":
                deleteItem(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void listItems(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Item> items = itemService.getAllItems();
        request.setAttribute("itemList", items);
        request.getRequestDispatcher("/views/view-items.jsp").forward(request, response);
    }

    private void showAddItemForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/views/add-item.jsp").forward(request, response);
    }

    private void showEditItemForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            long id = Long.parseLong(request.getParameter("id"));
            Optional<Item> itemOptional = itemService.getItemById(id);
            if (itemOptional.isPresent()) {
                request.setAttribute("item", itemOptional.get());
                request.getRequestDispatcher("/views/edit-item.jsp").forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Item not found.");
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid item ID.");
        }
    }

    private void addItem(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Item item = extractItemFromRequest(request);
            itemService.addItem(item);
            response.sendRedirect(request.getContextPath() + "/items?action=list");
        } catch (ValidationException | NumberFormatException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.setAttribute("item", extractItemFromRequest(request));
            showAddItemForm(request, response);
        }
    }

    private void updateItem(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            long id = Long.parseLong(request.getParameter("id"));
            Item item = extractItemFromRequest(request);
            item.setId(id);
            itemService.updateItem(item);
            response.sendRedirect(request.getContextPath() + "/items?action=list");
        } catch (ValidationException | NumberFormatException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.setAttribute("item", extractItemFromRequest(request));
            request.getRequestDispatcher("/views/edit-item.jsp").forward(request, response);
        }
    }

    private void deleteItem(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            long id = Long.parseLong(request.getParameter("id"));
            itemService.deleteItem(id);
            response.sendRedirect(request.getContextPath() + "/items?action=list");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid item ID.");
        }
    }

    private Item extractItemFromRequest(HttpServletRequest request) {
        String sku = request.getParameter("sku");
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String category = request.getParameter("category");
        String author = request.getParameter("author");
        BigDecimal price = new BigDecimal(request.getParameter("price"));
        int stockQuantity = Integer.parseInt(request.getParameter("stockQuantity"));

        return new Item(sku, name, description, category, author, price, stockQuantity);
    }
}