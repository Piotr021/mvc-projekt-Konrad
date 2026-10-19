package pl.merito.serwis.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/tickets/added")
public class TicketAddedServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String device = req.getParameter("device");

        if (device == null || device.isBlank()) {
            resp.sendRedirect(req.getContextPath() + "/tickets/add");
            return;
        }

        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        out.println("<!DOCTYPE html><html lang='pl'><body>");
        out.println("<h1>Potwierdzenie zgłoszenia serwisowego</h1>");
        out.println("<p>Formularz został poprawnie przesłany.</p>");
        out.println("<p>Sprzęt: " + escapeHtml(device) + "</p>");
        out.println("<p>Wersja demo, bez zapisu do bazy danych.</p>");
        out.println("<a href='" + req.getContextPath()
                + "/tickets/add'>Dodaj kolejne zgłoszenie</a>");
        out.println("</body></html>");
    }

    private String escapeHtml(String value) {
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}