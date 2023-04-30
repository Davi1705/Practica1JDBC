package com.example.filter.filters;

import com.example.filter.connection.ConnectionBD;
import com.example.filter.exception.ServiceJdbcException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebFilter("/*")
public class ConnectionFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try (Connection conn = ConnectionBD.getInstance()){
            if(conn.getAutoCommit()){
                conn.setAutoCommit(false);
            }
            try{
                servletRequest.setAttribute("conn", conn);
                filterChain.doFilter(servletRequest, servletResponse);
                conn.commit();
            }catch (SQLException | ServiceJdbcException e){
                conn.rollback();
                ((HttpServletResponse)servletResponse).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                e.printStackTrace();
            }
        }catch(SQLException throwables){
            throwables.printStackTrace();
        }
    }
}
