/*
 * Copyright (C) 2021, FPT University<br>
 * J3.L.P0004<br>
 * DigitalNews<br>
 *
 * Record of change:<br>
 * DATE          Version    Author           DESCRIPTION<br>
 * 2021-07-01    1.0        DatDuyTran       Release 1.0<br>
 */
package controller;

import dao.PostDAO;
import dao.impl.PostDAOImpl;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Post;

/**
 * Process:<br>
 * 1. Get latest post to display its excerpt below 'Digital News' title<br>
 * 2. Get list of latest posts (except the one above)<br>
 * 3. Get a post by its ID
 *
 * @author DatDuyTran
 */
public class PostServlet extends HttpServlet {

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int postID = Integer.parseInt(request.getParameter("id"));

            PostDAO postDAO = new PostDAOImpl();

            // Get a list 6 latest posts
            List<Post> listOfLatestPosts = postDAO.getListOfLatests(6);
            
            // Get a post by its ID
            Post currentPost = postDAO.getById(postID);
            
            // Get the latest post to display below 'Digital News' title
            Post latestPost = listOfLatestPosts.get(0);
            
            // Remove the current post from the list of latest posts
            for (Iterator<Post> iterator = listOfLatestPosts.iterator(); iterator.hasNext();) {
                if (iterator.next().getId() == currentPost.getId()) {
                    iterator.remove();
                }
            }

            request.setAttribute("latestPost", latestPost);
            request.setAttribute("currentPost", currentPost);
            request.setAttribute("listOfLatestPosts", listOfLatestPosts);

            request.getRequestDispatcher("post.jsp").forward(request, response);
        } catch (Exception ex) {
            Logger.getLogger(HomeServlet.class.getName()).log(Level.SEVERE, null, ex);

            // Forward request to error page if any exception occurs
            request.setAttribute("errorMessage", ex.toString());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}
