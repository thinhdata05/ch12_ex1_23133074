package murach.sql;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import murach.data.ConnectionPool;

public class SqlGatewayServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String sqlStatement = request.getParameter("sqlStatement");
        String sqlResult = "";

        Connection connection = null;
        Statement statement = null;

        try {
            // ✅ Lấy connection từ pool
            ConnectionPool pool = ConnectionPool.getInstance();
            connection = pool.getConnection();

            // Tạo statement
            statement = connection.createStatement();

            sqlStatement = sqlStatement.trim();
            if (sqlStatement.length() >= 6) {
                String sqlType = sqlStatement.substring(0, 6);
                if (sqlType.equalsIgnoreCase("select")) {
                    ResultSet resultSet = statement.executeQuery(sqlStatement);
                    sqlResult = SQLUtil.getHtmlTable(resultSet);
                    resultSet.close();
                } else {
                    int i = statement.executeUpdate(sqlStatement);
                    if (i == 0) {
                        sqlResult = "<p>The statement executed successfully.</p>";
                    } else {
                        sqlResult = "<p>The statement executed successfully.<br>"
                                + i + " row(s) affected.</p>";
                    }
                }
            }
        } catch (SQLException e) {
            sqlResult = "<p>Error executing the SQL statement:<br>"
                    + e.getMessage() + "</p>";
        } finally {
            // ✅ Đóng tài nguyên & trả connection về pool
            try {
                if (statement != null) statement.close();
            } catch (SQLException e) { e.printStackTrace(); }

            ConnectionPool pool = ConnectionPool.getInstance();
            pool.freeConnection(connection);
        }

        HttpSession session = request.getSession();
        session.setAttribute("sqlResult", sqlResult);
        session.setAttribute("sqlStatement", sqlStatement);

        String url = "/index.jsp";
        getServletContext().getRequestDispatcher(url).forward(request, response);
    }
}
