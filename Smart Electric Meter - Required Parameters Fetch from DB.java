import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


     
           
    public class bill extends HttpServlet {
     
    private Date date1;
    private String time1;
    private String watt;
    private Date sd;
      private Date ed;
    private int count;
    private int sum;
    private String s; 
    private String t;
    private String x;
 
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
        
     
            s = request.getParameter("s");
            t = request.getParameter("t");
            x = request.getParameter("x");
            
        
       out.print("<script>alert('try to connected to database')</script>");
          
          Class.forName("com.mysql.jdbc.Driver");  
           Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/abcd","root",""); 
           out.print("<script>alert('connected to database')</script>");
            
           
           Statement stmt = con.createStatement();
           
      ResultSet rs = stmt.executeQuery("select count(*) as count, sum(watt) as sum from sensor where date1 between cast('"+s+"' as date) and cast('"+t+"' as date) ;"); 
      
        while (rs.next()) {
                 
                 int count = rs.getInt("count");
                 int sum = rs.getInt("sum");
}
con.close();  

  
                request.setAttribute("count",count);
                request.setAttribute("sum",sum);
              request.setAttribute("x", x);
                request.getRequestDispatcher("finalbill").forward(request, response);
               
               
}catch(Exception e){ System.out.println(e);}  
}
    
    }
