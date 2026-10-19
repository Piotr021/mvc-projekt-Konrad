package pl.merito.serwis.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/tickets/add")
public class TicketFormServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        renderForm(resp, new ArrayList<>(), "", "", "");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        req.setCharacterEncoding("UTF-8");

        String device = trim(req.getParameter("device"));
        String description = trim(req.getParameter("description"));
        String receivedDate = trim(req.getParameter("receivedDate"));

        List<String> errors = new ArrayList<>();

        if (device.isEmpty()) {
            errors.add("Rodzaj sprzętu jest wymagany.");
        }

        if (description.isEmpty()) {
            errors.add("Opis usterki jest wymagany.");
        }

        if (receivedDate.isEmpty()) {
            errors.add("Data przyjęcia jest wymagana.");
        } else {
            try {
                LocalDate date = LocalDate.parse(receivedDate);

                if (date.isAfter(LocalDate.now())) {
                    errors.add("Data przyjęcia nie może być z przyszłości.");
                }
            } catch (DateTimeParseException e) {
                errors.add("Podaj poprawną datę przyjęcia.");
            }
        }

        if (!errors.isEmpty()) {
            renderForm(resp, errors, device, description, receivedDate);
            return;
        }

        String encoded = URLEncoder.encode(device, StandardCharsets.UTF_8);

        resp.sendRedirect(req.getContextPath()
                + "/tickets/added?device=" + encoded);
    }

    private String trim(String value) {
        return value == null ? "" : value.trim();
    }

    private void renderForm(HttpServletResponse resp, List<String> errors,
                            String device, String description,
                            String receivedDate) throws IOException {

        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        out.println("<!DOCTYPE html><html lang='pl'><body>");
        out.println("<h1>Nowe zgłoszenie serwisowe</h1>");

        for (String error : errors) {
            out.println("<p style='color:red'>" + escapeHtml(error) + "</p>");
        }

        out.println("<form method='post'>");

        out.println("<p>Rodzaj sprzętu: "
                + "<input name='device' value='"
                + escapeHtml(device) + "'></p>");

        out.println("<p>Opis usterki: "
                + "<textarea name='description'>"
                + escapeHtml(description) + "</textarea></p>");

        out.println("<p>Data przyjęcia: "
                + "<input type='date' name='receivedDate' value='"
                + escapeHtml(receivedDate) + "'></p>");

        out.println("<button type='submit'>Utwórz zgłoszenie</button>");
        out.println("</form></body></html>");
    }

    private String escapeHtml(String value) {
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
