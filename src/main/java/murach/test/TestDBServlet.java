package murach.test;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import murach.data.ConnectionPool;

@WebServlet("/TestDBServlet")
public class TestDBServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("text/plain; charset=UTF-8");
        PrintWriter out = response.getWriter();

        try (Connection conn = ConnectionPool.getInstance().getConnection()) {
            if (conn != null) {
                out.println("✅ Kết nối PostgreSQL thành công!");
                out.println("URL: " + conn.getMetaData().getURL());
                out.println("User: " + conn.getMetaData().getUserName());
            } else {
                out.println("❌ Không thể kết nối database (Connection null).");
            }
        } catch (Exception e) {
            e.printStackTrace(out);
        }
    }
}
