/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tender.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static tender.model.hash.hexEncode;
import tender.model.query;

/**
 *
 * @author marlon
 */
public class signup extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String fname = request.getParameter("first_name");
        String lname = request.getParameter("last_name");
        String password = request.getParameter("password");
        String conPassword = request.getParameter("confirm_password");
        String emailAddress = request.getParameter("email");
        String address = request.getParameter("address");
        String city = request.getParameter("city");
        String province = request.getParameter("province");
        String country = request.getParameter("country");

        query data = new query();
        HashMap location = new HashMap();
        HashMap person = new HashMap();
        int pkLocation = 0;
        int pkPerson = 0;

        try (PrintWriter out = response.getWriter()) {
            try {
                /*Class.forName(jdbcDriver).newInstance();
                Connection conn = DriverManager.getConnection(jdbcURL, user, DBpassword);
                Statement stmt = conn.createStatement();*/
                person.put("email", emailAddress);
                if (password.length() < 7) {
                    out.println("Passwords must be longer than 6 characters");
                } else if (data.exists("person", person)) {
                    out.println("A user with that email already exists.");
                } else if (fname == null || lname == null || password == null || conPassword == null || (password == null ? conPassword != null : !password.equals(conPassword))
                        || emailAddress == null || address == null || city == null || province == null || country == null) {
                    out.println("Please Fill in All Fields");
                } else {
                    person.clear();
                    MessageDigest sha = MessageDigest.getInstance("SHA-1");
                    byte[] hashOne = sha.digest(password.getBytes());
                    String modPass = hexEncode(hashOne);
                    //out.println(modPass);

                    location.put("address", address);
                    location.put("city", city);
                    location.put("country", country);
                    location.put("province", province);
                    pkLocation = data.insert("location", location);

                    person.put("firstname", fname);
                    person.put("lastname", lname);
                    person.put("password", modPass);
                    person.put("email", emailAddress);
                    person.put("location_pk", pkLocation);
                    pkPerson = data.insert("person", person);
                }
                out.println(pkLocation + ", " + pkPerson);

            } catch (Exception e) {

                out.println("<p>" + e + "</p>");
            }
        }
    }

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
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
