package org.jsp.jsp_19_sgnr.command.order;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jsp.jsp_19_sgnr.command.Command;
import org.jsp.jsp_19_sgnr.dao.OrderDao;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class OrderDetailJsonCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String orderId = request.getParameter("orderId");
        PrintWriter out = response.getWriter();

        if (orderId == null || orderId.isEmpty()) {
            out.print("{\"error\": \"주문 ID가 제공되지 않았습니다.\"}");
            return;
        }

        OrderDao orderDao = new OrderDao();
        Map<String, Object> orderDetail = orderDao.getOrderDetail(orderId);

        if (orderDetail == null || orderDetail.isEmpty()) {
            out.print("{\"error\": \"주문 정보를 찾을 수 없습니다.\"}");
            return;
        }

        StringBuilder json = new StringBuilder();
        json.append("{");

        org.jsp.jsp_19_sgnr.dto.OrderWithUser order = (org.jsp.jsp_19_sgnr.dto.OrderWithUser) orderDetail.get("order");
        if (order != null) {
            json.append("\"order\": {");
            json.append("\"id_order\": \"").append(order.getId_order()).append("\", ");
            json.append("\"nm_user\": \"").append(order.getNm_user()).append("\", ");
            json.append("\"nm_receiver\": \"").append(order.getNm_receiver()).append("\", ");
            json.append("\"qt_order_amount\": ").append(order.getQt_order_amount()).append(", ");
            json.append("\"st_payment\": \"").append(order.getSt_payment()).append("\", ");
            json.append("\"da_order\": \"").append(order.getDa_order()).append("\"");
            json.append("}");
        }

        json.append(", \"orderItems\": [");
        Object orderItemsObj = orderDetail.get("orderItems");
        if (orderItemsObj != null && orderItemsObj instanceof java.util.List) {
            java.util.List<Map<String, Object>> orderItems = (java.util.List<Map<String, Object>>) orderItemsObj;
            for (int i = 0; i < orderItems.size(); i++) {
                Map<String, Object> item = orderItems.get(i);
                if (i > 0) {
                    json.append(", ");
                }
                json.append("{");
                json.append("\"nm_product\": \"").append(item.get("nm_product")).append("\", ");
                json.append("\"qt_order_item\": ").append(item.get("qt_order_item")).append(", ");
                json.append("\"qt_unit_price\": ").append(item.get("qt_unit_price")).append(", ");
                json.append("\"qt_order_item_amount\": ").append(item.get("qt_order_item_amount")).append(", ");
                json.append("\"st_payment\": \"").append(item.get("st_payment")).append("\"");
                json.append("}");
            }
        }
        json.append("]");

        json.append("}");

        out.print(json.toString());
    }
}
